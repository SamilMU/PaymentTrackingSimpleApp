package com.example.paymenttracking.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.paymenttracking.bll.PaymentLogic
import com.example.paymenttracking.bll.PaymentTypeLogic
import com.example.paymenttracking.model.PaymentTypeEntity
import com.example.paymenttracking.view.listing.paymentTypes.PaymentTypeAdapter
import com.example.paymenttracking.databinding.ActivityMainBinding
import com.example.paymenttracking.model.TypePeriods
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    /** Shared Objects */
    private lateinit var typeAdap: PaymentTypeAdapter
    private var paymentTypeList: ArrayList<PaymentTypeEntity> = arrayListOf()

    private var paymentTypeObject = PaymentTypeEntity()
    private var defaultTypeIdList = ArrayList<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setActivityView()
        setAdapter2RV()
        clickListeners()

    }

    /** Views */
    private fun setActivityView() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.rvMain.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val pref = getSharedPreferences(packageName, MODE_PRIVATE)
        val switchState = pref.getBoolean("switchChecked",false)
        if (switchState){
            binding.switchMain.isChecked = true
        }
    }

    private fun setAdapter2RV() {
        paymentTypeList = PaymentTypeLogic.getAllPaymentTypes(this)
        typeAdap =
            PaymentTypeAdapter(this, paymentTypeList, ::cardClickEvent, ::addPaymentClickEvent)
        binding.rvMain.adapter = typeAdap
    }

    /** Button Click Listeners, just one in this case because card clicks are listened in ViewHolder. */
    private fun clickListeners() {

        binding.btnMainAddType.setOnClickListener {
            val intent = Intent(this, TypeAdditionActivity::class.java)
            addTypeRL.launch(intent)
        }

        binding.btnClearMain.setOnClickListener {
            PaymentTypeLogic.clearTable(this)
            PaymentLogic.clearPayments(this)
            defaultTypeIdList = arrayListOf()
            binding.switchMain.isChecked = false
            setAdapter2RV()
        }
        // Default Type Switch
        binding.switchMain.setOnCheckedChangeListener { _, b ->
            if(b){
                createInitTypes()
                setAdapter2RV()
                // SharedPref init
                val pref = getSharedPreferences(packageName, MODE_PRIVATE)
                val editor = pref.edit()
                // Switch state is stored in sharedPref
                editor.putBoolean("switchChecked",true)
                var localCounter = 0
                // Saving added default types to sharedPref and local list.
                paymentTypeList.takeLast(4).forEach {
                    // Local id list of default types.
                    defaultTypeIdList.add(it.id)
                    // Shared pref id list of default types.
                    editor.putInt("defId$localCounter", it.id)
                    editor.apply()
                    localCounter++
                }
            }else{
                val pref = getSharedPreferences(packageName, MODE_PRIVATE)
                val editor = pref.edit()
                // Switch state is changed in shared pref
                editor.putBoolean("switchChecked",false)
                // No more input to sharedpref after this point.
                editor.apply()
                // Filling list again if app is closed after switching on.
                if(defaultTypeIdList.isEmpty()){
                    for(i in 0..3){
                        defaultTypeIdList.add(pref.getInt("defId$i",0))
                        Log.e("Logcat",pref.getInt("defId$i",0).toString())
                    }
                }
                // Iterate over list and delete each type
                defaultTypeIdList.forEach{
//                    Log.e("Logcat",it.toString())
                    PaymentTypeLogic.deleteType(this,it,false)
                }
                // Empty local list after.
                defaultTypeIdList = arrayListOf()
                // View Update
                setAdapter2RV()
            }
        }
    }

    /** Card Click Event for navigation to DetailActivity */
    private fun cardClickEvent(paymentTypeArg: PaymentTypeEntity) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("paymentTypeObject", paymentTypeArg)
        startActivity(intent)
    }

    /** Card button Click Event for navigation to PaymentAddition */
    private fun addPaymentClickEvent(paymentTypeArg: PaymentTypeEntity) {
        val intent = Intent(this, PaymentAdditionActivity::class.java)
        paymentTypeObject = paymentTypeArg
        intent.putExtra("paymentTypeObject", paymentTypeArg)
        addPaymentRL.launch(intent)
    }

    /** Result Launchers */
    private val addTypeRL =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                setAdapter2RV()
            }
        }

    private val addPaymentRL =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result ->
                if(result.resultCode == RESULT_OK){
                    // Custom Snackbar to go to details of type which the payment is added to.
                    val detailSnackbar = Snackbar.make(binding.rvMain,
                        paymentTypeObject.title, Snackbar.LENGTH_LONG)
                    detailSnackbar.setAction("Detaylara git") { cardClickEvent(paymentTypeObject) }
                    detailSnackbar.show()
                }
        }

    /** Func to Create Initial Types */
    private fun createInitTypes() {
            val paymentType1 = PaymentTypeEntity().apply {
                title = "Elektrik Faturası"
//                period = "Aylık"
                timeOfPeriod = 15
                period = TypePeriods.Aylik
            }
            val paymentType2 = PaymentTypeEntity().apply {
                title = "Su Faturası"
//                period = "Yıllık"
                timeOfPeriod = 180
                period = TypePeriods.Yillik
            }
            val paymentType3 = PaymentTypeEntity().apply {
                title = "Doğalgaz Faturası"
//                period = "Haftalık"
                period = TypePeriods.Haftalik
            }
            val paymentType4 = PaymentTypeEntity().apply {
                title = "İnternet Faturası"
            }
            PaymentTypeLogic.addPaymentType(this, paymentType1,false)
            PaymentTypeLogic.addPaymentType(this, paymentType2,false)
            PaymentTypeLogic.addPaymentType(this, paymentType3,false)
            PaymentTypeLogic.addPaymentType(this, paymentType4,false)
    }

    override fun onResume() {
        setAdapter2RV()
        super.onResume()
    }

    override fun onBackPressed() {
        val adb = AlertDialog.Builder(this)
        adb.setTitle("Ödeme Takibi")
        adb.setMessage("Uygulamadan çıkmak istiyor musunuz ?")

        adb.setPositiveButton("Evet") { _, _ ->
            super.onBackPressed()

//            android.os.Process.killProcess(android.os.Process.myPid())
        }
        adb.setNegativeButton("Hayır", null)
        adb.show()
    }
}
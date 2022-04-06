package com.example.paymenttracking.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.paymenttracking.bll.PaymentLogic
import com.example.paymenttracking.bll.PaymentTypeLogic
import com.example.paymenttracking.model.PaymentTypeEntity
import com.example.paymenttracking.view.listing.paymentTypes.PaymentTypeAdapter
import com.example.paymenttracking.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    /** Shared Objects */
    private lateinit var typeAdap: PaymentTypeAdapter
    private var paymentTypeList: ArrayList<PaymentTypeEntity> = arrayListOf()

    private var paymentTypeObject = PaymentTypeEntity()
    private var defaultTypeIdList = ArrayList<Int>()

    private val paymentTypeLogic = PaymentTypeLogic()
    private var initTypeCount = 0

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
            var resultDB = PaymentTypeLogic.clearTable(this)
            if(resultDB) resultDB = PaymentLogic.clearPayments(this)
            dbErrorCheck(resultDB,true)
            defaultTypeIdList = arrayListOf()
            binding.switchMain.isChecked = false
            setAdapter2RV()
        }
        // Default Type Switch
        binding.switchMain.setOnCheckedChangeListener { _, b ->
            // SharedPref init
            val pref = getSharedPreferences(packageName, MODE_PRIVATE)
            val editor = pref.edit()
            if(b){
                createInitTypes()
                setAdapter2RV()
                // takeLast should be equal to the number of items added.
                defaultTypeIdList = paymentTypeLogic.writeToSharedPreferences(editor,paymentTypeList,initTypeCount)
            }else{
                paymentTypeLogic.readFromSharedPreferencesAndDelete(this,editor,pref,initTypeCount)
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
            initTypeCount = paymentTypeLogic.createInitTypes(this)
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

    /** DB Error Warning*/
    fun dbErrorCheck(result: Boolean, showPositive: Boolean){
        if(result && showPositive){
            Toast.makeText(this,"İşlem başarıyla gerçekleştirildi.", Toast.LENGTH_SHORT).show()
        }else if(!result){
            Toast.makeText(this,
                "İşlemi gerçekleştirirken bir hata oluştu. Lütfen daha sonra tekrar deneyiniz",
                Toast.LENGTH_SHORT).show()
        }
    }
}
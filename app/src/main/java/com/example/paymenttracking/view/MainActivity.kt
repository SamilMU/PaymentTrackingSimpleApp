package com.example.paymenttracking.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.paymenttracking.bll.PaymentLogic
import com.example.paymenttracking.bll.PaymentTypeLogic
import com.example.paymenttracking.model.PaymentTypeEntity
import com.example.paymenttracking.view.listing.paymentTypes.PaymentTypeAdapter
import com.example.paymenttracking.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    /** Shared Objects */
    private lateinit var typeAdap: PaymentTypeAdapter
    private var paymentTypeList: ArrayList<PaymentTypeEntity> = arrayListOf()

    private var paymentTypeObject = PaymentTypeEntity()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setActivityView()
        createInitTypes()
        setAdapter2RV()
        startListeners()

    }

    /** Views */
    private fun setActivityView() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.rvMain.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    private fun setAdapter2RV() {
        paymentTypeList = PaymentTypeLogic.getAllPaymentTypes(this)
        typeAdap =
            PaymentTypeAdapter(this, paymentTypeList, ::cardClickEvent, ::addPaymentClickEvent)
        binding.rvMain.adapter = typeAdap
    }

    /** Button Click Listeners, just one in this case because card clicks are listened in ViewHolder. */
    private fun startListeners() {

        binding.btnMainAddType.setOnClickListener {
            val intent = Intent(this, TypeAdditionActivity::class.java)
            addTypeRL.launch(intent)
        }

        binding.btnClearMain.setOnClickListener {
            PaymentTypeLogic.clearTable(this)
            PaymentLogic.clearPayments(this)
            setAdapter2RV()
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
        startActivity(intent)
    }

    /** Result Launchers */
    private val addTypeRL =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                setAdapter2RV()
            }
        }

    /** Func to Create Initial Types */
    private fun createInitTypes() {
        if (PaymentTypeLogic.getAllPaymentTypes(this).isNullOrEmpty()) {
            val paymentType1 = PaymentTypeEntity().apply {
                title = "Elektrik Faturası"
                period = "Aylık"
                timeOfPeriod = 15
            }
            val paymentType2 = PaymentTypeEntity().apply {
                title = "Su Faturası"
                period = "Yıllık"
                timeOfPeriod = 180
            }
            val paymentType3 = PaymentTypeEntity().apply {
                title = "Doğalgaz Faturası"
                period = "Aylık"
            }
            val paymentType4 = PaymentTypeEntity().apply {
                title = "İnternet Faturası"
            }
            PaymentTypeLogic.addPaymentType(this, paymentType1)
            PaymentTypeLogic.addPaymentType(this, paymentType2)
            PaymentTypeLogic.addPaymentType(this, paymentType3)
            PaymentTypeLogic.addPaymentType(this, paymentType4)
        }
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
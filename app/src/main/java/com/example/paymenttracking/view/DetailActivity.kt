package com.example.paymenttracking.view

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.paymenttracking.bll.PaymentLogic
import com.example.paymenttracking.model.PaymentEntity
import com.example.paymenttracking.model.PaymentTypeEntity
import com.example.paymenttracking.view.listing.payments.PaymentAdapter
import com.example.paymenttracking.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDetailBinding


    /** Shared Objects*/
    lateinit var adap : PaymentAdapter
    var paymentList = ArrayList<PaymentEntity>()

    var paymentTypeObject = PaymentTypeEntity()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setActivityView()
        setAdapter2RV()
        clickListeners()
    }

    /** Views*/
    fun setActivityView(){
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvDetail.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        paymentTypeObject = intent.getSerializableExtra("paymentTypeObject") as PaymentTypeEntity

        PaymentLogic.getSpesificPayments(this,paymentTypeObject)
    }

    /** Setting adapter to RV */
    fun setAdapter2RV(){
        paymentList = PaymentLogic.getSpesificPayments(this,paymentTypeObject)
        adap = PaymentAdapter(this,paymentList, ::clickEvent)
        binding.rvDetail.adapter = adap
    }

    /** Click Listeners */
    fun clickListeners(){
        binding.btnDetailAddPayment.setOnClickListener {
            val intent = Intent(this, PaymentAdditionActivity::class.java)
            intent.putExtra("paymentTypeObject", paymentTypeObject)
            startActivity(intent)
        }

        binding.btnDetailEdit.setOnClickListener {
            val intent = Intent(this, TypeAdditionActivity::class.java)
            intent.putExtra("paymentTypeObject", paymentTypeObject)
            editTypeRL.launch(intent)
        }

    }

    /** Click event for RV Cards */
    fun clickEvent(paymentArg : PaymentEntity){
        val adb = AlertDialog.Builder(this)

        adb.setTitle("Ödeme Silme")
        adb.setMessage("Seçilen ${paymentArg.date} tarihli ${paymentArg.amount} tutarındaki ödemeyi silmek istediğinize emin misiniz?")

        adb.setPositiveButton("Evet",DialogInterface.OnClickListener { dialogInterface, i ->
            PaymentLogic.deletePayment(this,paymentArg.Id)
            setAdapter2RV()
        })
        adb.setNegativeButton("Hayır", DialogInterface.OnClickListener { dialogInterface, i ->
            Toast.makeText(this,"Silme iptal edildi", Toast.LENGTH_SHORT).show()
        })

        adb.show()

    }

    /** Result Launchers */
    val editTypeRL = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result ->
            if(result.resultCode == RESULT_OK){
                paymentTypeObject = result.data!!.getSerializableExtra("paymentTypeObject") as PaymentTypeEntity
                onResume()
            }
    }

    /** View update */
    override fun onResume() {
        binding.tvDetailTitle.text = paymentTypeObject.title.uppercase()
        if (!paymentTypeObject.period.isNullOrEmpty()){
            if(paymentTypeObject.timeOfPeriod != null && paymentTypeObject.timeOfPeriod != 0){
                binding.tvDetailSummary.text = "${paymentTypeObject.period}, ${paymentTypeObject.timeOfPeriod}.günü ödeniyor."
            }else{
                binding.tvDetailSummary.text = "${paymentTypeObject.period} olarak ödeniyor."
            }
        }else{
            binding.tvDetailSummary.text = "Bu tipin düzenli bir ödeme planı yok."
        }

        setAdapter2RV()
        super.onResume()
    }

}
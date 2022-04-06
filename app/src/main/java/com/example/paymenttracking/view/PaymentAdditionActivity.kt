package com.example.paymenttracking.view

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.paymenttracking.bll.PaymentLogic
import com.example.paymenttracking.model.PaymentEntity
import com.example.paymenttracking.model.PaymentTypeEntity
import com.example.paymenttracking.databinding.ActivityPaymentAdditionBinding
import java.lang.NumberFormatException
import java.util.*

class PaymentAdditionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPaymentAdditionBinding

    /** Shared Variables */
    private lateinit var paymentTypeObj : PaymentTypeEntity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setActivityView()
        clickListeners()
    }

    /** Views*/
    @SuppressLint("SetTextI18n")
    private fun setActivityView(){
        binding = ActivityPaymentAdditionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        paymentTypeObj = intent.getSerializableExtra("paymentTypeObject") as PaymentTypeEntity
        binding.tvToolbarAddpayment.text = paymentTypeObj.title.uppercase()
        val dateList = calenderFunc()
        binding.btnDateAddpayment.text = "${dateList[0]}.${dateList[1] +1}.${dateList[2]}"

    }

    /** Click Listeners */
    private fun clickListeners(){
        // Date Button
        binding.btnDateAddpayment.setOnClickListener {
            customDatePicker(calenderFunc())
        }

        // Save Button
        binding.btnSaveAddpayment.setOnClickListener {
            try {
                if(binding.etAmountAddpayment.text.toString().isEmpty()) {
                    Toast.makeText(this, "Lütfen ödemenin miktarını giriniz!", Toast.LENGTH_SHORT)
                        .show()
                }
                else{
                    val newPayment = PaymentEntity().apply {
                        date = binding.btnDateAddpayment.text.toString()
                        amount = binding.etAmountAddpayment.text.toString().toDouble()
                    }
                    newPayment.ownerId = paymentTypeObj.id

                    val intent = Intent()
                    val resultDB = PaymentLogic.addPayment(this,newPayment)
                    if(resultDB){
                        setResult(RESULT_OK,intent)
                        finish()
                    }else{
                        Toast.makeText(this,"Ödemeyi eklerken bir hata oluştu. Lütfen daha sonra tekrar deneyiniz.", Toast.LENGTH_SHORT).show()
                    }
                }
            }catch (e : NumberFormatException){
                Toast.makeText(this,"Girilen ödeme miktarı hatalıdır!", Toast.LENGTH_SHORT).show()
            }

        }
    }

    /** Func to get current date */
    private fun calenderFunc() : ArrayList<Int> {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dom = calendar.get(Calendar.DAY_OF_MONTH)
        return arrayListOf(dom,month,year)
    }

    /** Date picker */
    @SuppressLint("SetTextI18n")
    private fun customDatePicker(dateList : ArrayList<Int>){

        val dp = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { _, year, month, dom ->
            binding.btnDateAddpayment.text = "$dom.${month+1}.$year"
        }, dateList[2], dateList[1], dateList[0])

        dp.datePicker.maxDate = System.currentTimeMillis()
        dp.setButton(DialogInterface.BUTTON_POSITIVE, "Seç", dp)
        dp.setButton(DialogInterface.BUTTON_NEGATIVE, "İptal", dp)
        dp.show()
    }

}
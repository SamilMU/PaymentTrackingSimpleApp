package com.example.paymenttracking.view

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
    lateinit var paymentTypeObj : PaymentTypeEntity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setActivityView()
        clickListeners()
    }

    /** Views*/
    fun setActivityView(){
        binding = ActivityPaymentAdditionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        paymentTypeObj = intent.getSerializableExtra("paymentTypeObject") as PaymentTypeEntity
        binding.tvToolbarAddpayment.text = paymentTypeObj.title.uppercase()
        val dateList = calenderFunc()
        binding.btnDateAddpayment.text = "${dateList.get(0)}.${dateList.get(1)+1}.${dateList.get(2)}"

    }

    /** Click Listeners */
    fun clickListeners(){
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
                    val newPayment = PaymentEntity(binding.btnDateAddpayment.text.toString(),binding.etAmountAddpayment.text.toString().toDouble())
                    newPayment.owner = paymentTypeObj

                    val intent = Intent()
                    PaymentLogic.addPayment(this,newPayment)
                    setResult(RESULT_OK,intent)
                    finish()
                }
            }catch (e : NumberFormatException){
                Toast.makeText(this,"Girilen ödeme miktarı hatalıdır!", Toast.LENGTH_SHORT).show()
            }

        }
    }

    /** Func to get current date */
    fun calenderFunc() : ArrayList<Int> {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dom = calendar.get(Calendar.DAY_OF_MONTH)
        return arrayListOf(dom,month,year)
    }

    /** Date picker */
    fun customDatePicker(dateList : ArrayList<Int>){

        val dp = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { datePicker, i, i2, i3 ->
            binding.btnDateAddpayment.text = "$i3.${i2+1}.$i"
        }, dateList.get(2), dateList.get(1), dateList.get(0))

        dp.datePicker.maxDate = System.currentTimeMillis()
        dp.setButton(DialogInterface.BUTTON_POSITIVE, "Seç", dp)
        dp.setButton(DialogInterface.BUTTON_NEGATIVE, "İptal", dp)
        dp.show()
    }

}
package com.example.paymenttracking.bll

import android.app.AlertDialog
import android.content.Context
import android.widget.Toast
import com.example.paymenttracking.dal.PaymentOperation
import com.example.paymenttracking.model.PaymentTypeEntity

class PaymentTypeLogic {

    companion object{

        fun addPaymentType(context: Context, paymentTypeArg: PaymentTypeEntity) : Boolean {
            val paymentOperation = PaymentOperation(context)

            if(paymentOperation.getAllPaymentTypes().size>0){
                paymentOperation.getAllPaymentTypes().forEach{
                    // Similar name but different ID.
                    if(it.Id != paymentTypeArg.Id && it.title == paymentTypeArg.title){
                        val adb = AlertDialog.Builder(context)
                        adb.setTitle("Var olan tip")
                        adb.setMessage("Belirttiğiniz isimde bir ödeme tipi mevcut. Yinede eklemek istiyor musunuz ?")

                        adb.setPositiveButton("Evet") { _, _ ->
                            paymentOperation.addPaymentType(paymentTypeArg)
                            Toast.makeText(context, "Benzer isimli yeni tip oluşturuldu", Toast.LENGTH_SHORT).show()
                        }
                        adb.setNegativeButton("Hayır",null)
                        return true
                    }
                    // Same ID. An Update
                    else if(it.Id == paymentTypeArg.Id){
                        paymentOperation.updateType(paymentTypeArg)
                        Toast.makeText(context, "Tip güncellendi", Toast.LENGTH_SHORT).show()
                        return true
                    }
                }
                // If it reached this point, it should be added normally
                paymentOperation.addPaymentType(paymentTypeArg)
                Toast.makeText(context, "Yeni tip eklendi.", Toast.LENGTH_SHORT).show()
                return true
            }else{
                paymentOperation.addPaymentType(paymentTypeArg)
                return true
            }

        }

        fun getAllPaymentTypes(context: Context) : ArrayList<PaymentTypeEntity> {
            val paymentOperation = PaymentOperation(context)
            return paymentOperation.getAllPaymentTypes()
        }

        fun getSpecificPaymentType(context: Context, paymentTypeId : Int) {
            val paymentOperation = PaymentOperation(context)
            paymentOperation.getSpecificPaymentType(paymentTypeId)
        }

        fun clearTable(context: Context) : Boolean{
            val paymentOperation = PaymentOperation(context)
            return paymentOperation.clearTypeTable()
        }

        fun deleteType(context: Context, paymentTypeId: Int) : Boolean{
            val paymentOperation = PaymentOperation(context)
            return paymentOperation.deleteType(paymentTypeId)
        }
    }
}
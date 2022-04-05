package com.example.paymenttracking.bll

import android.content.Context
import android.widget.Toast
import com.example.paymenttracking.dal.PaymentOperation
import com.example.paymenttracking.model.PaymentTypeEntity

class PaymentTypeLogic {

    companion object{

        fun addPaymentType(context: Context, paymentTypeArg: PaymentTypeEntity) : Boolean {
            val paymentOperation = PaymentOperation(context)
            return paymentOperation.addPaymentType(paymentTypeArg).also {
                Toast.makeText(context, "Yeni tip eklendi.", Toast.LENGTH_SHORT).show()
            }
        }

        fun updatePaymentType(context: Context, paymentTypeArg: PaymentTypeEntity) : Boolean{
            val paymentOperation = PaymentOperation(context)
            return paymentOperation.updateType(paymentTypeArg).also {
                Toast.makeText(context, "Tip güncellendi", Toast.LENGTH_SHORT).show()
            }
        }

        fun getAllPaymentTypes(context: Context) : ArrayList<PaymentTypeEntity> {
            val paymentOperation = PaymentOperation(context)
            return paymentOperation.getAllPaymentTypes()
        }

        fun clearTable(context: Context) : Boolean{
            val paymentOperation = PaymentOperation(context)
            return paymentOperation.clearTypeTable()
        }

        fun deleteType(context: Context, paymentTypeId: Int) : Boolean{
            val paymentOperation = PaymentOperation(context)
            return paymentOperation.deleteType(paymentTypeId)
        }

         /*It is better to transfer local object rather than get it from DB every time a Detail
         page is requested. 2B Used in case of need
        fun getSpecificPaymentType(context: Context, paymentTypeId : Int) {
            val paymentOperation = PaymentOperation(context)
            paymentOperation.getSpecificPaymentType(paymentTypeId)
        }*/
    }
}
package com.example.paymenttracking.bll

import android.content.Context
import android.widget.Toast
import com.example.paymenttracking.dal.PaymentOperation
import com.example.paymenttracking.model.PaymentTypeEntity

class PaymentTypeLogic {

    companion object{

        fun addPaymentType(context: Context, paymentTypeArg: PaymentTypeEntity, notiToast: Boolean = true) : Boolean {
            val paymentOperation = PaymentOperation(context)
            return paymentOperation.addPaymentType(paymentTypeArg).also {
                if(it)
                    if (notiToast)
                            Toast.makeText(context, "Yeni tip eklendi.", Toast.LENGTH_SHORT).show()
            }
        }

        fun updatePaymentType(context: Context, paymentTypeArg: PaymentTypeEntity) : Boolean{
            val paymentOperation = PaymentOperation(context)
            return paymentOperation.updateType(paymentTypeArg).also {
                if(it)
                    Toast.makeText(context, "Tip güncellendi", Toast.LENGTH_SHORT).show()
            }
        }

        fun getAllPaymentTypes(context: Context) : ArrayList<PaymentTypeEntity> {
            val paymentOperation = PaymentOperation(context)
            return paymentOperation.getAllPaymentTypes()
        }

        fun clearTable(context: Context) : Boolean{
            val paymentOperation = PaymentOperation(context)
            paymentOperation.clearTypeTable()
            return paymentOperation.getAllPaymentTypes().isNullOrEmpty()
        }

        fun deleteType(context: Context, paymentTypeId: Int, notiToast: Boolean = true) : Boolean{
            val paymentOperation = PaymentOperation(context)
            return paymentOperation.deleteType(paymentTypeId).also {
                if(it)
                    if (notiToast)
                        Toast.makeText(context, "Ödeme tipi silindi.", Toast.LENGTH_SHORT).show()
            }
        }

         /*It is better to transfer local object rather than get it from DB every time a Detail
         page is requested. 2B Used in case of need
        fun getSpecificPaymentType(context: Context, paymentTypeId : Int) {
            val paymentOperation = PaymentOperation(context)
            paymentOperation.getSpecificPaymentType(paymentTypeId)
        }*/
    }
}
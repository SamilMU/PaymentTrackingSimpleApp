package com.example.paymenttracking.bll

import android.content.Context
import android.widget.Toast
import com.example.paymenttracking.dal.PaymentOperation
import com.example.paymenttracking.model.PaymentEntity
import com.example.paymenttracking.model.PaymentTypeEntity
import com.google.android.material.snackbar.Snackbar

class PaymentLogic {

    companion object{
        fun addPayment(context: Context, paymentArg: PaymentEntity) : Boolean{
            val paymentOperation = PaymentOperation(context)
            return paymentOperation.addPayment(paymentArg).also {
                if(it)
                    Toast.makeText(context,"Ödeme başarıyla eklenmiştir", Toast.LENGTH_SHORT).show()
            }
        }
        fun deletePayment(context: Context, paymentIdArg: Int) : Boolean{
            val paymentOperation = PaymentOperation(context)
            return paymentOperation.deletePayment(paymentIdArg)
        }
        fun clearPayments(context: Context) : Boolean{
            val paymentOperation = PaymentOperation(context)
            paymentOperation.clearPayments()
            return paymentOperation.getAllPayments().isNullOrEmpty()
        }
        fun getSpesificPayments(context: Context, paymentTypeArg: PaymentTypeEntity) : ArrayList<PaymentEntity>{
            val paymentOperation = PaymentOperation(context)
            return paymentOperation.getSpecificPayments(paymentTypeArg)
        }
    }
}
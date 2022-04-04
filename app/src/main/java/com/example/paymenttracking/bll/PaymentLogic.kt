package com.example.paymenttracking.bll

import android.content.Context
import com.example.paymenttracking.dal.PaymentOperation
import com.example.paymenttracking.model.PaymentEntity
import com.example.paymenttracking.model.PaymentTypeEntity

class PaymentLogic {

    companion object{
        fun addPayment(context: Context, paymentArg: PaymentEntity){
            val paymentOperation = PaymentOperation(context)
            paymentOperation.addPayment(paymentArg)
        }
        fun deletePayment(context: Context, paymentIdArg: Int){
            val paymentOperation = PaymentOperation(context)
            paymentOperation.deletePayment(paymentIdArg)
        }
        fun clearPayments(context: Context){
            val paymentOperation = PaymentOperation(context)
            paymentOperation.clearPayments()
        }
        fun getSpesificPayments(context: Context, paymentTypeArg: PaymentTypeEntity) : ArrayList<PaymentEntity>{
            val paymentOperation = PaymentOperation(context)
            return paymentOperation.getSpecificPayments(paymentTypeArg)
        }
    }
}
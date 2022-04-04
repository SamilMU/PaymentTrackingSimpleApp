package com.example.paymenttracking.view.listing.payments

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.paymenttracking.model.PaymentEntity
import com.example.paymenttracking.R

class PaymentViewHolder(itemView : View, var itemClick: (paymentArg : PaymentEntity) -> Unit) : RecyclerView.ViewHolder(itemView) {

    var sharedPaymentObj = PaymentEntity()
    /** Define View Elements*/
    val tv_date : TextView
    val tv_amount : TextView


    init {
        tv_date = itemView.findViewById(R.id.tv_date_paymentcard)
        tv_amount = itemView.findViewById(R.id.tv_amount_paymentcard)

        itemView.setOnClickListener {
            itemClick(sharedPaymentObj)
        }
    }

    fun bindData(paymentArg : PaymentEntity){
        sharedPaymentObj = paymentArg
        tv_date.text = paymentArg.date
        tv_amount.text = paymentArg.amount.toString()
    }
}
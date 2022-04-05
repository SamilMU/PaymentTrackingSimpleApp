package com.example.paymenttracking.view.listing.payments

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.paymenttracking.model.PaymentEntity
import com.example.paymenttracking.R

class PaymentViewHolder(itemView : View, var itemClick: (paymentArg : PaymentEntity) -> Unit) : RecyclerView.ViewHolder(itemView) {

    private var sharedPaymentObj = PaymentEntity()
    /** Define View Elements*/
    private val tvdate : TextView
    private val tvamount : TextView


    init {
        tvdate = itemView.findViewById(R.id.tv_date_paymentcard)
        tvamount = itemView.findViewById(R.id.tv_amount_paymentcard)

        itemView.setOnClickListener {
            itemClick(sharedPaymentObj)
        }
    }

    fun bindData(paymentArg : PaymentEntity){
        sharedPaymentObj = paymentArg
        tvdate.text = paymentArg.date
        tvamount.text = paymentArg.amount.toString()
    }
}
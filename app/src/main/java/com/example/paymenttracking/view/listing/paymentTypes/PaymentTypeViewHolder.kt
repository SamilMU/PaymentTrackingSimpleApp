package com.example.paymenttracking.view.listing.paymentTypes

import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.example.paymenttracking.model.PaymentTypeEntity
import com.example.paymenttracking.R

class PaymentTypeViewHolder(
    itemView: View,
    var cardClick: (paymentTypeEntity: PaymentTypeEntity) -> Unit,
    var paymentClick: (paymentTypeEntity: PaymentTypeEntity) -> Unit
) : RecyclerView.ViewHolder(itemView) {

    /** Define View Elements */
    var tv_title: TextView
    var tv_period: TextView
    var btn_add_payment: AppCompatButton

    // Shared object 2 be sent to cardClick
    lateinit var paymentObject: PaymentTypeEntity

    init {
        tv_title = itemView.findViewById(R.id.tv_title_typecard)
        tv_period = itemView.findViewById(R.id.tv_period_typecard)
        btn_add_payment = itemView.findViewById(R.id.btn_addpayment_typecard)

        itemView.setOnClickListener {
            cardClick(paymentObject)
        }

    }

    fun bindData(paymentTypeArg: PaymentTypeEntity) {
        paymentObject = paymentTypeArg
        tv_title.text = paymentTypeArg.title

        if (paymentTypeArg.period.isNullOrEmpty()) {
            tv_period.text = "Ödeme planı yok."
        } else if (paymentTypeArg.timeOfPeriod == null || paymentTypeArg.timeOfPeriod == 0) {
            tv_period.text = "${paymentTypeArg.period}"
        } else {
            tv_period.text = "${paymentTypeArg.period}, ${paymentTypeArg.timeOfPeriod}.günü"
        }


        btn_add_payment.setOnClickListener {
            paymentClick(paymentTypeArg)
        }

    }


}
package com.example.paymenttracking.view.listing.paymentTypes

import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.example.paymenttracking.model.PaymentTypeEntity
import com.example.paymenttracking.R

class PaymentTypeViewHolder(
    itemView: View,
    private var cardClick: (paymentTypeEntity: PaymentTypeEntity) -> Unit,
    private var paymentClick: (paymentTypeEntity: PaymentTypeEntity) -> Unit
) : RecyclerView.ViewHolder(itemView) {

    /** Define View Elements */
    private var tvtitle: TextView
    private var tvperiod: TextView
    private var btnaddpayment: AppCompatButton

    // Shared object 2 be sent to cardClick
    lateinit var paymentObject: PaymentTypeEntity

    init {
        tvtitle = itemView.findViewById(R.id.tv_title_typecard)
        tvperiod = itemView.findViewById(R.id.tv_period_typecard)
        btnaddpayment = itemView.findViewById(R.id.btn_addpayment_typecard)

        itemView.setOnClickListener {
            cardClick(paymentObject)
        }

    }

    fun bindData(paymentTypeArg: PaymentTypeEntity) {
        paymentObject = paymentTypeArg
        tvtitle.text = paymentTypeArg.title

        if (paymentTypeArg.period == null) {
            tvperiod.text = "Ödeme planı yok."
        } else if (paymentTypeArg.timeOfPeriod == null || paymentTypeArg.timeOfPeriod == 0) {
            tvperiod.text = "${paymentTypeArg.period!!.str}"
        } else {
            tvperiod.text = "${paymentTypeArg.period!!.str}, ${paymentTypeArg.timeOfPeriod}.günü"
        }


        btnaddpayment.setOnClickListener {
            paymentClick(paymentTypeArg)
        }

    }


}
package com.example.paymenttracking.view.listing.paymentTypes

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.paymenttracking.model.PaymentTypeEntity
import com.example.paymenttracking.R

class PaymentTypeAdapter(
    val mContext: Context,
    var paymentTypeList: ArrayList<PaymentTypeEntity>,
    var cardClick: (paymentTypeEntity: PaymentTypeEntity) -> Unit,
    var paymentClick: (paymentTypeEntity: PaymentTypeEntity) -> Unit
) : RecyclerView.Adapter<PaymentTypeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentTypeViewHolder {
        val v = LayoutInflater.from(mContext).inflate(R.layout.payment_type_card, parent, false)

        return PaymentTypeViewHolder(v, cardClick, paymentClick)
    }

    override fun onBindViewHolder(holder: PaymentTypeViewHolder, position: Int) {
        holder.bindData(paymentTypeList.get(position))
    }

    override fun getItemCount(): Int {
        return paymentTypeList.size
    }
}
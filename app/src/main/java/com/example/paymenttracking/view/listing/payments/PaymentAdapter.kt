package com.example.paymenttracking.view.listing.payments

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.paymenttracking.model.PaymentEntity
import com.example.paymenttracking.R

class PaymentAdapter(var context: Context,
                     var paymentList: ArrayList<PaymentEntity>,
                     var itemClick: (paymentArg : PaymentEntity) -> Unit)
    : RecyclerView.Adapter<PaymentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.payment_card, parent, false)
        return PaymentViewHolder(v,itemClick)
    }

    override fun onBindViewHolder(holder: PaymentViewHolder, position: Int) {
        holder.bindData(paymentList.get(position))
    }

    override fun getItemCount(): Int {
        return paymentList.size
    }
}
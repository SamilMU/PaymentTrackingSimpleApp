package com.example.paymenttracking.view.listing.paymentTypes

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.paymenttracking.model.PaymentTypeEntity
import com.example.paymenttracking.R

class PaymentTypeAdapter(
    val context: Context,
    private var paymentTypeList: ArrayList<PaymentTypeEntity>,
    private var cardClick: (paymentTypeEntity: PaymentTypeEntity) -> Unit,
    private var paymentClick: (paymentTypeEntity: PaymentTypeEntity) -> Unit
) : RecyclerView.Adapter<PaymentTypeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentTypeViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.payment_type_card, parent, false)
        return PaymentTypeViewHolder(v, cardClick, paymentClick)
    }

    override fun onBindViewHolder(holder: PaymentTypeViewHolder, position: Int) {
        holder.bindData(paymentTypeList[position])
    }

    override fun getItemCount(): Int {
        return paymentTypeList.size
    }
}
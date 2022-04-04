package com.example.paymenttracking.model

import java.io.Serializable

class PaymentEntity(var date : String = "",
                    var amount: Double = 0.0) : Serializable{

    var Id = 0
//            by Delegates.notNull<Int>()

    lateinit var owner: PaymentTypeEntity
}
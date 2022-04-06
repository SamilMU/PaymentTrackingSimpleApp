package com.example.paymenttracking.model

import java.io.Serializable

class PaymentEntity() : Serializable{

    var date : String = ""
    var amount: Double = 0.0
    var id = 0

    // Foreign Key
    var ownerId: Int = 0
}
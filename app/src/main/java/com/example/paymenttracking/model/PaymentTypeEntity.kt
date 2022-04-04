package com.example.paymenttracking.model

import java.io.Serializable

class PaymentTypeEntity(
    var title: String = "",
    var period: String? = null,
    var timeOfPeriod : Int? = null,
) : Serializable {

    var Id : Int = 0
//            by Delegates.notNull<Int>()

}

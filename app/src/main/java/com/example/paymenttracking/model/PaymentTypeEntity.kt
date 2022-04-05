package com.example.paymenttracking.model

import java.io.Serializable

class PaymentTypeEntity : Serializable {

    var title: String = ""
    var period: String? = null
    var timeOfPeriod : Int? = null
    var id : Int = 0
//            by Delegates.notNull<Int>()

}

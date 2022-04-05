package com.example.paymenttracking.model

import java.io.Serializable

class PaymentTypeEntity : Serializable {

    var title: String = ""
    // TODO Enum Class 4 Periods
    var timeOfPeriod : Int? = null
    var id : Int = 0
    var period: TypePeriods? = null
//            by Delegates.notNull<Int>()

}

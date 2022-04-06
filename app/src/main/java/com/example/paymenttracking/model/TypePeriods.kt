package com.example.paymenttracking.model

enum class TypePeriods(val str : String) {
    Gunluk("Günlük"),
    Haftalik("Haftalık"),
    Aylik("Aylık"),
    Yillik("Yıllık");

    companion object {
        fun getPaymentTypeByStr(str: String?) : TypePeriods? {
            values().forEach {
                if(it.str==str){
                    return it
                }
            }
            return null
        }
    }
}
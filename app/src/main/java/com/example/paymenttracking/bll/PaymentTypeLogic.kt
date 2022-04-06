package com.example.paymenttracking.bll

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import com.example.paymenttracking.dal.PaymentOperation
import com.example.paymenttracking.model.PaymentTypeEntity
import com.example.paymenttracking.model.TypePeriods

class PaymentTypeLogic {

    companion object {

        fun addPaymentType(
            context: Context,
            paymentTypeArg: PaymentTypeEntity,
            notiToast: Boolean = true
        ): Boolean {
            val paymentOperation = PaymentOperation(context)
            return paymentOperation.addPaymentType(paymentTypeArg).also {
                if (it)
                    if (notiToast)
                        Toast.makeText(context, "Yeni tip eklendi.", Toast.LENGTH_SHORT).show()
            }
        }

        fun updatePaymentType(context: Context, paymentTypeArg: PaymentTypeEntity): Boolean {
            val paymentOperation = PaymentOperation(context)
            return paymentOperation.updateType(paymentTypeArg).also {
                if (it)
                    Toast.makeText(context, "Tip güncellendi", Toast.LENGTH_SHORT).show()
            }
        }

        fun getAllPaymentTypes(context: Context): ArrayList<PaymentTypeEntity> {
            val paymentOperation = PaymentOperation(context)
            return paymentOperation.getAllPaymentTypes()
        }

        fun clearTable(context: Context): Boolean {
            val paymentOperation = PaymentOperation(context)
            paymentOperation.clearTypeTable()
            return paymentOperation.getAllPaymentTypes().isNullOrEmpty()
        }

        fun deleteType(context: Context, paymentTypeId: Int, notiToast: Boolean = true): Boolean {
            val paymentOperation = PaymentOperation(context)
            return paymentOperation.deleteType(paymentTypeId).also {
                if (it)
                    if (notiToast)
                        Toast.makeText(context, "Ödeme tipi silindi.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /** Write Shared Pref Func. Used for Default(Init) Types */
    fun writeToSharedPreferences(
        editor: SharedPreferences.Editor,
        paymentTypeList: List<PaymentTypeEntity>,
        itemCount: Int
    ): ArrayList<Int> {
        val defaultIDList = ArrayList<Int>()
        // Switch state is stored in sharedPref
        editor.putBoolean("switchChecked", true)
        var localCounter = 0
        // Saving added default types to sharedPref and local list.
        paymentTypeList.takeLast(itemCount).forEach {
            // Local id list of default types.
            defaultIDList.add(it.id)
            // Shared pref id list of default types.
            editor.putInt("defId$localCounter", it.id)
            // To be used in case of app close.
            editor.putInt("itemCount", localCounter)
            editor.apply()
            localCounter++
        }

        return defaultIDList
    }

    /** Read From Shared Pref and Delete Default Types*/
    fun readFromSharedPreferencesAndDelete(
        context: Context,
        editor: SharedPreferences.Editor,
        pref: SharedPreferences,
        itemCount: Int
    ){
        val defaultIDList = ArrayList<Int>()

        // Switch state is changed in shared pref
        editor.putBoolean("switchChecked",false)
        // No more input to sharedpref after context point.
        editor.apply()

        var size = itemCount
        // If app is closed, itemCount is 0 because MainActivity is finished.
        if(size == 0){
            size = pref.getInt("itemCount", 0)
        }
        // Filling list again if app is closed after switching on.
        if (defaultIDList.isEmpty()) {
            for (i in 0..size) {
                defaultIDList.add(pref.getInt("defId$i", 0))
            }
        }

        // Iterate over list and delete each type
        defaultIDList.forEach {
            deleteType(context, it, false)
        }
    }

    fun createInitTypes(context: Context) : Int{
        var itemCount : Int = 0
        val paymentType1 = PaymentTypeEntity().apply {
            title = "Elektrik Faturası"
            timeOfPeriod = 15
            period = TypePeriods.Aylik
            itemCount++
        }
        val paymentType2 = PaymentTypeEntity().apply {
            title = "Su Faturası"
            timeOfPeriod = 180
            period = TypePeriods.Yillik
            itemCount++
        }
        val paymentType3 = PaymentTypeEntity().apply {
            title = "Doğalgaz Faturası"
            period = TypePeriods.Haftalik
            itemCount++
        }
        val paymentType5 = PaymentTypeEntity().apply {
            title = "Mutfak Giderleri"
            period = TypePeriods.Gunluk
            itemCount++
        }
        val paymentType4 = PaymentTypeEntity().apply {
            title = "İnternet Faturası"
            itemCount++
        }
        addPaymentType(context, paymentType1,false)
        addPaymentType(context, paymentType2,false)
        addPaymentType(context, paymentType3,false)
        addPaymentType(context, paymentType4,false)
        addPaymentType(context, paymentType5,false)

        return itemCount
    }
}
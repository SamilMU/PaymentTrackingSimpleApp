package com.example.paymenttracking.dal

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.paymenttracking.model.PaymentEntity
import com.example.paymenttracking.model.PaymentTypeEntity

class PaymentOperation(context: Context) {
    private var paymentDB : SQLiteDatabase? = null
    private var dbOpenHelper: DatabaseOpenHelper

    /** Strings */
    private val typeTableStr = "PaymentType"
    private val paymentTableStr = "Payment"
    private val titleStr = "Title"
    private val periodStr = "Period"
    private val timeOfPeriodStr = "TimeOfPeriod"
    private val dateStr = "Date"
    private val amountStr = "Amount"
    private val ownerStr = "Owner"

    init {
        dbOpenHelper = DatabaseOpenHelper(context, "PaymentTracking",null,1)
    }

    /** Open Close DB */
    private fun openDB(){
        paymentDB = dbOpenHelper.writableDatabase
    }
    private fun closeDB(){
        if(paymentDB != null && paymentDB!!.isOpen){
            paymentDB!!.close()
        }
    }

    /** Payment Type Functions */
    // Get All Payment Types
    @SuppressLint("Range")
    fun getAllPaymentTypes() : ArrayList<PaymentTypeEntity>{
        val typeList : ArrayList<PaymentTypeEntity> = arrayListOf()
        var paymentTypeObject : PaymentTypeEntity

        openDB()
        val c : Cursor = getAllPaymentTypesCursor()

        // Go to specified row/record. Returns false if non-existent
        if (c.moveToFirst()){
            do{
                paymentTypeObject = PaymentTypeEntity()
                // Get data from 0th column of selected row(outer).
                paymentTypeObject.id = c.getInt(0)
                // Preferred method over getting data from index. Indexes may shift.
                paymentTypeObject.title = c.getString(c.getColumnIndex(titleStr))
                paymentTypeObject.period = c.getString(c.getColumnIndex(periodStr))
                paymentTypeObject.timeOfPeriod = c.getInt(3)
                typeList.add(paymentTypeObject)
            }while (c.moveToNext())

        }

        closeDB()

        return typeList
    }

    // Used in GetAll
    private fun getAllPaymentTypesCursor() : Cursor {

        val query = "SELECT * FROM $typeTableStr"

        return paymentDB!!.rawQuery(query, null)
    }

    // Get One Payment Type
/*
    @SuppressLint("Range")
    fun getSpecificPaymentType(paymentTypeId : Int){
        val paymentTypeObject : PaymentTypeEntity

        openDB()
        val dbObject = paymentDB!!.rawQuery("SELECT * FROM $typeTableStr WHERE Id = $paymentTypeId", null)

        if(dbObject.moveToFirst()) {
            paymentTypeObject = PaymentTypeEntity()
            // Get data from 0th column of selected row(outer).
            paymentTypeObject.Id = dbObject.getInt(0)
            // Preferred method over getting data from index. Indexes may shift.
            paymentTypeObject.title = dbObject.getString(dbObject.getColumnIndex(titleStr))
            paymentTypeObject.period = dbObject.getString(dbObject.getColumnIndex(periodStr))
            paymentTypeObject.timeOfPeriod = dbObject.getInt(3)
        }
        closeDB()
    }
*/

    // Add Type
    fun addPaymentType(paymentTypeArg: PaymentTypeEntity) : Boolean{
        val cv = ContentValues()
        cv.put(titleStr,paymentTypeArg.title)
        cv.put(periodStr, paymentTypeArg.period)
        cv.put(timeOfPeriodStr, paymentTypeArg.timeOfPeriod)
        openDB()
        val effectedRowCount = paymentDB!!.insert(typeTableStr, null, cv)
        closeDB()

        // TODO Return count to give a Toast.
        if(effectedRowCount != -1L){
            return false
        }
        return true
    }

    // Update Type
    fun updateType(paymentTypeArg: PaymentTypeEntity) : Boolean{
        val cv = ContentValues()
        cv.put(titleStr,paymentTypeArg.title)
        cv.put(periodStr, paymentTypeArg.period)
        cv.put(timeOfPeriodStr, paymentTypeArg.timeOfPeriod)

        openDB()
        paymentDB!!.update(typeTableStr, cv, "Id = ?", arrayOf(paymentTypeArg.id.toString()))
        closeDB()

        // Check rowCount return
        return true
    }

    // Delete Type
    fun deleteType(paymentTypeId: Int): Boolean{
        openDB()
        val effectedRowCount = paymentDB!!.delete(typeTableStr,"Id = ?", arrayOf(paymentTypeId.toString()))
        closeDB()

        return effectedRowCount>0
    }

    // Clear Type Table
    fun clearTypeTable() : Boolean{
        openDB()
        paymentDB!!.execSQL("delete from $typeTableStr")
        closeDB()

        return false
    }

    /** Payment Functions */
    // Add Payment
    fun addPayment(paymentArg : PaymentEntity) : Boolean{
        val cv = ContentValues()
        cv.put(dateStr , paymentArg.date)
        cv.put(amountStr, paymentArg.amount)
        cv.put(ownerStr, paymentArg.owner.id)

        openDB()
        val effectedRowCount = paymentDB!!.insert(paymentTableStr, null, cv)
        closeDB()

        // TODO Return count to give a Toast.
        if(effectedRowCount != -1L){
            return false
        }
        return true
    }

    // Delete Payment
    fun deletePayment(paymentId : Int) : Boolean{

        openDB()
        val effectedRowCount = paymentDB!!.delete(paymentTableStr,"Id = ?",arrayOf(paymentId.toString()))
        closeDB()

        print(effectedRowCount.toString())
        // Check rowCount return
        return true
    }

    // Clear Payment Table
    fun clearPayments() {
        openDB()
        paymentDB!!.execSQL("delete from $paymentTableStr")
        closeDB()
    }

    // Get Payments of One Type
    @SuppressLint("Range")
    fun getSpecificPayments(paymentTypeArg: PaymentTypeEntity) : ArrayList<PaymentEntity> {
        val paymentList4Return : ArrayList<PaymentEntity> = arrayListOf()
        var paymentObject : PaymentEntity

        openDB()
        val dbObject = paymentDB!!.rawQuery("SELECT * FROM $paymentTableStr WHERE Owner = ${paymentTypeArg.id}", null)

        if(dbObject.moveToFirst()){
            do {
                paymentObject = PaymentEntity()
                // Get data from 0th column of selected row(outer).
                paymentObject.id = dbObject.getInt(0)
                // Preferred method over getting data from index. Indexes may shift.
                paymentObject.date = dbObject.getString(dbObject.getColumnIndex(dateStr))
                paymentObject.amount = dbObject.getDouble(dbObject.getColumnIndex(amountStr))
                paymentList4Return.add(paymentObject)
            }while (dbObject.moveToNext())
        }
        dbObject.close()
        closeDB()

        return paymentList4Return
    }

}
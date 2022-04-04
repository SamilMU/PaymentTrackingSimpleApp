package com.example.paymenttracking.dal

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseOpenHelper(context: Context, name : String, factory: SQLiteDatabase.CursorFactory?, ver : Int) : SQLiteOpenHelper(context,name,factory,ver){

    override fun onCreate(p0: SQLiteDatabase) {
        val typeTable = "CREATE TABLE PaymentType(Id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, Title TEXT, Period TEXT, TimeOfPeriod INTEGER)"
        val paymentTable = "CREATE TABLE Payment(Id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, Date TEXT, Amount REAL, Owner INTEGER NOT NULL, FOREIGN KEY(Owner) REFERENCES PaymentType(Id))"
        p0.execSQL(typeTable)
        p0.execSQL(paymentTable)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {}
}
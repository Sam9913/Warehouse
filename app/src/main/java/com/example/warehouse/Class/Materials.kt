package com.example.warehouse.Class

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName ="materials")
data class Materials(@PrimaryKey val MaterialID: Int,
    val SerialNo: String,
    val Quantity: Int,
    val Status: Int,
    val ReceivedDate: String,
    val ReceivedBy: Int) {
}
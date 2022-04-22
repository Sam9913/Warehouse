package com.example.warehouse.Class

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName ="racks")
data class Racks(@PrimaryKey val RackID: Int,
    val RackName: String) {
}
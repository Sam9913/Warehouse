package com.example.warehouse.Class

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName ="user")
data class User(@PrimaryKey val uid: Int,
                @ColumnInfo(name = "username") val Username: String,
                @ColumnInfo(name = "password") val Password: String) {
}
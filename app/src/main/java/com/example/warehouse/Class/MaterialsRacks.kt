package com.example.warehouse.Class

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName ="materialsRacks")
data class MaterialsRacks(@PrimaryKey val MaterialRackID: Int,
        val RackNo: Int,
        val PartNo: String,
        val SerialNo: String,
        val RackInDate: String,
        val RackOutDate: String) {
}
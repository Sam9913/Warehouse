package com.example.warehouse.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.warehouse.Class.MaterialsRacks
import com.example.warehouse.Class.User

@Dao
interface MRackDAO {
    @Query("SELECT * FROM materialsRacks")
    fun getAll(): List<MaterialsRacks>

    @Query("SELECT * FROM materialsRacks WHERE SerialNo LIKE :serialNo")
    fun findBySerialNo(serialNo: String): MaterialsRacks

    @Query("UPDATE materialsRacks SET RackOutDate = :rackOutDate WHERE SerialNo LIKE :serialNo")
    fun updateRackOutDate(rackOutDate: String, serialNo: String)

    @Insert
    fun insertAll(vararg materialsRacks: MaterialsRacks)

    @Delete
    fun delete(materialsRacks: MaterialsRacks)
}
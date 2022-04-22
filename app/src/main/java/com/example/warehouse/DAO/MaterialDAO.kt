package com.example.warehouse.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.warehouse.Class.Materials

@Dao
interface MaterialDAO {
    @Query("SELECT * FROM materials")
    fun getAll(): List<Materials>

    @Query("SELECT * FROM materials WHERE Status = :status")
    fun getAllWithStatus(status: Int): List<Materials>

    @Query("SELECT * FROM materials WHERE SerialNo LIKE :serialNo")
    fun findBySerialNo(serialNo: String): Materials

    @Query("SELECT * FROM materials WHERE MaterialID IN (:materialIds)")
    fun loadAllByIds(materialIds: IntArray): List<Materials>

    @Query("UPDATE materials SET Status = :status WHERE SerialNo LIKE :serialNo")
    fun updateStatus(status: Int, serialNo: String)

    @Query("UPDATE materials SET Quantity = :qty WHERE SerialNo LIKE :serialNo")
    fun updateQuantity(qty: Int, serialNo: String)

    @Insert
    fun insertAll(vararg materials: Materials)
}
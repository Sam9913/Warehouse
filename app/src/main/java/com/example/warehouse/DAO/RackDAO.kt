package com.example.warehouse.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.warehouse.Class.Racks

@Dao
interface RackDAO {
    @Query("SELECT * FROM racks")
    fun getAll(): List<Racks>

    @Query("SELECT * FROM racks WHERE RackID IN (:rackIds)")
    fun loadAllByIds(rackIds: IntArray): List<Racks>

    @Query("SELECT * FROM racks WHERE RackName LIKE :rackName")
    fun findByRackName(rackName: String): Racks

    @Query("SELECT * FROM racks WHERE RackID LIKE :rackNo")
    fun findByRackNo(rackNo: Int): Racks

    @Insert
    fun insertAll(vararg racks: Racks)
}
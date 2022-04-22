package com.example.warehouse.DAO

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.warehouse.Class.Issue
import com.example.warehouse.Class.Materials

@Dao
interface IssueDAO {
    @Query("SELECT * FROM issues")
    fun getAll(): List<Issue>

    @Insert
    fun insertAll(vararg issues: Issue)
}
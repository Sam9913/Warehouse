package com.example.warehouse.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.warehouse.Class.User

@Dao
interface UserDAO {
    @Query("SELECT * FROM user")
    fun getAll(): List<User>

    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<User>

    @Query("SELECT * FROM user WHERE username LIKE :username AND " +
            "password LIKE :password")
    fun findByUsernameAndPassword(username: String, password: String): User

    @Query("SELECT * FROM user WHERE Username LIKE :username")
    fun findByUsername(username: String): User

    @Query("SELECT * FROM user WHERE uid LIKE :uid")
    fun findByUid(uid: Int): User

    @Query("UPDATE user SET Password = :password WHERE Username LIKE :username")
    fun updatePassword(username: String, password: String)

    @Insert
    fun insertAll(vararg users: User)

    @Delete
    fun delete(user: User)
}
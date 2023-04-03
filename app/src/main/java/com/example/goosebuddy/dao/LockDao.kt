package com.example.goosebuddy.dao

import androidx.room.*
import com.example.goosebuddy.models.Lock

@Dao
interface LockDao {
    @Query("SELECT * FROM Locks")
    fun getAll(): List<Lock>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(lock: Lock)

    @Delete
    fun delete(lock: Lock)

    @Update
    fun update(Lock: Lock)
}
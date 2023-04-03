package com.example.goosebuddy.dao

import androidx.room.*
import com.example.goosebuddy.models.Routines

@Dao
interface RoutinesDao {
    @Query("SELECT * FROM routines")
    fun getAll(): List<Routines>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg routines: Routines)

    @Delete
    fun delete(routines: Routines)

    @Update
    fun update(routines: Routines)
}
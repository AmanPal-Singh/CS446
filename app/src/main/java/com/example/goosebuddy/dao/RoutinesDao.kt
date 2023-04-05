package com.example.goosebuddy.dao

import androidx.room.*
import com.example.goosebuddy.models.Routines
import com.example.goosebuddy.models.Subroutines
import com.example.goosebuddy.models.RoutineWithSubroutine

@Dao
interface RoutinesDao {
    @Transaction
    @Query("SELECT * FROM routines")
    fun getAll(): List<RoutineWithSubroutine>

    @Transaction
    @Query("SELECT * FROM routines WHERE id = :routineId")
    fun get(routineId: Int): RoutineWithSubroutine

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg routines: Routines)

    @Delete
    fun delete(routines: Routines)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(routine: Routines, subroutines: List<Subroutines>)
}

@Dao
interface SubroutinesDao {
    @Query("SELECT * FROM subroutines")
    fun getAll(): List<Subroutines>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg subroutines: Subroutines)

    @Delete
    fun delete(subroutines: Subroutines)

    @Update
    fun update(subroutines: Subroutines)
}
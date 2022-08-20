package com.equationl.calculator_compose.database

import androidx.room.*
import com.equationl.calculator_compose.dataModel.HistoryData

@Dao
interface HistoryDao {
    @Query("select * from history order by id DESC")
    fun getAll(): List<HistoryData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: HistoryData)

    @Update
    fun update(item: HistoryData)

    @Delete
    fun delete(item: HistoryData)

    @Query("DELETE FROM history")
    fun deleteAll()
}
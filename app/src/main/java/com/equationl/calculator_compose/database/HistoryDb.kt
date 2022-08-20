package com.equationl.calculator_compose.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.equationl.calculator_compose.dataModel.HistoryData

@Database(
    entities = [HistoryData::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(HistoryConverters::class)
abstract class HistoryDb : RoomDatabase() {
    companion object {
        fun create(context: Context, useInMemory: Boolean = false): HistoryDb {
            val databaseBuilder = if (useInMemory) {
                Room.inMemoryDatabaseBuilder(context, HistoryDb::class.java)
            } else {
                Room.databaseBuilder(context, HistoryDb::class.java, "history.db")
            }
            return databaseBuilder
                .fallbackToDestructiveMigration()
                .build()
        }
    }

    abstract fun history(): HistoryDao
}
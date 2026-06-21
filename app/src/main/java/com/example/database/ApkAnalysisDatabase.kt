package com.example.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ApkAnalysisRecord::class], version = 1, exportSchema = false)
abstract class ApkAnalysisDatabase : RoomDatabase() {
    abstract fun apkAnalysisDao(): ApkAnalysisDao

    companion object {
        @Volatile
        private var INSTANCE: ApkAnalysisDatabase? = null

        fun getDatabase(context: Context): ApkAnalysisDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ApkAnalysisDatabase::class.java,
                    "malsentinel_analysis_db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}

package com.example.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ApkAnalysisDao {
    @Query("SELECT * FROM apk_analysis_records ORDER BY timestamp DESC")
    fun getAllHistory(): Flow<List<ApkAnalysisRecord>>

    @Query("SELECT * FROM apk_analysis_records WHERE id = :id LIMIT 1")
    suspend fun getRecordById(id: Long): ApkAnalysisRecord?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord(record: ApkAnalysisRecord): Long

    @Delete
    suspend fun deleteRecord(record: ApkAnalysisRecord)

    @Query("DELETE FROM apk_analysis_records")
    suspend fun clearHistory()
}

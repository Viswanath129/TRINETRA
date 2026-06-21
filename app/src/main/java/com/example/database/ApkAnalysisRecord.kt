package com.example.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "apk_analysis_records")
data class ApkAnalysisRecord(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val fileName: String,
    val packageName: String,
    val sizeInBytes: Long,
    val riskScore: Int,
    val verdict: String, // "Safe", "Warning", "High Risk", "Malicious"
    val permissions: String, // Comma separated permissions
    val sensitivePermissionsCount: Int,
    val matchedThreatFamily: String, // e.g. "Anubis.Banker", "Flubot.SMS", "None"
    val geminiNarrative: String,
    val mitreTechniques: String, // Comma or piped list of codes e.g. "T1425,T1411"
    val fraudVectors: String, // JSON format or simple string for vectors
    val timestamp: Long,
    val isSimulated: Boolean
)

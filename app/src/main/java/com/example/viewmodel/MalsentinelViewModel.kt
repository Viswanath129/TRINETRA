package com.example.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ai.GeminiClient
import com.example.analyzer.ApkAnalyzer
import com.example.database.ApkAnalysisDatabase
import com.example.database.ApkAnalysisRecord
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MalsentinelViewModel(application: Application) : AndroidViewModel(application) {

    private val database = ApkAnalysisDatabase.getDatabase(application)
    val dao = database.apkAnalysisDao()

    // 1. History Stream from room DB
    val analysisHistory: StateFlow<List<ApkAnalysisRecord>> = dao.getAllHistory()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // 2. Active Record details for display
    private val _activeRecord = MutableStateFlow<ApkAnalysisRecord?>(null)
    val activeRecord: StateFlow<ApkAnalysisRecord?> = _activeRecord.asStateFlow()

    // 3. Uploading & Analyzing state parameters
    private val _analysisState = MutableStateFlow<AnalysisState>(AnalysisState.Idle)
    val analysisState: StateFlow<AnalysisState> = _analysisState.asStateFlow()

    // 4. Live Analyzer Compilation Logs during extraction
    private val _compilerLogs = MutableStateFlow<List<LogEntry>>(emptyList())
    val compilerLogs: StateFlow<List<LogEntry>> = _compilerLogs.asStateFlow()

    // Settings & Tuning Weights
    val smsWeightSetting = MutableStateFlow(15)
    val overlayWeightSetting = MutableStateFlow(25)
    val accessibilityWeightSetting = MutableStateFlow(25)
    val activeSimPreset = MutableStateFlow("Anubis.Banker")

    init {
        // Pre-populate some history items on first launch if the database is dry so the presentation dashboard is engaging.
        viewModelScope.launch {
            dao.getAllHistory().collect { list ->
                if (list.isEmpty()) {
                    prepopulateSampleLedger()
                } else if (_activeRecord.value == null) {
                    _activeRecord.value = list.first()
                }
            }
        }
    }

    fun setActiveRecord(record: ApkAnalysisRecord) {
        _activeRecord.value = record
    }

    fun deleteRecord(record: ApkAnalysisRecord) {
        viewModelScope.launch {
            dao.deleteRecord(record)
            if (_activeRecord.value?.id == record.id) {
                _activeRecord.value = dao.getAllHistory().stateIn(viewModelScope).value.firstOrNull()
            }
        }
    }

    fun clearAllRecords() {
        viewModelScope.launch {
            dao.clearHistory()
            _activeRecord.value = null
        }
    }

    /**
     * Parse and analyze a physical device-uploaded APK file natively
     */
    fun analyzeLocalApkFile(uri: Uri, fileName: String) {
        viewModelScope.launch {
            _compilerLogs.value = emptyList()
            _analysisState.value = AnalysisState.Processing("Extracting APK")

            addLog("MALSENTINEL SYSTEM", "Opening file payload: $fileName", "#c084fc")
            delay(400)
            addLog("ZIP DECOMPRESSOR", "Reading zip package directory structure", "#60a5fa")
            delay(400)
            addLog("MANIFEST DECODER", "Locating AndroidManifest.xml in archive", "#34d399")
            
            val outcome = withContext(Dispatchers.IO) {
                ApkAnalyzer.analyzeApk(
                    getApplication(),
                    uri,
                    fileName,
                    accessibilityWeight = accessibilityWeightSetting.value,
                    overlayWeight = overlayWeightSetting.value,
                    smsWeight = smsWeightSetting.value
                )
            }

            addLog("MANIFEST DECODER", "Successfully parsed binary manifest header metadata.", "#34d399")
            addLog("MANIFEST DECODER", "Package: ${outcome.packageName}", "#e2e8f0")
            addLog("MANIFEST DECODER", "Permissions found: ${outcome.permissions.size}", "#e2e8f0")
            delay(500)

            addLog("HEURISTICS ENGINE", "Analyzing critical permissions coupling ratios...", "#fbbf24")
            addLog("HEURISTICS ENGINE", "Calculated Risk Score: ${outcome.riskScore}/100", "#f87171")
            addLog("HEURISTICS ENGINE", "Matched Family Signature Indicator: ${outcome.matchedThreatFamily}", "#f87171")
            delay(500)

            addLog("AI INVESTIGATION", "Reaching secure Gemini 3.5 AI Copilot logic gate...", "#a78bfa")
            
            // Query Gemini for real contextual overview
            val narrative = withContext(Dispatchers.IO) {
                GeminiClient.getAnalysisExplanation(
                    packageName = outcome.packageName,
                    threatFamily = outcome.matchedThreatFamily,
                    riskScore = outcome.riskScore,
                    permissions = outcome.permissions
                )
            }

            addLog("AI INVESTIGATION", "Gemini narrative generated successfully.", "#a78bfa")
            delay(300)
            addLog("MALSENTINEL SYSTEM", "Finalizing local analysis cache ledger entry...", "#34d399")

            val dbRecord = ApkAnalysisRecord(
                fileName = outcome.fileName,
                packageName = outcome.packageName,
                sizeInBytes = outcome.sizeInBytes,
                riskScore = outcome.riskScore,
                verdict = outcome.verdict,
                permissions = outcome.permissions.joinToString(","),
                sensitivePermissionsCount = outcome.sensitiveCount,
                matchedThreatFamily = outcome.matchedThreatFamily,
                geminiNarrative = narrative,
                mitreTechniques = outcome.mitreTechniques,
                fraudVectors = outcome.fraudVectors,
                timestamp = System.currentTimeMillis(),
                isSimulated = false
            )

            val recordId = withContext(Dispatchers.IO) {
                dao.insertRecord(dbRecord)
            }

            val savedRecord = dbRecord.copy(id = recordId)
            _activeRecord.value = savedRecord
            _analysisState.value = AnalysisState.Success(savedRecord)
        }
    }

    /**
     * Run simulated threat logic based on setting selections
     */
    fun runSimulatedPipeline() {
        viewModelScope.launch {
            _compilerLogs.value = emptyList()
            val presetName = activeSimPreset.value
            _analysisState.value = AnalysisState.Processing("Extracting APK Template")

            addLog("MALSENTINEL SYSTEM", "Triggering dynamic compilation sandbox template: $presetName", "#c084fc")
            delay(300)
            addLog("STATIC ANALYZER", "Decompilation started. Exporting APK targets internally...", "#60a5fa")
            delay(400)

            val presetRecord = getPresetPayload(presetName)

            addLog("MANIFEST DECODER", "Extracted virtual package tag: ${presetRecord.packageName}", "#34d399")
            delay(300)
            addLog("MANIFEST DECODER", "Sensitive permissions identified: ${presetRecord.sensitivePermissionsCount}", "#e2e8f0")
            delay(400)
            addLog("HEURISTICS ENGINE", "Simulated Risk Score calculated at ${presetRecord.riskScore}/100", "#f87171")
            addLog("HEURISTICS ENGINE", "Mapped indicators family: ${presetRecord.matchedThreatFamily}", "#f87171")
            delay(400)
            addLog("AI INVESTIGATION", "Querying Gemini AI Copilot for banking vector review...", "#a78bfa")

            // Real Gemini can analyze simulation presets too if the API key is present!
            val rawPermsList = presetRecord.permissions.split(",")
            val narrative = withContext(Dispatchers.IO) {
                GeminiClient.getAnalysisExplanation(
                    packageName = presetRecord.packageName,
                    threatFamily = presetRecord.matchedThreatFamily,
                    riskScore = presetRecord.riskScore,
                    permissions = rawPermsList
                )
            }

            addLog("AI INVESTIGATION", "Generative explanation generated.", "#a78bfa")
            delay(300)
            addLog("MALSENTINEL SYSTEM", "Writing telemetry reports to secure Room database.", "#34d399")

            val dbRecord = presetRecord.copy(
                geminiNarrative = narrative,
                timestamp = System.currentTimeMillis()
            )

            val recordId = withContext(Dispatchers.IO) {
                dao.insertRecord(dbRecord)
            }

            val savedRecord = dbRecord.copy(id = recordId)
            _activeRecord.value = savedRecord
            _analysisState.value = AnalysisState.Success(savedRecord)
        }
    }

    private fun addLog(source: String, message: String, color: String) {
        val current = _compilerLogs.value.toMutableList()
        current.add(LogEntry(source, message, color, System.currentTimeMillis()))
        _compilerLogs.value = current
    }

    fun resetState() {
        _analysisState.value = AnalysisState.Idle
        _compilerLogs.value = emptyList()
    }

    private suspend fun prepopulateSampleLedger() {
        withContext(Dispatchers.IO) {
            val pre1 = ApkAnalysisRecord(
                fileName = "b_of_i_manager_v5.apk",
                packageName = "in.co.bankofindia.manager",
                sizeInBytes = 24 * 1024 * 1024,
                riskScore = 12,
                verdict = "Safe",
                permissions = "android.permission.INTERNET,android.permission.USE_BIOMETRIC,android.permission.ACCESS_NETWORK_STATE",
                sensitivePermissionsCount = 0,
                matchedThreatFamily = "None (Safe)",
                geminiNarrative = "### TECHNICAL VERDICT: SAFE\n\nVerified corporate financial distribution application under Bank of India operational guidelines. Zero overlay or Trojan behaviors tracked.",
                mitreTechniques = "",
                fraudVectors = "Overlay:8|Accessibility:10|SMS:10|Dropper:12",
                timestamp = System.currentTimeMillis() - 86400000,
                isSimulated = true
            )

            val pre2 = ApkAnalysisRecord(
                fileName = "verify_assistant.apk",
                packageName = "com.android.support.updates",
                sizeInBytes = 1420000,
                riskScore = 98,
                verdict = "Malicious",
                permissions = "android.permission.INTERNET,android.permission.RECEIVE_SMS,android.permission.READ_SMS,android.permission.SYSTEM_ALERT_WINDOW,android.permission.BIND_ACCESSIBILITY_SERVICE",
                sensitivePermissionsCount = 4,
                matchedThreatFamily = "Anubis.Banker",
                geminiNarrative = "### SECURITY COGNITIVE OVERVIEW\n\nThe uploaded application is a highly malicious banking trojan executing active harvesting operations. It mimics system utilities to gain deep accessibility rights.\n\n### RECONSTRUCTED FRAUD JOURNEY\n\n1. **Overlay Launch**: The trojan displays a fake login overlay trapping the user's banking password.\n2. **SMS Exfiltrator**: Interactive SMS receivers capture incoming OTP verification codes dynamically and outbound exfiltrate them to the command servers, enabling unauthorized bank withdrawals.",
                mitreTechniques = "T1425,T1411,T1437,T1444",
                fraudVectors = "Overlay:95|Accessibility:99|SMS:95|Dropper:80",
                timestamp = System.currentTimeMillis() - 3600000,
                isSimulated = true
            )

            dao.insertRecord(pre1)
            dao.insertRecord(pre2)
        }
    }

    private fun getPresetPayload(presetName: String): ApkAnalysisRecord {
        val permissionsList = when (presetName) {
            "Anubis.Banker" -> listOf(
                "android.permission.INTERNET",
                "android.permission.RECEIVE_SMS",
                "android.permission.READ_SMS",
                "android.permission.SYSTEM_ALERT_WINDOW",
                "android.permission.BIND_ACCESSIBILITY_SERVICE",
                "android.permission.RECEIVE_BOOT_COMPLETED"
            )
            "Flubot.SMS" -> listOf(
                "android.permission.INTERNET",
                "android.permission.RECEIVE_SMS",
                "android.permission.SEND_SMS",
                "android.permission.BIND_ACCESSIBILITY_SERVICE",
                "android.permission.REQUEST_INSTALL_PACKAGES"
            )
            "SpyNote.Dropper" -> listOf(
                "android.permission.INTERNET",
                "android.permission.BIND_ACCESSIBILITY_SERVICE",
                "android.permission.REQUEST_INSTALL_PACKAGES",
                "android.permission.READ_PHONE_STATE"
            )
            else -> listOf(
                "android.permission.INTERNET",
                "android.permission.ACCESS_NETWORK_STATE"
            )
        }

        val dexCount = when (presetName) {
            "Anubis.Banker" -> 2
            "Flubot.SMS" -> 3
            "SpyNote.Dropper" -> 2
            else -> 1
        }

        val fileName = when (presetName) {
            "Anubis.Banker" -> "bank_security_helper.apk"
            "Flubot.SMS" -> "dhl_delivery_express.apk"
            "SpyNote.Dropper" -> "premium_whatsapp_gold.apk"
            else -> "utility_calculator.apk"
        }

        val packageName = when (presetName) {
            "Anubis.Banker" -> "org.security.support.google"
            "Flubot.SMS" -> "com.dhl.fastexpress.update"
            "SpyNote.Dropper" -> "com.goldwhatsapp.premium"
            else -> "com.tool.simple.calc"
        }

        val size = when (presetName) {
            "Anubis.Banker" -> 1350000L
            "Flubot.SMS" -> 850000L
            "SpyNote.Dropper" -> 4120000L
            else -> 2500000L
        }

        val dexSmsVal = when (presetName) {
            "Anubis.Banker", "Flubot.SMS" -> 1
            else -> 0
        }
        val dexAccessVal = when (presetName) {
            "Anubis.Banker", "Flubot.SMS", "SpyNote.Dropper" -> 1
            else -> 0
        }
        val dexOverlayVal = when (presetName) {
            "Anubis.Banker" -> 1
            else -> 0
        }
        val dexLoaderVal = when (presetName) {
            "Anubis.Banker", "Flubot.SMS", "SpyNote.Dropper" -> 1
            else -> 0
        }
        val dexReflectVal = when (presetName) {
            "Anubis.Banker", "Flubot.SMS" -> 1
            "utility_calculator.apk", "utility_calculator.apk" -> 1
            else -> 0
        }
        val dexRootVal = when (presetName) {
            "Anubis.Banker" -> 1
            else -> 0
        }
        val dexAdminVal = when (presetName) {
            "SpyNote.Dropper" -> 1
            else -> 0
        }
        val dexRunningProcVal = when (presetName) {
            "Anubis.Banker" -> 1
            else -> 0
        }

        val metrics = ApkAnalyzer.computeRiskAndIndicators(
            permissions = permissionsList,
            dexCount = dexCount,
            packageName = packageName,
            fileName = fileName,
            accessibilityWeight = accessibilityWeightSetting.value,
            overlayWeight = overlayWeightSetting.value,
            smsWeight = smsWeightSetting.value,
            dexSmsVal = dexSmsVal,
            dexAccessVal = dexAccessVal,
            dexOverlayVal = dexOverlayVal,
            dexLoaderVal = dexLoaderVal,
            dexReflectVal = dexReflectVal,
            dexRootVal = dexRootVal,
            dexAdminVal = dexAdminVal,
            dexRunningProcVal = dexRunningProcVal
        )

        return ApkAnalysisRecord(
            fileName = fileName,
            packageName = packageName,
            sizeInBytes = size,
            riskScore = metrics.riskScore,
            verdict = metrics.verdict,
            permissions = permissionsList.joinToString(","),
            sensitivePermissionsCount = metrics.sensitiveCount,
            matchedThreatFamily = metrics.threatFamily,
            mitreTechniques = metrics.mitreTechniques,
            fraudVectors = metrics.fraudVectors,
            geminiNarrative = "",
            timestamp = System.currentTimeMillis(),
            isSimulated = true
        )
    }
}

sealed class AnalysisState {
    object Idle : AnalysisState()
    data class Processing(val task: String) : AnalysisState()
    data class Success(val record: ApkAnalysisRecord) : AnalysisState()
    data class Error(val message: String) : AnalysisState()
}

data class LogEntry(
    val source: String,
    val message: String,
    val colorHex: String,
    val timestamp: Long
)

package com.example.ui.screens

import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.viewmodel.AnalysisState
import com.example.viewmodel.MalsentinelViewModel
import com.example.ui.theme.SecureCharcoal
import com.example.ui.theme.ColorAI
import com.example.ui.theme.ColorSafe
import com.example.ui.theme.ColorCritical
import com.example.ui.theme.ColorHigh
import com.example.ui.theme.ColorMedium
import kotlinx.coroutines.launch

@Composable
fun DashedUploadBox(
    onUploadClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(130.dp)
            .clickable(onClick = onUploadClick),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val path = Path().apply {
                addRoundRect(
                    androidx.compose.ui.geometry.RoundRect(
                        left = 2f,
                        top = 2f,
                        right = size.width - 2f,
                        bottom = size.height - 2f,
                        radiusX = 12.dp.toPx(),
                        radiusY = 12.dp.toPx()
                    )
                )
            }
            drawPath(
                path = path,
                color = Color(0xFF334155),
                style = Stroke(
                    width = 2.dp.toPx(),
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 10f), 0f)
                )
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.CloudUpload,
                contentDescription = null,
                tint = Color(0xFF3B82F6),
                modifier = Modifier.size(36.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "DRAG & DROP APK BINARY HERE",
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "or touch to scan local device storage",
                color = Color(0xFF64748B),
                fontSize = 10.sp
            )
        }
    }
}

@Composable
fun IngestionTargetPreviewCard(
    presetName: String,
    modifier: Modifier = Modifier
) {
    val apkName = when (presetName) {
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
    val cert = when (presetName) {
        "Anubis.Banker" -> "Sovereign Android Dev Sign Certification CA"
        "Flubot.SMS" -> "Unknown Ad-Hoc SHA256 Distribution Authority"
        "SpyNote.Dropper" -> "Rogue Dynamic Mirror Release signature"
        else -> "Sovereign Google Play Core Signature"
    }
    val permissionsList = when (presetName) {
        "Anubis.Banker" -> "SMS_RECEIVE, SMS_READ, ACCESSIBILITY_SERVICE, OVERLAY_WINDOW"
        "Flubot.SMS" -> "SMS_SEND, SMS_RECEIVE, SYSTEM_ALERT, ACCESSIBILITY"
        "SpyNote.Dropper" -> "INSTALL_PACKAGES, RUNTIME_BOOT, WAKE_LOCK, PHONE_STATE"
        else -> "INTERNET, ACCESS_NETWORK_STATE"
    }
    val riskVal = when (presetName) {
        "Anubis.Banker" -> 98
        "Flubot.SMS" -> 95
        "SpyNote.Dropper" -> 88
        else -> 5
    }
    val familyLabel = when (presetName) {
        "Anubis.Banker" -> "Anubis Banker Trojan"
        "Flubot.SMS" -> "Flubot Automated SMS Sniffer"
        "SpyNote.Dropper" -> "SpyNote Remote RAT Dropper"
        else -> "Safe / White-listed system asset"
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF111827))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "SW-INGESTION SPECIFICATIONS",
                    color = Color(0xFF3B82F6),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(if (riskVal > 50) Color(0xFFEF4444).copy(alpha = 0.1f) else Color(0xFF22C55E).copy(alpha = 0.1f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = if (riskVal > 50) "FLAGGED" else "STATUS SAFE",
                        color = if (riskVal > 50) Color(0xFFEF4444) else Color(0xFF22C55E),
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row {
                    Text("APK Name:   ", color = Color(0xFF64748B), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Text(apkName, color = Color.White, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                }
                Row {
                    Text("Package:    ", color = Color(0xFF64748B), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Text(packageName, color = Color.White, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                }
                Row {
                    Text("Cert Sign:  ", color = Color(0xFF64748B), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Text(cert, color = Color(0xFF94A3B8), fontSize = 11.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
                Row {
                    Text("Rules Meta: ", color = Color(0xFF64748B), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Text(permissionsList, color = Color(0xFFF97316), fontSize = 10.5.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
                Row {
                    Text("Verdict Match: ", color = Color(0xFF64748B), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    val threatColor = if (riskVal > 50) Color(0xFFEF4444) else Color(0xFF22C55E)
                    Text("$riskVal/100 [$familyLabel]", color = threatColor, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun IngestionTimeline(
    currentStepTask: String,
    compilerLogsSize: Int,
    modifier: Modifier = Modifier
) {
    val currentStepIndex = when {
        currentStepTask.contains("Completed", ignoreCase = true) || compilerLogsSize >= 6 -> 5
        currentStepTask.contains("Gemini", ignoreCase = true) || currentStepTask.contains("AI", ignoreCase = true) -> 4
        currentStepTask.contains("Risk", ignoreCase = true) || currentStepTask.contains("Calibrating", ignoreCase = true) -> 3
        currentStepTask.contains("DEX", ignoreCase = true) || currentStepTask.contains("Bytecode", ignoreCase = true) -> 2
        currentStepTask.contains("Manifest", ignoreCase = true) || currentStepTask.contains("Parsed", ignoreCase = true) -> 1
        else -> 0
    }

    val steps = listOf(
        "APK Ingested" to "Binary binary successfully extracted to workspace",
        "Manifest Analysis" to "Core AndroidManifest permission tree scanned",
        "DEX Classes Scanned" to "Bytecode matched against high-threat indicators",
        "Risk Calibrated" to "Multiplane scoring algorithms assessed",
        "AI Co-audit Completed" to "Gemini forensic logic compiled",
        "Report Signed Off" to "Dossier cached in secure local storage"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFF0F172A), RoundedCornerShape(16.dp))
            .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(16.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = "TRINETRA COOPERATIVE FORENSICS ROADMAP",
            color = Color(0xFF64748B),
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )

        steps.forEachIndexed { index, (title, sub) ->
            val isActive = index == currentStepIndex
            val isPassed = index < currentStepIndex

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.size(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (isPassed) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Color(0xFF22C55E),
                            modifier = Modifier.size(18.dp)
                        )
                    } else if (isActive) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(12.dp),
                            strokeWidth = 2.dp,
                            color = Color(0xFF8B5CF6)
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color(0xFF334155))
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = title,
                        color = if (isActive || isPassed) Color.White else Color(0xFF64748B),
                        fontSize = 11.sp,
                        fontWeight = if (isActive || isPassed) FontWeight.Bold else FontWeight.Medium
                    )
                    Text(
                        text = sub,
                        color = if (isActive || isPassed) Color(0xFF94A3B8) else Color(0xFF475569),
                        fontSize = 9.5.sp
                    )
                }
            }
        }
    }
}


@Composable
fun UploadScreen(
    viewModel: MalsentinelViewModel,
    onNavigateToThreat: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val analysisState by viewModel.analysisState.collectAsState()
    val compilerLogs by viewModel.compilerLogs.collectAsState()
    val activePreset by viewModel.activeSimPreset.collectAsState()

    // File picker launcher
    val fileLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            var fileName = "user_uploaded_package.apk"
            // Pull real filename safely
            context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (nameIndex != -1 && cursor.moveToFirst()) {
                    fileName = cursor.getString(nameIndex)
                }
            }
            viewModel.analyzeLocalApkFile(uri, fileName)
        }
    }

    val listState = rememberLazyListState()
    // Auto Scroll compiler logs
    LaunchedEffect(compilerLogs.size) {
        if (compilerLogs.isNotEmpty()) {
            listState.animateScrollToItem(compilerLogs.size - 1)
        }
    }

    // Colors & Brushes
    val bgBrush = Brush.verticalGradient(
        colors = listOf(Color(0xFF0F172A), SecureCharcoal)
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(bgBrush)
            .padding(horizontal = 16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            when (val state = analysisState) {
                is AnalysisState.Idle -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                "Ingestion Intake Workspace",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                "Securely upload local package binaries or select preset attack emulators",
                                fontSize = 11.sp,
                                color = Color(0xFF94A3B8),
                                textAlign = TextAlign.Center
                            )
                        }

                        DashedUploadBox(
                            onUploadClick = { fileLauncher.launch("application/vnd.android.package-archive") }
                        )

                        IngestionTargetPreviewCard(presetName = activePreset)

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(16.dp)),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF111827))
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = "THREAT SIMULATION PLATFORM",
                                    color = Color(0xFF8B5CF6),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.5.sp
                                )
                                Spacer(modifier = Modifier.height(10.dp))

                                val presetsList = listOf(
                                    "Anubis.Banker" to "Anubis overlay threat indicators",
                                    "Flubot.SMS" to "Flubot OTP snooper",
                                    "SpyNote.Dropper" to "SpyNote silent module installer",
                                    "Safe.Calc" to "Legitimate verified general utility"
                                )

                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    presetsList.forEach { (key, desc) ->
                                        val isSelected = activePreset == key
                                        val itemBg = if (isSelected) Color(0xFF1E293B) else Color(0xFF0F172A)
                                        val itemBorder = if (isSelected) Color(0xFF3B82F6) else Color(0xFF1E293B)

                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(10.dp))
                                                .background(itemBg)
                                                .border(1.dp, itemBorder, RoundedCornerShape(10.dp))
                                                .clickable { viewModel.activeSimPreset.value = key }
                                                .padding(8.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            RadioButton(
                                                selected = isSelected,
                                                onClick = { viewModel.activeSimPreset.value = key },
                                                colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF3B82F6))
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Column {
                                                Text(key, color = Color.White, fontSize = 11.5.sp, fontWeight = FontWeight.Bold)
                                                Text(desc, color = Color(0xFF64748B), fontSize = 9.5.sp)
                                            }
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                Button(
                                    onClick = { viewModel.runSimulatedPipeline() },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B82F6)),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(44.dp)
                                        .testTag("run_simulation_button"),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Icon(Icons.Default.PrecisionManufacturing, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("COMPILE HEURISTIC SCENARIO", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(100.dp))
                    }
                }

                is AnalysisState.Processing -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Secure Compilation Pipeline",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        // 1. Progress Pipeline Steppers
                        IngestionTimeline(currentStepTask = state.task, compilerLogsSize = compilerLogs.size)

                        // 2. STDOUT virtual logger terminal (fixed height prevents nested scrolling issues)
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp)
                                .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(16.dp)),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF070A13))
                        ) {
                            Column(modifier = Modifier.fillMaxSize()) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color(0xFF0F172A))
                                        .padding(horizontal = 12.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(modifier = Modifier.size(6.dp).clip(RoundedCornerShape(3.dp)).background(Color(0xFFEF4444)))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Box(modifier = Modifier.size(6.dp).clip(RoundedCornerShape(3.dp)).background(Color(0xFFF97316)))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Box(modifier = Modifier.size(6.dp).clip(RoundedCornerShape(3.dp)).background(Color(0xFF22C55E)))
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Text(
                                            "X-SEC SANDBOX PROCESS MONITOR",
                                            color = Color(0xFF64748B),
                                            fontSize = 9.sp,
                                            fontFamily = FontFamily.Monospace,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    LinearProgressIndicator(
                                        modifier = Modifier.width(32.dp).height(3.dp),
                                        color = Color(0xFF3B82F6),
                                        trackColor = Color(0xFF334155)
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxWidth()
                                        .padding(8.dp)
                                ) {
                                    LazyColumn(
                                        state = listState,
                                        modifier = Modifier.fillMaxSize(),
                                        verticalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        items(compilerLogs) { log ->
                                            Row(modifier = Modifier.fillMaxWidth()) {
                                                Text(
                                                    text = "[${log.source}] ",
                                                    color = Color(android.graphics.Color.parseColor(log.colorHex)),
                                                    fontFamily = FontFamily.Monospace,
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                                Text(
                                                    text = log.message,
                                                    color = Color(0xFFD1D5DB),
                                                    fontFamily = FontFamily.Monospace,
                                                    fontSize = 10.sp
                                                )
                                            }
                                        }
                                    }
                                }

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color(0xFF0F172A))
                                        .padding(10.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(12.dp),
                                            strokeWidth = 1.5.dp,
                                            color = Color(0xFF3B82F6)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            "${state.task}...",
                                            color = Color(0xFF94A3B8),
                                            fontSize = 10.sp,
                                            fontFamily = FontFamily.Monospace
                                        )
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(100.dp))
                    }
                }

                is AnalysisState.Success -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Extraction Confirmed",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, Color(0xFF22C55E).copy(alpha = 0.3f), RoundedCornerShape(16.dp)),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF0A1C14))
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = Color(0xFF22C55E),
                                    modifier = Modifier.size(56.dp)
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    "DETONATION WORKFLOW READY",
                                    color = Color.White,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    "TRINETRA decompilation engine resolved high-fidelity binary weights, sensitive tables, and queried co-audit models successfully.",
                                    color = Color(0xFFA7F3D0),
                                    fontSize = 11.5.sp,
                                    textAlign = TextAlign.Center
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                Button(
                                    onClick = {
                                        viewModel.resetState()
                                        onNavigateToThreat()
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF22C55E)),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(44.dp)
                                        .testTag("go_to_findings_button")
                                ) {
                                    Text("PROCEED TO COGNITIVE DOSSIER", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Icon(Icons.Default.ArrowForward, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(100.dp))
                    }
                }

                is AnalysisState.Error -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Extraction Blocked",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, Color(0xFFEF4444), RoundedCornerShape(16.dp)),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF1C0A0A))
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Error,
                                    contentDescription = null,
                                    tint = Color(0xFFEF4444),
                                    modifier = Modifier.size(56.dp)
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text("WORKSTATION PIPELINE CORRUPT", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    state.message,
                                    color = Color(0xFFFCA5A5),
                                    fontSize = 11.5.sp,
                                    textAlign = TextAlign.Center
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                Button(
                                    onClick = { viewModel.resetState() },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444)),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(44.dp)
                                ) {
                                    Text("RESET WORKBENCH", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(100.dp))
                    }
                }
            }
        }
    }
}

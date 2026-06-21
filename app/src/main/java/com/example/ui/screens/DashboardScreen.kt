package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.database.ApkAnalysisRecord
import com.example.viewmodel.MalsentinelViewModel
import com.example.ui.theme.SecureCharcoal
import com.example.ui.theme.ColorCritical
import com.example.ui.theme.ColorHigh
import com.example.ui.theme.ColorMedium
import com.example.ui.theme.ColorSafe
import com.example.ui.theme.ColorAI
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DashboardScreen(
    viewModel: MalsentinelViewModel,
    onNavigateToUpload: () -> Unit,
    onNavigateToThreat: () -> Unit,
    modifier: Modifier = Modifier
) {
    val history by viewModel.analysisHistory.collectAsState()
    val activeRecord by viewModel.activeRecord.collectAsState()

    // Real-Time Live UTC SOC Clock
    var socTime by remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        while (true) {
            val sdf = SimpleDateFormat("HH:mm:ss 'UTC'", Locale.US).apply {
                timeZone = TimeZone.getTimeZone("UTC")
            }
            socTime = sdf.format(Date())
            kotlinx.coroutines.delay(1000L)
        }
    }

    // Rotating Threat Intelligence Advisory Marquee
    var intelIndex by remember { mutableStateOf(0) }
    val intelBulletins = remember {
        listOf(
            "CRITICAL ALERT: TeaBot banking trojan overlay campaigns targeting BOI Retail Banking modules [HEURISTIC CONFIDENCE 98%]",
            "STRIKE REPORT: SpyNote remote access RAT hijacking active OTP streams via custom RECEIVE_SMS intercept receivers",
            "CCTC BRIEFING: Hidden DexClassLoader instructions discovered in recent unverified system update APK wrappers",
            "MUMBAI INTEL SECURE: Accessibility automation loop controls identified mimicking BOI banking transaction screens"
        )
    }
    LaunchedEffect(Unit) {
        while (true) {
            kotlinx.coroutines.delay(4500L)
            intelIndex = (intelIndex + 1) % intelBulletins.size
        }
    }

    // Pulse animation for LIVE badge
    val infiniteTransition = rememberInfiniteTransition(label = "pulse_clock")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_alpha"
    )

    // Calculate metrics
    val totalScans = history.size
    val maliciousCount = history.count { it.verdict == "Malicious" || it.verdict == "High Risk" }
    val averageScore = if (history.isNotEmpty()) history.map { it.riskScore }.average().toInt() else 0

    // Custom design palette
    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(Color(0xFF0F172A), SecureCharcoal) // Deep Slate to our premium secure charcoal background
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundBrush)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Security Posture Card
            item {
                val lastAnalysisDate = if (history.isNotEmpty()) {
                    val lastRecord = history.first()
                    val sdf = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())
                    sdf.format(Date(lastRecord.timestamp))
                } else {
                    "No Ingested Targets"
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(16.dp)),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF111827))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Shield,
                                    contentDescription = null,
                                    tint = Color(0xFF22C55E),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "SECURITY POSTURE: OPERATIONAL",
                                    color = Color(0xFF22C55E),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.5.sp
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xFF22C55E).copy(alpha = 0.1f))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "ACTIVE SECURE MODE",
                                    color = Color(0xFF22C55E),
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Protected Nodes", color = Color(0xFF64748B), fontSize = 11.sp, fontWeight = FontWeight.Medium)
                                Spacer(modifier = Modifier.height(2.dp))
                                Text("4,850 Active Devices", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                Text("RBI & CERT-In Compliant", color = Color(0xFF475569), fontSize = 9.sp)
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Threats Blocked", color = Color(0xFF64748B), fontSize = 11.sp, fontWeight = FontWeight.Medium)
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "$maliciousCount Targets Detonated",
                                    color = if (maliciousCount > 0) Color(0xFFEF4444) else Color.White,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text("Heuristics Sandbox Active", color = Color(0xFF475569), fontSize = 9.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Risk Exposure", color = Color(0xFF64748B), fontSize = 11.sp, fontWeight = FontWeight.Medium)
                                Spacer(modifier = Modifier.height(2.dp))
                                val exposureLevel = when {
                                    averageScore >= 60 -> "CRITICAL LIMIT"
                                    averageScore >= 30 -> "ELEVATED ALERT"
                                    else -> "SAFE OPERATIONAL BASELINE"
                                }
                                val exposureColor = when {
                                    averageScore >= 60 -> Color(0xFFEF4444)
                                    averageScore >= 30 -> Color(0xFFF97316)
                                    else -> Color(0xFF22C55E)
                                }
                                Text("$averageScore%", color = exposureColor, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                Text(exposureLevel, color = exposureColor, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Last Analysis", color = Color(0xFF64748B), fontSize = 11.sp, fontWeight = FontWeight.Medium)
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = lastAnalysisDate,
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text("TRINETRA Decompilation Log", color = Color(0xFF475569), fontSize = 9.sp)
                            }
                        }
                    }
                }
            }

            // Quick Ingestion Button
            item {
                Button(
                    onClick = onNavigateToUpload,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B82F6)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("dashboard_quick_upload_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "NEW THREAT ANALYSIS WORKFLOW (INGEST APK)",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                }
            }

            // Threat Feed (Live Rotating ticker)
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color(0xFFEF4444).copy(alpha = 0.3f), RoundedCornerShape(12.dp)),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF181010))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = null,
                                tint = Color(0xFFEF4444),
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "TRINETRA SECURE BROADCAST • LIVE FEEDS",
                                color = Color(0xFFEF4444),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        AnimatedContent(
                            targetState = intelIndex,
                            transitionSpec = {
                                fadeIn(animationSpec = tween(400)) togetherWith fadeOut(animationSpec = tween(400))
                            },
                            label = "threat_intel_scroller"
                        ) { index ->
                            val bulletinsRebranded = intelBulletins[index]
                                .replace("MALSentinel", "TRINETRA AI")
                            Text(
                                text = bulletinsRebranded,
                                color = Color(0xFFFEF08A),
                                fontSize = 11.sp,
                                lineHeight = 15.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }

            // History Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Forensic Records Cache",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    if (history.isNotEmpty()) {
                        Text(
                            text = "Purge Ledger",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFEF4444),
                            modifier = Modifier
                                .clickable { viewModel.clearAllRecords() }
                                .padding(4.dp)
                        )
                    }
                }
            }

            // History List
            if (history.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(20.dp))
                            .background(Color(0xFF0B1329)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.Inbox,
                                contentDescription = null,
                                tint = Color(0xFF475569),
                                modifier = Modifier.size(36.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Database Log Clear", color = Color(0xFF64748B), fontSize = 13.sp)
                            Text("Decompile an APK wrapper to initialize history.", color = Color(0xFF475569), fontSize = 11.sp)
                        }
                    }
                }
            } else {
                items(history) { record ->
                    HistoryItemCard(
                        record = record,
                        isActive = activeRecord?.id == record.id,
                        onClick = {
                            viewModel.setActiveRecord(record)
                            onNavigateToThreat()
                        },
                        onDelete = { viewModel.deleteRecord(record) }
                    )
                }
            }
        }
    }
}

@Composable
fun HistoryItemCard(
    record: ApkAnalysisRecord,
    isActive: Boolean,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    val dateString = remember(record.timestamp) {
        val sdf = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())
        sdf.format(Date(record.timestamp))
    }

    val itemBg = if (isActive) Color(0xFF1E293B) else Color(0xFF0F172A)
    val itemBorder = if (isActive) Color(0xFF3B82F6) else Color(0xFF1E293B)

    val verdictColor = when (record.verdict) {
        "Malicious" -> Color(0xFFEF4444)
        "High Risk" -> Color(0xFFF59E0B)
        "Warning" -> Color(0xFFFDE047)
        else -> Color(0xFF10B981)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(itemBg)
            .border(1.dp, itemBorder, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left Risk Indicator
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(verdictColor.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "${record.riskScore}",
                color = verdictColor,
                fontSize = 17.sp,
                fontWeight = FontWeight.Black
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        // Center Details
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = record.fileName,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(2.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${record.verdict} • ${record.matchedThreatFamily}",
                    color = verdictColor,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = " • $dateString",
                    color = Color(0xFF64748B),
                    fontSize = 11.sp
                )
            }
        }

        // Delete Trigger or Simulation tag
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (record.isSimulated) {
                Box(
                    modifier = Modifier
                        .background(Color(0xFF1E293B), RoundedCornerShape(4.dp))
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                ) {
                    Text("SEM", color = Color(0xFF94A3B8), fontSize = 8.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.width(8.dp))
            }

            IconButton(
                onClick = onDelete,
                modifier = Modifier.size(24.dp).testTag("delete_record_${record.id}")
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Record",
                    tint = Color(0xFF475569)
                )
            }
        }
    }
}

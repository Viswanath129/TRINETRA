package com.example.ui.screens

import android.content.Intent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.database.ApkAnalysisRecord
import com.example.viewmodel.MalsentinelViewModel
import com.example.ui.theme.SecureCharcoal
import com.example.ui.theme.ColorAI
import com.example.ui.theme.ColorSafe
import com.example.ui.theme.ColorCritical
import com.example.ui.theme.ColorHigh
import com.example.ui.theme.ColorMedium
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ReportScreen(
    viewModel: MalsentinelViewModel,
    modifier: Modifier = Modifier
) {
    val activeRecordState by viewModel.activeRecord.collectAsState()
    val context = LocalContext.current

    val bgBrush = Brush.verticalGradient(
        colors = listOf(Color(0xFF0F172A), SecureCharcoal)
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(bgBrush)
    ) {
        val record = activeRecordState
        if (record == null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(24.dp)) {
                    Icon(
                        imageVector = Icons.Default.Article,
                        contentDescription = null,
                        tint = Color(0xFF475569),
                        modifier = Modifier.size(56.dp)
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        "No Active Incident Records Loaded",
                        color = Color.White,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Please select or process an APK compilation package under the Upload or Dashboard modules first.",
                        color = Color(0xFF475569),
                        fontSize = 11.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            val dateString = remember(record.timestamp) {
                val sdf = SimpleDateFormat("dd MMMM yyyy, hh:mm a", Locale.getDefault())
                sdf.format(Date(record.timestamp))
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header Panel
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            "Executive Forensic Docket",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                        Text(
                            "Official cyber-intelligence report mapped to Room cache ledger",
                            fontSize = 11.sp,
                            color = Color(0xFF94A3B8)
                        )
                    }

                    // Share button
                    IconButton(
                        onClick = {
                            val reportText = """
                                Bank of India Forensic Intelligence Report
                                ------------------------------------------
                                File: ${record.fileName}
                                Package: ${record.packageName}
                                Risk Score: ${record.riskScore}/100
                                Verdict: ${record.verdict}
                                Match: ${record.matchedThreatFamily}
                                Generated on: $dateString
                                
                                Synopsis:
                                ${record.geminiNarrative.take(300)}...
                            """.trimIndent()
                            
                            val sendIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, reportText)
                                type = "text/plain"
                            }
                            val shareIntent = Intent.createChooser(sendIntent, "Export Security Audit Log")
                            context.startActivity(shareIntent)
                        },
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF1E293B))
                            .testTag("export_report_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Export Report",
                            tint = Color.White
                        )
                    }
                }

                // Official Bank Logo Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color(0xFFF59E0B).copy(alpha = 0.4f), RoundedCornerShape(16.dp)),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1C1917))
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Security,
                                contentDescription = null,
                                tint = Color(0xFFF59E0B),
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    "BANK OF INDIA",
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 1.sp
                                )
                                Text(
                                    "CENTRAL COGNITIVE THREAT CELL • MUMBAI",
                                    color = Color(0xFFF59E0B),
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        Divider(color = Color(0xFFF59E0B).copy(alpha = 0.2f))
                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("SECTION ENROLMENT ID", color = Color(0xFF64748B), fontSize = 8.sp, fontWeight = FontWeight.Bold)
                                Text("BOI-SEC-2026-${record.id + 1000}", color = Color.White, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text("INVESTIGATION TIMESTAMP", color = Color(0xFF64748B), fontSize = 8.sp, fontWeight = FontWeight.Bold)
                                Text(dateString, color = Color.White, fontSize = 11.sp, fontFamily = FontFamily.Monospace)
                            }
                        }
                    }
                }

                // Cyberforce Docket Metadata Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(16.dp)),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF111827))
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("INCIDENT TELEMETRY CLASSIFICATION", color = Color(0xFF94A3B8), fontSize = 11.sp, fontWeight = FontWeight.Bold)

                        ReportDetailRow(label = "Package Name ID", value = record.packageName)
                        ReportDetailRow(label = "Identified Payload File", value = record.fileName)
                        ReportDetailRow(label = "Static Code Weight Size", value = "${String.format("%,d", record.sizeInBytes)} Bytes (Compiled)")
                        ReportDetailRow(label = "Threat Scoring Level", value = "${record.riskScore} / 100")
                        ReportDetailRow(label = "Active Threat Signature", value = record.matchedThreatFamily)
                        ReportDetailRow(label = "Forensic State Node", value = if (record.isSimulated) "SANDBOX EMULATED" else "PHYSICAL DISK ANALYSIS")
                    }
                }

                // Executive forensic analysis synopsis
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(16.dp)),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "OFFICIAL SECURITY EXECUTIVE SUMMARY",
                            color = Color(0xFF3B82F6),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 10.dp)
                        )
                        
                        Text(
                            text = if (record.riskScore >= 60) {
                                "Under physical decompilation of ${record.fileName}, the MALSentinel heuristics engine successfully extracted signature call indicators linking this application binary to specific mobile financial trojans. High-risk permissions mapped to overlapping activities suggest active intents to capture client bank login inputs, intercepts OTP transactions, and bypass corporate security rules. Actionable MDM locking is heavily recommended."
                            } else {
                                "The analysis of ${record.fileName} has concluded that this APK exhibits conventional behavior indicators matching clean profiles. No suspicious accessibility services loops, overlay actions, or unexpected outbound SMS trackers were mapped. It is classified as Safe under current Bank of India administrative scoring frameworks."
                            },
                            color = Color(0xFFE2E8F0),
                            fontSize = 12.sp,
                            lineHeight = 16.sp
                        )
                    }
                }

                // Mitigation Controls Checklist
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(16.dp)),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF111827))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "MAPPING TECHNICAL ACTIONABLE CONTROLS",
                            color = Color(0xFF10B981),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        val mitigationSteps = if (record.riskScore >= 60) {
                            listOf(
                                "Enforce immediate MDM server-side quarantine on target package identifier." to true,
                                "Revoke all dynamic accessibility automation permissions on user terminals." to true,
                                "Update regional firewalls to isolate exfiltration socket IP addresses." to true,
                                "Notify corporate risk teams of suspected overlay attacks on retail BOI apps." to false
                            )
                        } else {
                            listOf(
                                "Store hash signatures in corporate clean application repositories." to true,
                                "Permit standard background installer execution queries safely." to true,
                                "Enlist package tag under trusted internal employee app arrays." to false
                            )
                        }

                        mitigationSteps.forEach { (step, isCritical) ->
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = if (isCritical) Icons.Default.Warning else Icons.Default.Check,
                                    contentDescription = null,
                                    tint = if (isCritical) Color(0xFFEF4444) else Color(0xFF10B981),
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = step,
                                    color = Color(0xFF94A3B8),
                                    fontSize = 11.5.sp
                                )
                            }
                        }
                    }
                }

                // Counter-Signature sign-off block
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(16.dp)),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A))
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            "OFFICIAL COUNTERSIGNATURE SIGN-OFF",
                            color = Color(0xFF64748B),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 14.dp)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            // Column 1
                            Column(modifier = Modifier.weight(1f).align(Alignment.Bottom)) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(1.dp)
                                        .background(Color(0xFF334155))
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    "MALSENTINEL AUDITOR",
                                    color = Color.White,
                                    fontSize = 8.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Text(
                                    "SecOps Systems Verification",
                                    color = Color(0xFF475569),
                                    fontSize = 7.5.sp,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }

                            // Column 2
                            Column(modifier = Modifier.weight(1f).align(Alignment.Bottom)) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(1.dp)
                                        .background(Color(0xFF334155))
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    "CISO APPROVAL GATE",
                                    color = Color.White,
                                    fontSize = 8.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Text(
                                    "Risk Assurances counter-sign",
                                    color = Color(0xFF475569),
                                    fontSize = 7.5.sp,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}

@Composable
fun ReportDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, color = Color(0xFF475569), fontSize = 11.sp, fontWeight = FontWeight.Bold)
        Text(
            text = value,
            color = Color.White,
            fontSize = 11.5.sp,
            fontFamily = FontFamily.Monospace,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1f).padding(start = 12.dp)
        )
    }
}

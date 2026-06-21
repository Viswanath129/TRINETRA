package com.example.ui.screens

import androidx.compose.animation.*
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.viewmodel.MalsentinelViewModel

@Composable
fun MoreScreen(
    viewModel: MalsentinelViewModel,
    modifier: Modifier = Modifier
) {
    val smsWeight by viewModel.smsWeightSetting.collectAsState()
    val overlayWeight by viewModel.overlayWeightSetting.collectAsState()
    val accessibilityWeight by viewModel.accessibilityWeightSetting.collectAsState()

    val bgBrush = Brush.verticalGradient(
        colors = listOf(Color(0xFF0F172A), Color(0xFF020617))
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(bgBrush)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Screen Header
            Column {
                Text(
                    "Security Operations Desk",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )
                Text(
                    "Calibrate scoring sensitivities, manage local databases, and view system specifications",
                    fontSize = 11.sp,
                    color = Color(0xFF94A3B8)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 1. RISK CONFIGURATION COLLAPSIBLE CARD
            MoreCollapsibleCard(
                title = "Risk Engine Configuration",
                subtitle = "Tune heuristics sensitivities and dynamic score indices",
                icon = Icons.Default.Tune,
                initialExpanded = true
            ) {
                Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Text(
                        "Risk Engine Calibration Weights",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Calibrate base weights for suspicious code features. Higher weights increase calculated threat index scores.",
                        color = Color(0xFF64748B),
                        fontSize = 11.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    WeightSliderRow(
                        label = "Accessibility Overrun Weight",
                        value = accessibilityWeight,
                        color = Color(0xFF3B82F6),
                        onValueChange = { viewModel.accessibilityWeightSetting.value = it }
                    )

                    WeightSliderRow(
                        label = "Overlay Redirection Weight",
                        value = overlayWeight,
                        color = Color(0xFFEF4444),
                        onValueChange = { viewModel.overlayWeightSetting.value = it }
                    )

                    WeightSliderRow(
                        label = "SMS Capture Exfiltration Weight",
                        value = smsWeight,
                        color = Color(0xFFF59E0B),
                        onValueChange = { viewModel.smsWeightSetting.value = it }
                    )
                }
            }

            // 2. DATABASE RECOVERY CARD
            MoreCollapsibleCard(
                title = "Database Administration",
                subtitle = "Manage SQLite persistent audit records status",
                icon = Icons.Default.Storage
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        "Administrative Cache Ledger Control",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Purge cached Room records of decompiled modules and generated CISO intelligence dockets.",
                        color = Color(0xFF64748B),
                        fontSize = 11.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { viewModel.clearAllRecords() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("wipe_central_db_settings_button")
                    ) {
                        Icon(imageVector = Icons.Default.DeleteForever, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Purge Analysis History Ledger", fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Interactive Fraud Financial Impact Simulator Card
            var fleetSize by remember { mutableStateOf(5000f) }
            var avgBalance by remember { mutableStateOf(45000f) }
            var captureRate by remember { mutableStateOf(65f) }
            var selectedScenario by remember { mutableStateOf("Anubis overlay injector") }

            MoreCollapsibleCard(
                title = "Fraud Financial Impact Simulator",
                subtitle = "Visualize portfolio loss matrices and estimated security savings",
                icon = Icons.Default.TrendingUp,
                initialExpanded = true
            ) {
                Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Text(
                        "BOI Loss Projection & Savings Calculator",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Simulate direct financial exposures, breach mitigation levels, and expected savings correlated against current risk engine calibration weights.",
                        color = Color(0xFF64748B),
                        fontSize = 11.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Scenario selector dropdown trigger (Row chips)
                    Text("Select Threat Attack Scenario Pattern:", color = Color(0xFFE2E8F0), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    val scenarios = listOf("Anubis overlay injector", "Flubot SMS exfil", "SpyNote package dropper")
                    Row(
                        modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        scenarios.forEach { sc ->
                            val isSelected = selectedScenario == sc
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) Color(0xFF2563EB) else Color(0xFF1E293B))
                                    .clickable { selectedScenario = sc }
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(sc, color = if (isSelected) Color.White else Color(0xFF94A3B8), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Fleet Size slider
                    Column {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Active Infection Fleet (Target Devices)", color = Color(0xFFE2E8F0), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Text("${fleetSize.toInt()} compromised", color = Color(0xFF3B82F6), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                        Slider(
                            value = fleetSize,
                            onValueChange = { fleetSize = it },
                            valueRange = 500f..25000f,
                            colors = SliderDefaults.colors(
                                thumbColor = Color(0xFF3B82F6),
                                activeTrackColor = Color(0xFF3B82F6),
                                inactiveTrackColor = Color(0xFF1E293B)
                            )
                        )
                    }

                    // Average retail account balance slider
                    Column {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("BOI Average Account Balance Spec (₹)", color = Color(0xFFE2E8F0), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Text("₹${String.format("%,.0f", avgBalance)} INR", color = Color(0xFF10B981), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                        Slider(
                            value = avgBalance,
                            onValueChange = { avgBalance = it },
                            valueRange = 5000f..150000f,
                            colors = SliderDefaults.colors(
                                thumbColor = Color(0xFF10B981),
                                activeTrackColor = Color(0xFF10B981),
                                inactiveTrackColor = Color(0xFF1E293B)
                            )
                        )
                    }

                    // Evasion coefficient / Capture rate slider
                    Column {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Compromise Outflow Efficiency (Evasion Ratio)", color = Color(0xFFE2E8F0), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Text("${captureRate.toInt()}% payload rate", color = Color(0xFFF59E0B), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                        Slider(
                            value = captureRate,
                            onValueChange = { captureRate = it },
                            valueRange = 10f..100f,
                            colors = SliderDefaults.colors(
                                thumbColor = Color(0xFFF59E0B),
                                activeTrackColor = Color(0xFFF59E0B),
                                inactiveTrackColor = Color(0xFF1E293B)
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Calculations
                    val grossExposure = fleetSize * avgBalance
                    val rawUnmitigatedLoss = grossExposure * (captureRate / 100f) * 0.92f

                    // Mitigation strength dynamically bound to scoring sensitivities!
                    val avgCalibratedWeight = (smsWeight + overlayWeight + accessibilityWeight) / 3f
                    val mitigationStrength = (0.35f + (avgCalibratedWeight / 40f) * 0.63f).coerceIn(0.35f, 0.98f)
                    val mitigatedLoss = rawUnmitigatedLoss * (1f - mitigationStrength)
                    val savedCapital = rawUnmitigatedLoss - mitigatedLoss

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Color(0xFF334155), RoundedCornerShape(12.dp)),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF020617))
                    ) {
                        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("AUM Gross Portfolio Exposure:", color = Color(0xFF94A3B8), fontSize = 11.sp)
                                Text("₹${String.format("%,.0f", grossExposure)}", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Unmitigated Financial Theft Est:", color = Color(0xFFEF4444), fontSize = 11.sp)
                                Text("₹${String.format("%,.0f", rawUnmitigatedLoss)}", color = Color(0xFFEF4444), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("MALSentinel Block Efficiency:", color = Color(0xFF3B82F6), fontSize = 11.sp)
                                Text("${String.format("%.1f", mitigationStrength * 100f)}%", color = Color(0xFF3B82F6), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }

                            HorizontalDivider(color = Color(0xFF1E293B))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text("Estimated Credit Capital Saved:", color = Color(0xFF10B981), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    Text("Due to customized rule calibration", color = Color(0xFF64748B), fontSize = 9.sp)
                                }
                                Text(
                                    "₹${String.format("%,.0f", savedCapital)}",
                                    color = Color(0xFF10B981),
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                        }
                    }
                }
            }

            // 3. ARCHITECTURE DETAILS CARD
            MoreCollapsibleCard(
                title = "Technical Details & Architecture",
                subtitle = "Under-the-hood microservices specifications",
                icon = Icons.Default.Code
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        "MALSentinel Stack Architecture",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    StackLogField("Frontend Engine", "Jetpack Compose, Material Design 3, Screen Navigation API")
                    StackLogField("Database Cache", "Android Room SQLite (Reactive Entity streams via Flow)")
                    StackLogField("Intelligence Copilot", "Gemini 3.5 Flash Model (Direct REST API, JSON mapping)")
                    StackLogField("Extraction Sandbox", "Native JVM ZipInputStream & Manifest XML string pool decompiler")
                    StackLogField("Framework Standard", "MITRE ATT&CK Matrix Mapping compliance profiles")
                }
            }

            // 4. ABOUT CARD
            MoreCollapsibleCard(
                title = "About MALSentinel",
                subtitle = "System purpose, threat coverage, and background",
                icon = Icons.Default.Info
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = null,
                            tint = Color(0xFF3B82F6),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                "MALSentinel Audit Bench",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Black
                            )
                            Text(
                                "BANK OF INDIA • CENTRAL COGNITIVE THREAT CELL",
                                color = Color(0xFFF59E0B),
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Text(
                        text = "MALSentinel is an offline-first high-assurance APK static decompilation, runtime behavior emulator, and vulnerability detection pipeline. Custom-engineered for Bank of India CISO operations, it analyzes mobile packages for critical risk patterns—specifically targeting Accessibility Service abuses, malicious overlays, OTP SMS interceptors, and background package droppers.\n\nBy leveraging localized string tables and permissions matrix, MALSentinel maps potential attacks to standardized MITRE ATT&CK techniques, generating compliant and readable forensics intelligence report cards powered by Google Gemini AI.",
                        color = Color(0xFFE2E8F0),
                        fontSize = 11.5.sp,
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF1E293B).copy(alpha = 0.5f))
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "SecOps Classification Level: Secret // Internals Only",
                            color = Color(0xFF94A3B8),
                            fontSize = 9.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(120.dp))
        }
    }
}

@Composable
fun MoreCollapsibleCard(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    initialExpanded: Boolean = false,
    content: @Composable () -> Unit
) {
    var expanded by remember { mutableStateOf(initialExpanded) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF111827))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFF1E293B))
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = Color(0xFF3B82F6),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            title,
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            subtitle,
                            color = Color(0xFF64748B),
                            fontSize = 10.5.sp
                        )
                    }
                }
                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = Color(0xFF64748B)
                )
            }

            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(modifier = Modifier.padding(top = 18.dp)) {
                    content()
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
        }
    }
}

@Composable
fun WeightSliderRow(
    label: String,
    value: Int,
    color: Color,
    onValueChange: (Int) -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(label, color = Color(0xFFE2E8F0), fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Text("Weight: $value pts", color = color, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
        Slider(
            value = value.toFloat(),
            onValueChange = { onValueChange(it.toInt()) },
            valueRange = 5f..40f,
            colors = SliderDefaults.colors(
                thumbColor = color,
                activeTrackColor = color,
                inactiveTrackColor = Color(0xFF1E293B)
            )
        )
    }
}

@Composable
fun StackLogField(tech: String, dsc: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = Color(0xFF10B981),
            modifier = Modifier.size(12.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = "$tech: ",
            color = Color(0xFF94A3B8),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = dsc,
            color = Color.White,
            fontSize = 11.sp,
            fontFamily = FontFamily.Monospace
        )
    }
}


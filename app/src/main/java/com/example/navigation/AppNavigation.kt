package com.example.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import com.example.ui.screens.*
import com.example.viewmodel.MalsentinelViewModel
import kotlinx.coroutines.launch

// Navigation Routes retained for compatibility
const val ROUTE_DASHBOARD = "dashboard"
const val ROUTE_UPLOAD = "upload"
const val ROUTE_THREAT = "threat"
const val ROUTE_REPORT = "report"
const val ROUTE_SETTINGS = "settings"

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainAppContainer(
    navController: NavHostController,
    viewModel: MalsentinelViewModel
) {
    val configuration = LocalConfiguration.current
    val isWideScreen = configuration.screenWidthDp >= 640

    // Main 5-page Pager State
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { 5 })
    val scope = rememberCoroutineScope()

    // Gesture helper hint banner state
    var showGestureHint by remember { mutableStateOf(true) }

    // Swipe-Aware Context banner system
    var previousPage by remember { mutableStateOf(0) }
    var transitionMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(pagerState.currentPage) {
        val cur = pagerState.currentPage
        val prev = previousPage
        if (cur != prev) {
            val msg = when {
                prev == 0 && cur == 1 -> "Threat Intake Pipeline Initiated"
                prev == 1 && cur == 2 -> "Static Analysis Phase Complete"
                prev == 2 && cur == 3 -> "Generating Executive Security Docket"
                prev == 3 && cur == 4 -> "Calibrating Dynamic Scoring Rules"
                prev == 4 && cur == 3 -> "Audit Reports Core Synchronized"
                prev == 3 && cur == 2 -> "Malware Signature Database Online"
                prev == 2 && cur == 1 -> "Static Decoupling Intel Engaged"
                prev == 1 && cur == 0 -> "Primary SOC Dashboard Restored"
                else -> "SOC Operations Telemetry Online"
            }
            transitionMessage = msg
            previousPage = cur
            showGestureHint = false // Automatically dismiss swipe instructions upon interaction

            kotlinx.coroutines.delay(2000L)
            if (transitionMessage == msg) {
                transitionMessage = null
            }
        }
    }

    Scaffold(
        topBar = {
            if (!isWideScreen) {
                EnterpriseSOCHeader(
                    currentPage = pagerState.currentPage
                )
            }
        },
        bottomBar = {
            if (!isWideScreen) {
                NavigationBar(
                    containerColor = Color(0xFF090F1E),
                    modifier = Modifier
                        .navigationBarsPadding()
                        .border(1.dp, Color(0xFF1E293B).copy(alpha = 0.5f), BoxSheetShape())
                ) {
                    navMenuItems.forEachIndexed { index, item ->
                        val selected = pagerState.currentPage == index
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                scope.launch {
                                    pagerState.animateScrollToPage(index)
                                }
                            },
                            icon = { 
                                Icon(
                                    imageVector = if (selected) item.activeIcon else item.inactiveIcon, 
                                    contentDescription = item.label 
                                ) 
                            },
                            label = { 
                                Text(
                                    item.label, 
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (selected) Color.White else Color(0xFF64748B)
                                ) 
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color.White,
                                unselectedIconColor = Color(0xFF64748B),
                                selectedTextColor = Color.White,
                                unselectedTextColor = Color(0xFF64748B),
                                indicatorColor = Color(0xFF8B5CF6)
                            ),
                            modifier = Modifier.testTag("nav_item_${item.route}")
                        )
                    }
                }
            }
        },
        contentWindowInsets = WindowInsets.safeDrawing
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF0B1220))
                .padding(innerPadding)
        ) {
            Row(modifier = Modifier.fillMaxSize()) {
                if (isWideScreen) {
                    NavigationRail(
                        containerColor = Color(0xFF090F1E),
                        modifier = Modifier
                            .fillMaxHeight()
                            .border(1.dp, Color(0xFF1E293B).copy(alpha = 0.5f)),
                        header = {
                            Icon(
                                imageVector = Icons.Default.Shield,
                                contentDescription = null,
                                tint = Color(0xFF8B5CF6),
                                modifier = Modifier
                                    .size(48.dp)
                                    .padding(vertical = 12.dp)
                            )
                        }
                    ) {
                        navMenuItems.forEachIndexed { index, item ->
                            val selected = pagerState.currentPage == index
                            NavigationRailItem(
                                selected = selected,
                                onClick = {
                                    scope.launch {
                                        pagerState.animateScrollToPage(index)
                                    }
                                },
                                icon = { 
                                    Icon(
                                        imageVector = if (selected) item.activeIcon else item.inactiveIcon, 
                                        contentDescription = item.label 
                                    ) 
                                },
                                label = { Text(item.label) },
                                colors = NavigationRailItemDefaults.colors(
                                    selectedIconColor = Color.White,
                                    unselectedIconColor = Color(0xFF64748B),
                                    selectedTextColor = Color.White,
                                    unselectedTextColor = Color(0xFF64748B),
                                    indicatorColor = Color(0xFF8B5CF6)
                                ),
                                modifier = Modifier.testTag("nav_rail_item_${item.route}")
                            )
                        }
                    }
                }

                // Main Pager content viewport
                Box(modifier = Modifier.weight(1f).fillMaxHeight()) {
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.fillMaxSize()
                    ) { pageIndex ->
                        when (pageIndex) {
                            0 -> DashboardScreen(
                                viewModel = viewModel,
                                onNavigateToUpload = {
                                    scope.launch { pagerState.animateScrollToPage(1) }
                                },
                                onNavigateToThreat = {
                                    scope.launch { pagerState.animateScrollToPage(2) }
                                },
                                modifier = Modifier.fillMaxSize()
                            )
                            1 -> UploadScreen(
                                viewModel = viewModel,
                                onNavigateToThreat = {
                                    scope.launch { pagerState.animateScrollToPage(2) }
                                },
                                modifier = Modifier.fillMaxSize()
                            )
                            2 -> ThreatScreen(
                                viewModel = viewModel,
                                modifier = Modifier.fillMaxSize()
                            )
                            3 -> ReportScreen(
                                viewModel = viewModel,
                                modifier = Modifier.fillMaxSize()
                            )
                            4 -> MoreScreen(
                                viewModel = viewModel,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }

                    // Foreground HUD & Gesture Tips Floating indicators
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(bottom = 76.dp, end = 8.dp), // Safely clear bottom navigation
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Sticky Threat Index HUD Overlay (Minimizable tactical readout)
                        StickyThreatScore(viewModel = viewModel)

                        // Floating AI Command Hub globally available FAB
                        FloatingAICommandButton(
                            onActionSelected = { command ->
                                scope.launch {
                                    when (command) {
                                        "Scan APK" -> pagerState.animateScrollToPage(1)
                                        "Generate Report" -> pagerState.animateScrollToPage(3)
                                        "View Threats" -> pagerState.animateScrollToPage(2)
                                        "Search MITRE" -> pagerState.animateScrollToPage(2)
                                        "Open Recent Analysis" -> pagerState.animateScrollToPage(0)
                                    }
                                }
                            }
                        )
                    }

                    // Non-intrusive Gesture Indicator Swipe Advice
                    androidx.compose.animation.AnimatedVisibility(
                        visible = showGestureHint && pagerState.currentPage == 0,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically(),
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(top = 12.dp)
                            .zIndex(100f)
                    ) {
                        Card(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .clickable { showGestureHint = false }
                                .border(1.dp, Color(0xFF8B5CF6).copy(alpha = 0.4f), RoundedCornerShape(20.dp)),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF111827).copy(alpha = 0.95f))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Swipe,
                                    contentDescription = null,
                                    tint = Color(0xFF8B5CF6),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "← Swipe horizontally to explore threat intel workflows →",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Dismiss",
                                    tint = Color(0xFF64748B),
                                    modifier = Modifier.size(12.dp)
                                )
                            }
                        }
                    }

                    // Swipe-Aware Context Transient Transition Overlay
                    androidx.compose.animation.AnimatedVisibility(
                        visible = transitionMessage != null,
                        enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
                        exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(top = 8.dp)
                            .zIndex(99f)
                    ) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .border(1.5.dp, Color(0xFF8B5CF6).copy(alpha = 0.8f), RoundedCornerShape(12.dp)),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1B4B))
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(Color(0xFF22C55E))
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = transitionMessage ?: "",
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.5.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TrinetraLogo(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.size(24.dp),
        contentAlignment = Alignment.Center
    ) {
        androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            // 1. Draw elegant modern security shield path
            val shieldPath = androidx.compose.ui.graphics.Path().apply {
                moveTo(width * 0.5f, 2f)
                cubicTo(width * 0.85f, 2f, width * 0.95f, 6f, width * 0.95f, 12f)
                cubicTo(width * 0.95f, 22f, width * 0.5f, height - 2f, width * 0.5f, height - 2f)
                cubicTo(width * 0.5f, height - 2f, width * 0.05f, 22f, width * 0.05f, 12f)
                cubicTo(width * 0.05f, 6f, width * 0.15f, 2f, width * 0.5f, 2f)
            }
            drawPath(
                path = shieldPath,
                color = Color(0xFF3B82F6),
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = 1.5.dp.toPx())
            )
            drawPath(
                path = shieldPath,
                color = Color(0xFF3B82F6).copy(alpha = 0.15f)
            )

            // 2. Center eye arc
            val eyePath = androidx.compose.ui.graphics.Path().apply {
                moveTo(width * 0.25f, height * 0.5f)
                quadraticTo(width * 0.5f, height * 0.3f, width * 0.75f, height * 0.5f)
                quadraticTo(width * 0.5f, height * 0.7f, width * 0.25f, height * 0.5f)
            }
            drawPath(
                path = eyePath,
                color = Color(0xFF8B5CF6),
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = 1.2.dp.toPx())
            )

            // 3. Central AI Eye pupil
            drawCircle(
                color = Color(0xFF3B82F6),
                radius = 3.dp.toPx(),
                center = androidx.compose.ui.geometry.Offset(width * 0.5f, height * 0.5f)
            )
            
            // Lateral dots for "three-eye"
            drawCircle(
                color = Color(0xFFa78bfa),
                radius = 1.2.dp.toPx(),
                center = androidx.compose.ui.geometry.Offset(width * 0.38f, height * 0.5f)
            )
            drawCircle(
                color = Color(0xFFa78bfa),
                radius = 1.2.dp.toPx(),
                center = androidx.compose.ui.geometry.Offset(width * 0.62f, height * 0.5f)
            )
        }
    }
}

@Composable
fun EnterpriseSOCHeader(
    currentPage: Int,
    modifier: Modifier = Modifier
) {
    var pulseOn by remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        while (true) {
            kotlinx.coroutines.delay(1000L)
            pulseOn = !pulseOn
        }
    }
    val pulseAlphaAnim by animateFloatAsState(
        targetValue = if (pulseOn) 1f else 0.4f,
        animationSpec = tween(600),
        label = "pulse"
    )

    var utcTimeStr by remember { mutableStateOf("00:00:00 UTC") }
    LaunchedEffect(Unit) {
        while (true) {
            val sdf = java.text.SimpleDateFormat("HH:mm:ss", java.util.Locale.US).apply {
                timeZone = java.util.TimeZone.getTimeZone("UTC")
            }
            utcTimeStr = sdf.format(java.util.Date()) + " UTC"
            kotlinx.coroutines.delay(1000L)
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFF090F1E))
            .border(1.dp, Color(0xFF1E293B).copy(alpha = 0.5f))
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                TrinetraLogo()
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "TRINETRA AI",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0xFF22C55E).copy(alpha = pulseAlphaAnim))
                )
                Text(
                    text = "SOC Monitoring Active",
                    color = Color(0xFF22C55E),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 0.5.sp
                )
                Text(
                    text = "•",
                    color = Color(0xFF334155),
                    fontSize = 12.sp
                )
                Text(
                    text = utcTimeStr,
                    color = Color(0xFF94A3B8),
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Spacer(modifier = Modifier.height(2.dp))
            val breadcrumbText = when (currentPage) {
                0 -> "Executive Dashboard • Threat Intel Overview"
                1 -> "Threat Intake • APK Decompilation & Manifest Workspace"
                2 -> "Signature Engine • MITRE ATT&CK Matrix Core"
                3 -> "Security Dossier • System Compliance & Reports"
                4 -> "Control Panel • Configurations & Advanced Audit logs"
                else -> "Live Threat Intelligence Pipeline"
            }
            Text(
                text = breadcrumbText,
                color = Color(0xFF8B5CF6),
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.2.sp
            )
        }


    }
}

@Composable
fun StickyThreatScore(
    viewModel: MalsentinelViewModel,
    modifier: Modifier = Modifier
) {
    val activeRecord by viewModel.activeRecord.collectAsState()
    val score = activeRecord?.riskScore ?: 82
    val verdict = activeRecord?.verdict ?: "HIGH RISK"
    
    val color = when {
        score >= 70 -> Color(0xFFEF4444)
        score >= 40 -> Color(0xFFF97316)
        else -> Color(0xFF22C55E)
    }
    
    var isExpanded by remember { mutableStateOf(false) }
    
    Card(
        modifier = modifier
            .clickable { isExpanded = !isExpanded }
            .border(
                1.dp,
                color.copy(alpha = 0.5f),
                RoundedCornerShape(12.dp)
            ),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A).copy(alpha = 0.95f))
    ) {
        if (!isExpanded) {
            Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(color)
                )
                Text("Threat Index:", color = Color(0xFF94A3B8), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                Text("$score/100", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Black)
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .width(110.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Tactical HUD", color = Color(0xFF64748B), fontSize = 8.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(2.dp))
                Text("Threat Index", color = Color(0xFF94A3B8), fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text("$score", color = color, fontSize = 18.sp, fontWeight = FontWeight.Black)
                    Text("/100", color = Color(0xFF64748B), fontSize = 10.sp)
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = verdict.uppercase(),
                    color = color,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 0.5.sp
                )
            }
        }
    }
}

@Composable
fun FloatingAICommandButton(
    modifier: Modifier = Modifier,
    onActionSelected: (String) -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }
    
    Box(modifier = modifier) {
        FloatingActionButton(
            onClick = { showMenu = true },
            containerColor = Color(0xFF8B5CF6),
            contentColor = Color.White,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.testTag("global_ai_floating_button")
        ) {
            Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = "AI Security Menu",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }
        
        if (showMenu) {
            AlertDialog(
                onDismissRequest = { showMenu = false },
                confirmButton = {},
                dismissButton = {
                    TextButton(onClick = { showMenu = false }) {
                        Text("Close", color = Color(0xFF64748B))
                    }
                },
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, tint = Color(0xFF8B5CF6))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("AI Command Core", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            "Select an AI command to route telemetry workflows instantaneously:",
                            color = Color(0xFF94A3B8),
                            fontSize = 11.sp,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                        
                        val commands = listOf(
                            Triple("Scan APK", Icons.Default.DocumentScanner, "Scan New Application Package"),
                            Triple("Generate Report", Icons.Default.Assessment, "Export Security Audit Dossier"),
                            Triple("View Threats", Icons.Default.Security, "Check Heuristics & Signatures"),
                            Triple("Search MITRE", Icons.Default.TrackChanges, "Locate MITRE ATT&CK Matrix"),
                            Triple("Open Recent Analysis", Icons.Default.History, "Recall Last Investigation Record")
                        )
                        
                        commands.forEach { (title, icon, subtitle) ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        showMenu = false
                                        onActionSelected(title)
                                    }
                                    .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(12.dp)),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A))
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = icon,
                                        contentDescription = null,
                                        tint = Color(0xFFa78bfa),
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(title, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                        Text(subtitle, color = Color(0xFF64748B), fontSize = 10.sp)
                                    }
                                }
                            }
                        }
                    }
                },
                containerColor = Color(0xFF0B1220),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.border(1.dp, Color(0xFF334155), RoundedCornerShape(24.dp))
            )
        }
    }
}

private fun BoxSheetShape() = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)

data class NavItem(
    val route: String,
    val label: String,
    val activeIcon: androidx.compose.ui.graphics.vector.ImageVector,
    val inactiveIcon: androidx.compose.ui.graphics.vector.ImageVector
)

val navMenuItems = listOf(
    NavItem(ROUTE_DASHBOARD, "Overview", Icons.Default.Dashboard, Icons.Default.Dashboard),
    NavItem(ROUTE_UPLOAD, "Analyze", Icons.Default.FindInPage, Icons.Default.FindInPage),
    NavItem(ROUTE_THREAT, "Threats", Icons.Default.GppMaybe, Icons.Default.GppMaybe),
    NavItem(ROUTE_REPORT, "Reports", Icons.Default.Article, Icons.Default.Article),
    NavItem(ROUTE_SETTINGS, "More", Icons.Default.Settings, Icons.Default.Settings)
)

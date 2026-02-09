package com.yusufvural.kaloritakip.ui.dashboard

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yusufvural.kaloritakip.model.DailySummary
import com.yusufvural.kaloritakip.model.MealType
import com.yusufvural.kaloritakip.ui.theme.*
import com.yusufvural.kaloritakip.R
import androidx.compose.ui.res.stringResource
import java.util.Calendar

// --- STATEFUL COMPOSABLE ---
@Composable
fun DashboardScreen(
    dashboardViewModel: DashboardViewModel = hiltViewModel(),
    onNavigate: (String) -> Unit = {}
) {
    val uiState by dashboardViewModel.uiState.collectAsState()

    DashboardContentV3(
        uiState = uiState,
        onNavigate = onNavigate
    )
}

// --- STATELESS COMPOSABLE (V3 - Premium Redesign) ---
@Composable
fun DashboardContentV3(
    uiState: DashboardUiState,
    onNavigate: (String) -> Unit = {}
) {
    // Meals data is now coming from UiState -> MealUiModel

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFBFBFD)), // Keep light background for now as requested
        contentPadding = PaddingValues(start = 24.dp, end = 24.dp, top = 0.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp), // Increased spacing
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Modular Items
        dashboardHeader(uiState)
        
        dashboardSummary(uiState.summary)
        
        dashboardNutrition(uiState.mealList, onNavigate)
    }
}

// --- MODULAR SECTION EXTENSIONS ---

fun LazyListScope.dashboardHeader(uiState: DashboardUiState) {
    item { HeaderSectionV3(uiState) }
}

fun LazyListScope.dashboardSummary(summary: DailySummary) {
    item { 
        SectionTitle(stringResource(R.string.summary))
        EnhancedSummaryCard(summary = summary)
    }
}

fun LazyListScope.dashboardNutrition(
    meals: List<MealUiModel>, 
    onNavigate: (String) -> Unit
) {
    item { 
        SectionTitle(stringResource(R.string.nutrition))
    }
    
    items(meals) { meal ->
        MealRow(
            uiModel = meal,
            onAddClick = { onNavigate("add_food?mealType=${meal.type.name}") },
            onClick = { onNavigate("meal_detail/${meal.type.name}") }
        )
    }
}

@Composable
fun SectionTitle(title: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp, bottom = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        Text(
            title,
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp, // Reverted to 24
                letterSpacing = (-0.5).sp,
                color = TextPrimary
            )
        )
    }
}

@Composable
fun HeaderSectionV3(uiState: DashboardUiState) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 32.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                uiState.dayName,
                style = MaterialTheme.typography.displayMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 36.sp, // Reverted to 36
                    letterSpacing = (-1).sp,
                    color = Color.Black
                )
            )
            Text(
                "${stringResource(R.string.week)} ${uiState.weekCount}",
                color = TextSecondaryLight,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Medium
                    // Removed explicit 16.sp to likely match old default or keep it if it looks close. Old was just bodyLarge.
                )
            )
        }
        
        Row(
            modifier = Modifier
                .padding(top = 8.dp)
                .background(Color.White, RoundedCornerShape(50))
                .border(1.dp, Color(0xFFEEEEEE), RoundedCornerShape(50))
                .padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Rounded.Whatshot, null, tint = Color(0xFFFF5722), modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("${uiState.streakCount}", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }
    }
}

@Composable
fun EnhancedSummaryCard(summary: DailySummary) {
    // Animation States
    var animationPlayed by remember { mutableStateOf(false) }
    
    // Animate progress on launch
    LaunchedEffect(key1 = true) {
        animationPlayed = true
    }

    val caloriesProgress by animateFloatAsState(
        targetValue = if (animationPlayed) (summary.totalCalories.toFloat() / summary.goalCalories).coerceIn(0f, 1f) else 0f,
        animationSpec = tween(durationMillis = 1200, delayMillis = 100), label = "cal"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 16.dp, 
                shape = RoundedCornerShape(32.dp),
                spotColor = PremiumShadow,
                ambientColor = PremiumShadow
            ),
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFFAFAFA))
    ) {
        Box(
            modifier = Modifier.background(
                brush = Brush.verticalGradient(
                    colors = listOf(PremiumCardGradientStart, PremiumCardGradientEnd)
                )
            )
        ) {
            Column(modifier = Modifier.padding(24.dp)) { // Padding 24
                // Main Ring Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // LEFT: Consumed
                    SummaryValueColumn(stringResource(R.string.consumed), "${summary.totalCalories}")
                    
                    // CENTER: Ring
                    Box(contentAlignment = Alignment.Center) {
                        val isExceeded = summary.totalCalories > summary.goalCalories
                        val uiColor = if (isExceeded) PrimaryGradientStart else SuccessGreen

                        // Background Ring
                        Canvas(modifier = Modifier.size(150.dp)) { // Reverted size to 150 (was 160 in new, 150 in old)
                            drawCircle(Color(0xFFF3F4F6), style = Stroke(width = 14.dp.toPx(), cap = StrokeCap.Round)) // Width 14
                        }
                        
                        // Foreground Ring (Animated)
                        Canvas(modifier = Modifier.size(150.dp)) {
                            drawArc(
                                color = uiColor,
                                startAngle = -90f,
                                sweepAngle = 360 * caloriesProgress,
                                useCenter = false,
                                style = Stroke(width = 14.dp.toPx(), cap = StrokeCap.Round)
                            )
                        }
                        
                        // Center Text
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                "${summary.caloriesLeft}", 
                                style = MaterialTheme.typography.displayMedium.copy(
                                    fontWeight = FontWeight.Black, // Reverted Black
                                    fontSize = 36.sp, // Reverted to 36
                                    letterSpacing = (-1).sp // Reverted -1
                                ),
                                color = if (isExceeded) com.yusufvural.kaloritakip.ui.theme.PrimaryRed else TextPrimary
                            )
                            Text(
                                if (isExceeded) stringResource(R.string.limit_exceeded) else stringResource(R.string.remaining), 
                                color = TextSecondaryLight, 
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                            )
                        }
                    }
                    
                    // RIGHT: Burned
                    SummaryValueColumn(stringResource(R.string.burned), "${summary.burnedCalories}")
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Macros
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val carbProgress = if (summary.carbsGoal > 0) (summary.carbsConsumed / summary.carbsGoal).toFloat() else 0f
                    val protProgress = if (summary.proteinGoal > 0) (summary.proteinConsumed / summary.proteinGoal).toFloat() else 0f
                    val fatProgress = if (summary.fatGoal > 0) (summary.fatConsumed / summary.fatGoal).toFloat() else 0f
                    
                    MacroVerticalBar(stringResource(R.string.carb), "${summary.carbsConsumed.toInt()}", carbProgress, NutrientCarb, Modifier.weight(1f), animationPlayed)
                    Spacer(modifier = Modifier.width(16.dp))
                    MacroVerticalBar(stringResource(R.string.protein), "${summary.proteinConsumed.toInt()}", protProgress, NutrientProtein, Modifier.weight(1f), animationPlayed)
                    Spacer(modifier = Modifier.width(16.dp))
                    MacroVerticalBar(stringResource(R.string.fat), "${summary.fatConsumed.toInt()}", fatProgress, NutrientFat, Modifier.weight(1f), animationPlayed)
                }
            }
        }
    }
}

@Composable
fun SummaryValueColumn(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            value, 
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Black, 
                fontSize = 26.sp, // Reverted to 26
                color = TextPrimary
            )
        )
        Text(
            label, 
            color = TextSecondaryLight, 
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.ExtraBold,
                fontSize = 14.sp
            )
        )
    }
}

@Composable
fun MacroVerticalBar(
    label: String, 
    value: String, 
    targetProgress: Float, 
    color: Color, 
    modifier: Modifier,
    playAnimation: Boolean
) {
    val animatedProgress by animateFloatAsState(
        targetValue = if (playAnimation) targetProgress.coerceIn(0f, 1f) else 0f,
        animationSpec = tween(durationMillis = 1000, delayMillis = 300),
        label = "macro"
    )

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        // Text on top
        Text(value + "g", fontWeight = FontWeight.Black, fontSize = 17.sp, color = TextPrimary) // Reverted to 17 + Black
        Text(label, fontSize = 15.sp, color = TextSecondaryLight, fontWeight = FontWeight.Bold) // Reverted to 15 + Bold
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Progress Bar
        LinearProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp) // Slightly thicker
                .clip(CircleShape),
            color = color,
            trackColor = color.copy(alpha = 0.15f),
        )
    }
}

@Composable
fun MealRow(
    uiModel: MealUiModel,
    onAddClick: () -> Unit,
    onClick: () -> Unit
) {
    var animationPlayed by remember { mutableStateOf(false) }
    LaunchedEffect(key1 = true) { animationPlayed = true }
    
    val progress by animateFloatAsState(
        targetValue = if (animationPlayed && uiModel.goalCal > 0) (uiModel.currentCal.toFloat() / uiModel.goalCal).coerceIn(0f, 1f) else 0f,
        animationSpec = tween(durationMillis = 800, delayMillis = 200),
        label = "meal"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(24.dp), spotColor = PremiumShadow)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(0.5.dp, Color(0xFFF0F0F0))
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Animated Icon + Ring
            Box(contentAlignment = Alignment.Center, modifier = Modifier.size(56.dp)) { // Reverted to 56
                Canvas(modifier = Modifier.size(56.dp)) {
                    drawCircle(Color(0xFFF3F4F6), style = Stroke(width = 5.dp.toPx())) // Reverted width
                    drawArc(
                        color = if (uiModel.isExceeded) com.yusufvural.kaloritakip.ui.theme.PrimaryRed else com.yusufvural.kaloritakip.ui.theme.SuccessGreen,
                        startAngle = -90f,
                        sweepAngle = 360 * progress,
                        useCenter = false,
                        style = Stroke(width = 5.dp.toPx(), cap = StrokeCap.Round)
                    )
                }
                Icon(uiModel.icon, contentDescription = null, tint = TextSecondaryLight, modifier = Modifier.size(26.dp)) // Reverted to 26
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    stringResource(uiModel.labelResId), 
                    fontWeight = FontWeight.Black, 
                    fontSize = 22.sp, // Reverted to 22
                    color = TextPrimary
                )
                if (uiModel.description.isNotEmpty()) {
                    Text(
                        uiModel.description,
                        color = TextSecondaryLight,
                        fontSize = 13.sp, // Kept small-ish for description
                        maxLines = 1,
                        lineHeight = 16.sp,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
                
                Text(
                    "${uiModel.currentCal} / ${uiModel.goalCal} Kal", 
                    color = if (uiModel.isExceeded) com.yusufvural.kaloritakip.ui.theme.PrimaryRed else TextSecondaryLight, 
                    fontSize = 15.sp, // Reverted to 15
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            IconButton(
                onClick = onAddClick,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.Black) // Or PrimaryRed
            ) {
                Icon(Icons.Rounded.Add, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DashboardPreviewV3() {
    KaloritakipTheme {
        DashboardContentV3(
            uiState = DashboardUiState(
                summary = DailySummary(totalCalories = 1020, goalCalories = 1888, carbsConsumed = 120.0, carbsGoal = 200.0, proteinConsumed = 80.0, proteinGoal = 150.0, fatConsumed = 40.0, fatGoal = 70.0)
            )
        )
    }
}

package com.yusufvural.kaloritakip.ui.dashboard

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yusufvural.kaloritakip.model.DailySummary
import com.yusufvural.kaloritakip.model.MealType
import com.yusufvural.kaloritakip.ui.theme.KaloritakipTheme

// --- STATEFUL COMPOSABLE ---
@Composable
fun DashboardScreen(
    dashboardViewModel: DashboardViewModel = hiltViewModel(),
    onNavigate: (String) -> Unit = {}
) {
    val uiState by dashboardViewModel.uiState.collectAsState()

    DashboardContentV2(
        uiState = uiState,
        onNavigate = onNavigate,
        onAddWater = { dashboardViewModel.addWater(it) }
    )
}

// --- STATELESS COMPOSABLE ---
@Composable
fun DashboardContentV2(
    uiState: DashboardUiState,
    onNavigate: (String) -> Unit = {},
    onAddWater: (Int) -> Unit = {}
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFBFBFD))
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HeaderSection()

        Spacer(modifier = Modifier.height(20.dp))

        MainCalorieCard(uiState.summary)

        Spacer(modifier = Modifier.height(24.dp))

        MacroGrid(uiState.summary)

        Spacer(modifier = Modifier.height(24.dp))

        WaterTrackerCard(
            currentValue = uiState.waterIntake,
            goalValue = uiState.waterGoal,
            onAddWater = onAddWater
        )

        Spacer(modifier = Modifier.height(24.dp))

        StepTrackerCard(
            currentSteps = uiState.steps,
            stepGoal = uiState.stepGoal
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            "GÜNLÜK ÖĞÜNLER",
            style = MaterialTheme.typography.titleSmall.copy(
                letterSpacing = 1.2.sp,
                color = Color.Gray,
                fontWeight = FontWeight.ExtraBold
            ),
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(16.dp))

        MealGridSection(onNavigate = onNavigate)

        // Menünün alt içeriği kapatmaması için ekstra boşluk
        Spacer(modifier = Modifier.height(120.dp))
    }
}


@Composable
fun HeaderSection() {
    val calendar = remember { java.util.Calendar.getInstance() }
    val hour = calendar.get(java.util.Calendar.HOUR_OF_DAY)
    val greeting = when (hour) {
        in 6..11 -> "Günaydın,"
        in 12..17 -> "Tünaydın,"
        in 18..21 -> "İyi Akşamlar,"
        else -> "İyi Geceler,"
    }

    Row(
        modifier = Modifier.fillMaxWidth().padding(top = 24.dp, bottom = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(greeting, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            Text("Hedefine Az Kaldı! 🚀", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        }
        // Bildirim İkonu
        Box(
            modifier = Modifier.size(45.dp).clip(CircleShape).background(Color(0xFFE31E24).copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Rounded.Notifications, contentDescription = null, tint = Color(0xFFE31E24))
        }
    }
}

@Composable
fun MainCalorieCard(summary: DailySummary) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(30.dp, RoundedCornerShape(32.dp), ambientColor = Color(0xFFE31E24).copy(alpha = 0.4f)),
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(contentAlignment = Alignment.Center) {
                Canvas(modifier = Modifier.size(180.dp)) {
                    drawCircle(Color(0xFFF5F5F5), style = Stroke(width = 16.dp.toPx()))
                }
                val progress = (summary.totalCalories.toFloat() / summary.goalCalories).coerceIn(0f, 1f)
                Canvas(modifier = Modifier.size(180.dp)) {
                    drawArc(
                        brush = Brush.sweepGradient(listOf(Color(0xFFFF5F6D), Color(0xFFE31E24))),
                        startAngle = -90f,
                        sweepAngle = 360 * progress,
                        useCenter = false,
                        style = Stroke(width = 16.dp.toPx(), cap = StrokeCap.Round)
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("${summary.totalCalories}", style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.ExtraBold)
                    Text("kcal", color = Color.Gray, style = MaterialTheme.typography.labelMedium)
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                InfoColumn("Hedef", "${summary.goalCalories}")
                VerticalDivider(modifier = Modifier.height(30.dp).width(1.dp), color = Color(0xFFF0F0F0))
                InfoColumn("Kalan", "${summary.caloriesLeft}", Color(0xFFE31E24))
            }
        }
    }
}

@Composable
fun InfoColumn(label: String, value: String, color: Color = Color.Unspecified) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
        Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = color)
    }
}

@Composable
fun MacroGrid(summary: DailySummary) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        MacroCard("Protein", summary.proteinProgress, Color(0xFFE31E24), Modifier.weight(1f))
        MacroCard("Karbon.", summary.carbsProgress, Color(0xFF4CAF50), Modifier.weight(1f))
        MacroCard("Yağ", summary.fatProgress, Color(0xFFFFC107), Modifier.weight(1f))
    }
}

@Composable
fun MacroCard(name: String, progress: Float, color: Color, modifier: Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFF8F8F8))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(name, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { progress.coerceIn(0f, 1f) },
                modifier = Modifier.fillMaxWidth().height(6.dp).clip(CircleShape),
                color = color,
                trackColor = color.copy(alpha = 0.1f)
            )
        }
    }
}

@Composable
fun MealGridSection(onNavigate: (String) -> Unit = {}) {
    val meals: List<Triple<String, ImageVector, MealType>> = listOf(
        Triple("Kahvaltı", Icons.Rounded.WbSunny, MealType.BREAKFAST),
        Triple("Öğle", Icons.Rounded.LunchDining, MealType.LUNCH),
        Triple("Akşam", Icons.Rounded.DinnerDining, MealType.DINNER),
        Triple("Atıştırma", Icons.Rounded.LocalPizza, MealType.SNACK)
    )
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.height(260.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(meals) { (label, icon, type) ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .clickable { onNavigate("add_food?mealType=${type.name}") },
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0xFFF0F0F0))
            ) {
                Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                    Icon(icon, contentDescription = null, tint = Color(0xFFE31E24), modifier = Modifier.align(Alignment.TopStart))
                    Text(label, fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.BottomStart))
                }
            }
        }
    }
}

@Composable
fun WaterTrackerCard(
    currentValue: Int,
    goalValue: Int,
    onAddWater: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(10.dp, RoundedCornerShape(24.dp), ambientColor = Color.Blue.copy(alpha = 0.1f)),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(45.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFF2196F3).copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Rounded.WaterDrop, contentDescription = null, tint = Color(0xFF2196F3))
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text("Su Takibi", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge)
                    Text("$currentValue / $goalValue ml", color = Color.Gray, style = MaterialTheme.typography.labelMedium)
                }
            }
            
            Button(
                onClick = { onAddWater(250) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3).copy(alpha = 0.1f)),
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Icon(Icons.Rounded.Add, contentDescription = null, tint = Color(0xFF2196F3), modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("250ml", color = Color(0xFF2196F3), fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun StepTrackerCard(
    currentSteps: Int,
    stepGoal: Int
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(10.dp, RoundedCornerShape(24.dp), ambientColor = Color(0xFFFF9800).copy(alpha = 0.1f)),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(45.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFFFF9800).copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Rounded.DirectionsRun, contentDescription = null, tint = Color(0xFFFF9800))
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Adım Sayar", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge)
                    Text("$currentSteps / $stepGoal", color = Color.Gray, style = MaterialTheme.typography.labelSmall)
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                val progress = (currentSteps.toFloat() / stepGoal).coerceIn(0f, 1f)
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxWidth().height(8.dp).clip(CircleShape),
                    color = Color(0xFFFF9800),
                    trackColor = Color(0xFFFF9800).copy(alpha = 0.1f)
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFF9800).copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Rounded.Whatshot, contentDescription = null, tint = Color(0xFFFF9800), modifier = Modifier.size(18.dp))
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewV2() {
    KaloritakipTheme {
        // 1. Mock veri seti (DailySummary)
        val mockSummary = DailySummary(
            totalCalories = 1650,
            goalCalories = 2200,
            proteinConsumed = 110.0,
            proteinGoal = 150.0,
            carbsConsumed = 180.0,
            carbsGoal = 250.0,
            fatConsumed = 45.0,
            fatGoal = 70.0
        )

        // 2. UI State'i mock veriyle oluştur
        val mockUiState = DashboardUiState(summary = mockSummary)

        // 3. Stateless content'e tüm parametreleri gönder
        DashboardContentV2(
            uiState = mockUiState
        )
    }
}
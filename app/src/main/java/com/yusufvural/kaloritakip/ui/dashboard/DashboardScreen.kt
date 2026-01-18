package com.yusufvural.kaloritakip.ui.dashboard

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yusufvural.kaloritakip.model.DailySummary
import com.yusufvural.kaloritakip.model.MealType
import com.yusufvural.kaloritakip.ui.theme.KaloritakipTheme
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
        onNavigate = onNavigate,
        onAddWater = { dashboardViewModel.addWater(it) }
    )
}

// --- STATELESS COMPOSABLE (V3 - Reference Match) ---
@Composable
fun DashboardContentV3(
    uiState: DashboardUiState,
    onNavigate: (String) -> Unit = {},
    onAddWater: (Int) -> Unit = {}
) {
    val meals = listOf(
        MealItemData("Kahvaltı", Icons.Rounded.Coffee, MealType.BREAKFAST, "", 466, 566),
        MealItemData("Öğle Yemeği", Icons.Rounded.LunchDining, MealType.LUNCH, "", 554, 755),
        MealItemData("Akşam Yemeği", Icons.Rounded.DinnerDining, MealType.DINNER, "", 0, 600),
        MealItemData("Atıştırmalık", Icons.Rounded.Fastfood, MealType.SNACK, "", 120, 200)
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFBFBFD)),
        contentPadding = PaddingValues(start = 24.dp, end = 24.dp, top = 24.dp, bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp), // Öğeler arası boşluğu 12dp'ye indirdim
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Modular Items
        dashboardHeader()
        
        dashboardSummary(uiState.summary)
        
        dashboardNutrition(meals, uiState, onNavigate)
    }
}

// --- MODULAR SECTION EXTENSIONS ---

fun LazyListScope.dashboardHeader() {
    item { HeaderSectionV3() }
}

fun LazyListScope.dashboardSummary(summary: DailySummary) {
    item { 
        SectionTitle("Özet", actionText = "Detaylar", onActionClick = { /* Details */ })
        EnhancedSummaryCard(summary = summary)
    }
}

fun LazyListScope.dashboardNutrition(
    meals: List<MealItemData>, 
    uiState: DashboardUiState, 
    onNavigate: (String) -> Unit
) {
    item { 
        SectionTitle("Beslenme", actionText = "Daha Fazla", onActionClick = { /* More */ })
    }
    
    items(meals) { meal ->
        val entriesForMeal = uiState.entries.filter { it.mealType == meal.type }
        val currentCal = entriesForMeal.sumOf { it.calories }
        val description = if (entriesForMeal.isEmpty()) "" else entriesForMeal.joinToString(", ") { it.name }.take(35) + (if (entriesForMeal.joinToString(", ").length > 35) "..." else "")
        
        MealRow(
            label = meal.label,
            icon = meal.icon,
            currentCal = currentCal,
            goalCal = meal.goalCal,
            description = description,
            onAddClick = { onNavigate("add_food?mealType=${meal.type.name}") }
        )
    }
}

@Composable
fun SectionTitle(title: String, actionText: String = "Details", onActionClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        Text(
            title,
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Black,
                fontSize = 24.sp,
                letterSpacing = (-0.5).sp
            )
        )
        Text(
            actionText,
            color = Color(0xFFE31E24), // Kırmızı tema
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            modifier = Modifier.padding(bottom = 4.dp).clickable { onActionClick() }
        )
    }
}

@Composable
fun HeaderSectionV3() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 40.dp), // Üstte daha fazla boşluk
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                "Bugün",
                style = MaterialTheme.typography.displayMedium.copy(
                    fontWeight = FontWeight.Black,
                    fontSize = 40.sp,
                    letterSpacing = (-1.5).sp
                )
            )
            Text(
                "Hafta 1",
                color = Color.Gray,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
            )
        }
        
        Row(
            modifier = Modifier.padding(top = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HeaderStatusIcon(Icons.Rounded.Star, "2000", Color(0xFF2196F3))
            Spacer(modifier = Modifier.width(16.dp))
            HeaderStatusIcon(Icons.Rounded.Whatshot, "135", Color(0xFFFF5722))
            Spacer(modifier = Modifier.width(16.dp))
            Icon(
                Icons.Rounded.DateRange, 
                contentDescription = null, 
                modifier = Modifier.size(24.dp),
                tint = Color.Black
            )
        }
    }
}

@Composable
fun HeaderStatusIcon(icon: ImageVector, text: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(22.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Text(text, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
    }
}

@Composable
fun EnhancedSummaryCard(summary: DailySummary) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFF0F0F0))
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SummaryValueColumn("Alınan", "${summary.totalCalories}")
                
                Box(contentAlignment = Alignment.Center) {
                    Canvas(modifier = Modifier.size(150.dp)) {
                        drawCircle(Color(0xFFF5F5F5), style = Stroke(width = 14.dp.toPx()))
                    }
                    val progress = (summary.totalCalories.toFloat() / summary.goalCalories).coerceIn(0f, 1f)
                    Canvas(modifier = Modifier.size(150.dp)) {
                        drawArc(
                            color = Color(0xFFE31E24),
                            startAngle = -90f,
                            sweepAngle = 360 * progress,
                            useCenter = false,
                            style = Stroke(width = 14.dp.toPx(), cap = StrokeCap.Round)
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            "${summary.caloriesLeft}", 
                            style = MaterialTheme.typography.displayLarge.copy(
                                fontWeight = FontWeight.Black,
                                fontSize = 48.sp, // Daha büyük merkez değeri
                                letterSpacing = (-1).sp
                            )
                        )
                        Text(
                            "Kalan", 
                            color = Color.Gray, 
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        )
                    }
                }

                SummaryValueColumn("Yakılan", "142")
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                MacroHorizontalBar("Karb", summary.carbsConsumed.toInt(), summary.carbsGoal.toInt(), Color(0xFF00C49F), Modifier.weight(1f))
                Spacer(modifier = Modifier.width(12.dp))
                MacroHorizontalBar("Protein", summary.proteinConsumed.toInt(), summary.proteinGoal.toInt(), Color(0xFFE31E24), Modifier.weight(1f))
                Spacer(modifier = Modifier.width(12.dp))
                MacroHorizontalBar("Yağ", summary.fatConsumed.toInt(), summary.fatGoal.toInt(), Color(0xFFFFBB28), Modifier.weight(1f))
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
                fontSize = 26.sp
            )
        )
        Text(
            label, 
            color = Color.Gray, 
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.ExtraBold,
                fontSize = 14.sp
            )
        )
    }
}

@Composable
fun MacroHorizontalBar(label: String, current: Int, goal: Int, color: Color, modifier: Modifier) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            label, 
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.ExtraBold,
                fontSize = 15.sp
            ), 
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(10.dp))
        val progress = if (goal > 0) (current.toFloat() / goal) else 0f
        LinearProgressIndicator(
            progress = { progress.coerceIn(0f, 1f) },
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp) // Daha kalın ve kullanışlı bar
                .clip(CircleShape),
            color = color,
            trackColor = color.copy(alpha = 0.15f)
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            "${current} / ${goal} g", 
            fontSize = 17.sp, 
            fontWeight = FontWeight.Black,
            letterSpacing = 0.5.sp
        )
    }
}

data class MealItemData(
    val label: String,
    val icon: ImageVector,
    val type: MealType,
    val description: String,
    val currentCal: Int,
    val goalCal: Int
)

@Composable
fun MealRow(
    label: String,
    icon: ImageVector,
    currentCal: Int,
    goalCal: Int,
    description: String,
    onAddClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFF0F0F0))
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.size(56.dp)) {
                val progress = if (goalCal > 0) (currentCal.toFloat() / goalCal) else 0f
                Canvas(modifier = Modifier.size(56.dp)) {
                    drawCircle(Color(0xFFF5F5F5), style = Stroke(width = 5.dp.toPx()))
                    drawArc(
                        color = Color(0xFF00C49F),
                        startAngle = -90f,
                        sweepAngle = 360 * progress.coerceIn(0f, 1f),
                        useCenter = false,
                        style = Stroke(width = 5.dp.toPx(), cap = StrokeCap.Round)
                    )
                }
                Icon(icon, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(26.dp))
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    label, 
                    fontWeight = FontWeight.Black, 
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 22.sp)
                )
                Text(
                    "$currentCal / $goalCal Kal", 
                    color = Color.Gray, 
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                )
                if (description.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        description,
                        color = Color.LightGray,
                        style = MaterialTheme.typography.labelMedium,
                        maxLines = 1
                    )
                } else {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "Henüz eklenmedi",
                        color = Color.LightGray,
                        style = MaterialTheme.typography.labelMedium,
                        maxLines = 1
                    )
                }
            }

            IconButton(
                onClick = onAddClick,
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(Color.Black)
            ) {
                Icon(Icons.Rounded.Add, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp))
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DashboardPreviewV3() {
    KaloritakipTheme {
        DashboardContentV3(
            uiState = DashboardUiState(
                summary = DailySummary(totalCalories = 1020, goalCalories = 1888)
            )
        )
    }
}

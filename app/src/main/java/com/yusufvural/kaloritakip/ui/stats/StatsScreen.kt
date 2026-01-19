package com.yusufvural.kaloritakip.ui.stats

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Whatshot
import androidx.compose.material3.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Calendar
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
fun StatsScreen(
    viewModel: StatsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFBFBFD)),
        contentPadding = PaddingValues(start = 24.dp, end = 24.dp, top = 24.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item { Spacer(modifier = Modifier.height(12.dp)) }

        // --- WEEKLY CHART ---
        item {
            ChartHeader(uiState)
            WeeklyChartCard(uiState)
        }

        item { Spacer(modifier = Modifier.height(24.dp)) }

        // --- MACRO BALANCES ---
        item {
            SectionTitle("Makro Dengesi")
            MacroAveragesCard(uiState)
        }

        item { Spacer(modifier = Modifier.height(12.dp)) }

        // --- STREAK CARD ---
        item {
            StreakCard(uiState)
        }
    }
}

@Composable
fun ChartHeader(uiState: StatsUiState) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        Column {
            Text(
                "Kalori Alımı", 
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Black, 
                    fontSize = 30.sp,
                    letterSpacing = (-1).sp
                )
            )
            Text(
                "Son 7 Günlük Enerji Akışın",
                color = Color.Gray,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold, fontSize = 18.sp)
            )
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(
                "${uiState.averageCalories}", 
                style = MaterialTheme.typography.displaySmall.copy(
                    fontWeight = FontWeight.Black,
                    color = Color(0xFFE31E24),
                    fontSize = 38.sp
                )
            )
            Text(
                "Ortalama kcal", 
                color = Color.Gray, 
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, fontSize = 14.sp)
            )
        }
    }
}

@Composable
fun WeeklyChartCard(uiState: StatsUiState) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFF0F0F0))
    ) {
        Column(modifier = Modifier.padding(vertical = 28.dp, horizontal = 12.dp)) {
            // Borsa Tarzı Çizgi Grafik (Line Chart)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(horizontal = 8.dp)
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val points = uiState.weeklyData
                    if (points.isEmpty()) return@Canvas

                    val widthBetweenPoints = size.width / (points.size - 1)
                    val maxH = size.height

                    // 1. Alan Gradyanı (Gölge Etkisi)
                    val fillPath = Path().apply {
                        moveTo(0f, maxH)
                        points.forEachIndexed { index, ratio ->
                            val x = index * widthBetweenPoints
                            val y = maxH * (1f - ratio.coerceIn(0.05f, 1f))
                            lineTo(x, y)
                        }
                        lineTo(size.width, maxH)
                        close()
                    }
                    drawPath(
                        path = fillPath,
                        brush = Brush.verticalGradient(
                            colors = listOf(Color(0xFFE31E24).copy(alpha = 0.2f), Color.Transparent)
                        )
                    )

                    // 2. Ana Çizgi (Borsa Çizgisi)
                    val strokePath = Path().apply {
                        points.forEachIndexed { index, ratio ->
                            val x = index * widthBetweenPoints
                            val y = maxH * (1f - ratio.coerceIn(0.05f, 1f))
                            if (index == 0) moveTo(x, y) else lineTo(x, y)
                        }
                    }
                    drawPath(
                        path = strokePath,
                        color = Color(0xFFE31E24),
                        style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
                    )

                    // 3. Noktalar ve Değerler
                    points.forEachIndexed { index, ratio ->
                        val x = index * widthBetweenPoints
                        val y = maxH * (1f - ratio.coerceIn(0.05f, 1f))
                        
                        drawCircle(
                            color = Color.White,
                            radius = 6.dp.toPx(),
                            center = Offset(x, y)
                        )
                        drawCircle(
                            color = Color(0xFFE31E24),
                            radius = 3.dp.toPx(),
                            center = Offset(x, y)
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))

            // Gün İsimleri
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                uiState.weeklyLabels.forEach { label ->
                    val calendar = Calendar.getInstance()
                    val daysLabels = listOf("Paz", "Pzt", "Sal", "Çar", "Per", "Cum", "Cmt")
                    val currentDayLabel = daysLabels[calendar.get(Calendar.DAY_OF_WEEK) - 1]
                    val isToday = label == currentDayLabel
                    
                    Text(
                        label,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = if (isToday) FontWeight.Black else FontWeight.Bold,
                            fontSize = if (isToday) 18.sp else 16.sp,
                            color = if (isToday) Color.Black else Color.Gray.copy(alpha = 0.7f)
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun MacroAveragesCard(uiState: StatsUiState) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFF0F0F0))
    ) {
        Column(modifier = Modifier.padding(24.dp)) {

            // Görsel Dağılım Barı (Segmented Progress Bar)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(16.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFF5F5F5))
            ) {
                if (uiState.proteinPercentage > 0) {
                    Box(modifier = Modifier.weight(uiState.proteinPercentage.toFloat().coerceAtLeast(1f)).fillMaxHeight().background(Color(0xFFE31E24)))
                }
                if (uiState.carbsPercentage > 0) {
                    Box(modifier = Modifier.weight(uiState.carbsPercentage.toFloat().coerceAtLeast(1f)).fillMaxHeight().background(Color(0xFF00C49F)))
                }
                if (uiState.fatPercentage > 0) {
                    Box(modifier = Modifier.weight(uiState.fatPercentage.toFloat().coerceAtLeast(1f)).fillMaxHeight().background(Color(0xFFFFBB28)))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                MacroStatRow("Protein", "%${uiState.proteinPercentage}", "${uiState.proteinGram}g", Color(0xFFE31E24))
                MacroStatRow("Karbonhidrat", "%${uiState.carbsPercentage}", "${uiState.carbsGram}g", Color(0xFF00C49F))
                MacroStatRow("Yağ", "%${uiState.fatPercentage}", "${uiState.fatGram}g", Color(0xFFFFBB28))
            }
        }
    }
}

@Composable
fun MacroStatRow(label: String, percentage: String, amount: String, color: Color) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(color))
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            label, 
            modifier = Modifier.weight(1f), 
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold, fontSize = 19.sp), // 17 -> 19
            color = Color.Gray
        )
        Text(
            amount,
            modifier = Modifier.padding(end = 12.dp),
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold, fontSize = 19.sp) // 17 -> 19
        )
        Text(
            percentage, 
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black, fontSize = 21.sp), // 19 -> 21
            color = Color.Black
        )
    }
}

@Composable
fun StreakCard(uiState: StatsUiState) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(32.dp))
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(Color(0xFFE31E24).copy(alpha = 0.1f), Color(0xFFFF9800).copy(alpha = 0.05f))
                )
            )
            .border(1.dp, Color(0xFFE31E24).copy(alpha = 0.2f), RoundedCornerShape(32.dp))
            .padding(28.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Ateş Efekti Alanı
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(64.dp)
                .background(Color(0xFFE31E24).copy(alpha = 0.1f), CircleShape)
        ) {
            Icon(
                Icons.Rounded.Whatshot, 
                contentDescription = null, 
                tint = Color(0xFFE31E24), 
                modifier = Modifier.size(36.dp)
            )
        }
        
        Spacer(modifier = Modifier.width(20.dp))
        
        Column {
            Text(
                uiState.insightTitle, 
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Black, 
                    fontSize = 24.sp,
                    color = Color(0xFFE31E24)
                )
            )
            Text(
                uiState.insightMessage, 
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold, 
                    fontSize = 18.sp,
                    color = Color.Black.copy(alpha = 0.7f)
                )
            )
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        Text(
            title,
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Black,
                fontSize = 26.sp, // 24 -> 26
                letterSpacing = (-0.5).sp
            )
        )
    }
}

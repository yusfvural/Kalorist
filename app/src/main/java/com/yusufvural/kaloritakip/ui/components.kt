package com.yusufvural.kaloritakip.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import com.yusufvural.kaloritakip.model.DailySummary

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
fun MacroCard(name: String, progress: Float, color: Color, modifier: Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFF8F8F8))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(name, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = Color.Gray)
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
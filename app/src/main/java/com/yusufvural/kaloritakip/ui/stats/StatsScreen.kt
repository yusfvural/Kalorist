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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun StatsScreen() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFBFBFD)),
        contentPadding = PaddingValues(start = 24.dp, end = 24.dp, top = 40.dp, bottom = 120.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- HEADER ---
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    "İstatistikler",
                    style = MaterialTheme.typography.displayMedium.copy(
                        fontWeight = FontWeight.Black,
                        fontSize = 40.sp,
                        letterSpacing = (-1.5).sp
                    )
                )
                Text(
                    "Son 7 gündeki performansın",
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
                )
            }
        }

        item { Spacer(modifier = Modifier.height(12.dp)) }

        // --- WEEKLY CHART ---
        item {
            SectionTitle("Haftalık Özet", onActionClick = { /* Details */ })
            WeeklyChartCard()
        }

        item { Spacer(modifier = Modifier.height(12.dp)) }

        // --- MACRO BALANCES ---
        item {
            SectionTitle("Makro Dengesi", actionText = "Detaylar", onActionClick = { /* More */ })
            MacroAveragesCard()
        }

        item { Spacer(modifier = Modifier.height(12.dp)) }

        // --- INSIGHTS ---
        item {
            InsightsCard()
        }
    }
}

@Composable
fun WeeklyChartCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFF0F0F0))
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "Kalori Alımı", 
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black, fontSize = 20.sp)
                )
                Text(
                    "Ort: 1950 kcal", 
                    color = Color(0xFFE31E24), 
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.ExtraBold)
                )
            }
            
            Spacer(modifier = Modifier.height(28.dp))

            // Basit ve Modern Çubuk Grafik
            Row(
                modifier = Modifier.fillMaxWidth().height(150.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.Bottom
            ) {
                val data = listOf(0.6f, 0.8f, 0.5f, 0.9f, 0.7f, 1f, 0.4f)
                val days = listOf("Pzt", "Sal", "Çar", "Per", "Cum", "Cmt", "Paz")
                
                data.forEachIndexed { index, height ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .width(14.dp)
                                .fillMaxHeight(height)
                                .clip(CircleShape)
                                .background(if (height >= 0.9f) Color(0xFFE31E24) else Color(0xFFF0F0F0))
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            days[index], 
                            fontSize = 11.sp, 
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MacroAveragesCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFF0F0F0))
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            MacroStatRow("Protein", "%35", Color(0xFFE31E24))
            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFF8F8F8))
            MacroStatRow("Karbonhidrat", "%45", Color(0xFF00C49F))
            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFF8F8F8))
            MacroStatRow("Yağ", "%20", Color(0xFFFFBB28))
        }
    }
}

@Composable
fun MacroStatRow(label: String, percentage: String, color: Color) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(12.dp).clip(CircleShape).background(color))
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            label, 
            modifier = Modifier.weight(1f), 
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
        )
        Text(
            percentage, 
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black)
        )
    }
}

@Composable
fun InsightsCard() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(32.dp))
            .background(Color(0xFFE31E24).copy(alpha = 0.05f))
            .border(1.dp, Color(0xFFE31E24).copy(alpha = 0.1f), RoundedCornerShape(32.dp))
            .padding(24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Rounded.Whatshot, contentDescription = null, tint = Color(0xFFE31E24), modifier = Modifier.size(36.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                "Harika Gidiyorsun!", 
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black, fontSize = 18.sp),
                color = Color(0xFFE31E24)
            )
            Text(
                "Son 3 gündür kalori hedefini hiç aşmadın.", 
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                color = Color.Black.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
fun SectionTitle(title: String, actionText: String = "Detaylar", onActionClick: () -> Unit) {
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
                fontSize = 24.sp,
                letterSpacing = (-0.5).sp
            )
        )
        Text(
            actionText,
            color = Color(0xFFE31E24),
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            modifier = Modifier.padding(bottom = 4.dp).clickable { onActionClick() }
        )
    }
}

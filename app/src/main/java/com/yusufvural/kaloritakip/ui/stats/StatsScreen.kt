package com.yusufvural.kaloritakip.ui.stats

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Whatshot
import androidx.compose.material3.*
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
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFBFBFD)) // Dashboard ile aynı arka plan
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        
        Text("HAFTALIK ÖZET", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Text("Son 7 gündeki performansın", color = Color.Gray, style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = Modifier.height(24.dp))

        // 1. HAFTALIK KALORİ GRAFİĞİ KARTI
        WeeklyChartCard()

        Spacer(modifier = Modifier.height(24.dp))

        // 2. HAFTALIK MAKRO ORTALAMALARI
        Text("HAFTALIK MAKRO DENGESİ", 
            style = MaterialTheme.typography.titleSmall.copy(letterSpacing = 1.2.sp, color = Color.Gray, fontWeight = FontWeight.ExtraBold))
        
        Spacer(modifier = Modifier.height(16.dp))
        
        MacroAveragesCard()

        Spacer(modifier = Modifier.height(24.dp))

        // 3. BAŞARI ÖZETİ (Insights)
        InsightsCard()
        
        Spacer(modifier = Modifier.height(120.dp)) // Dock menü için boşluk
    }
}

@Composable
fun WeeklyChartCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(20.dp, RoundedCornerShape(32.dp), ambientColor = Color.Black.copy(alpha = 0.1f)),
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Kalori Alımı", fontWeight = FontWeight.Bold)
                Text("Ort: 1950 kcal", color = Color(0xFFE31E24), fontWeight = FontWeight.SemiBold)
            }
            
            Spacer(modifier = Modifier.height(32.dp))

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
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(days[index], fontSize = 10.sp, color = Color.Gray)
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
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFF0F0F0))
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            MacroStatRow("Protein", "%35", Color(0xFFE31E24))
            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFF8F8F8))
            MacroStatRow("Karbonhidrat", "%45", Color(0xFF4CAF50))
            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFF8F8F8))
            MacroStatRow("Yağ", "%20", Color(0xFFFFC107))
        }
    }
}

@Composable
fun MacroStatRow(label: String, percentage: String, color: Color) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(color))
        Spacer(modifier = Modifier.width(12.dp))
        Text(label, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium)
        Text(percentage, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun InsightsCard() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xFFE31E24).copy(alpha = 0.05f))
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Rounded.Whatshot, contentDescription = null, tint = Color(0xFFE31E24), modifier = Modifier.size(32.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text("Harika Gidiyorsun!", fontWeight = FontWeight.Bold, color = Color(0xFFE31E24))
            Text("Son 3 gündür kalori hedefini hiç aşmadın.", fontSize = 12.sp, color = Color.Black.copy(alpha = 0.7f))
        }
    }
}

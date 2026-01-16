package com.yusufvural.kaloritakip.ui


import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import com.yusufvural.kaloritakip.ui.dashboard.DashboardUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardContentV2(
    uiState: DashboardUiState,
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    Scaffold(
        containerColor = Color(0xFFFBFBFD),
        bottomBar = { ModernFloatingNavigationBar(currentRoute, onNavigate) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HeaderSection()
            Spacer(modifier = Modifier.height(20.dp))
            MainCalorieCard(uiState.summary) // Shared components'den geliyor
            Spacer(modifier = Modifier.height(24.dp))

            // Makro Row
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                MacroCard("Protein", uiState.summary.proteinProgress, Color(0xFFE31E24), Modifier.weight(1f))
                MacroCard("Karbon.", uiState.summary.carbsProgress, Color(0xFF4CAF50), Modifier.weight(1f))
                MacroCard("Yağ", uiState.summary.fatProgress, Color(0xFFFFC107), Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(32.dp))
            Text("GÜNLÜK ÖĞÜNLER", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.ExtraBold, color = Color.Gray, modifier = Modifier.align(Alignment.Start))
            Spacer(modifier = Modifier.height(16.dp))

            MealGrid()
            Spacer(modifier = Modifier.height(120.dp))
        }
    }
}

@Composable
fun HeaderSection() {
    Row(modifier = Modifier.fillMaxWidth().padding(top = 24.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Column {
            Text("Hoşgeldiniz,", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            Text("Hedefine Az Kaldı! 🚀", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        }
        Box(modifier = Modifier.size(45.dp).clip(CircleShape).background(Color(0xFFE31E24).copy(alpha = 0.1f)), contentAlignment = Alignment.Center) {
            Icon(Icons.Rounded.Notifications, contentDescription = null, tint = Color(0xFFE31E24))
        }
    }
}

@Composable
fun MealGrid() {
    val meals = listOf("Kahvaltı" to Icons.Rounded.WbSunny, "Öğle" to Icons.Rounded.LunchDining, "Akşam" to Icons.Rounded.DinnerDining, "Atıştırma" to Icons.Rounded.LocalPizza)
    LazyVerticalGrid(columns = GridCells.Fixed(2), modifier = Modifier.height(260.dp), horizontalArrangement = Arrangement.spacedBy(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        items(meals) { (name, icon) ->
            Card(modifier = Modifier.fillMaxWidth().height(110.dp), shape = RoundedCornerShape(28.dp), colors = CardDefaults.cardColors(containerColor = Color.White), border = BorderStroke(1.dp, Color(0xFFF0F0F0))) {
                Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                    Icon(icon, contentDescription = null, tint = Color(0xFFE31E24), modifier = Modifier.align(Alignment.TopStart))
                    Text(name, fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.BottomStart))
                }
            }
        }
    }
}
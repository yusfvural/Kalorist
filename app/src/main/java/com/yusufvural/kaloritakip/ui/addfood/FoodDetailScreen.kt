package com.yusufvural.kaloritakip.ui.addfood

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodDetailScreen(
    foodName: String,
    baseCalories: Int,
    onNavigateBack: () -> Unit,
    onAddComplete: () -> Unit
) {
    var portion by remember { mutableStateOf(100f) } // Default 100g
    val totalCalories = remember(portion) { (baseCalories * (portion / 100f)).roundToInt() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Besin Detayı", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Rounded.ArrowBack, contentDescription = null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFFBFBFD))
            )
        },
        containerColor = Color(0xFFFBFBFD)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // 1. BESİN HERO BÖLÜMÜ
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE31E24).copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Rounded.Fastfood, contentDescription = null, tint = Color(0xFFE31E24), modifier = Modifier.size(60.dp))
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(foodName, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold)
            Text("100g için $baseCalories kcal", color = Color.Gray)

            Spacer(modifier = Modifier.height(40.dp))

            // 2. KALORİ GÖSTERGESİ
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0xFFF0F0F0))
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("TOPLAM KALORİ", color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text("$totalCalories", style = MaterialTheme.typography.displayMedium, fontWeight = FontWeight.ExtraBold, color = Color(0xFFE31E24))
                    Text("kcal", color = Color.Gray)
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // 3. PORSİYON SEÇİCİ
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("MİKTAR (gram)", fontWeight = FontWeight.Bold)
                Text("${portion.roundToInt()} g", color = Color(0xFFE31E24), fontWeight = FontWeight.ExtraBold)
            }
            
            Spacer(modifier = Modifier.height(16.dp))

            Slider(
                value = portion,
                onValueChange = { portion = it },
                valueRange = 10f..500f,
                steps = 49,
                colors = SliderDefaults.colors(
                    thumbColor = Color(0xFFE31E24),
                    activeTrackColor = Color(0xFFE31E24),
                    inactiveTrackColor = Color(0xFFE31E24).copy(alpha = 0.1f)
                )
            )

            Spacer(modifier = Modifier.weight(1f))

            // 4. EKLE BUTONU
            Button(
                onClick = onAddComplete,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .padding(bottom = 8.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE31E24))
            ) {
                Text("Günüme Ekle", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

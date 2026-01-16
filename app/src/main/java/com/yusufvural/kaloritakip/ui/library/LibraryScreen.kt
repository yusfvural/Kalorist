package com.yusufvural.kaloritakip.ui.library

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen() {
    var searchQuery by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFBFBFD)) // Dashboard ile aynı arka plan
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Text("BESİN KÜTÜPHANESİ", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Text("Besin değerlerini hızlıca öğren", color = Color.Gray, style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = Modifier.height(20.dp))

        // 1. MODERN ARAMA ÇUBUĞU
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp)),
            placeholder = { Text("Besin ara...", color = Color.Gray) },
            leadingIcon = { Icon(Icons.Rounded.Search, contentDescription = null, tint = Color(0xFFE31E24)) },
            singleLine = true,
            shape = RoundedCornerShape(20.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFE31E24),
                unfocusedBorderColor = Color(0xFFEEEEEE),
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 2. BESİN LİSTESİ
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 120.dp) // Dock menü için boşluk
        ) {
            // Örnek besin listesi (Buraya daha sonra API/Room gelecek)
            val foods = listOf(
                LibraryFoodItem("Fıstık Ezmesi", "100g", 588, 25.0, 20.0, 50.0),
                LibraryFoodItem("Yumurta", "1 adet", 78, 6.0, 0.6, 5.0),
                LibraryFoodItem("Tavuk Göğsü", "100g", 165, 31.0, 0.0, 3.6),
                LibraryFoodItem("Yulaf", "100g", 389, 16.9, 66.0, 6.9),
                LibraryFoodItem("Brokoli", "100g", 34, 2.8, 7.0, 0.4)
            )

            items(foods) { food ->
                LibraryFoodCard(food)
            }
        }
    }
}

@Composable
fun LibraryFoodCard(food: LibraryFoodItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp), // Dashboard kartlarıyla uyumlu yumuşaklık
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFF0F0F0))
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(food.name, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                    Text(food.portion, color = Color.Gray, style = MaterialTheme.typography.bodySmall)
                }
                Text("${food.kcal} kcal", fontWeight = FontWeight.ExtraBold, color = Color(0xFFE31E24), fontSize = 18.sp)
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Makro Bilgi Satırı (Hızlı Bakış)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                MacroMiniInfo("Protein", "${food.protein}g", Color(0xFFE31E24))
                MacroMiniInfo("Karbon.", "${food.carbs}g", Color(0xFF4CAF50))
                MacroMiniInfo("Yağ", "${food.fat}g", Color(0xFFFFC107))
            }
        }
    }
}

@Composable
fun MacroMiniInfo(label: String, value: String, color: Color) {
    Column {
        Text(label, fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
        Text(value, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = color)
    }
}

// Veri Modeli
data class LibraryFoodItem(
    val name: String,
    val portion: String,
    val kcal: Int,
    val protein: Double,
    val carbs: Double,
    val fat: Double
)

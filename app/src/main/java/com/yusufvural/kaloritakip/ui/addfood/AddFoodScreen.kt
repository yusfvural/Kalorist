package com.yusufvural.kaloritakip.ui.addfood

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFoodScreen(
    onNavigate: (String) -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFBFBFD))
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Text("BESİN EKLE", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        
        Spacer(modifier = Modifier.height(20.dp))

        // 1. MODERN ARAMA ÇUBUĞU
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp)),
            placeholder = { Text("Yemek, marka veya barkod...", color = Color.Gray) },
            leadingIcon = { Icon(Icons.Rounded.Search, contentDescription = null, tint = Color(0xFFE31E24)) },
            trailingIcon = { 
                IconButton(onClick = { /* Barkod Aç */ }) {
                    Icon(Icons.Rounded.QrCodeScanner, contentDescription = null, tint = Color.Gray)
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(20.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFE31E24),
                unfocusedBorderColor = Color(0xFFEEEEEE),
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(20.dp))

        // 2. KATEGORİ ÇİPLERİ (Horizontal Scroll)
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            val categories = listOf("Tümü", "Kahvaltı", "Öğle", "Akşam", "Atıştırmalık", "İçecek")
            categories.forEach { category ->
                FilterChip(
                    selected = category == "Tümü",
                    onClick = { },
                    label = { Text(category) },
                    shape = CircleShape,
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFFE31E24),
                        selectedLabelColor = Color.White
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 3. BESİN LİSTESİ (Son Aramalar veya Sonuçlar)
        Text(
            if (searchQuery.isEmpty()) "SON EKLENENLER" else "ARAMA SONUÇLARI",
            style = MaterialTheme.typography.labelLarge,
            color = Color.Gray,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 120.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            val mockFoods = listOf(
                Triple("Haşlanmış Yumurta", "1 adet (50g)", "78 kcal"),
                Triple("Tam Buğday Ekmeği", "1 dilim (25g)", "65 kcal"),
                Triple("Fıstık Ezmesi", "1 yemek kaşığı", "94 kcal"),
                Triple("Yulaf Ezmesi", "100g", "389 kcal"),
                Triple("Süzme Yoğurt", "1 kase", "130 kcal")
            )

            items(mockFoods) { (name, portion, cal) ->
                val calValue = cal.filter { it.isDigit() }.toIntOrNull() ?: 0
                FoodResultItem(name, portion, cal, onClick = {
                    onNavigate("food_detail/$name/$calValue")
                })
            }
        }
    }
}

@Composable
fun FoodResultItem(name: String, portion: String, cal: String, onClick: () -> Unit = {}) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFF5F5F5))
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(name, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge)
                Text(portion, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(cal, fontWeight = FontWeight.ExtraBold, color = Color(0xFFE31E24), fontSize = 16.sp)
                Spacer(modifier = Modifier.width(12.dp))
                // Hızlı Ekleme Butonu
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE31E24).copy(alpha = 0.1f))
                        .clickable { /* Ekleme Mantığı */ },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Rounded.Add, contentDescription = null, tint = Color(0xFFE31E24), modifier = Modifier.size(20.dp))
                }
            }
        }
    }
}

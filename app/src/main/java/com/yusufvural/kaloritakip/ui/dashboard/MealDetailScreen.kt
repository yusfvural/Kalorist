package com.yusufvural.kaloritakip.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yusufvural.kaloritakip.model.FoodEntry
import com.yusufvural.kaloritakip.model.MealType

@Composable
fun MealDetailScreen(
    mealType: MealType,
    viewModel: DashboardViewModel,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val mealEntries = uiState.entries.filter { it.mealType == mealType }
    
    val totalKcal = mealEntries.sumOf { it.calories }
    val totalProtein = mealEntries.sumOf { it.protein }
    val totalCarbs = mealEntries.sumOf { it.carbs }
    val totalFat = mealEntries.sumOf { it.fat }

    val mealTitle = when (mealType) {
        MealType.BREAKFAST -> "Kahvaltı"
        MealType.LUNCH -> "Öğle Yemeği"
        MealType.DINNER -> "Akşam Yemeği"
        MealType.SNACK -> "Atıştırmalık"
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFBFBFD)),
        contentPadding = PaddingValues(start = 24.dp, end = 24.dp, top = 24.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- HEADER With Custom Back Button ---
        item {
             Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onNavigateBack,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .border(1.dp, Color(0xFFF0F0F0), CircleShape)
                ) {
                    Icon(Icons.Rounded.ArrowBack, contentDescription = "Geri", tint = Color.Black)
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        mealTitle,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Black,
                            fontSize = 32.sp,
                            letterSpacing = (-1).sp
                        )
                    )
                    Text(
                        "${mealEntries.size} besin eklendi",
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
                    )
                }
            }
        }

        // --- SUMMARY CARD ---
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF0F0F0))
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Öğün Özeti", fontWeight = FontWeight.Black, fontSize = 20.sp)
                        Text("$totalKcal kcal", fontWeight = FontWeight.Black, fontSize = 24.sp, color = Color(0xFFE31E24))
                    }
                    
                    Spacer(modifier = Modifier.height(20.dp))
                    
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        MacroDetailItem("Protein", String.format("%.0f", totalProtein) + "g", Color(0xFFE31E24))
                        MacroDetailItem("Karb", String.format("%.0f", totalCarbs) + "g", Color(0xFF00C49F))
                        MacroDetailItem("Yağ", String.format("%.0f", totalFat) + "g", Color(0xFFFFBB28))
                    }
                }
            }
        }

        // --- FOOD LIST HEADER ---
        item {
             Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    "Besinler",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Black,
                        fontSize = 24.sp,
                        letterSpacing = (-0.5).sp
                    )
                )
            }
        }

        // --- FOOD LIST ITEMS ---
        if (mealEntries.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .background(Color.White, RoundedCornerShape(24.dp))
                        .border(1.dp, Color(0xFFF0F0F0), RoundedCornerShape(24.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Bu öğün için henüz besin eklenmemiş.", color = Color.Gray)
                }
            }
        } else {
            items(mealEntries) { entry ->
                FoodEntryItemRefined(entry = entry, onDelete = { viewModel.deleteFood(entry) })
            }
        }
    }
}

@Composable
fun FoodEntryItemRefined(entry: FoodEntry, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF0F0F0))
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            
            // Icon Placeholder
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Color(0xFFF5F5F5), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                 Text(
                    entry.name.take(1).uppercase(),
                    fontWeight = FontWeight.Black,
                    fontSize = 20.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(entry.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "${entry.calories} kcal • P:${entry.protein.toInt()} K:${entry.carbs.toInt()} Y:${entry.fat.toInt()}",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium
                )
            }

            IconButton(
                onClick = onDelete,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFFEBEE))
            ) {
                Icon(Icons.Rounded.Delete, contentDescription = "Sil", tint = Color(0xFFE31E24), modifier = Modifier.size(20.dp))
            }
        }
    }
}

@Composable
fun MacroDetailItem(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(modifier = Modifier.size(8.dp).background(color, CircleShape))
        Spacer(modifier = Modifier.height(8.dp))
        Text(value, fontWeight = FontWeight.Bold, fontSize = 19.sp)
        Text(label, color = Color.Gray, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
    }
}

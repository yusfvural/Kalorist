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
import kotlin.math.roundToInt

import androidx.hilt.navigation.compose.hiltViewModel
import com.yusufvural.kaloritakip.domain.model.SearchResult

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(
    viewModel: LibraryViewModel = hiltViewModel(),
    onNavigateToDetail: (String, Int, Double, Double, Double) -> Unit = { _, _, _, _, _ -> }
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFBFBFD))
            .padding(horizontal = 24.dp)
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        // --- HEADER ---
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                "Kütüphane",
                style = MaterialTheme.typography.displayMedium.copy(
                    fontWeight = FontWeight.Black,
                    fontSize = 40.sp,
                    letterSpacing = (-1.5).sp
                )
            )
            Text(
                "Besin değerlerini hızlıca öğren",
                color = Color.Gray,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 1. MODERN ARAMA ÇUBUĞU
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { viewModel.onSearchQueryChanged(it) },
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp)),
            placeholder = { Text("Besin veya marka ara...", color = Color.Gray) },
            leadingIcon = { Icon(Icons.Rounded.Search, contentDescription = null, tint = Color(0xFFE31E24)) },
            singleLine = true,
            shape = RoundedCornerShape(24.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFE31E24),
                unfocusedBorderColor = Color(0xFFF0F0F0),
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 2. BESİN LİSTESİ (REAKTİF)
        Box(modifier = Modifier.fillMaxSize()) {
            when (val state = uiState) {
                is LibraryUiState.Idle -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Rounded.Fastfood, 
                            contentDescription = null, 
                            modifier = Modifier.size(80.dp), 
                            tint = Color(0xFFE31E24).copy(alpha = 0.05f)
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            "Binlerce besin seni bekliyor", 
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = Color.Gray,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
                is LibraryUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = Color(0xFFE31E24)
                    )
                }
                is LibraryUiState.Success -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(bottom = 120.dp)
                    ) {
                        items(state.results) { food ->
                            LibraryFoodCard(food, onClick = {
                                onNavigateToDetail(food.label, food.calories, food.protein, food.fat, food.carbs)
                            })
                        }
                    }
                }
                is LibraryUiState.Empty -> {
                    Text(
                        "Sonuç bulunamadı.",
                        modifier = Modifier.align(Alignment.Center),
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold, color = Color.Gray)
                    )
                }
                is LibraryUiState.Error -> {
                    Text(
                        state.message,
                        modifier = Modifier.align(Alignment.Center),
                        color = Color(0xFFE31E24),
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Black)
                    )
                }
            }
        }
    }
}

@Composable
fun LibraryFoodCard(food: SearchResult, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFF0F0F0))
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        food.label, 
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black)
                    )
                    Text(
                        "100g için değerler", 
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                        color = Color.Gray
                    )
                }
                Text(
                    "${food.calories} kcal", 
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Black, 
                        color = Color(0xFFE31E24)
                    )
                )
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                MacroMiniInfo("Protein", "${food.protein.roundToInt()}g", Color(0xFFE31E24))
                MacroMiniInfo("Karbon.", "${food.carbs.roundToInt()}g", Color(0xFF00C49F))
                MacroMiniInfo("Yağ", "${food.fat.roundToInt()}g", Color(0xFFFFBB28))
            }
        }
    }
}

@Composable
fun MacroMiniInfo(label: String, value: String, color: Color) {
    Column {
        Text(
            label, 
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Black),
            color = Color.Gray
        )
        Text(
            value, 
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Black),
            color = color
        )
    }
}

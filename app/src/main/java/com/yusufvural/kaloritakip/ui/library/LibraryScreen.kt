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
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Text("BESİN KÜTÜPHANESİ", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Text("Besin değerlerini hızlıca öğren", color = Color.Gray, style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = Modifier.height(20.dp))

        // 1. MODERN ARAMA ÇUBUĞU
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { viewModel.onSearchQueryChanged(it) },
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

        // 2. BESİN LİSTESİ (REAKTİF)
        Box(modifier = Modifier.fillMaxSize()) {
            when (val state = uiState) {
                is LibraryUiState.Idle -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Rounded.Search, contentDescription = null, modifier = Modifier.size(64.dp), tint = Color.LightGray)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Binlerce besin arasından ara...", color = Color.Gray)
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
                        color = Color.Gray
                    )
                }
                is LibraryUiState.Error -> {
                    Text(
                        state.message,
                        modifier = Modifier.align(Alignment.Center),
                        color = Color(0xFFE31E24),
                        fontWeight = FontWeight.Bold
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
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFF0F0F0))
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(food.label, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                    Text("100g için değerler", color = Color.Gray, style = MaterialTheme.typography.bodySmall)
                }
                Text("${food.calories} kcal", fontWeight = FontWeight.ExtraBold, color = Color(0xFFE31E24), fontSize = 18.sp)
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                MacroMiniInfo("Protein", "${food.protein.roundToInt()}g", Color(0xFFE31E24))
                MacroMiniInfo("Karbon.", "${food.carbs.roundToInt()}g", Color(0xFF4CAF50))
                MacroMiniInfo("Yağ", "${food.fat.roundToInt()}g", Color(0xFFFFC107))
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

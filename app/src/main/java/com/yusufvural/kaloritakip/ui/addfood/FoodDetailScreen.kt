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
import com.yusufvural.kaloritakip.model.MealType
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodDetailScreen(
    foodName: String,
    baseCalories: Int,
    protein: Double = 0.0,
    fat: Double = 0.0,
    carbs: Double = 0.0,
    initialMealType: MealType? = null,
    onNavigateBack: () -> Unit,
    onAddComplete: (String, Int, Double, Double, Double, MealType) -> Unit
) {
    var portion by remember { mutableStateOf(100f) } // Default 100g
    var selectedMealType by remember { mutableStateOf(initialMealType ?: MealType.BREAKFAST) }

    val factor = portion / 100f
    val totalCalories = remember(portion) { (baseCalories * factor).roundToInt() }
    val totalProtein = remember(portion) { protein * factor }
    val totalFat = remember(portion) { fat * factor }
    val totalCarbs = remember(portion) { carbs * factor }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Besin Detayı", 
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black)
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Rounded.ArrowBack, contentDescription = null, tint = Color.Black)
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
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // 1. BESİN HERO BÖLÜMÜ
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(com.yusufvural.kaloritakip.ui.theme.PrimaryRed.copy(alpha = 0.05f))
                    .border(1.dp, com.yusufvural.kaloritakip.ui.theme.PrimaryRed.copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Rounded.Fastfood, contentDescription = null, tint = com.yusufvural.kaloritakip.ui.theme.PrimaryRed, modifier = Modifier.size(50.dp))
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                foodName, 
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Black,
                    fontSize = 32.sp,
                    letterSpacing = (-1).sp
                )
            )
            Text(
                "100g için $baseCalories kcal", 
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(30.dp))

            // 2. KALORİ GÖSTERGESİ
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0xFFF0F0F0))
            ) {
                Column(
                    modifier = Modifier.padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "TOPLAM KALORİ", 
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Black),
                        color = Color.Gray
                    )
                    Text(
                        "$totalCalories", 
                        style = MaterialTheme.typography.displayLarge.copy(
                            fontWeight = FontWeight.Black,
                            fontSize = 64.sp,
                            letterSpacing = (-2).sp
                        ), 
                        color = com.yusufvural.kaloritakip.ui.theme.PrimaryRed
                    )
                    Text(
                        "kcal", 
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
            
            // YENİ: ÖĞÜN SEÇİMİ
            Text(
                "ÖĞÜN SEÇİMİ", 
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black),
                modifier = Modifier.align(Alignment.Start)
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                MealTypeOption(
                    label = "Kahvaltı", 
                    isSelected = selectedMealType == MealType.BREAKFAST,
                    onClick = { selectedMealType = MealType.BREAKFAST },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                MealTypeOption(
                    label = "Öğle", 
                    isSelected = selectedMealType == MealType.LUNCH,
                    onClick = { selectedMealType = MealType.LUNCH },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                MealTypeOption(
                    label = "Akşam", 
                    isSelected = selectedMealType == MealType.DINNER,
                    onClick = { selectedMealType = MealType.DINNER },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                MealTypeOption(
                    label = "Ara", 
                    isSelected = selectedMealType == MealType.SNACK,
                    onClick = { selectedMealType = MealType.SNACK },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            // 3. PORSİYON SEÇİCİ
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Bottom) {
                Text(
                    "MİKTAR (gram)", 
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black)
                )
                Text(
                    "${portion.roundToInt()} g", 
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Black),
                    color = com.yusufvural.kaloritakip.ui.theme.PrimaryRed
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))

            Slider(
                value = portion,
                onValueChange = { portion = it },
                valueRange = 10f..500f,
                steps = 49,
                colors = SliderDefaults.colors(
                    thumbColor = com.yusufvural.kaloritakip.ui.theme.PrimaryRed,
                    activeTrackColor = com.yusufvural.kaloritakip.ui.theme.PrimaryRed,
                    inactiveTrackColor = com.yusufvural.kaloritakip.ui.theme.PrimaryRed.copy(alpha = 0.1f)
                )
            )

            Spacer(modifier = Modifier.weight(1f))

            // 4. EKLE BUTONU
            Button(
                onClick = { onAddComplete(foodName, totalCalories, totalProtein, totalFat, totalCarbs, selectedMealType) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                shape = RoundedCornerShape(32.dp),
                colors = ButtonDefaults.buttonColors(containerColor = com.yusufvural.kaloritakip.ui.theme.PrimaryRed)
            ) {
                Text(
                    "Günüme Ekle", 
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black)
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun MealTypeOption(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isSelected) com.yusufvural.kaloritakip.ui.theme.PrimaryRed else Color.White
    val contentColor = if (isSelected) Color.White else Color.Black
    val borderColor = if (isSelected) com.yusufvural.kaloritakip.ui.theme.PrimaryRed else Color(0xFFE0E0E0)

    Box(
        modifier = modifier
            .height(40.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .border(1.dp, borderColor, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
            color = contentColor,
            fontSize = 13.sp
        )
    }
}

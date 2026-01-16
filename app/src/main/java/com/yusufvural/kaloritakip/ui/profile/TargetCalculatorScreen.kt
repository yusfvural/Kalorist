package com.yusufvural.kaloritakip.ui.profile

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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TargetCalculatorScreen(
    onNavigateBack: () -> Unit
) {
    var gender by remember { mutableStateOf("Erkek") }
    var age by remember { mutableStateOf("25") }
    var weight by remember { mutableStateOf("80") }
    var height by remember { mutableStateOf("180") }
    var activityLevel by remember { mutableStateOf(1.2f) } // Sedentary by default

    val scrollState = rememberScrollState()

    // Calculation Logic (Mifflin-St Jeor)
    val bmr = remember(gender, age, weight, height) {
        val w = weight.toDoubleOrNull() ?: 0.0
        val h = height.toDoubleOrNull() ?: 0.0
        val a = age.toDoubleOrNull() ?: 0.0
        
        if (gender == "Erkek") {
            10 * w + 6.25 * h - 5 * a + 5
        } else {
            10 * w + 6.25 * h - 5 * a - 161
        }
    }
    
    val tdee = (bmr * activityLevel).roundToInt()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Hedef Hesaplayıcı", fontWeight = FontWeight.Bold) },
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
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // 1. GENDER SELECTION
            Text("Cinsiyet", fontWeight = FontWeight.Bold, color = Color.Gray, fontSize = 12.sp)
            Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                listOf("Erkek", "Kadın").forEach { item ->
                    val isSelected = gender == item
                    Button(
                        onClick = { gender = item },
                        modifier = Modifier.weight(1f).height(48.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isSelected) Color(0xFFE31E24) else Color.White
                        ),
                        border = if (!isSelected) BorderStroke(1.dp, Color(0xFFEEEEEE)) else null
                    ) {
                        Text(item, color = if (isSelected) Color.White else Color.Black)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 2. INPUT FIELDS (Age, Weight, Height)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                CalculatorInput("Yaş", age, { age = it }, "Yıl", Modifier.weight(1f))
                CalculatorInput("Kilo", weight, { weight = it }, "kg", Modifier.weight(1f))
                CalculatorInput("Boy", height, { height = it }, "cm", Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 3. ACTIVITY LEVEL
            Text("Aktivite Seviyesi", fontWeight = FontWeight.Bold, color = Color.Gray, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(8.dp))
            ActivityPicker(activityLevel) { activityLevel = it }

            Spacer(modifier = Modifier.height(32.dp))

            // 4. RESULTS SECTION
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0xFFF0F0F0))
            ) {
                Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("GÜNLÜK KALORİ İHTİYACI", color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text("$tdee", style = MaterialTheme.typography.displayMedium, fontWeight = FontWeight.ExtraBold, color = Color(0xFFE31E24))
                    Text("kcal / gün", color = Color.Gray)
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    HorizontalDivider(color = Color(0xFFF8F8F8))
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                        ResultSummary("Kilo Ver", (tdee * 0.8).roundToInt().toString(), Color(0xFF4CAF50))
                        ResultSummary("Koru", tdee.toString(), Color(0xFFE31E24))
                        ResultSummary("Kilo Al", (tdee * 1.2).roundToInt().toString(), Color(0xFFFFC107))
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onNavigateBack,
                modifier = Modifier.fillMaxWidth().height(60.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE31E24))
            ) {
                Text("Hedefimi Kaydet", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorInput(label: String, value: String, onValueChange: (String) -> Unit, suffix: String, modifier: Modifier) {
    Column(modifier = modifier) {
        Text(label, fontWeight = FontWeight.Bold, color = Color.Gray, fontSize = 12.sp)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            shape = RoundedCornerShape(16.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFE31E24),
                unfocusedBorderColor = Color(0xFFEEEEEE),
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            )
        )
    }
}

@Composable
fun ActivityPicker(current: Float, onSelect: (Float) -> Unit) {
    val levels = listOf(
        Triple("Az", "Hareketsiz yaşam", 1.2f),
        Triple("Orta", "Haftada 2-3 gün", 1.375f),
        Triple("Yüksek", "Haftada 4rd gün+", 1.55f)
    )
    
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        levels.forEach { (label, sub, value) ->
            val isSelected = current == value
            Card(
                modifier = Modifier.weight(1f).clickable { onSelect(value) },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) Color(0xFFE31E24).copy(alpha = 0.05f) else Color.White
                ),
                border = BorderStroke(1.dp, if (isSelected) Color(0xFFE31E24) else Color(0xFFEEEEEE))
            ) {
                Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(label, fontWeight = FontWeight.Bold, color = if (isSelected) Color(0xFFE31E24) else Color.Black)
                    Text(sub, fontSize = 8.sp, color = Color.Gray)
                }
            }
        }
    }
}

@Composable
fun ResultSummary(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
        Text(value, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = color)
        Text("kcal", fontSize = 8.sp, color = Color.Gray)
    }
}

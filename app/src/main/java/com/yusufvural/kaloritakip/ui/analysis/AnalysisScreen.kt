package com.yusufvural.kaloritakip.ui.analysis

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.yusufvural.kaloritakip.model.ExerciseEntry

@Composable
fun AnalysisScreen(
    viewModel: AnalysisViewModel = hiltViewModel()
) {
    val waterIntake by viewModel.waterIntake.collectAsState()
    val exercises by viewModel.exercises.collectAsState()

    var showExerciseDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item {
            Text(
                text = "Su Takibi",
                fontSize = 26.sp,
                fontWeight = FontWeight.Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            WaterTrackerCard(
                currentAmount = waterIntake,
                goalAmount = 2200, // Örnek hedef: 2200 ml (~74 fl oz)
                onAddWater = { viewModel.addWater(250) },
                onRemoveWater = { viewModel.removeWater(250) }
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Aktiviteler",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Black
                )
                TextButton(onClick = { /* TODO: Detaylı activite */ }) {
                    Text("Daha fazla", color = Color(0xFF00695C))
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            ActivitiesCard(
                steps = 6540, // Mock veri
                exercises = exercises,
                onDeleteExercise = { viewModel.deleteExercise(it) }
            )
        }
        
        item {
             Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                AddActivityButton(icon = Icons.Default.Add, label = "Ekle", onClick = {
                     showExerciseDialog = true
                })
            }
        }
    }

    if (showExerciseDialog) {
        AddExerciseDialog(
            onDismiss = { showExerciseDialog = false },
            onConfirm = { name, calories ->
                viewModel.addExercise(name, calories, 30) // Varsayılan süre 30dk örneği
                showExerciseDialog = false
            }
        )
    }
}

@Composable
fun WaterTrackerCard(
    currentAmount: Int,
    goalAmount: Int,
    onAddWater: () -> Unit,
    onRemoveWater: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Su",
                fontSize = 24.sp,
                fontWeight = FontWeight.Black
            )
            Text(
                text = "Hedef: $goalAmount ml",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "$currentAmount ml",
                fontSize = 40.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.Black
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Dynamic Status Text
            val percentage = if (goalAmount > 0) currentAmount.toFloat() / goalAmount else 0f
            val statusText = when {
                percentage >= 1f -> "Harika! Hedefine ulaştın \uD83C\uDF89"
                percentage > 0.5f -> "Yarıyı geçtin, devam et! \uD83D\uDCA7"
                currentAmount > 0 -> "Su içmeyi unutma"
                else -> "Güne bir bardak suyla başla \uD83C\uDF1E"
            }
            val statusColor = if (percentage >= 1f) Color(0xFF00C853) else Color.Gray

            Text(
                text = statusText,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = statusColor
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Water Glasses Grid
            val glassCapacity = 250
            val maxGlasses = 12 // Daha fazla bardak istendi
            
            // Kaç bardak dolu?
            val filledCount = (currentAmount / glassCapacity).coerceAtMost(maxGlasses)
            
            // Grid mantığı
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 1. Sıra (6 bardak)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                     for (i in 0 until 6) {
                         val isFilled = i < filledCount
                         WaterGlass(isFilled = isFilled)
                     }
                }
                
                // 2. Sıra (6 bardak/buton)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                     for (i in 6 until 12) {
                         if (i == 11) {
                             // Son slot kontrol butonları
                             AddWaterControl(onAdd = onAddWater, onRemove = onRemoveWater)
                         } else {
                             val isFilled = i < filledCount
                             val showCheck = isFilled && (i == filledCount - 1)
                             WaterGlass(isFilled = isFilled, showCheck = showCheck)
                         }
                     }
                }
            }
        }
    }
}

@Composable
fun AddWaterControl(onAdd: () -> Unit, onRemove: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = onRemove, modifier = Modifier.size(36.dp)) {
            Icon(Icons.Default.Remove, contentDescription = "Azalt", tint = Color.Gray)
        }
        Spacer(modifier = Modifier.width(4.dp))
        Box(
            modifier = Modifier
                .size(40.dp, 55.dp) // Bardağa denk boyut
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFFE0F7FA))
                .clickable { onAdd() },
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Add, contentDescription = "Su Ekle", tint = Color(0xFF0277BD), modifier = Modifier.size(28.dp))
        }
    }
}

@Composable
fun WaterGlass(isFilled: Boolean, showCheck: Boolean = false) {
    Box(contentAlignment = Alignment.BottomEnd) {
        Canvas(modifier = Modifier.size(40.dp, 55.dp)) {
            val path = Path().apply {
                moveTo(0f, 0f)
                lineTo(size.width, 0f)
                lineTo(size.width * 0.85f, size.height)
                lineTo(size.width * 0.15f, size.height)
                close()
            }
            
            drawPath(
                path = path,
                color = if (isFilled) Color(0xFF42A5F5) else Color(0xFFE0F7FA), 
                style = Fill
            )
        }
        
        if (showCheck) {
            Box(
                modifier = Modifier
                    .offset(x = 8.dp, y = 6.dp)
                    .size(20.dp)
                    .background(Color(0xFF00C853), CircleShape)
                    .border(1.dp, Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Check, 
                    contentDescription = null, 
                    tint = Color.White,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}

@Composable
fun ActivitiesCard(
    steps: Int,
    exercises: List<ExerciseEntry>,
    onDeleteExercise: (ExerciseEntry) -> Unit
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            // Steps Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
               Icon(
                   imageVector = Icons.Default.DirectionsRun,
                   contentDescription = "Adımlar",
                   modifier = Modifier.size(48.dp),
                   tint = Color(0xFFE53935) // Kırmızı
               )
               Spacer(modifier = Modifier.width(16.dp))
               Column(modifier = Modifier.weight(1f)) {
                   Text(
                       text = "Adımlar",
                       fontSize = 22.sp,
                       fontWeight = FontWeight.Bold
                   )
                   Text(
                       text = "Otomatik Takip",
                       fontSize = 16.sp,
                       color = Color.Gray
                   )
               }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { /* Adım sayar bağlantısı */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935)), // Kırmızı
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth().height(48.dp)
            ) {
                Text("Bağla", color = Color.White, fontWeight = FontWeight.Bold)
            }
             Spacer(modifier = Modifier.height(16.dp))
             TextButton(
                onClick = { /* Manuel giriş */ },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Adımları manuel ekle", color = Color(0xFF00695C), fontWeight = FontWeight.Bold)
            }

            // Exercise List with Delete
            if (exercises.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Divider()
                Spacer(modifier = Modifier.height(16.dp))
                exercises.forEach { exercise ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(exercise.name, fontWeight = FontWeight.Medium, fontSize = 16.sp)
                            Text("${exercise.caloriesBurnt} kcal", color = Color.Gray, fontSize = 14.sp)
                        }
                        IconButton(onClick = { onDeleteExercise(exercise) }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Sil",
                                tint = Color.Red.copy(alpha = 0.6f)
                            )
                        }
                    }
                    Divider(color = Color.LightGray.copy(alpha = 0.5f))
                }
            }
        }
    }
}

@Composable
fun AddActivityButton(icon: ImageVector, label: String, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .border(1.5.dp, Color.LightGray, RoundedCornerShape(16.dp))
                .clip(RoundedCornerShape(16.dp))
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = label, tint = Color.Black)
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = label, fontSize = 12.sp)
    }
}

@Composable
fun AddExerciseDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, Int) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var calories by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Egzersiz Ekle",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                
                Spacer(modifier = Modifier.height(24.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Egzersiz Adı") },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = calories,
                    onValueChange = { calories = it },
                    label = { Text("Yakılan Kalori") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(32.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = onDismiss,
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text("İptal", fontSize = 16.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                    }
                    
                    Button(
                        onClick = {
                            val cal = calories.toIntOrNull() ?: 0
                            if (name.isNotEmpty() && cal > 0) {
                                onConfirm(name, cal)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFE53935), // Modern Red
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Text(
                            text = "Ekle", 
                            fontSize = 16.sp, 
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

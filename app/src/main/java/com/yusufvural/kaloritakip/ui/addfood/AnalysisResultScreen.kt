package com.yusufvural.kaloritakip.ui.addfood

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yusufvural.kaloritakip.ui.dashboard.MacroDetailItem

@Composable
fun AnalysisResultScreen(
    imagePath: String,
    foodName: String,
    calories: Int,
    protein: Double,
    carbs: Double,
    fat: Double,
    onConfirm: () -> Unit,
    onRetake: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFBFBFD))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "AI Analiz Sonucu",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Black,
                fontSize = 28.sp
            )
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Image Preview
        val bitmap = BitmapFactory.decodeFile(imagePath)
        if (bitmap != null) {
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .clip(RoundedCornerShape(32.dp))
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF0F0F0))
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(foodName, fontWeight = FontWeight.Black, fontSize = 24.sp)
                Text("Tahmini Değerler (1 Porsiyon)", color = Color.Gray, fontSize = 14.sp)
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    MacroDetailItem("Kalori", "$calories", Color.Gray)
                    MacroDetailItem("Protein", "${protein}g", Color(0xFFE31E24))
                    MacroDetailItem("Karb", "${carbs}g", Color(0xFF00C49F))
                    MacroDetailItem("Yağ", "${fat}g", Color(0xFFFFBB28))
                }
            }
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Button(
                onClick = onRetake,
                modifier = Modifier.weight(1f).height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF0F0F0)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Tekrar Çek", color = Color.Black, fontWeight = FontWeight.Bold)
            }
            
            Button(
                onClick = onConfirm,
                modifier = Modifier.weight(1f).height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Öğüne Ekle", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}

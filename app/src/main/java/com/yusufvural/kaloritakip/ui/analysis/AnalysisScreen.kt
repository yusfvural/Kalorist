package com.yusufvural.kaloritakip.ui.analysis

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Analytics
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AnalysisScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFBFBFD))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Rounded.Analytics,
            contentDescription = null,
            modifier = Modifier.size(100.dp),
            tint = Color(0xFFE31E24).copy(alpha = 0.5f)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            "ANALİZ & EGZERSİZ",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.ExtraBold,
            color = Color.Black
        )
        Text(
            "Çok Yakında...",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "Burada günlük aktivitelerini ve detaylı grafiklerini görebileceksin.",
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            color = Color.Gray,
            fontSize = 14.sp
        )
    }
}

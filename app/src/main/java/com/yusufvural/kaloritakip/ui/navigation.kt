package com.yusufvural.kaloritakip.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun ModernFloatingNavigationBar(
    currentRoute: String,
    onNavigate: (String) -> Unit
) {
    Surface(
        modifier = Modifier
            .padding(horizontal = 24.dp, vertical = 30.dp)
            .height(80.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(40.dp),
        color = Color.White.copy(alpha = 0.95f),
        shadowElevation = 20.dp
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavIcon(Icons.Rounded.Home, "dashboard", currentRoute, onNavigate)
            NavIcon(Icons.Rounded.BarChart, "stats", currentRoute, onNavigate)

            // Merkez Artı Butonu
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE31E24))
                    .clickable { onNavigate("add_food") },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Rounded.Add, contentDescription = null, tint = Color.White, modifier = Modifier.size(35.dp))
            }

            NavIcon(Icons.Rounded.DirectionsRun, "analysis", currentRoute, onNavigate)
            NavIcon(Icons.Rounded.Person, "profile", currentRoute, onNavigate)
        }
    }
}

@Composable
private fun NavIcon(icon: ImageVector, route: String, currentRoute: String, onNavigate: (String) -> Unit) {
    IconButton(onClick = { onNavigate(route) }) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (currentRoute == route) Color(0xFFE31E24) else Color.LightGray,
            modifier = Modifier.size(28.dp)
        )
    }
}
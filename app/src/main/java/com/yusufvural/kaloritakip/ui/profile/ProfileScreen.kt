package com.yusufvural.kaloritakip.ui.profile

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ProfileScreen(
    onNavigate: (String) -> Unit = {}
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFBFBFD))
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        
        Text("PROFİL", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        
        Spacer(modifier = Modifier.height(24.dp))

        // 1. KULLANICI ÖZET KARTI
        UserProfileHeader()

        Spacer(modifier = Modifier.height(32.dp))

        // 2. HEDEF VE AYARLAR BÖLÜMÜ
        Text(
            "HEDEFLERİM", 
            style = MaterialTheme.typography.labelLarge, 
            color = Color.Gray, 
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        
        ProfileOptionItem(
            title = "Günlük Kalori Hedefi", 
            value = "2200 kcal", 
            icon = Icons.Rounded.TrackChanges,
            onClick = { onNavigate("target_calculator") }
        )
        ProfileOptionItem("Hedef Kilo", "75 kg", Icons.Rounded.MonitorWeight)

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            "UYGULAMA AYARLARI", 
            style = MaterialTheme.typography.labelLarge, 
            color = Color.Gray, 
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        
        ProfileOptionItem("Hatırlatıcılar", "Açık", Icons.Rounded.NotificationsActive)
        ProfileOptionItem("Tema", "Aydınlık", Icons.Rounded.DarkMode)
        ProfileOptionItem("Bize Ulaşın", "", Icons.Rounded.SupportAgent)

        Spacer(modifier = Modifier.height(40.dp))

        // 3. ÇIKIŞ BUTONU
        Button(
            onClick = { /* Logout */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(1.dp, Color(0xFFE31E24).copy(alpha = 0.2f))
        ) {
            Icon(Icons.Rounded.Logout, contentDescription = null, tint = Color(0xFFE31E24))
            Spacer(modifier = Modifier.width(12.dp))
            Text("Çıkış Yap", color = Color(0xFFE31E24), fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(120.dp)) // Dock menü boşluğu
    }
}

@Composable
fun UserProfileHeader() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(15.dp, RoundedCornerShape(32.dp), ambientColor = Color.Black.copy(alpha = 0.1f)),
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Profil Resmi Alanı
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE31E24).copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Rounded.Person, 
                    contentDescription = null, 
                    tint = Color(0xFFE31E24), 
                    modifier = Modifier.size(45.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(20.dp))
            
            Column {
                Text("Yusuf Vural", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text("yusufvural@example.com", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Surface(
                    color = Color(0xFFE31E24),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        "PREMIUM ÜYE", 
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        }
    }
}

@Composable
fun ProfileOptionItem(title: String, value: String, icon: ImageVector, onClick: () -> Unit = {}) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFF5F5F5))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFFBFBFD)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = Color(0xFFE31E24), modifier = Modifier.size(22.dp))
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Text(title, modifier = Modifier.weight(1f), fontWeight = FontWeight.Medium)
            
            if (value.isNotEmpty()) {
                Text(value, color = Color.Gray, style = MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.width(8.dp))
            }
            
            Icon(Icons.Rounded.ChevronRight, contentDescription = null, tint = Color.LightGray)
        }
    }
}

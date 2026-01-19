package com.yusufvural.kaloritakip.ui.profile

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFBFBFD)),
        contentPadding = PaddingValues(start = 24.dp, end = 24.dp, top = 24.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- HEADER ---
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    "Profil",
                    style = MaterialTheme.typography.displayMedium.copy(
                        fontWeight = FontWeight.Black,
                        fontSize = 40.sp,
                        letterSpacing = (-1.5).sp
                    )
                )
                Text(
                    "Kişisel hedeflerin ve ayarların",
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
                )
            }
        }

        item { Spacer(modifier = Modifier.height(12.dp)) }

        // --- USER HEADER ---
        item {
            UserProfileHeader()
        }

        item { Spacer(modifier = Modifier.height(12.dp)) }

        // --- GOALS SECTION ---
        item {
            SectionTitle("Hedeflerim", onActionClick = { /* More */ })
        }
        
        item {
            ProfileOptionItem(
                title = "Günlük Kalori Hedefi", 
                value = "2200 kcal", 
                icon = Icons.Rounded.TrackChanges,
                onClick = { onNavigate("target_calculator") }
            )
        }
        item { ProfileOptionItem("Hedef Kilo", "75 kg", Icons.Rounded.MonitorWeight) }

        item { Spacer(modifier = Modifier.height(12.dp)) }

        // --- SETTINGS SECTION ---
        item {
            SectionTitle("Uygulama Ayarları", actionText = "Yardım", onActionClick = { /* More */ })
        }
        
        item { ProfileOptionItem("Hatırlatıcılar", "Açık", Icons.Rounded.NotificationsActive) }
        item { ProfileOptionItem("Tema", "Aydınlık", Icons.Rounded.DarkMode) }
        item { ProfileOptionItem("Bize Ulaşın", "", Icons.Rounded.SupportAgent) }

        item { Spacer(modifier = Modifier.height(24.dp)) }

        // --- LOGOUT BUTTON ---
        item {
            Button(
                onClick = { /* Logout */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                shape = RoundedCornerShape(32.dp),
                border = BorderStroke(1.dp, Color(0xFFE31E24).copy(alpha = 0.1f))
            ) {
                Icon(Icons.Rounded.Logout, contentDescription = null, tint = Color(0xFFE31E24))
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    "Çıkış Yap", 
                    color = Color(0xFFE31E24), 
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black)
                )
            }
        }
    }
}

@Composable
fun UserProfileHeader() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFF0F0F0))
    ) {
        Row(
            modifier = Modifier.padding(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE31E24).copy(alpha = 0.05f))
                    .border(1.dp, Color(0xFFE31E24).copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Rounded.Person, 
                    contentDescription = null, 
                    tint = Color(0xFFE31E24), 
                    modifier = Modifier.size(40.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(20.dp))
            
            Column {
                Text(
                    "Yusuf Vural", 
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black)
                )
                Text(
                    "yusufvural@example.com", 
                    style = MaterialTheme.typography.bodyMedium, 
                    color = Color.Gray
                )
                
                Spacer(modifier = Modifier.height(10.dp))
                
                Surface(
                    color = Color(0xFFE31E24),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        "PREMIUM", 
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black
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
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFF0F0F0))
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFFFBFBFD)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = Color(0xFFE31E24), modifier = Modifier.size(24.dp))
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Text(
                title, 
                modifier = Modifier.weight(1f), 
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
            )
            
            if (value.isNotEmpty()) {
                Text(
                    value, 
                    color = Color.Gray, 
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            
            Icon(Icons.Rounded.ChevronRight, contentDescription = null, tint = Color.LightGray)
        }
    }
}

@Composable
fun SectionTitle(title: String, actionText: String = "Detaylar", onActionClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        Text(
            title,
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Black,
                fontSize = 22.sp,
                letterSpacing = (-0.5).sp
            )
        )
        Text(
            actionText,
            color = Color(0xFFE31E24),
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            modifier = Modifier.padding(bottom = 2.dp).clickable { onActionClick() }
        )
    }
}

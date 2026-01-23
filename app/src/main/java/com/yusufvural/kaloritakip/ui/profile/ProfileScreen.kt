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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
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
    val viewModel: androidx.lifecycle.ViewModel = androidx.hilt.navigation.compose.hiltViewModel<ProfileViewModel>()
    // Cast to ProfileViewModel since hiltViewModel returns ViewModel type in some versions setup
    val profileViewModel = viewModel as ProfileViewModel
    val currentUser by profileViewModel.currentUser.collectAsState()
    
    // Use user data or default
    val user = currentUser ?: com.yusufvural.kaloritakip.model.UserEntity()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFBFBFD)),
        contentPadding = PaddingValues(start = 24.dp, end = 24.dp, top = 24.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // --- USER HEADER ---
        item {
            UserProfileHeader()
        }
        
        // --- BASIC INFO SECTION ---
        item {
            ProfileSection(title = "Temel Bilgiler") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    InfoCard(label = "Yaş", value = "${user.age}", unit = "Yıl", modifier = Modifier.weight(1f))
                    Spacer(modifier = Modifier.width(12.dp))
                    InfoCard(label = "Kilo", value = "${user.currentWeight}", unit = "kg", modifier = Modifier.weight(1f))
                    Spacer(modifier = Modifier.width(12.dp))
                    InfoCard(label = "Boy", value = "${user.height}", unit = "cm", modifier = Modifier.weight(1f))
                }
            }
        }

        // --- GOALS SECTION ---
        item {
            ProfileSection(title = "Hedeflerim") {
                ProfileOptionRow(
                    title = "Günlük Kalori Hedefi", 
                    value = "${user.dailyCalories} kcal", 
                    icon = Icons.Rounded.TrackChanges,
                    showDivider = true,
                    onClick = { onNavigate("target_calculator") }
                )
                ProfileOptionRow(
                    title = "Hedef Kilo", 
                    value = "${user.goalWeight} kg", 
                    icon = Icons.Rounded.MonitorWeight,
                    showDivider = true
                )
                 ProfileOptionRow(
                    title = "Su Hedefi", 
                    value = "${user.waterGoal} ml", 
                    icon = Icons.Rounded.WaterDrop,
                    showDivider = false
                )
            }
        }
        
        // --- PREFERENCES SECTION ---
        item {
             ProfileSection(title = "Tercihler") {
                ProfileOptionRow(
                    title = "Beslenme Tipi", 
                    value = user.dietType, 
                    icon = Icons.Rounded.RestaurantMenu,
                    showDivider = true
                )
                ProfileOptionRow(
                    title = "Birim Sistemi", 
                    value = user.unitSystem, 
                    icon = Icons.Rounded.Straighten,
                    showDivider = false
                )
             }
        }

        // --- SETTINGS SECTION ---
        item {
            ProfileSection(title = "Uygulama Ayarları", actionText = "Yardım", onActionClick = { /* More */ }) {
                ProfileOptionRow(
                    title = "Hatırlatıcılar", 
                    value = "Açık", 
                    icon = Icons.Rounded.NotificationsActive,
                    showDivider = true
                )
                ProfileOptionRow(
                    title = "Özel Besinlerim", 
                    value = "", 
                    icon = Icons.Rounded.EditNote,
                    showDivider = true
                )
                ProfileOptionRow(
                    title = "Bize Ulaşın", 
                    value = "", 
                    icon = Icons.Rounded.SupportAgent,
                    showDivider = false
                )
            }
        }

        // --- LOGOUT BUTTON ---
        item {
            Button(
                onClick = { /* Logout */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, Color(0xFFE31E24).copy(alpha = 0.1f)),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
            ) {
                Icon(Icons.Rounded.Logout, contentDescription = null, tint = Color(0xFFE31E24))
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    "Çıkış Yap", 
                    color = Color(0xFFE31E24), 
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}

@Composable
fun InfoCard(label: String, value: String, unit: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .background(Color(0xFFFBFBFD), RoundedCornerShape(16.dp))
            .border(1.dp, Color(0xFFF0F0F0), RoundedCornerShape(16.dp))
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
            color = Color.Gray,
            fontSize = 16.sp
        )
        Text(
            text = value,
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Black),
            color = Color(0xFFE31E24),
            fontSize = 30.sp
        )
        Text(
            text = unit,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
            color = Color.Gray
        )
    }
}

@Composable
fun UserProfileHeader() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, Color(0xFFF0F0F0))
    ) {
        Row(
            modifier = Modifier.padding(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE31E24).copy(alpha = 0.05f))
                    .border(1.dp, Color(0xFFE31E24).copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Rounded.Person, 
                    contentDescription = null, 
                    tint = Color(0xFFE31E24), 
                    modifier = Modifier.size(36.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(20.dp))
            
            Column {
                Text(
                    "Yusuf Vural", 
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Black,
                        fontSize = 24.sp
                    )
                )
                Text(
                    "yusufvural@example.com", 
                    style = MaterialTheme.typography.bodyLarge, 
                    color = Color.Gray
                )
                
                Spacer(modifier = Modifier.height(10.dp))
                
                Surface(
                    color = Color(0xFFE31E24),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        "PREMIUM", 
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }
            }
        }
    }
}

@Composable
fun ProfileSection(
    title: String, 
    actionText: String? = null, 
    onActionClick: () -> Unit = {},
    content: @Composable ColumnScope.() -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp, start = 4.dp, end = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                title,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.Black.copy(alpha = 0.8f)
                )
            )
            if (actionText != null) {
                Text(
                    actionText,
                    color = Color(0xFFE31E24),
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    modifier = Modifier.clickable { onActionClick() }
                )
            }
        }
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            border = BorderStroke(1.dp, Color(0xFFF0F0F0))
        ) {
            Column(
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                content()
            }
        }
    }
}

@Composable
fun ProfileOptionRow(
    title: String, 
    value: String, 
    icon: ImageVector, 
    showDivider: Boolean = true,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFFBFBFD))
                    .border(1.dp, Color(0xFFF0F0F0), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon, 
                    contentDescription = null, 
                    tint = Color(0xFFE31E24), 
                    modifier = Modifier.size(24.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Text(
                title, 
                modifier = Modifier.weight(1f), 
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 19.sp
                )
            )
            
            if (value.isNotEmpty()) {
                Text(
                    value, 
                    color = Color.Gray, 
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 17.sp
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            
            Icon(
                Icons.Rounded.ChevronRight, 
                contentDescription = null, 
                tint = Color.LightGray.copy(alpha = 0.8f),
                modifier = Modifier.size(22.dp)
            )
        }
        
        if (showDivider) {
            HorizontalDivider(
                modifier = Modifier.padding(start = 80.dp, end = 20.dp),
                color = Color(0xFFF0F0F0)
            )
        }
    }
}

package com.yusufvural.kaloritakip.ui.profile

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.yusufvural.kaloritakip.model.UserEntity
import com.yusufvural.kaloritakip.ui.theme.*

@Composable
fun ProfileScreen(
    onNavigate: (String) -> Unit = {}
) {
    val viewModel: androidx.lifecycle.ViewModel = androidx.hilt.navigation.compose.hiltViewModel<ProfileViewModel>()
    val profileViewModel = viewModel as ProfileViewModel
    val currentUser by profileViewModel.currentUser.collectAsState()
    val user = currentUser ?: UserEntity()

    // Dialog States
    var showCalorieDialog by remember { mutableStateOf(false) }
    var showAgeDialog by remember { mutableStateOf(false) }
    var showWeightDialog by remember { mutableStateOf(false) }
    var showHeightDialog by remember { mutableStateOf(false) }
    var showTargetWeightDialog by remember { mutableStateOf(false) }
    var showGenderDialog by remember { mutableStateOf(false) }

    // Minimalist Color Palette
    val backgroundColor = if (isSystemInDarkTheme()) BackgroundDark else BackgroundLight // Gray 50 or Gray 900
    val contentColor = if (isSystemInDarkTheme()) Color.White else Color(0xFF1F2937) // Gray 800
    val subTextColor = if (isSystemInDarkTheme()) TextSecondaryDark else Color(0xFF6B7280) // Gray 500
    val cardColor = if (isSystemInDarkTheme()) SurfaceDark else Color.White

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // --- MINIMALIST HEADER ---
            // Removed Gradient/Large Blocks. Using clean whitespace.
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
               Column {
                   Text(
                       text = "Profilim",
                       style = MaterialTheme.typography.headlineMedium.copy(
                           fontWeight = FontWeight.Bold,
                           color = contentColor,
                           fontSize = 32.sp // Increased +4sp
                       )
                   )
                   Text(
                       text = "Hoş geldin!",
                       style = MaterialTheme.typography.bodyMedium.copy(
                           color = subTextColor,
                           fontWeight = FontWeight.Medium,
                           fontSize = 18.sp // Increased +4sp
                       )
                   )
               }

                // Simplified Avatar
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(PrimaryContainer), // Light red bg
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Person,
                        contentDescription = null,
                        tint = PrimaryRed,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            // --- SCROLLABLE CONTENT ---
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                contentPadding = PaddingValues(bottom = 32.dp), // Reduced from 100.dp to 32.dp based on user feedback
                verticalArrangement = Arrangement.spacedBy(32.dp) // Generous spacing
            ) {
                // PERSONAL INFO ROW
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        SectionTitleMinimal("Vücut Analizi", contentColor)
                        
                        // Using a single row for key metrics to save space and look cleaner
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Helper for quick info items
                            val infoModifier = Modifier.weight(1f)
                            
                            InfoItemMinimal(
                                title = "YAŞ", value = "${user.age}", unit = "", 
                                onClick = { showAgeDialog = true }, modifier = infoModifier,
                                cardColor = cardColor, textColor = contentColor, subTextColor = subTextColor
                            )
                            InfoItemMinimal(
                                title = "KİLO", value = "${user.currentWeight}", unit = "kg", 
                                onClick = { showWeightDialog = true }, modifier = infoModifier,
                                cardColor = cardColor, textColor = contentColor, subTextColor = subTextColor
                            )
                             InfoItemMinimal(
                                title = "BOY", value = "${user.height}", unit = "cm", 
                                onClick = { showHeightDialog = true }, modifier = infoModifier,
                                cardColor = cardColor, textColor = contentColor, subTextColor = subTextColor
                            )
                        }
                        
                        // Gender as a separate, wider strip or integrated? Let's keep it separate for now or add to edit later.
                        // For minimalism, maybe we don't need to show 'Gender' prominently if it's not changing often.
                        // Let's add it as a secondary detail row.
                        
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(cardColor)
                                .clickable { showGenderDialog = true }
                                .padding(horizontal = 20.dp, vertical = 16.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Rounded.Wc, null, tint = subTextColor, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(12.dp))
                                Text("Cinsiyet", style = MaterialTheme.typography.bodyMedium.copy(
                                    color = subTextColor, 
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 18.sp // Increased +4sp
                                ))
                                Spacer(modifier = Modifier.weight(1f))
                                Text(user.gender, style = MaterialTheme.typography.bodyLarge.copy(
                                    color = contentColor, 
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 20.sp // Increased +4sp
                                ))
                                Icon(Icons.Rounded.ChevronRight, null, tint = subTextColor.copy(alpha = 0.5f))
                            }
                        }
                    }
                }

                // GOALS SECTION
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        SectionTitleMinimal("Hedefler", contentColor)
                        
                        GoalCardMinimal(
                            title = "Günlük Kalori",
                            value = "${user.dailyCalories}",
                            unit = "kcal",
                            icon = Icons.Rounded.LocalFireDepartment,
                            cardColor = cardColor,
                            textColor = contentColor,
                            subTextColor = subTextColor,
                            onClick = { showCalorieDialog = true }
                        )

                        GoalCardMinimal(
                            title = "Hedef Kilo",
                            value = "${user.goalWeight}",
                            unit = "kg",
                            icon = Icons.Rounded.TrackChanges,
                            cardColor = cardColor,
                            textColor = contentColor,
                            subTextColor = subTextColor,
                            onClick = { showTargetWeightDialog = true }
                        )
                    }
                }

                // SETTINGS & ACTIONS
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        SectionTitleMinimal("Ayarlar", contentColor)
                        
                         Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(20.dp))
                                .background(cardColor)
                        ) {
                            ActionItemMinimal(
                                title = "Aktivite Geçmişi",
                                icon = Icons.Rounded.History,
                                textColor = contentColor,
                                onClick = { /* Navigate */ },
                                showDivider = true
                            )
                            ActionItemMinimal(
                                title = "Uygulama Ayarları",
                                icon = Icons.Rounded.Settings,
                                textColor = contentColor,
                                onClick = { /* Navigate */ },
                                showDivider = false
                            )
                        }
                    }
                }
                
                item {
                    TextButton(
                        onClick = { 
                            profileViewModel.logout()
                            onNavigate("auth")
                        },
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFFEF4444)) // Red 500
                    ) {
                        Text(
                            text = "Çıkış Yap",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 20.sp // Increased +4sp from bodyLarge (~16)
                            )
                        )
                    }
                }
            }
        }
    }

    // --- DIALOGS (Kept same logic, simplified styles inside if needed) ---
    if (showCalorieDialog) {
         SingleInputIntDialog(title = "Günlük Kalori Hedefi", label = "Kalori (kcal)", current = user.dailyCalories) {
             profileViewModel.saveUser(user.copy(dailyCalories = it))
             showCalorieDialog = false
         }
    }
    if (showAgeDialog) {
        SingleInputIntDialog(title = "Yaş Seçiniz", label = "Yaş", current = user.age) {
            profileViewModel.saveUser(user.copy(age = it))
            showAgeDialog = false
        }
    }
    if (showWeightDialog) {
        SingleInputDoubleDialog(title = "Kilo Seçiniz", label = "Kilo (kg)", current = user.currentWeight) {
            profileViewModel.saveUser(user.copy(currentWeight = it))
            showWeightDialog = false
        }
    }
    if (showHeightDialog) {
        SingleInputIntDialog(title = "Boy Seçiniz", label = "Boy (cm)", current = user.height) {
            profileViewModel.saveUser(user.copy(height = it))
            showHeightDialog = false
        }
    }
    if(showGenderDialog) {
         SelectionDialog(title = "Cinsiyet Seçiniz", options = listOf("Erkek", "Kadın"), current = user.gender) {
             profileViewModel.saveUser(user.copy(gender = it))
             showGenderDialog = false
         }
    }
    if (showTargetWeightDialog) {
        SingleInputDoubleDialog(title = "Hedef Kilo Seçiniz", label ="Kilo (kg)", current = user.goalWeight) {
             profileViewModel.saveUser(user.copy(goalWeight = it))
             showTargetWeightDialog = false
        }
    }
}

// --- MINIMALIST COMPONENTS ---

@Composable
fun SectionTitleMinimal(text: String, color: Color) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            color = color,
            letterSpacing = (-0.5).sp, // Tighter tracking for modern look
            fontSize = 20.sp // Increased +4sp (TitleMedium is usually 16sp)
        ),
        modifier = Modifier.padding(start = 4.dp)
    )
}

@Composable
fun InfoItemMinimal(
    title: String,
    value: String,
    unit: String,
    modifier: Modifier = Modifier,
    cardColor: Color,
    textColor: Color,
    subTextColor: Color,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(cardColor)
            .clickable { onClick() }
            .padding(vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.SemiBold, // Not bold, just semi
                color = textColor,
                letterSpacing = (-1).sp,
                fontSize = 28.sp // Increased +4sp (HeadlineSmall is ~24sp)
            )
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = if (unit.isNotEmpty()) "$title ($unit)" else title,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Medium,
                color = subTextColor,
                letterSpacing = 0.5.sp,
                fontSize = 15.sp // Increased +4sp (LabelSmall is ~11sp)
            )
        )
    }
}

@Composable
fun GoalCardMinimal(
    title: String,
    value: String,
    unit: String,
    icon: ImageVector,
    cardColor: Color,
    textColor: Color,
    subTextColor: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(cardColor)
            .clickable { onClick() }
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(PrimaryContainer, CircleShape), // Soft Red background
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = PrimaryRed,
                modifier = Modifier.size(24.dp)
            )
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = subTextColor,
                    fontWeight = FontWeight.Medium,
                    fontSize = 18.sp // Increased +4sp (BodyMedium is ~14sp)
                )
            )
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = textColor,
                        fontSize = 26.sp // Increased +4sp (TitleLarge is ~22sp)
                    )
                )
                Text(
                    text = " $unit",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = subTextColor,
                        fontSize = 18.sp // Increased +4sp
                    ),
                    modifier = Modifier.padding(bottom = 2.dp)
                )
            }
        }
        
        Icon(
            imageVector = Icons.Rounded.Edit,
            contentDescription = null,
            tint = subTextColor.copy(alpha = 0.5f),
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
fun ActionItemMinimal(
    title: String,
    icon: ImageVector,
    textColor: Color,
    showDivider: Boolean,
    onClick: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(horizontal = 20.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = textColor.copy(alpha = 0.7f),
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Medium,
                    color = textColor,
                    fontSize = 20.sp // Increased +4sp (BodyLarge is ~16sp)
                ),
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Rounded.ChevronRight,
                contentDescription = null,
                tint = textColor.copy(alpha = 0.3f),
                modifier = Modifier.size(20.dp)
            )
        }
        if (showDivider) {
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 20.dp),
                thickness = 0.5.dp,
                color = textColor.copy(alpha = 0.1f)
            )
        }
    }
}


// --- DIALOGS REUSABLE ---

@Composable
fun SingleInputIntDialog(title: String, label: String, current: Int, onConfirm: (Int) -> Unit) {
    var text by remember { mutableStateOf(current.toString()) }
    AlertDialog(
        onDismissRequest = { onConfirm(current) },
        title = {
            Text(title, style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
        },
        text = {
            OutlinedTextField(
                value = text, 
                onValueChange = { if(it.all { c-> c.isDigit() }) text = it },
                label = { Text(label) },
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryRed,
                    focusedLabelColor = PrimaryRed,
                    cursorColor = PrimaryRed
                )
            ) 
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(text.toIntOrNull() ?: current) }, 
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryRed),
                shape = RoundedCornerShape(12.dp)
            ) { Text("Kaydet") }
        },
        containerColor = if(isSystemInDarkTheme()) Color(0xFF1F2937) else Color.White,
        shape = RoundedCornerShape(24.dp)
    )
}

@Composable
fun SingleInputDoubleDialog(title: String, label: String, current: Double, onConfirm: (Double) -> Unit) {
    var text by remember { mutableStateOf(current.toString()) }
     AlertDialog(
        onDismissRequest = { onConfirm(current) },
        title = { Text(title, style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)) },
        text = {
            OutlinedTextField(
                value = text, 
                onValueChange = { text = it },
                label = { Text(label) },
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                 colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryRed,
                    focusedLabelColor = PrimaryRed,
                    cursorColor = PrimaryRed
                )
            ) 
        },
        confirmButton = {
            Button(onClick = { onConfirm(text.toDoubleOrNull() ?: current) }, colors = ButtonDefaults.buttonColors(containerColor = PrimaryRed), shape = RoundedCornerShape(12.dp)) { Text("Kaydet") }
        },
        containerColor = if(isSystemInDarkTheme()) Color(0xFF1F2937) else Color.White,
        shape = RoundedCornerShape(24.dp)
    )
}

@Composable
fun SelectionDialog(title: String, options: List<String>, current: String, onSelect: (String) -> Unit) {
     AlertDialog(
        onDismissRequest = { /* Dismiss */ },
        title = { Text(title, style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)) },
        text = {
            Column {
                options.forEach { opt ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelect(opt) }
                            .padding(16.dp), // More padding in dialog items
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(selected = current == opt, onClick = { onSelect(opt) }, colors = RadioButtonDefaults.colors(selectedColor = PrimaryRed))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(opt, style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = { TextButton(onClick = { onSelect(current) }, colors = ButtonDefaults.textButtonColors(contentColor = PrimaryRed)) { Text("İptal") } },
        containerColor = if(isSystemInDarkTheme()) Color(0xFF1F2937) else Color.White,
        shape = RoundedCornerShape(24.dp)
    )
}

package com.yusufvural.kaloritakip.ui.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.RoundedCornerShape
import com.yusufvural.kaloritakip.R
import com.yusufvural.kaloritakip.ui.theme.PrimaryGradientStart
import com.yusufvural.kaloritakip.ui.theme.PrimaryGradientEnd
import com.yusufvural.kaloritakip.ui.theme.TextPrimary
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onNavigateToDashboard: () -> Unit
) {
    var startAnimation by remember { mutableStateOf(false) }
    
    val alphaAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 1000)
    )

    val scaleAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.5f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
    )

    LaunchedEffect(key1 = true) {
        startAnimation = true
        delay(2000)
        onNavigateToDashboard()
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .graphicsLayer {
                    scaleX = scaleAnim.value
                    scaleY = scaleAnim.value
                    alpha = alphaAnim.value
                }
        ) {
            // Custom Logo
            Image(
                painter = painterResource(id = R.drawable.ic_splash_logo),
                contentDescription = null,
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(32.dp))
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "KaloriTakip",
                style = MaterialTheme.typography.displayMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp,
                    letterSpacing = (-1).sp,
                    color = TextPrimary
                )
            )
        }
        
        // Bottom copyright or version info (optional)
        Text(
            text = "v1.0.0",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
                .graphicsLayer { alpha = alphaAnim.value },
            color = Color.Gray,
            fontSize = 12.sp
        )
    }
}

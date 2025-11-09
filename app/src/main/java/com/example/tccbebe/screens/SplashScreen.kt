package com.example.tccbebe.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.tccbebe.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navegacao: NavHostController?) {
    val scale = remember { Animatable(0f) }
    val alpha = remember { Animatable(0f) }
    
    // Animações
    LaunchedEffect(key1 = true) {
        // Animação da logo (escala)
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 800)
        )
        
        // Animação do texto (fade in)
        alpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 600)
        )
        
        // Aguardar 2 segundos e navegar para login
        delay(2000)
        navegacao?.navigate("login") {
            popUpTo("splash") { inclusive = true }
        }
    }
    
    // Background com gradiente
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF708EF1), // Azul principal
                        Color(0xFF9BB5FF), // Azul mais claro
                        Color.White
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo com animação de escala
            Image(
                painter = painterResource(id = R.drawable.logocerta),
                contentDescription = "Logo do App",
                modifier = Modifier
                    .size(180.dp)
                    .scale(scale.value)
            )
            
            Spacer(modifier = Modifier.height(40.dp))
            
            // Texto do app com animação de fade
            Text(
                text = "TCC Bebê",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.alpha(alpha.value)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Cuidando do seu bebê",
                fontSize = 16.sp,
                color = Color.White.copy(alpha = 0.8f),
                modifier = Modifier.alpha(alpha.value)
            )
            
            Spacer(modifier = Modifier.height(60.dp))
            
            // Indicador de carregamento
            CircularProgressIndicator(
                color = Color.White,
                strokeWidth = 3.dp,
                modifier = Modifier
                    .size(40.dp)
                    .alpha(alpha.value)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SplashScreenPreview() {
    SplashScreen(navegacao = null)
}

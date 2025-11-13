package br.senai.sp.jandira.telarotina.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tccbebe.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BemVindoInicialScreen() {
    var showDialog by remember { mutableStateOf(false) }
    
    // Define font family - using system default for now
    val kronaOneFont = FontFamily.Default
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // Header
        TopAppBar(
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo_baby),
                        contentDescription = "S♥S Baby Logo",
                        modifier = Modifier
                            .size(80.dp)
                    )
                }
            },
            navigationIcon = {
                IconButton(onClick = { }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Voltar",
                        tint = Color.Gray
                    )
                }
            },
            actions = {
                IconButton(onClick = { }) {
                    Image(
                        painter = painterResource(id = R.drawable.notificacoes),
                        contentDescription = "S♥S Baby Logo",
                        modifier = Modifier
                            .size(50.dp)
                    )
                }
                IconButton(onClick = { }) {
                    Image(
                        painter = painterResource(id = R.drawable.perfil),
                        contentDescription = "S♥S Baby Logo",
                        modifier = Modifier
                            .size(50.dp)

                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White
            )
        )
        
        // Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            
            // Title with Arrow
            Row(
                verticalAlignment = Alignment.Top
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Seta",
                    tint = Color.Black,
                    modifier = Modifier
                        .size(24.dp)
                        .padding(top = 4.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Bem vindo\na Rotina !!",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = kronaOneFont,
                    color = Color.Black,
                    lineHeight = 28.sp
                )
            }
            
            Spacer(modifier = Modifier.height(60.dp))
            
            // Dialog Card with buttons inside
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Card(
                    modifier = Modifier
                        .width(280.dp)
                        .height(120.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF6C7CE7)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Deseja utilizar\numa rotina\npronta?",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            fontFamily = kronaOneFont,
                            color = Color.White,
                            textAlign = TextAlign.Start,
                            lineHeight = 18.sp
                        )
                        
                        // Buttons Row at the bottom
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End)
                        ) {
                            // Sim Button with black border
                            OutlinedButton(
                                onClick = { showDialog = true },
                                modifier = Modifier
                                    .width(60.dp)
                                    .height(32.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = Color.White,
                                    contentColor = Color.Black
                                ),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color.Black),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Text(
                                    text = "Sim",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Normal,
                                    fontFamily = kronaOneFont
                                )
                            }
                            
                            // Não Button without border
                            Button(
                                onClick = { },
                                modifier = Modifier
                                    .width(60.dp)
                                    .height(32.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(color = 0xFF6C7CE7),
                                    contentColor = Color.Black
                                ),
                                shape = RoundedCornerShape(16.dp),
                                elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                            ) {
                                Text(
                                    text = "Não",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Normal,
                                    fontFamily = kronaOneFont
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BemVindoInicialScreenPreview() {
    BemVindoInicialScreen()
}

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tccbebe.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RotinaDetalhesScreen() {
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
                Image(
                    painter = painterResource(id = R.drawable.logo_baby),
                    contentDescription = "S♥S Baby Logo",
                    modifier = Modifier
                        .height(56.dp)
                        .width(100.dp)
                        .padding(start = 8.dp)
                )
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
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notificações",
                        tint = Color.Blue
                    )
                }
                IconButton(onClick = { }) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Perfil",
                        tint = Color.Blue
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White
            )
        )
        
        // Logo SOS BABY
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Voltar",
                tint = Color.Gray
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = "SOS",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = kronaOneFont,
                    color = Color.Gray
                )
                Text(
                    text = "BABY",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = kronaOneFont,
                    color = Color.Gray
                )
            }
        }
        
        // Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            
            // Title
            Text(
                text = "Bem vindo\na Rotina !!",
                fontSize = 24.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = kronaOneFont,
                color = Color.Black,
                lineHeight = 28.sp
            )
            
            Spacer(modifier = Modifier.height(40.dp))
            
            // Create Button
            Button(
                onClick = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6C7CE7)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Criar",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = kronaOneFont,
                    color = Color.White
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Description Text
            Text(
                text = "Crie uma nova\nrotina ao seu Bebê",
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = kronaOneFont,
                color = Color.Black,
                lineHeight = 20.sp
            )
            
            Spacer(modifier = Modifier.height(40.dp))
            
            // Routine Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF6C7CE7)),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp)
                ) {
                    // Date
                    Text(
                        text = "11/09",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = kronaOneFont,
                        color = Color.White
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // ACORDAR Button
                    Button(
                        onClick = { },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF8A94F0)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "ACORDAR",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            fontFamily = kronaOneFont,
                            color = Color.White
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    // Tasks List
                    Column(
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "• Acordar Bebê",
                            fontSize = 12.sp,
                            fontFamily = kronaOneFont,
                            color = Color.White
                        )
                        Text(
                            text = "• Amamentar",
                            fontSize = 12.sp,
                            fontFamily = kronaOneFont,
                            color = Color.White
                        )
                        Text(
                            text = "• Café da",
                            fontSize = 12.sp,
                            fontFamily = kronaOneFont,
                            color = Color.White
                        )
                        Text(
                            text = "  manhã",
                            fontSize = 12.sp,
                            fontFamily = kronaOneFont,
                            color = Color.White
                        )
                    }
                    
                    Spacer(modifier = Modifier.weight(1f))
                    
                    // Editar Button
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            onClick = { },
                            modifier = Modifier
                                .width(80.dp)
                                .height(32.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White,
                                contentColor = Color.Black
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text(
                                text = "Editar",
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

@Preview(showBackground = true)
@Composable
fun RotinaDetalhesScreenPreview() {
    RotinaDetalhesScreen()
}

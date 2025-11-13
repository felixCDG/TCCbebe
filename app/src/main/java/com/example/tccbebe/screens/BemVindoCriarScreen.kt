package br.senai.sp.jandira.telarotina.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
fun BemVindoCriarScreen() {
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
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Black line under title
            androidx.compose.material3.HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 2.dp,
                color = Color.Black
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Create Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                OutlinedButton(
                onClick = { },
                modifier = Modifier
                    .width(140.dp)
                    .height(40.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color(0xFF6C7CE7),
                    contentColor = Color.White
                ),
                border = androidx.compose.foundation.BorderStroke(2.dp, Color.White),
                shape = RoundedCornerShape(12.dp),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 6.dp,
                    pressedElevation = 8.dp,
                    disabledElevation = 0.dp
                )
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_add_bold),
                        contentDescription = "Adicionar",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Criar",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = kronaOneFont,
                        color = Color.White
                    )
                }
            }
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
            
            Spacer(modifier = Modifier.height(100.dp))
            
            // Info Text
            Text(
                text = "Você não\npossui\nnenhuma\nrotina.",
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = kronaOneFont,
                color = Color.Black,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BemVindoCriarScreenPreview() {
    BemVindoCriarScreen()
}

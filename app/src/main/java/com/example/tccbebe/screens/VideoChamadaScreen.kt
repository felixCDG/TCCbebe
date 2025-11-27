package com.example.tccbebe.screens

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview as CameraPreview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import java.util.UUID

@Composable
fun VideoChamadaScreen(navegacao: NavHostController?, roomName: String? = null) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var isMicMuted by remember { mutableStateOf(false) }
    var isCameraOff by remember { mutableStateOf(false) }
    var isConnecting by remember { mutableStateOf(false) }
    var isConnected by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var hasCameraPermission by remember { mutableStateOf(false) }

    // Preview da câmera
    val previewView = remember { PreviewView(context) }
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

    // Launcher para solicitar permissão da câmera
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasCameraPermission = isGranted
        if (isGranted) {
            Log.i("VIDEOCHAMADA", "Permissão da câmera concedida")
        } else {
            Log.e("VIDEOCHAMADA", "Permissão da câmera negada")
        }
    }

    // Verificar permissão da câmera
    LaunchedEffect(Unit) {
        hasCameraPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

        if (!hasCameraPermission) {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    // Configurar câmera quando permissão for concedida
    LaunchedEffect(hasCameraPermission, isCameraOff) {
        if (hasCameraPermission && !isCameraOff) {
            val cameraProvider = cameraProviderFuture.get()
            val preview = CameraPreview.Builder().build()
            val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

            try {
                cameraProvider.unbindAll()
                preview.setSurfaceProvider(previewView.surfaceProvider)
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview
                )
                Log.i("VIDEOCHAMADA", "Câmera configurada com sucesso")
            } catch (e: Exception) {
                Log.e("VIDEOCHAMADA", "Erro ao configurar câmera: ${e.message}")
            }
        } else if (isCameraOff) {
            val cameraProvider = cameraProviderFuture.get()
            cameraProvider.unbindAll()
        }
    }

    // Gerar nome da sala se não fornecido
    val currentRoomName = roomName ?: "sala-${UUID.randomUUID().toString().take(8)}"

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF7986CB))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            // Dois cards empilhados com tamanho igual usando weight
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                // Conteúdo do card do médico
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(200.dp)
                            .background(Color.Black, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = "Dr. Souza",
                            tint = Color.White,
                            modifier = Modifier.size(120.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Dr. Souza",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = when {
                            isConnecting -> "Conectando..."
                            isConnected -> "Conectado - Sala: $currentRoomName"
                            errorMessage != null -> "Erro na conexão"
                            else -> "Aguardando conexão"
                        },
                        fontSize = 14.sp,
                        color = when {
                            isConnecting -> Color.Yellow
                            isConnected -> Color.Green
                            errorMessage != null -> Color.Red
                            else -> Color.Gray
                        }
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        IconButton(onClick = { /* Opções */ }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "Mais opções", tint = Color.Gray)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    when {
                        !hasCameraPermission -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color.Gray, RoundedCornerShape(16.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(
                                        Icons.Default.Warning,
                                        contentDescription = "Sem permissão",
                                        tint = Color.White,
                                        modifier = Modifier.size(36.dp)
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(text = "Sem permissão de câmera", color = Color.White)
                                }
                            }
                        }
                        isCameraOff -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color.Black.copy(alpha = 0.3f), RoundedCornerShape(16.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(
                                        Icons.Default.VideocamOff,
                                        contentDescription = "Câmera desligada",
                                        tint = Color.White,
                                        modifier = Modifier.size(48.dp)
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(text = "Câmera desligada", color = Color.White)
                                }
                            }
                        }
                        else -> {
                            AndroidView(
                                factory = { previewView },
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(16.dp))
                            )
                        }
                    }

                    // Nome e status no canto superior esquerdo do card
                    Column(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(12.dp)
                    ) {
                        Text(
                            text = "Você",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White
                        )
                        Text(
                            text = when {
                                !hasCameraPermission -> "Sem permissão"
                                isCameraOff -> "Câmera desligada"
                                else -> "Câmera ligada"
                            },
                            fontSize = 12.sp,
                            color = when {
                                !hasCameraPermission -> Color.Yellow
                                isCameraOff -> Color.Red
                                else -> Color.Green
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Barra de controles fora dos cards
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        if (hasCameraPermission) {
                            isCameraOff = !isCameraOff
                            Log.i("VIDEOCHAMADA", "Câmera ${if (isCameraOff) "desligada" else "ligada"}")
                        } else {
                            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                        }
                    },
                    modifier = Modifier
                        .size(56.dp)
                        .background(
                            if (isCameraOff) Color.Red.copy(alpha = 0.85f) else Color.Green.copy(alpha = 0.85f),
                            CircleShape
                        )
                ) {
                    Icon(
                        if (isCameraOff) Icons.Default.VideocamOff else Icons.Default.Videocam,
                        contentDescription = if (isCameraOff) "Ligar câmera" else "Desligar câmera",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(modifier = Modifier.width(24.dp))

                IconButton(
                    onClick = { isMicMuted = !isMicMuted },
                    modifier = Modifier
                        .size(56.dp)
                        .background(
                            if (isMicMuted) Color.Red.copy(alpha = 0.85f) else Color.Green.copy(alpha = 0.85f),
                            CircleShape
                        )
                ) {
                    Icon(
                        if (isMicMuted) Icons.Default.MicOff else Icons.Default.Mic,
                        contentDescription = if (isMicMuted) "Microfone mutado" else "Microfone ligado",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun VideoChamadaPreview() {
    VideoChamadaScreen(navegacao = null)
}

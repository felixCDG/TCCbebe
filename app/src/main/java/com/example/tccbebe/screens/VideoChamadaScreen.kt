package com.example.tccbebe.screens

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
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
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavHostController
import com.example.tccbebe.repository.VideoChamadaRepository
import kotlinx.coroutines.launch
import java.util.UUID
import java.util.concurrent.Executor
import java.util.concurrent.Executors

@Composable
fun VideoChamadaScreen(navegacao: NavHostController?, roomName: String? = null) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val videoChamadaRepository = remember { VideoChamadaRepository(context) }
    val coroutineScope = rememberCoroutineScope()
    
    var isMicMuted by remember { mutableStateOf(false) }
    var isCameraOff by remember { mutableStateOf(false) }
    var isConnecting by remember { mutableStateOf(false) }
    var isConnected by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var tokenData by remember { mutableStateOf<String?>(null) }
    var hasCameraPermission by remember { mutableStateOf(false) }
    
    // Preview da câmera
    val previewView = remember { PreviewView(context) }
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val executor = remember { Executors.newSingleThreadExecutor() }
    
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
            val preview = Preview.Builder().build()
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
    
    // Conectar à videochamada quando a tela carregar
    LaunchedEffect(Unit) {
        isConnecting = true
        coroutineScope.launch {
            videoChamadaRepository.gerarTokenVideoChamada(currentRoomName)
                .onSuccess { response ->
                    tokenData = response.token.token
                    isConnected = true
                    isConnecting = false
                    Log.i("VIDEOCHAMADA", "Token gerado: ${response.token.token}")
                    Log.i("VIDEOCHAMADA", "Sala: ${response.token.Room}")
                    Log.i("VIDEOCHAMADA", "Identidade: ${response.token.indentity}")
                    // Aqui você integraria com o SDK do Twilio Video
                }
                .onFailure { error ->
                    errorMessage = error.message
                    isConnecting = false
                    Log.e("VIDEOCHAMADA", "Erro ao gerar token: ${error.message}")
                }
        }
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF7986CB))
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Área superior - Ícone de microfone
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(
                    onClick = { /* Ação do microfone */ },
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color.White.copy(alpha = 0.2f), CircleShape)
                ) {
                    Icon(
                        Icons.Default.Mic,
                        contentDescription = "Microfone",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            
            // Área central - Card do Dr. Souza
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Card principal do médico
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        // Avatar do médico
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
                        
                        // Nome do médico e status da conexão
                        Text(
                            text = "Dr. Souza",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        // Status da conexão
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
                        
                        // Botão de opções (três pontos)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            IconButton(
                                onClick = { /* Opções */ }
                            ) {
                                Icon(
                                    Icons.Default.MoreVert,
                                    contentDescription = "Mais opções",
                                    tint = Color.Gray
                                )
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Card menor - "Você" com controles de câmera
                Card(
                    modifier = Modifier
                        .width(200.dp)
                        .height(120.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        // Conteúdo principal
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Preview da câmera ou indicador de câmera desligada
                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (isCameraOff || !hasCameraPermission) Color.Gray else Color.Black
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                when {
                                    !hasCameraPermission -> {
                                        Icon(
                                            Icons.Default.Warning,
                                            contentDescription = "Sem permissão",
                                            tint = Color.White,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                    isCameraOff -> {
                                        Icon(
                                            Icons.Default.VideocamOff,
                                            contentDescription = "Câmera desligada",
                                            tint = Color.White,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                    else -> {
                                        // Preview da câmera em miniatura
                                        AndroidView(
                                            factory = { previewView },
                                            modifier = Modifier
                                                .size(60.dp)
                                                .clip(CircleShape)
                                        )
                                    }
                                }
                            }
                            
                            Spacer(modifier = Modifier.width(12.dp))
                            
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = "Você",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.Black
                                )
                                Text(
                                    text = when {
                                        !hasCameraPermission -> "Sem permissão"
                                        isCameraOff -> "Câmera desligada"
                                        else -> "Câmera ligada"
                                    },
                                    fontSize = 12.sp,
                                    color = when {
                                        !hasCameraPermission -> Color.Orange
                                        isCameraOff -> Color.Red
                                        else -> Color.Green
                                    }
                                )
                            }
                            
                            // Ícone de microfone
                            Icon(
                                if (isMicMuted) Icons.Default.MicOff else Icons.Default.Mic,
                                contentDescription = if (isMicMuted) "Microfone mutado" else "Microfone ligado",
                                tint = if (isMicMuted) Color.Red else Color.Green,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        
                        // Botão de controle de câmera sobreposto
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
                                .align(Alignment.TopEnd)
                                .size(32.dp)
                                .background(
                                    if (isCameraOff) Color.Red.copy(alpha = 0.8f) else Color.Green.copy(alpha = 0.8f),
                                    CircleShape
                                )
                        ) {
                            Icon(
                                if (isCameraOff) Icons.Default.VideocamOff else Icons.Default.Videocam,
                                contentDescription = if (isCameraOff) "Ligar câmera" else "Desligar câmera",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        
                        // Overlay quando câmera está desligada
                        if (isCameraOff) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color.Black.copy(alpha = 0.3f), RoundedCornerShape(16.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(
                                        Icons.Default.VideocamOff,
                                        contentDescription = "Câmera desligada",
                                        tint = Color.White,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Text(
                                        text = "Câmera\ndesligada",
                                        color = Color.White,
                                        fontSize = 10.sp,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                }
            }
            
            // Área inferior - Controles da chamada
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Botão de câmera
                    IconButton(
                        onClick = { isCameraOff = !isCameraOff },
                        modifier = Modifier
                            .size(56.dp)
                            .background(
                                if (isCameraOff) Color.Gray else Color.Transparent,
                                CircleShape
                            )
                    ) {
                        Icon(
                            if (isCameraOff) Icons.Default.VideocamOff else Icons.Default.Videocam,
                            contentDescription = "Câmera",
                            tint = if (isCameraOff) Color.White else Color.Gray,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    
                    // Botão de microfone
                    IconButton(
                        onClick = { isMicMuted = !isMicMuted },
                        modifier = Modifier
                            .size(56.dp)
                            .background(
                                if (isMicMuted) Color.Gray else Color.Transparent,
                                CircleShape
                            )
                    ) {
                        Icon(
                            if (isMicMuted) Icons.Default.MicOff else Icons.Default.Mic,
                            contentDescription = "Microfone",
                            tint = if (isMicMuted) Color.White else Color.Gray,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    
                    // Botão de encerrar chamada
                    IconButton(
                        onClick = { 
                            // Aqui você desconectaria do Twilio Video
                            Log.i("VIDEOCHAMADA", "Encerrando chamada...")
                            navegacao?.popBackStack()
                        },
                        modifier = Modifier
                            .size(56.dp)
                            .background(Color.Red, CircleShape)
                    ) {
                        Icon(
                            Icons.Default.CallEnd,
                            contentDescription = "Encerrar chamada",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    
                    // Botão de chat
                    IconButton(
                        onClick = { /* Abrir chat */ },
                        modifier = Modifier.size(56.dp)
                    ) {
                        Icon(
                            Icons.Default.Chat,
                            contentDescription = "Chat",
                            tint = Color.Gray,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    
                    // Botão de mais opções
                    IconButton(
                        onClick = { /* Mais opções */ },
                        modifier = Modifier.size(56.dp)
                    ) {
                        Icon(
                            Icons.Default.MoreVert,
                            contentDescription = "Mais opções",
                            tint = Color.Gray,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun VideoChamadaScreenPreview() {
    VideoChamadaScreen(navegacao = null)
}

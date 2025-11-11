package com.example.tccbebe.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tccbebe.viewmodel.ChatViewModel
import com.example.tccbebe.model.Mensagem
import java.text.SimpleDateFormat
import java.util.*



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatIndividualScreen(
    navController: NavHostController?,
    contatoId: String,
    contatoNome: String
) {
    val context = LocalContext.current
    val viewModel: ChatViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return ChatViewModel(context) as T
            }
        }
    )
    val listState = rememberLazyListState()
    var mensagemTexto by remember { mutableStateOf("") }
    val uiState by viewModel.uiState.collectAsState()
    
    // Inicializar chat quando a tela é carregada
    LaunchedEffect(contatoId) {
        viewModel.criarOuBuscarChat(contatoId, contatoNome)
    }
    
    // Auto-scroll para a última mensagem
    LaunchedEffect(uiState.mensagens.size) {
        if (uiState.mensagens.isNotEmpty()) {
            listState.animateScrollToItem(uiState.mensagens.size - 1)
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF7986CB))
    ) {
        // Header do chat
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { navController?.popBackStack() }
            ) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "Voltar",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
            
            // Avatar do contato
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = "Avatar",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Text(
                text = contatoNome,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )
        }
        
        // Área de mensagens
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Color.White,
                    RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                )
        ) {
            // Lista de mensagens
            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = Color(0xFF7986CB)
                        )
                    }
                }
                uiState.errorMessage != null -> {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = uiState.errorMessage ?: "Erro desconhecido",
                                color = Color.Red,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = { viewModel.criarOuBuscarChat(contatoId, contatoNome) }
                            ) {
                                Text("Tentar novamente")
                            }
                        }
                    }
                }
                else -> {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier
                            .weight(1f)
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(uiState.mensagens) { mensagem ->
                            MensagemItem(
                                mensagem = mensagem,
                                isEnviada = viewModel.isMensagemEnviada(mensagem)
                            )
                        }
                    }
                }
            }
            
            // Campo de entrada de mensagem
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                // Campo de texto
                OutlinedTextField(
                    value = mensagemTexto,
                    onValueChange = { mensagemTexto = it },
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(min = 48.dp, max = 120.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF7986CB),
                        unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f),
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    placeholder = {
                        Text(
                            "Mensagem",
                            color = Color.Gray
                        )
                    },
                    trailingIcon = {
                        IconButton(
                            onClick = { /* Ação da câmera */ }
                        ) {
                            Icon(
                                Icons.Default.CameraAlt,
                                contentDescription = "Câmera",
                                tint = Color(0xFF7986CB)
                            )
                        }
                    }
                )
                
                Spacer(modifier = Modifier.width(8.dp))
                
                // Botão de enviar/microfone
                FloatingActionButton(
                    onClick = {
                        if (mensagemTexto.isNotBlank()) {
                            viewModel.enviarMensagem(mensagemTexto, contatoId)
                            mensagemTexto = ""
                        }
                    },
                    modifier = Modifier.size(48.dp),
                    containerColor = if (uiState.isEnviandoMensagem) Color.Gray else Color(0xFF7986CB),
                    contentColor = Color.White
                ) {
                    if (uiState.isEnviandoMensagem) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(
                            if (mensagemTexto.isBlank()) Icons.Default.Mic else Icons.Default.Send,
                            contentDescription = if (mensagemTexto.isBlank()) "Microfone" else "Enviar",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MensagemItem(mensagem: Mensagem, isEnviada: Boolean) {
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    // Converter created_at string para timestamp se necessário
    val horario = try {
        if (mensagem.created_at.isNotEmpty()) {
            // Se created_at é uma string de data, fazer parse
            mensagem.created_at.substring(11, 16) // Pegar apenas HH:mm
        } else {
            timeFormat.format(Date())
        }
    } catch (e: Exception) {
        timeFormat.format(Date())
    }
    
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isEnviada) Arrangement.End else Arrangement.Start
    ) {
        if (!isEnviada) {
            // Avatar para mensagens recebidas
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF7986CB)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = "Avatar",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(8.dp))
        }
        
        Column(
            horizontalAlignment = if (isEnviada) Alignment.End else Alignment.Start
        ) {
            // Bolha da mensagem
            Card(
                modifier = Modifier.widthIn(max = 280.dp),
                shape = RoundedCornerShape(
                    topStart = 16.dp,
                    topEnd = 16.dp,
                    bottomStart = if (isEnviada) 16.dp else 4.dp,
                    bottomEnd = if (isEnviada) 4.dp else 16.dp
                ),
                colors = CardDefaults.cardColors(
                    containerColor = if (isEnviada) 
                        Color(0xFFE3F2FD) else Color(0xFFF5F5F5)
                )
            ) {
                Text(
                    text = mensagem.conteudo,
                    modifier = Modifier.padding(12.dp),
                    fontSize = 14.sp,
                    color = Color.Black
                )
            }
            
            // Horário
            Text(
                text = horario,
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.padding(
                    top = 4.dp,
                    start = if (isEnviada) 0.dp else 8.dp,
                    end = if (isEnviada) 8.dp else 0.dp
                )
            )
        }
        
        if (isEnviada) {
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ChatIndividualScreenPreview() {
    ChatIndividualScreen(
        navController = null,
        contatoId = "1",
        contatoNome = "Ana Silva - mãe do João"
    )
}

@Preview(showBackground = true)
@Composable
fun MensagemItemEnviadaPreview() {
    MensagemItem(
        mensagem = Mensagem(
            id = "1",
            conteudo = "Olá! Como está o bebê hoje?",
            id_chat = "chat_1",
            id_user = "user_1",
            created_at = "2024-11-11T15:30:00Z",
            remetente = "Você"
        ),
        isEnviada = true
    )
}

@Preview(showBackground = true)
@Composable
fun MensagemItemRecebidaPreview() {
    MensagemItem(
        mensagem = Mensagem(
            id = "2",
            conteudo = "Ele está bem, brincando bastante!",
            id_chat = "chat_1",
            id_user = "user_2",
            created_at = "2024-11-11T15:31:00Z",
            remetente = "Ana Silva"
        ),
        isEnviada = false
    )
}

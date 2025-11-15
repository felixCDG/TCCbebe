package com.example.tccbebe.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.tccbebe.repository.ChatIARepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

data class MensagemIA(
    val id: String,
    val conteudo: String,
    val isFromUser: Boolean,
    val timestamp: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BabyIAScreen(navegacao: NavHostController?) {
    val context = LocalContext.current
    val chatIARepository = remember { ChatIARepository(context) }
    val coroutineScope = rememberCoroutineScope()
    
    var mensagemTexto by remember { mutableStateOf("") }
    var mensagens by remember { mutableStateOf(listOf(
        MensagemIA(
            id = "1",
            conteudo = "Olá! Eu sou a BabyIA, sua assistente especializada em cuidados com bebês. Como posso te ajudar hoje?",
            isFromUser = false,
            timestamp = "14:30"
        )
    )) }
    
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    
    val listState = rememberLazyListState()
    
    // Função para rolar para o final da lista
    LaunchedEffect(mensagens.size) {
        if (mensagens.isNotEmpty()) {
            listState.animateScrollToItem(mensagens.size - 1)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEFF2FF))
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF7986CB))
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { navegacao?.popBackStack() }
            ) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "Voltar",
                    tint = Color.White
                )
            }
            
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.SmartToy,
                    contentDescription = "IA",
                    tint = Color(0xFF7986CB),
                    modifier = Modifier.size(24.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column {
                Text(
                    text = "BabyIA",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Assistente Especializada",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 12.sp
                )
            }
        }

        // Área de mensagens
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            items(mensagens) { mensagem ->
                MensagemIAItem(mensagem = mensagem)
            }
            
            // Mostrar indicador de carregamento quando IA está processando
            if (isLoading) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .background(Color(0xFF7986CB), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.SmartToy,
                                contentDescription = "IA",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        
                        Card(
                            modifier = Modifier.widthIn(max = 280.dp),
                            shape = RoundedCornerShape(
                                topStart = 4.dp,
                                topEnd = 16.dp,
                                bottomStart = 16.dp,
                                bottomEnd = 16.dp
                            ),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    color = Color(0xFF7986CB),
                                    strokeWidth = 2.dp
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "BabyIA está pensando...",
                                    color = Color(0xFF333333),
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // Campo de entrada
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            OutlinedTextField(
                value = mensagemTexto,
                onValueChange = { mensagemTexto = it },
                modifier = Modifier.weight(1f),
                placeholder = { 
                    Text(
                        "Digite sua pergunta sobre bebês...",
                        color = Color.Gray
                    ) 
                },
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF7986CB),
                    unfocusedBorderColor = Color.Gray.copy(alpha = 0.3f)
                ),
                maxLines = 3
            )
            
            Spacer(modifier = Modifier.width(8.dp))
            
            FloatingActionButton(
                onClick = {
                    if (mensagemTexto.isNotBlank() && !isLoading) {
                        val pergunta = mensagemTexto
                        val novaMensagem = MensagemIA(
                            id = UUID.randomUUID().toString(),
                            conteudo = pergunta,
                            isFromUser = true,
                            timestamp = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
                        )
                        mensagens = mensagens + novaMensagem
                        mensagemTexto = ""
                        isLoading = true
                        errorMessage = null
                        
                        // Chamar API real
                        coroutineScope.launch {
                            chatIARepository.enviarPerguntaIA(pergunta)
                                .onSuccess { response ->
                                    val respostaIA = MensagemIA(
                                        id = UUID.randomUUID().toString(),
                                        conteudo = response.IA_response,
                                        isFromUser = false,
                                        timestamp = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
                                    )
                                    mensagens = mensagens + respostaIA
                                    isLoading = false
                                }
                                .onFailure { error ->
                                    errorMessage = error.message
                                    isLoading = false
                                    // Adicionar mensagem de erro
                                    val mensagemErro = MensagemIA(
                                        id = UUID.randomUUID().toString(),
                                        conteudo = "Desculpe, ocorreu um erro ao processar sua pergunta. Tente novamente.",
                                        isFromUser = false,
                                        timestamp = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
                                    )
                                    mensagens = mensagens + mensagemErro
                                }
                        }
                    }
                },
                containerColor = if (mensagemTexto.isNotBlank() && !isLoading) Color(0xFF7986CB) else Color.Gray,
                modifier = Modifier.size(48.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(
                        Icons.Default.Send,
                        contentDescription = "Enviar mensagem",
                        tint = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun MensagemIAItem(mensagem: MensagemIA) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (mensagem.isFromUser) Arrangement.End else Arrangement.Start
    ) {
        if (!mensagem.isFromUser) {
            // Avatar da IA
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(Color(0xFF7986CB), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.SmartToy,
                    contentDescription = "IA",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
        }
        
        Column(
            modifier = Modifier.widthIn(max = 280.dp),
            horizontalAlignment = if (mensagem.isFromUser) Alignment.End else Alignment.Start
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(
                    topStart = if (mensagem.isFromUser) 16.dp else 4.dp,
                    topEnd = if (mensagem.isFromUser) 4.dp else 16.dp,
                    bottomStart = 16.dp,
                    bottomEnd = 16.dp
                ),
                colors = CardDefaults.cardColors(
                    containerColor = if (mensagem.isFromUser) Color(0xFF7986CB) else Color.White
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp)
                ) {
                    Text(
                        text = mensagem.conteudo,
                        color = if (mensagem.isFromUser) Color.White else Color(0xFF333333),
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Text(
                        text = mensagem.timestamp,
                        color = if (mensagem.isFromUser) Color.White.copy(alpha = 0.7f) else Color.Gray,
                        fontSize = 11.sp,
                        textAlign = TextAlign.End,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
        
        if (mensagem.isFromUser) {
            Spacer(modifier = Modifier.width(8.dp))
            // Avatar do usuário
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(Color(0xFF7986CB), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "U",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun BabyIAScreenPreview() {
    BabyIAScreen(navegacao = null)
}


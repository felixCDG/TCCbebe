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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
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
    var mensagemTexto by remember { mutableStateOf("") }
    var mensagens by remember { mutableStateOf(listOf(
        MensagemIA(
            id = "1",
            conteudo = "Olá! Eu sou a BabyIA, sua assistente especializada em cuidados com bebês. Como posso te ajudar hoje?",
            isFromUser = false,
            timestamp = "14:30"
        ),
        MensagemIA(
            id = "2",
            conteudo = "Oi! Meu bebê está com 3 meses e não está dormindo bem à noite. O que posso fazer?",
            isFromUser = true,
            timestamp = "14:32"
        ),
        MensagemIA(
            id = "3",
            conteudo = "Entendo sua preocupação! Aos 3 meses, é normal que o padrão de sono ainda esteja se desenvolvendo. Algumas dicas que podem ajudar:\n\n• Estabeleça uma rotina noturna consistente\n• Mantenha o ambiente escuro e silencioso\n• Evite estímulos antes de dormir\n• Considere o método de \"swaddling\" (embrulhar o bebê)\n\nSe o problema persistir, consulte o pediatra. Posso te ajudar com mais alguma coisa?",
            isFromUser = false,
            timestamp = "14:33"
        )
    )) }
    
    val listState = rememberLazyListState()

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
                    if (mensagemTexto.isNotBlank()) {
                        val novaMensagem = MensagemIA(
                            id = UUID.randomUUID().toString(),
                            conteudo = mensagemTexto,
                            isFromUser = true,
                            timestamp = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
                        )
                        mensagens = mensagens + novaMensagem
                        mensagemTexto = ""
                        
                        // Simular resposta da IA após 2 segundos
                        Timer().schedule(object : TimerTask() {
                            override fun run() {
                                val respostaIA = MensagemIA(
                                    id = UUID.randomUUID().toString(),
                                    conteudo = gerarRespostaIA(novaMensagem.conteudo),
                                    isFromUser = false,
                                    timestamp = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
                                )
                                mensagens = mensagens + respostaIA
                            }
                        }, 2000)
                    }
                },
                containerColor = if (mensagemTexto.isNotBlank()) Color(0xFF7986CB) else Color.Gray,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    Icons.Default.Send,
                    contentDescription = "Enviar mensagem",
                    tint = Color.White
                )
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

fun gerarRespostaIA(pergunta: String): String {
    val respostasComuns = listOf(
        "Essa é uma excelente pergunta! Com base nas melhores práticas pediátricas, recomendo que você consulte sempre o pediatra do seu bebê para orientações personalizadas.",
        "Entendo sua preocupação. Cada bebê é único, mas posso compartilhar algumas informações gerais que podem ser úteis. Lembre-se sempre de consultar um profissional de saúde.",
        "Obrigada por compartilhar isso comigo! Vou te dar algumas dicas baseadas em evidências científicas, mas é importante sempre validar com o pediatra do seu bebê.",
        "Essa situação é mais comum do que você imagina! Vou te ajudar com algumas orientações, mas não esqueça de discutir isso na próxima consulta pediátrica."
    )
    
    return when {
        pergunta.contains("sono", ignoreCase = true) || pergunta.contains("dormir", ignoreCase = true) -> 
            "Para questões de sono, algumas dicas importantes:\n\n• Estabeleça uma rotina consistente\n• Ambiente escuro e silencioso\n• Temperatura adequada (18-20°C)\n• Evite estímulos antes de dormir\n\nCada bebê tem seu ritmo. Se persistir, consulte o pediatra!"
        
        pergunta.contains("alimentação", ignoreCase = true) || pergunta.contains("comer", ignoreCase = true) || pergunta.contains("leite", ignoreCase = true) ->
            "Sobre alimentação:\n\n• Até 6 meses: leite materno exclusivo (quando possível)\n• Introdução alimentar gradual após 6 meses\n• Observe sinais de fome e saciedade\n• Mantenha horários regulares\n\nSempre siga as orientações do seu pediatra!"
        
        pergunta.contains("choro", ignoreCase = true) || pergunta.contains("chorando", ignoreCase = true) ->
            "O choro é a principal forma de comunicação do bebê. Pode indicar:\n\n• Fome\n• Sono\n• Desconforto (fralda, temperatura)\n• Necessidade de colo\n• Cólicas\n\nTente identificar padrões. Se o choro for excessivo, consulte o pediatra."
        
        else -> respostasComuns.random()
    }
}

@Preview(showBackground = true)
@Composable
fun BabyIAScreenPreview() {
    BabyIAScreen(navegacao = null)
}


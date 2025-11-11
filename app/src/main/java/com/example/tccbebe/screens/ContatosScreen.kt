package com.example.tccbebe.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.ViewModelProvider
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.example.tccbebe.viewmodel.ContatosViewModel
import com.example.tccbebe.model.Contato
import androidx.compose.runtime.collectAsState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContatosScreen(
    navController: NavHostController?
) {
    val context = LocalContext.current
    val viewModel: ContatosViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return ContatosViewModel(context) as T
            }
        }
    )
    val uiState by viewModel.uiState.collectAsState()
    val contatosFiltrados = viewModel.contatosFiltrados()
    
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF7986CB))
    ) {
        // Header
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
            
            Text(
                text = "Encontre seus contatos",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
        
        // Conteúdo principal com fundo branco
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Color.White,
                    RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                )
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            
            // Campo de busca
            OutlinedTextField(
                value = uiState.searchText,
                onValueChange = { viewModel.atualizarBusca(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF7986CB),
                    unfocusedBorderColor = Color(0xFF7986CB),
                    focusedContainerColor = Color(0xFFF5F5F5),
                    unfocusedContainerColor = Color(0xFFF5F5F5)
                ),
                trailingIcon = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "Buscar",
                        tint = Color(0xFF7986CB)
                    )
                },
                placeholder = {
                    Text(
                        "Buscar contatos...",
                        color = Color.Gray
                    )
                }
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Botão de debug para carregar contatos de teste
            Button(
                onClick = { viewModel.carregarContatosTeste() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF7986CB)
                )
            ) {
                Text(
                    text = "Carregar Contatos de Teste",
                    color = Color.White
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Loading e erro
            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = Color(0xFF7986CB)
                        )
                    }
                }
                uiState.errorMessage != null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = uiState.errorMessage ?: "Erro desconhecido",
                            color = Color.Red,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                else -> {
                    // Lista de contatos
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(contatosFiltrados) { contato ->
                            ContatoItem(
                                contato = contato,
                                onClick = {
                                    // Navegar para o chat individual
                                    navController?.navigate("chat/${contato.id}/${contato.nome}")
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ContatoItem(
    contato: Contato,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar com indicador de status
            Box(
                modifier = Modifier.size(48.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF7986CB)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = "Avatar",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
                
                // Indicador de status online/offline
                if (contato.status == "online") {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(CircleShape)
                            .background(Color.Green)
                            .align(Alignment.BottomEnd)
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Nome e tipo
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = contato.nome,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
                
                Text(
                    text = contato.tipo,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ContatosScreenPreview() {
    ContatosScreen(navController = null)
}

@Preview(showBackground = true)
@Composable
fun ContatoItemPreview() {
    ContatoItem(
        contato = Contato(
            id = "1",
            nome = "Dr. João Silva",
            email = "joao.silva@clinica.com",
            tipo = "Pediatra",
            status = "online"
        ),
        onClick = {}
    )
}

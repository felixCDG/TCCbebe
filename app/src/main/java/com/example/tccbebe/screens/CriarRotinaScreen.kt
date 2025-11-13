package br.senai.sp.jandira.telarotina.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.tccbebe.R
import com.example.tccbebe.model.CadastroItemRotina
import com.example.tccbebe.model.CadastroUser
import com.example.tccbebe.service.AuthenticatedConexao
import com.example.tccbebe.utils.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.await
import kotlin.String

// Data class para representar um item de rotina
data class ItemRotina(
    var titulo: String = "",
    var descricao: String = "",
    var hora: String = "00:00",
    var data: String = "",
    var tituloError: String = "",
    var descricaoError: String = "",
    var horaError: String = "",
    var dataError: String = ""
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CriarRotinaScreen(navegacao: NavHostController?) {
    // Lista de itens de rotina
    var itensRotina = remember { mutableStateOf(listOf(ItemRotina())) }
    
    // Estados para validação geral
    var showErrorDialog = remember { mutableStateOf(false) }
    var errorMessage = remember { mutableStateOf("") }


    val context = LocalContext.current
    val clienteApi = AuthenticatedConexao(context).getItemRotinaService()

    // Função para formatar data com máscara DD/MM/YYYY
    fun formatDateInput(input: String): String {
        val digits = input.filter { it.isDigit() }
        return when {
            digits.length <= 2 -> digits
            digits.length <= 4 -> "${digits.substring(0, 2)}/${digits.substring(2)}"
            digits.length <= 8 -> "${digits.substring(0, 2)}/${digits.substring(2, 4)}/${digits.substring(4)}"
            else -> "${digits.substring(0, 2)}/${digits.substring(2, 4)}/${digits.substring(4, 8)}"
        }
    }
    
    // Função para converter data DD/MM/YYYY para YYYY-MM-DD (formato da API)
    fun convertDateToApiFormat(dateString: String): String {
        if (dateString.length != 10) return dateString
        
        val parts = dateString.split("/")
        if (parts.size != 3) return dateString
        
        val day = parts[0]
        val month = parts[1] 
        val year = parts[2]
        
        return "$year-$month-$day"
    }
    
    // Função para validar formato de data
    fun isValidDate(dateString: String): Boolean {
        if (dateString.length != 10) return false
        
        val parts = dateString.split("/")
        if (parts.size != 3) return false
        
        val day = parts[0].toIntOrNull() ?: return false
        val month = parts[1].toIntOrNull() ?: return false
        val year = parts[2].toIntOrNull() ?: return false
        
        return day in 1..31 && month in 1..12 && year >= 1900
    }

    // Função para validar os campos
    fun validarCampos(): Boolean {
        var isValid = true
        val erros = mutableListOf<String>()
        
        // Limpar erros anteriores
        tituloError.value = ""
        descricaoError.value = ""
        horaError.value = ""
        dataError.value = ""
        
        // Validar título
        if (tituloState.value.trim().isEmpty()) {
            tituloError.value = "Título é obrigatório"
            erros.add("• Título é obrigatório")
            isValid = false
        } else if (tituloState.value.trim().length < 3) {
            tituloError.value = "Título deve ter pelo menos 3 caracteres"
            erros.add("• Título deve ter pelo menos 3 caracteres")
            isValid = false
        }
        
        // Validar descrição
        if (descricaoState.value.trim().isEmpty()) {
            descricaoError.value = "Descrição é obrigatória"
            erros.add("• Descrição é obrigatória")
            isValid = false
        } else if (descricaoState.value.trim().length < 5) {
            descricaoError.value = "Descrição deve ter pelo menos 5 caracteres"
            erros.add("• Descrição deve ter pelo menos 5 caracteres")
            isValid = false
        }
        
        // Validar hora
        if (horaState.value.trim().isEmpty() || horaState.value == "00:00") {
            horaError.value = "Hora é obrigatória"
            erros.add("• Hora é obrigatória")
            isValid = false
        } else {
            // Validar formato da hora (HH:MM)
            val horaRegex = Regex("^([01]?[0-9]|2[0-3]):[0-5][0-9]$")
            if (!horaRegex.matches(horaState.value)) {
                horaError.value = "Formato de hora inválido (HH:MM)"
                erros.add("• Formato de hora inválido (use HH:MM)")
                isValid = false
            }
        }
        
        // Validar data
        if (dataState.value.trim().isEmpty()) {
            dataError.value = "Data é obrigatória"
            erros.add("• Data é obrigatória")
            isValid = false
        } else if (!isValidDate(dataState.value)) {
            dataError.value = "Data inválida (use DD/MM/AAAA)"
            erros.add("• Data inválida (use DD/MM/AAAA)")
            isValid = false
        }
        
        // Se houver erros, mostrar diálogo
        if (!isValid) {
            errorMessage.value = "Por favor, corrija os seguintes erros:\n\n${erros.joinToString("\n")}"
            showErrorDialog.value = true
        }
        
        return isValid
    }
    
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
            Spacer(modifier = Modifier.height(20.dp))
            
            // Title
            Text(
                text = "Crie uma\nnova Rotina",
                fontSize = 24.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = kronaOneFont,
                color = Color.Black,
                lineHeight = 28.sp
            )
            
            Spacer(modifier = Modifier.height(40.dp))
            
            // Form Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF6C7CE7)),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Título and Hora Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Título
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Título",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Normal,
                                fontFamily = kronaOneFont,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            OutlinedTextField(
                                value = tituloState.value,
                                onValueChange = { 
                                    tituloState.value = it
                                    if (tituloError.value.isNotEmpty()) {
                                        tituloError.value = ""
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = Color(0xFF8A94F0),
                                    unfocusedContainerColor = Color(0xFF8A94F0),
                                    focusedBorderColor = if (tituloError.value.isNotEmpty()) Color.Red else Color.Transparent,
                                    unfocusedBorderColor = if (tituloError.value.isNotEmpty()) Color.Red else Color.Transparent
                                ),
                                shape = RoundedCornerShape(24.dp),
                                isError = tituloError.value.isNotEmpty()
                            )
                            if (tituloError.value.isNotEmpty()) {
                                Text(
                                    text = tituloError.value,
                                    color = Color.Red,
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(start = 8.dp, top = 2.dp)
                                )
                            }
                        }
                        
                        // Hora
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Hora",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Normal,
                                fontFamily = kronaOneFont,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            OutlinedTextField(
                                value = horaState.value,
                                onValueChange = { 
                                    horaState.value = it
                                    if (horaError.value.isNotEmpty()) {
                                        horaError.value = ""
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = Color(0xFF8A94F0),
                                    unfocusedContainerColor = Color(0xFF8A94F0),
                                    focusedBorderColor = if (horaError.value.isNotEmpty()) Color.Red else Color.Transparent,
                                    unfocusedBorderColor = if (horaError.value.isNotEmpty()) Color.Red else Color.Transparent
                                ),
                                shape = RoundedCornerShape(24.dp),
                                isError = horaError.value.isNotEmpty()
                            )
                            if (horaError.value.isNotEmpty()) {
                                Text(
                                    text = horaError.value,
                                    color = Color.Red,
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(start = 8.dp, top = 2.dp)
                                )
                            }
                        }
                    }
                    
                    // Descrição and Data Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Descrição
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Descrição",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Normal,
                                fontFamily = kronaOneFont,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            OutlinedTextField(
                                value = descricaoState.value,
                                onValueChange = { 
                                    descricaoState.value = it
                                    if (descricaoError.value.isNotEmpty()) {
                                        descricaoError.value = ""
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = Color(0xFF8A94F0),
                                    unfocusedContainerColor = Color(0xFF8A94F0),
                                    focusedBorderColor = if (descricaoError.value.isNotEmpty()) Color.Red else Color.Transparent,
                                    unfocusedBorderColor = if (descricaoError.value.isNotEmpty()) Color.Red else Color.Transparent
                                ),
                                shape = RoundedCornerShape(24.dp),
                                isError = descricaoError.value.isNotEmpty()
                            )
                            if (descricaoError.value.isNotEmpty()) {
                                Text(
                                    text = descricaoError.value,
                                    color = Color.Red,
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(start = 8.dp, top = 2.dp)
                                )
                            }
                        }
                        
                        // Data
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Data",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Normal,
                                fontFamily = kronaOneFont,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            OutlinedTextField(
                                value = dataState.value,
                                onValueChange = { newValue ->
                                    val formatted = formatDateInput(newValue)
                                    if (formatted.length <= 10) {
                                        dataState.value = formatted
                                    }
                                    if (dataError.value.isNotEmpty()) {
                                        dataError.value = ""
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp),
                                placeholder = {
                                    Text(
                                        text = "DD/MM/AAAA",
                                        color = Color.White.copy(alpha = 0.7f),
                                        fontSize = 14.sp
                                    )
                                },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = Color(0xFF8A94F0),
                                    unfocusedContainerColor = Color(0xFF8A94F0),
                                    focusedBorderColor = if (dataError.value.isNotEmpty()) Color.Red else Color.Transparent,
                                    unfocusedBorderColor = if (dataError.value.isNotEmpty()) Color.Red else Color.Transparent
                                ),
                                shape = RoundedCornerShape(24.dp),
                                isError = dataError.value.isNotEmpty()
                            )
                            if (dataError.value.isNotEmpty()) {
                                Text(
                                    text = dataError.value,
                                    color = Color.Red,
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(start = 8.dp, top = 2.dp)
                                )
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.weight(1f))
                    
                    // Botões lado a lado
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Add Button
                        IconButton(
                            onClick = { },
                            modifier = Modifier
                                .size(40.dp)
                                .background(
                                    Color(0xFF8A94F0),
                                    RoundedCornerShape(20.dp)
                                )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Adicionar",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        
                        // Criar Button
                        Button(
                            onClick = {
                                // Validar campos antes de enviar
                                if (!validarCampos()) {
                                    Log.w("ROTINA_DEBUG", "❌ Validação falhou - campos obrigatórios não preenchidos")
                                    return@Button
                                }

                                // Converter data para formato da API (YYYY-MM-DD)
                                val dataFormatada = convertDateToApiFormat(dataState.value.trim())
                                Log.i("ROTINA_DEBUG", "Data original: '${dataState.value.trim()}' -> Data formatada: '$dataFormatada'")
                                
                                val cliente = CadastroItemRotina(
                                    id_item = 0,
                                    titulo = tituloState.value.trim(),
                                    descricao = descricaoState.value.trim(),
                                    data_rotina = dataFormatada,
                                    hora = horaState.value.trim(),

                                )

                                // 🔍 LOGS DETALHADOS - DADOS DO BODY
                                Log.i("ROTINA_DEBUG", "=== DADOS SENDO ENVIADOS ===")
                                Log.i("ROTINA_DEBUG", "Body da requisição: $cliente")
                                Log.i("ROTINA_DEBUG", "id_item: ${cliente.id_item}")
                                Log.i("ROTINA_DEBUG", "titulo: '${cliente.titulo}'")
                                Log.i("ROTINA_DEBUG", "descricao: '${cliente.descricao}'")
                                Log.i("ROTINA_DEBUG", "data_rotina: '${cliente.data_rotina}'")
                                Log.i("ROTINA_DEBUG", "hora: '${cliente.hora}'")
                                
                                // 🔍 LOGS DETALHADOS - TOKEN E HEADER
                                val token = SessionManager.getAuthToken(context)
                                val bearerToken = SessionManager.getBearerToken(context)
                                val userId = SessionManager.getUserId(context)
                                val responsavelId = SessionManager.getResponsavelId(context)
                                
                                Log.i("ROTINA_DEBUG", "=== DADOS DE AUTENTICAÇÃO ===")
                                Log.i("ROTINA_DEBUG", "Token bruto: $token")
                                Log.i("ROTINA_DEBUG", "Bearer token: $bearerToken")
                                Log.i("ROTINA_DEBUG", "User ID: $userId")
                                Log.i("ROTINA_DEBUG", "Responsavel ID: $responsavelId")
                                Log.i("ROTINA_DEBUG", "=== INICIANDO REQUISIÇÃO ===")

                                GlobalScope.launch(Dispatchers.IO) {
                                    try {
                                        Log.i("ROTINA_DEBUG", "🚀 Fazendo chamada para API...")
                                        val response = clienteApi.cadastrarItemR(cliente).await()

                                        Log.i("ROTINA_DEBUG", "=== RESPOSTA DA API ===")
                                        Log.i("ROTINA_DEBUG", "✅ Resposta recebida: $response")
                                        Log.i("ROTINA_DEBUG", "Status: ${response.status}")
                                        Log.i("ROTINA_DEBUG", "Status Code: ${response.status_code}")
                                        Log.i("ROTINA_DEBUG", "Mensagem: ${response.message}")
                                        Log.i("ROTINA_DEBUG", "Data: ${response.data}")

                                    } catch (e: Exception) {
                                        Log.e("ROTINA_DEBUG", "=== ERRO NA REQUISIÇÃO ===")
                                        Log.e("ROTINA_DEBUG", "❌ Erro ao cadastrar: ${e.message}")
                                        Log.e("ROTINA_DEBUG", "Tipo do erro: ${e.javaClass.simpleName}")
                                        Log.e("ROTINA_DEBUG", "Stack trace: ${e.stackTrace.contentToString()}")
                                        e.printStackTrace()
                                    }
                                }
                                navegacao?.navigate("login")

                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White,
                                contentColor = Color.Black
                            ),
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Text(
                                text = "Criar",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Normal,
                                fontFamily = kronaOneFont
                            )
                        }
                    }
                }
            }
        }
    }
    
    // Diálogo de erro
    if (showErrorDialog.value) {
        AlertDialog(
            onDismissRequest = { showErrorDialog.value = false },
            title = {
                Text(
                    text = "Erro de Validação",
                    fontWeight = FontWeight.Bold,
                    color = Color.Red
                )
            },
            text = {
                Text(
                    text = errorMessage.value,
                    color = Color.Black
                )
            },
            confirmButton = {
                Button(
                    onClick = { showErrorDialog.value = false },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF6C7CE7)
                    )
                ) {
                    Text("OK", color = Color.White)
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CriarRotinaScreenPreview() {
    CriarRotinaScreen(
        navegacao = null
    )
}

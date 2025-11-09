package com.example.tccbebe.screens

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Brush
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import br.senai.sp.jandira.foodrecipe.service.AzureUploadService.uploadImageToAzure
import coil.compose.AsyncImage
import com.example.tccbebe.model.RegistroResp
import com.example.tccbebe.service.AuthenticatedConexao
import com.example.tccbebe.utils.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.await

@Composable
fun CadastroResponsavelNovo(navegacao: NavHostController?) {
    
    // Funções de formatação
    fun formatarNome(textFieldValue: TextFieldValue): TextFieldValue {
        val texto = textFieldValue.text.split(" ").joinToString(" ") { palavra ->
            if (palavra.isNotEmpty()) {
                palavra.lowercase().replaceFirstChar { it.uppercase() }
            } else palavra
        }
        return textFieldValue.copy(
            text = texto,
            selection = TextRange(texto.length)
        )
    }
    
    fun formatarData(textFieldValue: TextFieldValue): TextFieldValue {
        val digitos = textFieldValue.text.filter { it.isDigit() }
        val textoFormatado = when {
            digitos.length <= 2 -> digitos
            digitos.length <= 4 -> "${digitos.substring(0, 2)}/${digitos.substring(2)}"
            digitos.length <= 8 -> "${digitos.substring(0, 2)}/${digitos.substring(2, 4)}/${digitos.substring(4)}"
            else -> "${digitos.substring(0, 2)}/${digitos.substring(2, 4)}/${digitos.substring(4, 8)}"
        }
        return textFieldValue.copy(
            text = textoFormatado,
            selection = TextRange(textoFormatado.length)
        )
    }
    
    fun converterDataParaBanco(dataFormatada: String): String {
        if (dataFormatada.length == 10) {
            val partes = dataFormatada.split("/")
            if (partes.size == 3) {
                return "${partes[2]}/${partes[1]}/${partes[0]}" // ano/mes/dia
            }
        }
        return dataFormatada
    }
    
    fun formatarCPF(textFieldValue: TextFieldValue): TextFieldValue {
        val digitos = textFieldValue.text.filter { it.isDigit() }
        val textoFormatado = when {
            digitos.length <= 3 -> digitos
            digitos.length <= 6 -> "${digitos.substring(0, 3)}.${digitos.substring(3)}"
            digitos.length <= 9 -> "${digitos.substring(0, 3)}.${digitos.substring(3, 6)}.${digitos.substring(6)}"
            digitos.length <= 11 -> "${digitos.substring(0, 3)}.${digitos.substring(3, 6)}.${digitos.substring(6, 9)}-${digitos.substring(9)}"
            else -> "${digitos.substring(0, 3)}.${digitos.substring(3, 6)}.${digitos.substring(6, 9)}-${digitos.substring(9, 11)}"
        }
        return textFieldValue.copy(
            text = textoFormatado,
            selection = TextRange(textoFormatado.length)
        )
    }
    
    fun formatarTelefone(textFieldValue: TextFieldValue): TextFieldValue {
        val digitos = textFieldValue.text.filter { it.isDigit() }
        val textoFormatado = when {
            digitos.length <= 2 -> if (digitos.isNotEmpty()) "($digitos" else ""
            digitos.length <= 7 -> "(${digitos.substring(0, 2)}) ${digitos.substring(2)}"
            digitos.length <= 11 -> "(${digitos.substring(0, 2)}) ${digitos.substring(2, 7)}-${digitos.substring(7)}"
            else -> "(${digitos.substring(0, 2)}) ${digitos.substring(2, 7)}-${digitos.substring(7, 11)}"
        }
        return textFieldValue.copy(
            text = textoFormatado,
            selection = TextRange(textoFormatado.length)
        )
    }
    
    fun formatarCEP(textFieldValue: TextFieldValue): TextFieldValue {
        val digitos = textFieldValue.text.filter { it.isDigit() }
        val textoFormatado = when {
            digitos.length <= 5 -> digitos
            digitos.length <= 8 -> "${digitos.substring(0, 5)}-${digitos.substring(5)}"
            else -> "${digitos.substring(0, 5)}-${digitos.substring(5, 8)}"
        }
        return textFieldValue.copy(
            text = textoFormatado,
            selection = TextRange(textoFormatado.length)
        )
    }
    // Estados
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var imageUrl by remember { mutableStateOf<String?>(null) }
    val pickImageLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        imageUri = uri
    }
    
    val context = LocalContext.current
    val expandedMenupai = remember { mutableStateOf(false) }
    val selectedOptionpai = remember { mutableStateOf("") }
    val pictureState = remember { mutableStateOf("") }
    val selectedOptionId = remember { mutableStateOf<Int?>(null) }
    val adicionalSegundo = remember { mutableStateOf(false) }
    
    var nomeState = remember { mutableStateOf(TextFieldValue("")) }
    var dataNState = remember { mutableStateOf(TextFieldValue("")) }
    var cpfState = remember { mutableStateOf(TextFieldValue("")) }
    var profissaoState = remember { mutableStateOf(TextFieldValue("")) }
    var telefoneState = remember { mutableStateOf(TextFieldValue("")) }
    var cepState = remember { mutableStateOf(TextFieldValue("")) }
    var CSCState = remember { mutableStateOf(TextFieldValue("")) }
    var emailState = remember { mutableStateOf(TextFieldValue("")) }
    
    val clienteApi = AuthenticatedConexao(context).getRegistroRspService()

    Row(
        modifier = Modifier.fillMaxSize()
    ) {
        // Lado esquerdo - Seção azul com degradê
        Box(
            modifier = Modifier
                .weight(1.2f)
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF9BB5FF), // Azul mais claro no topo
                            Color(0xFF708EF1)  // Azul mais escuro embaixo
                        )
                    )
                )
        ) {
            
            // Conteúdo da seção azul
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Espaçador para empurrar o conteúdo para o centro
                Spacer(modifier = Modifier.weight(1f))
                
                // Conteúdo central
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Cadastro do\nResponsável",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        lineHeight = 28.sp
                    )
                }
                
                // Espaçador para empurrar o botão para baixo
                Spacer(modifier = Modifier.weight(1f))
                
                // Botão "Voltar para o Início" na parte inferior
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navegacao?.navigate("login")
                        },
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Voltar",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Voltar para o Início",
                        color = Color.White,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp
                    )
                }
            }
        }
        
        // Lado direito - Formulário
        Column(
            modifier = Modifier
                .weight(1.4f)
                .fillMaxSize()
                .background(Color.White)
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top
        ) {
            // Cabeçalho com ícone e título
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Responsável",
                    modifier = Modifier.size(24.dp),
                    tint = Color.Black
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Responsável",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
            
            // Linha divisória
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(Color(0xFF708EF1))
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Campo Nome completo
            Text(
                text = "Nome completo *",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = nomeState.value,
                onValueChange = { 
                    nomeState.value = formatarNome(it)
                },
                placeholder = { Text("Digite o nome completo", fontSize = 14.sp, color = Color.Gray) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(25.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Gray,
                    unfocusedBorderColor = Color.Gray
                )
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Campo Data de Nascimento
            Text(
                text = "Data de Nascimento *",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = dataNState.value,
                onValueChange = { 
                    dataNState.value = formatarData(it)
                },
                placeholder = { Text("DD/MM/AAAA", fontSize = 14.sp, color = Color.Gray) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Data",
                        tint = Color.Gray
                    )
                },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Dropdown",
                        tint = Color.Gray
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(25.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Gray,
                    unfocusedBorderColor = Color.Gray
                )
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Campo CPF
            Text(
                text = "CPF",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = cpfState.value,
                onValueChange = { 
                    cpfState.value = formatarCPF(it)
                },
                placeholder = { Text("000.000.000-00", fontSize = 14.sp, color = Color.Gray) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(25.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Gray,
                    unfocusedBorderColor = Color.Gray
                )
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Campo Telefone
            Text(
                text = "Telefone",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = telefoneState.value,
                onValueChange = { 
                    telefoneState.value = formatarTelefone(it)
                },
                placeholder = { Text("(00) 00000-0000", fontSize = 14.sp, color = Color.Gray) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = "Telefone",
                        tint = Color.Gray
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(25.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Gray,
                    unfocusedBorderColor = Color.Gray
                )
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Campo Email
            Text(
                text = "Email",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = emailState.value,
                onValueChange = { emailState.value = it },
                placeholder = { Text("exemplo@email.com", fontSize = 14.sp, color = Color.Gray) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = "Email",
                        tint = Color.Gray
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(25.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Gray,
                    unfocusedBorderColor = Color.Gray
                )
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Campo Cartão SUS/Convênio
            Text(
                text = "Cartão SUS/Convênio",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = CSCState.value,
                onValueChange = { CSCState.value = it },
                placeholder = { Text("Selecione", fontSize = 14.sp, color = Color.Gray) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(25.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Gray,
                    unfocusedBorderColor = Color.Gray
                )
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Upload de arquivos
            Text(
                text = "Upload de arquivos",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            
            // Área de upload
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .border(2.dp, Color.Gray, RoundedCornerShape(8.dp))
                    .clickable { pickImageLauncher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                if (imageUri != null) {
                    AsyncImage(
                        model = imageUri,
                        contentDescription = "Imagem Selecionada",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Upload",
                            modifier = Modifier.size(40.dp),
                            tint = Color(0xFF708EF1)
                        )
                        Text(
                            text = "Clique para fazer upload",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Campo CEP
            Text(
                text = "CEP",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = cepState.value,
                onValueChange = { 
                    cepState.value = formatarCEP(it)
                },
                placeholder = { Text("00000-000", fontSize = 14.sp, color = Color.Gray) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "CEP",
                        tint = Color.Gray
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(25.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Gray,
                    unfocusedBorderColor = Color.Gray
                )
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Campo Sexo
            Text(
                text = "Sexo*",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = selectedOptionpai.value,
                onValueChange = {},
                readOnly = true,
                placeholder = { Text("Selecione", fontSize = 14.sp, color = Color.Gray) },
                trailingIcon = {
                    IconButton(onClick = { expandedMenupai.value = !expandedMenupai.value }) {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Abrir menu",
                            tint = Color.Gray
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(25.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Gray,
                    unfocusedBorderColor = Color.Gray
                )
            )
            
            DropdownMenu(
                expanded = expandedMenupai.value,
                onDismissRequest = { expandedMenupai.value = false },
                modifier = Modifier.background(Color.White)
            ) {
                DropdownMenuItem(
                    text = { Text("Masculino") },
                    onClick = {
                        selectedOptionpai.value = "Masculino"
                        selectedOptionId.value = 1
                        expandedMenupai.value = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Feminino") },
                    onClick = {
                        selectedOptionpai.value = "Feminino"
                        selectedOptionId.value = 2
                        expandedMenupai.value = false
                    }
                )
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            
            // Checkbox
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = adicionalSegundo.value,
                    onCheckedChange = { adicionalSegundo.value = it },
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color(0xFF708EF1),
                        uncheckedColor = Color.Gray
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Adicionar segundo responsável",
                    fontSize = 12.sp,
                    color = Color.Black
                )
            }
            
            Spacer(modifier = Modifier.height(30.dp))
            
            // Botão PRÓXIMO
            Button(
                onClick = {
                    if (imageUri != null) {
                        GlobalScope.launch(Dispatchers.IO) {
                            try {
                                val urlRetornada = uploadImageToAzure(context, imageUri!!)
                                
                                withContext(Dispatchers.Main) {
                                    pictureState.value = urlRetornada
                                }
                                
                                val idUser = SessionManager.getUserId(context)
                                
                                val cliente = RegistroResp(
                                    id_responsavel = 0,
                                    nome = nomeState.value.text,
                                    data_nascimento = converterDataParaBanco(dataNState.value.text),
                                    cpf = cpfState.value.text,
                                    telefone = telefoneState.value.text,
                                    arquivo = urlRetornada,
                                    cartao_medico = CSCState.value.text,
                                    cep = cepState.value.text,
                                    idSexo = selectedOptionId.value ?: 0,
                                    id_user = idUser
                                )
                                
                                val response = clienteApi.cadastrarResponsavel(cliente).await()
                                Log.i("API_CADASTRO", "Resposta: $response")
                                
                                SessionManager.saveUserId(context = context, userId = response.data.id_user)
                                SessionManager.saveResponsavelId(context, response.data.id_responsavel)
                                
                                withContext(Dispatchers.Main) {
                                    navegacao?.navigate("cadastroB")
                                }
                                
                            } catch (e: Exception) {
                                Log.e("API_CADASTRO", "Erro: ${e.message}")
                            }
                        }
                    } else {
                        Log.e("UPLOAD", "Nenhuma imagem selecionada")
                    }
                },
                colors = ButtonDefaults.buttonColors(Color(0xFF708EF1)),
                shape = RoundedCornerShape(25.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(
                    text = "PRÓXIMO",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
            
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CadastroResponsavelNovoPreview() {
    CadastroResponsavelNovo(navegacao = null)
}

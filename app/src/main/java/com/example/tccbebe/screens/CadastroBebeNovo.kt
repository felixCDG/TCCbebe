package com.example.tccbebe.screens

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Brush
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
import com.example.tccbebe.model.RegistroBebe
import com.example.tccbebe.service.AuthenticatedConexao
import com.example.tccbebe.utils.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.await

@Composable
fun CadastroBebeNovo(navegacao: NavHostController?) {
    
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
    
    fun formatarPeso(textFieldValue: TextFieldValue): TextFieldValue {
        val texto = textFieldValue.text.filter { it.isDigit() || it == '.' }
        return textFieldValue.copy(
            text = texto,
            selection = TextRange(texto.length)
        )
    }
    
    fun formatarAltura(textFieldValue: TextFieldValue): TextFieldValue {
        val digitos = textFieldValue.text.filter { it.isDigit() }
        return textFieldValue.copy(
            text = digitos,
            selection = TextRange(digitos.length)
        )
    }
    
    fun converterDataParaBanco(dataFormatada: String): String {
        if (dataFormatada.length == 10) {
            val partes = dataFormatada.split("/")
            if (partes.size == 3) {
                return "${partes[2]}-${partes[1]}-${partes[0]}"
            }
        }
        return dataFormatada
    }

    val context = LocalContext.current
    
    // Estados
    var nomeState = remember { mutableStateOf(TextFieldValue("")) }
    var dataNState = remember { mutableStateOf(TextFieldValue("")) }
    var pesoState = remember { mutableStateOf(TextFieldValue("")) }
    var alturaState = remember { mutableStateOf(TextFieldValue("")) }
    var cpfState = remember { mutableStateOf(TextFieldValue("")) }
    var certidaoState = remember { mutableStateOf(TextFieldValue("")) }
    var cartaoMedicoState = remember { mutableStateOf(TextFieldValue("")) }
    
    // Estados para dropdowns
    val expandedSexo = remember { mutableStateOf(false) }
    val selectedSexo = remember { mutableStateOf("") }
    val selectedSexoId = remember { mutableStateOf<Int?>(null) }
    
    val expandedSangue = remember { mutableStateOf(false) }
    val selectedSangue = remember { mutableStateOf("") }
    val selectedSangueId = remember { mutableStateOf<Int?>(null) }
    
    // Estados para imagem
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val pictureState = remember { mutableStateOf("") }
    
    val pickImageLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        imageUri = uri
    }
    
    val clienteApi = AuthenticatedConexao(context).getRegistroBebeService()

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
                        text = "Cadastro do\nBebê",
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
                            navegacao?.popBackStack()
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
                    imageVector = Icons.Default.Face,
                    contentDescription = "Bebê",
                    modifier = Modifier.size(24.dp),
                    tint = Color.Black
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Bebê",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2C2C2C)
                )
            }
            
            // Linha azul embaixo do título
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(Color(0xFF708EF1))
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Nome completo
            Text(
                text = "Nome completo *",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF666666),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = nomeState.value,
                onValueChange = { 
                    nomeState.value = formatarNome(it)
                },
                placeholder = { Text("Digite o nome completo", fontSize = 14.sp, color = Color.Gray) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Nome",
                        tint = Color.Gray
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF708EF1),
                    unfocusedBorderColor = Color(0xFFE0E0E0)
                )
            )
            
            Spacer(modifier = Modifier.height(20.dp))
            
            // Data de nascimento
            Text(
                text = "Data de nascimento *",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF666666),
                modifier = Modifier.fillMaxWidth()
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
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF708EF1),
                    unfocusedBorderColor = Color(0xFFE0E0E0)
                )
            )
            
            Spacer(modifier = Modifier.height(20.dp))
            
            // Sexo
            Text(
                text = "Sexo *",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF666666),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = selectedSexo.value,
                onValueChange = {},
                readOnly = true,
                placeholder = { Text("Selecione o sexo", fontSize = 14.sp, color = Color.Gray) },
                trailingIcon = {
                    IconButton(onClick = { expandedSexo.value = !expandedSexo.value }) {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Abrir menu",
                            tint = Color.Gray
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expandedSexo.value = !expandedSexo.value },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF708EF1),
                    unfocusedBorderColor = Color(0xFFE0E0E0)
                )
            )
            DropdownMenu(
                expanded = expandedSexo.value,
                onDismissRequest = { expandedSexo.value = false },
                modifier = Modifier.background(Color.White)
            ) {
                DropdownMenuItem(
                    text = { Text("Masculino") },
                    onClick = {
                        selectedSexo.value = "Masculino"
                        selectedSexoId.value = 1
                        expandedSexo.value = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Feminino") },
                    onClick = {
                        selectedSexo.value = "Feminino"
                        selectedSexoId.value = 2
                        expandedSexo.value = false
                    }
                )
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            
            // Peso e Altura
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Peso (kg) *",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF666666)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = pesoState.value,
                        onValueChange = { 
                            pesoState.value = formatarPeso(it)
                        },
                        placeholder = { Text("Ex: 3.2", fontSize = 14.sp, color = Color.Gray) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF708EF1),
                            unfocusedBorderColor = Color(0xFFE0E0E0)
                        )
                    )
                }
                
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Altura (cm) *",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF666666)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = alturaState.value,
                        onValueChange = { 
                            alturaState.value = formatarAltura(it)
                        },
                        placeholder = { Text("Ex: 50", fontSize = 14.sp, color = Color.Gray) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF708EF1),
                            unfocusedBorderColor = Color(0xFFE0E0E0)
                        )
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            
            // Tipo sanguíneo
            Text(
                text = "Tipo sanguíneo",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF666666),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = selectedSangue.value,
                onValueChange = {},
                readOnly = true,
                placeholder = { Text("Selecione o tipo sanguíneo", fontSize = 14.sp, color = Color.Gray) },
                trailingIcon = {
                    IconButton(onClick = { expandedSangue.value = !expandedSangue.value }) {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Abrir menu",
                            tint = Color.Gray
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expandedSangue.value = !expandedSangue.value },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF708EF1),
                    unfocusedBorderColor = Color(0xFFE0E0E0)
                )
            )
            DropdownMenu(
                expanded = expandedSangue.value,
                onDismissRequest = { expandedSangue.value = false },
                modifier = Modifier.background(Color.White)
            ) {
                val tiposSanguineos = listOf(
                    "A+" to 1, "A-" to 2, "B+" to 3, "B-" to 4,
                    "AB+" to 5, "AB-" to 6, "O+" to 7, "O-" to 8
                )
                tiposSanguineos.forEach { (tipo, id) ->
                    DropdownMenuItem(
                        text = { Text(tipo) },
                        onClick = {
                            selectedSangue.value = tipo
                            selectedSangueId.value = id
                            expandedSangue.value = false
                        }
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            
            // Certidão de nascimento
            Text(
                text = "Certidão de nascimento *",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF666666),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = certidaoState.value,
                onValueChange = { 
                    certidaoState.value = it
                },
                placeholder = { Text("Número da certidão", fontSize = 14.sp, color = Color.Gray) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF708EF1),
                    unfocusedBorderColor = Color(0xFFE0E0E0)
                )
            )
            
            Spacer(modifier = Modifier.height(20.dp))
            
            // CPF
            Text(
                text = "CPF",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF666666),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = cpfState.value,
                onValueChange = { 
                    cpfState.value = formatarCPF(it)
                },
                placeholder = { Text("000.000.000-00", fontSize = 14.sp, color = Color.Gray) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF708EF1),
                    unfocusedBorderColor = Color(0xFFE0E0E0)
                )
            )
            
            Spacer(modifier = Modifier.height(20.dp))
            
            // Cartão médico
            Text(
                text = "Cartão SUS/Convênio *",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF666666),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = cartaoMedicoState.value,
                onValueChange = { 
                    cartaoMedicoState.value = it
                },
                placeholder = { Text("Número do cartão", fontSize = 14.sp, color = Color.Gray) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF708EF1),
                    unfocusedBorderColor = Color(0xFFE0E0E0)
                )
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Upload de imagem
            Text(
                text = "Foto da certidão",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF666666),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = pictureState.value,
                onValueChange = { },
                placeholder = { Text("Selecionar imagem", fontSize = 14.sp, color = Color.Gray) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = "Imagem",
                        tint = Color.Gray
                    )
                },
                enabled = false,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        pickImageLauncher.launch("image/*")
                    },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    disabledBorderColor = Color(0xFFE0E0E0)
                )
            )
            
            imageUri?.let { uri ->
                Spacer(modifier = Modifier.height(12.dp))
                AsyncImage(
                    model = uri,
                    contentDescription = "Imagem Selecionada",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .border(2.dp, Color(0xFFE0E0E0), shape = RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Botão de cadastrar
            Button(
                onClick = {
                    GlobalScope.launch(Dispatchers.IO) {
                        try {
                            val urlRetornada = if (imageUri != null) {
                                uploadImageToAzure(context, imageUri!!)
                            } else {
                                ""
                            }
                            
                            withContext(Dispatchers.Main) {
                                pictureState.value = urlRetornada
                            }
                            
                            val cliente = RegistroBebe(
                                id_bebe = 0,
                                nome = nomeState.value.text,
                                data_nascimento = converterDataParaBanco(dataNState.value.text),
                                peso = pesoState.value.text,
                                altura = alturaState.value.text,
                                certidao_nascimento = certidaoState.value.text,
                                cpf = cpfState.value.text,
                                imagem_certidao = urlRetornada,
                                cartao_medico = cartaoMedicoState.value.text,
                                idSexo = selectedSexoId.value ?: 0,
                                idSangue = selectedSangueId.value ?: 0
                            )
                            
                            // Log detalhado dos dados antes de enviar
                            Log.i("CADASTRO_BEBE", "=== DADOS DO BEBÊ PARA ENVIO ===")
                            Log.i("CADASTRO_BEBE", "Nome: '${cliente.nome}'")
                            Log.i("CADASTRO_BEBE", "Data nascimento original: '${dataNState.value.text}'")
                            Log.i("CADASTRO_BEBE", "Data nascimento convertida: '${cliente.data_nascimento}'")
                            Log.i("CADASTRO_BEBE", "Peso: '${cliente.peso}'")
                            Log.i("CADASTRO_BEBE", "Altura: '${cliente.altura}'")
                            Log.i("CADASTRO_BEBE", "Certidão: '${cliente.certidao_nascimento}'")
                            Log.i("CADASTRO_BEBE", "CPF: '${cliente.cpf}'")
                            Log.i("CADASTRO_BEBE", "Cartão médico: '${cliente.cartao_medico}'")
                            Log.i("CADASTRO_BEBE", "ID Sexo: ${cliente.idSexo}")
                            Log.i("CADASTRO_BEBE", "ID Sangue: ${cliente.idSangue}")
                            Log.i("CADASTRO_BEBE", "URL Imagem: '${cliente.imagem_certidao}'")
                            Log.i("CADASTRO_BEBE", "Objeto completo: $cliente")
                            Log.i("CADASTRO_BEBE", "=== ENVIANDO PARA API ===")
                            
                            val response = clienteApi.cadastroBabe(cliente).await()
                            Log.i("API_CADASTRO", "Resposta da API: $response")
                            Log.i("CADASTRO_BEBE", "=== RESPOSTA RECEBIDA COM SUCESSO ===")
                            
                            withContext(Dispatchers.Main) {
                                navegacao?.navigate("home")
                            }
                            
                        } catch (e: Exception) {
                            Log.e("CADASTRO_BEBE", "=== ERRO NO CADASTRO ===")
                            Log.e("CADASTRO_BEBE", "Tipo do erro: ${e.javaClass.simpleName}")
                            Log.e("CADASTRO_BEBE", "Mensagem: ${e.message}")
                            Log.e("CADASTRO_BEBE", "Stack trace: ${e.stackTrace.contentToString()}")
                            Log.e("API_CADASTRO", "Erro completo: $e")
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF708EF1)
                )
            ) {
                Text(
                    text = "CADASTRAR BEBÊ",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CadastroBebeNovoPreview() {
    CadastroBebeNovo(navegacao = null)
}

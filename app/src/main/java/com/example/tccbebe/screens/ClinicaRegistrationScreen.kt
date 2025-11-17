package br.senai.sp.jandira.cadastroclinica.screens

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.tccbebe.R
import br.senai.sp.jandira.cadastroclinica.components.*
import br.senai.sp.jandira.foodrecipe.service.AzureUploadService.uploadImageToAzure
import coil.compose.AsyncImage
import com.example.tccbebe.model.CadastroClinicaData
import com.example.tccbebe.service.AuthenticatedConexao
import com.example.tccbebe.service.Conexao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.await
import kotlin.compareTo


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClinicaRegistrationScreen(
    onNavigateBack: () -> Unit = {},
    onSave: () -> Unit = {},
    navegacao: NavHostController?
) {

    fun cursorPosicaoParaDigitosAntes(texto: String, cursorPos: Int): Int {
        // conta quantos dígitos existem antes da posição do cursor no texto recebido
        return texto.take(cursorPos.coerceIn(0, texto.length)).count { it.isDigit() }
    }

    fun posicaoCursorNoFormatadoPorDigitos(formatted: String, digitosAntes: Int): Int {
        if (digitosAntes <= 0) return 0
        var count = 0
        for (i in formatted.indices) {
            if (formatted[i].isDigit()) {
                count++
                if (count == digitosAntes) {
                    // coloca o cursor logo após esse dígito
                    return i + 1
                }
            }
        }
        // se pediu mais dígitos do que existem, posiciona no fim
        return formatted.length
    }

    fun formatarTelefone(textFieldValue: TextFieldValue): TextFieldValue {
        val raw = textFieldValue.text
        val cursorBefore = textFieldValue.selection.end
        val digitosAntes = cursorPosicaoParaDigitosAntes(raw, cursorBefore)

        val digitos = raw.filter { it.isDigit() }
        val textoFormatado = when {
            digitos.length <= 2 -> if (digitos.isNotEmpty()) "($digitos" else ""
            digitos.length <= 7 -> "(${digitos.substring(0, 2)}) ${digitos.substring(2)}"
            digitos.length <= 11 -> "(${digitos.substring(0, 2)}) ${digitos.substring(2, 7)}-${digitos.substring(7)}"
            else -> "(${digitos.substring(0, 2)}) ${digitos.substring(2, 7)}-${digitos.substring(7, 11)}"
        }

        val newCursor = posicaoCursorNoFormatadoPorDigitos(textoFormatado, digitosAntes)
        return textFieldValue.copy(
            text = textoFormatado,
            selection = TextRange(newCursor)
        )
    }

    fun formatarCNPJ(textFieldValue: TextFieldValue): TextFieldValue {
        val raw = textFieldValue.text
        val cursorBefore = textFieldValue.selection.end
        val digitosAntes = cursorPosicaoParaDigitosAntes(raw, cursorBefore)

        val digitos = raw.filter { it.isDigit() }
        val textoFormatado = when {
            digitos.length <= 2 -> digitos
            digitos.length <= 5 -> "${digitos.substring(0, 2)}.${digitos.substring(2)}"
            digitos.length <= 8 -> "${digitos.substring(0, 2)}.${digitos.substring(2, 5)}.${digitos.substring(5)}"
            digitos.length <= 12 -> "${digitos.substring(0, 2)}.${digitos.substring(2, 5)}.${digitos.substring(5, 8)}/${digitos.substring(8)}"
            digitos.length <= 14 -> "${digitos.substring(0, 2)}.${digitos.substring(2, 5)}.${digitos.substring(5, 8)}/${digitos.substring(8, 12)}-${digitos.substring(12)}"
            else -> "${digitos.substring(0, 2)}.${digitos.substring(2, 5)}.${digitos.substring(5, 8)}/${digitos.substring(8, 12)}-${digitos.substring(12, 14)}"
        }

        val newCursor = posicaoCursorNoFormatadoPorDigitos(textoFormatado, digitosAntes)
        return textFieldValue.copy(
            text = textoFormatado,
            selection = TextRange(newCursor)
        )
    }

    var imageUri by remember { mutableStateOf<Uri?>(null) }

    // 2) Estado para armazenar a URL retornada pelo Azure
    var imageUrl by remember { mutableStateOf<String?>(null) }

    // 3) Launcher para pegar o arquivo via Galeria
    val pickImageLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            imageUri = uri
        }

    val pictureState = remember { mutableStateOf("") }

    var nomeState = remember {
        mutableStateOf("")
    }
    var emailState = remember {
        mutableStateOf("")
    }
    var CNPJState = remember {
        mutableStateOf(TextFieldValue(""))
    }
    var telefoneState = remember { mutableStateOf(TextFieldValue("")) }
    var BairroState = remember {
        mutableStateOf("")
    }
    var RUaState = remember {
        mutableStateOf("")
    }
    var NumeroState = remember {
        mutableStateOf("")
    }
    var Cidade = remember {
        mutableStateOf("")
    }
    val context = LocalContext.current

    val clienteApi = AuthenticatedConexao(context).getCadstroClinica()

    
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Imagem de fundo
        Image(
            painter = painterResource(id = R.drawable.plano),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Overlay branco com bordas arredondadas superiores
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 200.dp)
                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .background(Color.White)
        )
        
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Header
            RegistrationHeader(
                title = "Cadastro da Clínica",
                subtitle = "Clínica",
                icon = Icons.Default.Person
            )
            
            // Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                CustomTextField(
                    value = nomeState.value,
                    onValueChange = { nomeState.value= it },
                    label = "Nome",
                    placeholder = "Digite o nome completo",
                    isRequired = true,
                    modifier = Modifier.fillMaxWidth()
                )

                CustomTextField(
                    value = emailState.value,
                    onValueChange = { emailState.value= it},
                    label = "E-mail",
                    placeholder = "exemplo@email.com",
                    leadingIcon = Icons.Default.Email,
                    keyboardType = KeyboardType.Email,
                    modifier = Modifier.fillMaxWidth()
                )

                CustomTextField(
                    value = CNPJState.value.text,
                    onValueChange = { novoValor ->
                        val textFieldValue = TextFieldValue(novoValor)
                        CNPJState.value = formatarCNPJ(textFieldValue)
                    },
                    label = "CNPJ",
                    placeholder = "000.000.000-00",
                    keyboardType = KeyboardType.Number,
                    isRequired = true,
                    modifier = Modifier.fillMaxWidth()
                )

                CustomTextField(
                    value = telefoneState.value.text,
                    onValueChange = { novoValor ->
                        val textFieldValue = TextFieldValue(novoValor)
                        telefoneState.value = formatarTelefone(textFieldValue) },
                    label = "TELEFONE",
                    placeholder = "(00) 00000-0000",
                    leadingIcon = Icons.Default.Phone,
                    keyboardType = KeyboardType.Phone,
                    isRequired = true,
                    modifier = Modifier.fillMaxWidth()
                )

                // Address section
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CustomTextField(
                        value = BairroState.value,
                        onValueChange = { BairroState.value= it},
                        label = "BAIRRO",
                        placeholder = "",
                        modifier = Modifier.weight(1f)
                    )

                    CustomTextField(
                        value = RUaState.value,
                        onValueChange = {RUaState.value = it},
                        label = "RUA",
                        placeholder = "",
                        modifier = Modifier.weight(1f)
                    )

                    CustomTextField(
                        value = NumeroState.value,
                        onValueChange = { NumeroState.value= it},
                        label = "Nº",
                        placeholder = "",
                        keyboardType = KeyboardType.Number,
                        modifier = Modifier.weight(0.5f)
                    )
                }

                CustomTextField(
                    value = Cidade.value,
                    onValueChange = { Cidade.value= it},
                    label = "CIDADE",
                    placeholder = "",
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(7.dp))

                // Botão de cadastrar
                Button(
                    onClick = {
                        GlobalScope.launch(Dispatchers.IO) {
                        try {

                            val cliente = CadastroClinicaData(
                                id_clina = 0,
                                nome = nomeState.value,
                                cnpj = CNPJState.value.text,
                                telefone = telefoneState.value.text,
                                email = emailState.value,
                                cidade = Cidade.value,
                                rua = RUaState.value,
                                bairro = BairroState.value,
                                numero = NumeroState.value,
                                id_user = 5
                            )

                            // Log detalhado dos dados antes de enviar
                            Log.i("CADSTRO_CLINICA", "=== DADOS DA CLINICA PARA ENVIO ===")
                            Log.i("CADSTRO_CLINICA", "Nome: '${cliente.nome}'")
                            Log.i("CADSTRO_CLINICA", "cnpj: '${CNPJState.value}'")
                            Log.i("CADSTRO_CLINICA", "telefone: '${cliente.telefone}'")
                            Log.i("CADSTRO_CLINICA", "email: '${cliente.email}'")
                            Log.i("CADSTRO_CLINICA", "cidade: '${cliente.cidade}'")
                            Log.i("CADSTRO_CLINICA", "rua: '${cliente.rua}'")
                            Log.i("CADSTRO_CLINICA", "Bairro: '${cliente.bairro}'")
                            Log.i("CADSTRO_CLINICA", "numero: ${cliente.numero}")
                            Log.i("CADSTRO_CLINICA", "ID USer: ${cliente.id_user}")
                            Log.i("CADSTRO_CLINICA", "Objeto completo: $cliente")
                            Log.i("CADSTRO_CLINICA", "=== ENVIANDO PARA API ===")

                            val response = clienteApi.cadastrarUsuario(cliente).await()
                            Log.i("API_CADASTRO", "Resposta da API: $response")
                            Log.i("CADSTRO_CLINICA", "=== RESPOSTA RECEBIDA COM SUCESSO ===")

                            withContext(Dispatchers.Main) {
                                navegacao?.navigate("homeC")
                            }

                        } catch (e: Exception) {
                            Log.e("CADSTRO_CLINICA", "=== ERRO NO CADASTRO ===")
                            Log.e("CADSTRO_CLINICA", "Tipo do erro: ${e.javaClass.simpleName}")
                            Log.e("CADSTRO_CLINICA", "Mensagem: ${e.message}")
                            Log.e("CADSTRO_CLINICA", "Stack trace: ${e.stackTrace.contentToString()}")
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
                        text = "CADASTRAR CLINICA",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ClinicaRegistrationScreenPreview() {
    ClinicaRegistrationScreen(navegacao = null)
}
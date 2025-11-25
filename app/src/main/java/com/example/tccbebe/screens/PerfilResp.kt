package com.example.tccbebe.screens

import android.R.id.input
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import coil.compose.AsyncImage
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import br.senai.sp.jandira.foodrecipe.service.AzureUploadService
import br.senai.sp.jandira.foodrecipe.service.AzureUploadService.uploadImageToAzure
import com.example.tccbebe.R
import com.example.tccbebe.model.CadastroUser
import com.example.tccbebe.model.RegistroResp
import com.example.tccbebe.model.ResponseLoginUser
import com.example.tccbebe.model.ResponsePerfilResp
import com.example.tccbebe.model.ResponseRegistroResp
import com.example.tccbebe.service.Conexao
import com.example.tccbebe.utils.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.await

@Composable
fun PerfilResp(navegacao: NavHostController?) {


    val context = LocalContext.current

// Estados dos campos
    var nomeState = remember { mutableStateOf("") }
    var dataNState = remember { mutableStateOf("") }
    var cpfState = remember { mutableStateOf("") }
    var telefoneState = remember { mutableStateOf("") }
    var cepState = remember { mutableStateOf("") }
    var emailState = remember { mutableStateOf("") }

// Pegar o ID salvo localmente
    LaunchedEffect(Unit) {
        // ID fixo que você sabe que existe no banco
        val idResponsavel = SessionManager.getResponsavelId(context)

        val call = Conexao()
            .getRegistroRspService()
            .getResponsavelById(idResponsavel)

        Log.i("API_REQUEST", "GET URL: ${call.request().url}")
        Log.i("API_REQUEST", "Método: ${call.request().method}")
        Log.i("API_REQUEST", "Cabeçalhos: ${call.request().headers}")

        call.enqueue(object : retrofit2.Callback<ResponsePerfilResp> {
            override fun onResponse(
                call: retrofit2.Call<ResponsePerfilResp>,
                response: retrofit2.Response<ResponsePerfilResp>
            ) {
                if (response.isSuccessful) {
                    val respList = response.body()?.responsavel
                    if (!respList.isNullOrEmpty()) {
                        val respData = respList[0] // Pega o primeiro elemento
                        nomeState.value = respData.nome
                        dataNState.value = respData.data_nascimento
                        cpfState.value = respData.cpf
                        telefoneState.value = respData.telefone
                        cepState.value = respData.cep
                        emailState.value = respData.usuario?.firstOrNull()?.email ?: "Não disponível"

                    } else {
                        nomeState.value = "Não encontrado"
                        dataNState.value = "-"
                        cpfState.value = "-"
                        telefoneState.value = "-"
                        cepState.value = "-"
                    }
                } else {
                    Log.e("PerfilResp", "Erro HTTP: ${response.code()}")
                }
            }

            override fun onFailure(call: retrofit2.Call<ResponsePerfilResp>, t: Throwable) {
                Log.e("PerfilResp", "Erro ao buscar perfil: ${t.message}")
            }
        })
    }


    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFFAEDCFF))) {

        Image(
            painter = painterResource(id = R.drawable.fundoback2),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column (
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Bottom
        ) {
            Column (
                modifier = Modifier
                    .padding(13.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ){
                Row (
                    modifier = Modifier
                        .padding(bottom = 58.dp)
                        .fillMaxWidth(),
                ){
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "",
                        modifier = Modifier
                            .size(40.dp)
                    )
                    Spacer(modifier = Modifier .width(5.dp))
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "",
                        modifier = Modifier
                            .size(40.dp)
                    )
                    Spacer(modifier = Modifier .width(5.dp))
                    Text(
                        modifier = Modifier
                            .padding(top = 7.dp),
                        text = "Perfil do Responsavel",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 26.sp
                    )
                }
            }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(715.dp),
                shape = RoundedCornerShape(
                    topStart = 33.dp,
                    topEnd =  33.dp
                ),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFFFFFFF)
                )
            ) {
                // Novo layout interno do Card: topo em gradiente, título, avatar circular com botão de edição, campos e botão
                Box(modifier = Modifier.fillMaxSize()) {
                    // Top gradient header
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(Color(0xFF2C91DE), Color(0xFFAEDCFF))
                                )
                            )
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 20.dp, vertical = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(24.dp))

                        // Title (nome ou placeholder)
                        Text(
                            text = if (nomeState.value.isBlank()) "NOME DO RESPONSAVEL" else nomeState.value,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = 26.sp
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Avatar com botão de edição sobreposto
                        Box(modifier = Modifier.size(140.dp), contentAlignment = Alignment.BottomEnd) {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = null,
                                tint = Color.LightGray,
                                modifier = Modifier
                                    .size(140.dp)
                                    .clip(CircleShape)
                            )

                            IconButton(
                                onClick = { /* ação de editar avatar (atualmente vazio) */ },
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(Color.White, shape = CircleShape)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Create,
                                    contentDescription = "Editar",
                                    tint = Color(0xFF2C91DE)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = dataNState.value,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .verticalScroll(rememberScrollState())
                        ) {
                            // Nome
                            Text(
                                text = "Nome",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 18.sp,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            OutlinedTextField(
                                value = nomeState.value,
                                onValueChange = {},
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(
                                        width = 2.dp,
                                        color = Color(0xFF2C91DE),
                                        shape = RoundedCornerShape(30.dp)
                                    ),
                                shape = RoundedCornerShape(30.dp),
                                placeholder = { Text("Nome") },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = Color(0x65AEDCFF),
                                    unfocusedContainerColor = Color(0x65AEDCFF)
                                ),
                                singleLine = true
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            // CPF
                            Text(
                                text = "CPF",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 18.sp,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            OutlinedTextField(
                                value = cpfState.value,
                                onValueChange = { cpfState.value = it },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(
                                        width = 2.dp,
                                        color = Color(0xFF2C91DE),
                                        shape = RoundedCornerShape(30.dp)
                                    ),
                                shape = RoundedCornerShape(30.dp),
                                placeholder = { Text("000.000.000-00") },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = Color(0x65AEDCFF),
                                    unfocusedContainerColor = Color(0x65AEDCFF)
                                ),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            // Telefone
                            Text(
                                text = "TELEFONE",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 18.sp,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            OutlinedTextField(
                                value = telefoneState.value,
                                onValueChange = { telefoneState.value = it },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(
                                        width = 2.dp,
                                        color = Color(0xFF2C91DE),
                                        shape = RoundedCornerShape(30.dp)
                                    ),
                                shape = RoundedCornerShape(30.dp),
                                placeholder = { Text("(00) 00000-0000") },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = Color(0x65AEDCFF),
                                    unfocusedContainerColor = Color(0x65AEDCFF)
                                ),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            // E-mail
                            Text(
                                text = "E-MAIL",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 18.sp,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            OutlinedTextField(
                                value = emailState.value,
                                onValueChange = {},
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(
                                        width = 2.dp,
                                        color = Color(0xFF2C91DE),
                                        shape = RoundedCornerShape(30.dp)
                                    ),
                                shape = RoundedCornerShape(30.dp),
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Email,
                                        contentDescription = "email"
                                    )
                                },
                                placeholder = { Text("email@exemplo.com") },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = Color(0x65AEDCFF),
                                    unfocusedContainerColor = Color(0x65AEDCFF)
                                ),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            // CEP
                            Text(
                                text = "CEP",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 18.sp,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            OutlinedTextField(
                                value = cepState.value,
                                onValueChange = { cepState.value = it },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(
                                        width = 2.dp,
                                        color = Color(0xFF2C91DE),
                                        shape = RoundedCornerShape(30.dp)
                                    ),
                                shape = RoundedCornerShape(30.dp),
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.LocationOn,
                                        contentDescription = "CEP"
                                    )
                                },
                                placeholder = { Text("00000-000") },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = Color(0x65AEDCFF),
                                    unfocusedContainerColor = Color(0x65AEDCFF)
                                ),
                                singleLine = true
                            )

                            Spacer(modifier = Modifier.height(18.dp))

                            // Botão centralizado
                            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                                Button(
                                    onClick = { /* ação do botão */ },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFAEDCFF)),
                                    shape = RoundedCornerShape(30.dp),
                                    modifier = Modifier
                                        .padding(vertical = 16.dp)
                                        .width(270.dp)
                                        .border(
                                            width = 2.dp,
                                            color = Color(0xFF2C91DE),
                                            shape = RoundedCornerShape(38.dp)
                                        ),
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.Center,
                                        verticalAlignment = Alignment.CenterVertically
                                    ){
                                        Text(
                                            text = "PERFIL DO BEBE",
                                            color = Color.Black,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Icon(
                                            imageVector = Icons.Default.ArrowForward,
                                            contentDescription = "",
                                            tint = Color(0xFF000000),
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                        }

                    }

                }
            }
        }
    }



}

@Preview
@Composable
private fun PerfilRespPreview() {
    PerfilResp(navegacao = null)
}


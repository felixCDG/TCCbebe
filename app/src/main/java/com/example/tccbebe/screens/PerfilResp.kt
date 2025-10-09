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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowForward
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
import androidx.compose.ui.graphics.Color
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
                Column (
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxSize(),
                ) {
                    Column (
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Text(
                            modifier = Modifier
                                .padding(top = 12.dp),
                            text = nomeState.value,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = 28.sp
                        )
                        IconButton() { }
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "",
                            modifier = Modifier
                                .size(125.dp)
                        )
                        Text(
                            text = dataNState.value,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp
                        )
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()),
                    ) {
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            text = "Nome",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 20.sp,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(7.dp))
                        OutlinedTextField(
                            value = nomeState.value,
                            onValueChange = {},
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    width = 2.dp, // 👈 tamanho da borda
                                    color = Color(0xFF2C91DE),
                                    shape = RoundedCornerShape(30.dp)
                                ),
                            shape = RoundedCornerShape(30.dp),
                            placeholder = {
                                Text("000.000.000-00")
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color(0x65AEDCFF),
                                unfocusedContainerColor = Color(0x65AEDCFF)
                            ),
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            text = "CPF",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 20.sp,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(7.dp))
                        OutlinedTextField(
                            value = cpfState.value,
                            onValueChange = {
                                cpfState.value = it
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    width = 2.dp, // 👈 tamanho da borda
                                    color = Color(0xFF2C91DE),
                                    shape = RoundedCornerShape(30.dp)
                                ),
                            shape = RoundedCornerShape(30.dp),
                            placeholder = {
                                Text("000.000.000-00")
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color(0x65AEDCFF),
                                unfocusedContainerColor = Color(0x65AEDCFF)
                            ),
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            text = "TELEFONE",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 20.sp,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(7.dp))
                        OutlinedTextField(
                            value = telefoneState.value,
                            onValueChange = {
                                telefoneState.value = it
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    width = 2.dp, // 👈 tamanho da borda
                                    color = Color(0xFF2C91DE),
                                    shape = RoundedCornerShape(30.dp)
                                ),
                            shape = RoundedCornerShape(30.dp),
                            placeholder = {
                                Text("(00) 00000-0000")
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color(0x65AEDCFF),
                                unfocusedContainerColor = Color(0x65AEDCFF)
                            ),
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            text = "CEP",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 20.sp,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(7.dp))
                        OutlinedTextField(
                            value = cepState.value,
                            onValueChange = {
                                cepState.value = it
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    width = 2.dp, // 👈 tamanho da borda
                                    color = Color(0xFF2C91DE),
                                    shape = RoundedCornerShape(30.dp)
                                ),
                            shape = RoundedCornerShape(30.dp),
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = "Telefone"
                                )
                            },
                            placeholder = {
                                Text("cep")
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color(0x65AEDCFF),
                                unfocusedContainerColor = Color(0x65AEDCFF)
                            ),
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            text = "Email",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 20.sp,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(7.dp))
                        OutlinedTextField(
                            value = emailState.value,
                            onValueChange = {},
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    width = 2.dp, // 👈 tamanho da borda
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
                            placeholder = {
                                Text("email")
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color(0x65AEDCFF),
                                unfocusedContainerColor = Color(0x65AEDCFF)
                            ),
                        )
                        Spacer(modifier = Modifier.height(34.dp))
                        Column(
                            modifier = Modifier
                                .fillMaxSize(),
                            horizontalAlignment = Alignment.End
                        ) {

                            val context = LocalContext.current

                            Button(
                                onClick = {

                                },
                                colors = ButtonDefaults.buttonColors(Color(0xFFAEDCFF)),
                                shape = RoundedCornerShape(30.dp),
                                modifier = Modifier
                                    .padding(bottom = 70.dp)
                                    .width(270.dp)
                                    .border(
                                        width = 2.dp, // 👈 tamanho da borda
                                        color = Color(0xFF2C91DE),
                                        shape = RoundedCornerShape(38.dp)
                                    ),
                            ) {
                                Row (
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End
                                ){
                                    Text(
                                        text = "PERFIL DO BEBE",
                                        color = Color.Black,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp
                                    )
                                    Spacer(modifier = Modifier .width(6.dp))
                                    Icon(
                                        imageVector = Icons.Default.ArrowForward,
                                        contentDescription = "",
                                        tint = Color(0xFF000000),
                                        modifier = Modifier
                                            .size(20.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(3.dp))

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


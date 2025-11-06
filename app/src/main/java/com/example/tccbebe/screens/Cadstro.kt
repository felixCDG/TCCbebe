package com.example.tccbebe.screens

import android.R.attr.id
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import android.util.Log
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.tccbebe.R
import com.example.tccbebe.model.CadastroUser
import com.example.tccbebe.service.Conexao
import com.example.tccbebe.utils.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.await
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Cadastroscreen(navegacao: NavHostController?) {


    var expandedTipoId by remember { mutableStateOf(false) }
    var selectTipoId by remember { mutableStateOf("") }
    val selectedTipoIddrop = remember { mutableStateOf(0) }

    var nameState = remember {
        mutableStateOf("")
    }
    var emailState = remember {
        mutableStateOf("")
    }
    var senhaState = remember {
        mutableStateOf("")
    }
    var CsenhaState = remember {
        mutableStateOf("")
    }


    var mensagem by remember { mutableStateOf("") }
    var isErro by remember { mutableStateOf(false) }

    val clienteApi = Conexao().getCadastroService()

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFFAEDCFF))) {

        Column (
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Image(
                painter = painterResource(R.drawable.logocerta),
                contentDescription = "",
                modifier = Modifier
                    .size(200.dp),

                )
        }
        Column (
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Bottom
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(715.dp),
                shape = CurvedTopShape(),// aplica o shape
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFFFFFFF)
                )
            ) {
                Column (
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxSize(),
                ){
                    Row (modifier = Modifier .fillMaxWidth() .padding(20.dp) .padding(top = 50.dp)){
                        IconButton(
                            onClick = {
                                navegacao?.navigate("login")
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = ""
                            )
                        }
                        Spacer(modifier = Modifier ,)
                        Text(
                            modifier = Modifier
                                .padding(top = 12.dp),
                            text = "Volte para o login",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }
                    Text(
                        text = "Cadastre-se",
                        color = Color.Black,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 44.sp
                    )
                    Spacer(modifier = Modifier .height(24.dp))
                    OutlinedTextField(
                        value = nameState.value,
                        onValueChange = {
                            nameState.value = it
                        },
                        modifier = Modifier
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(30.dp),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = "Nome"
                            )
                        },
                        label = {
                            Text("Nome")
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF2C91DE),
                            unfocusedBorderColor = Color(0xFF2C91DE),
                            focusedContainerColor = Color(0x65AEDCFF),
                            unfocusedContainerColor = Color(0x65AEDCFF)
                        ),
                    )
                    Spacer(modifier = Modifier .height(24.dp))
                    OutlinedTextField(
                        value = emailState.value,
                        onValueChange = {
                            emailState.value = it
                        },
                        modifier = Modifier
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(30.dp),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = "Email"
                            )
                        },
                        label = {
                            Text("Email")
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF2C91DE),
                            unfocusedBorderColor = Color(0xFF2C91DE),
                            focusedContainerColor = Color(0x65AEDCFF),
                            unfocusedContainerColor = Color(0x65AEDCFF)
                        ),
                    )
                    Spacer(modifier = Modifier .height(24.dp))
                    OutlinedTextField(
                        value = senhaState.value,
                        onValueChange = {
                            senhaState.value = it
                        },
                        modifier = Modifier
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(30.dp),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Senha"
                            )
                        },
                        label = {
                            Text("Senha")
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF2C91DE),
                            unfocusedBorderColor = Color(0xFF2C91DE),
                            focusedContainerColor = Color(0x65AEDCFF),
                            unfocusedContainerColor = Color(0x65AEDCFF)
                        ),
                    )
                    Spacer(modifier = Modifier .height(24.dp))
                    OutlinedTextField(
                        value = CsenhaState.value,
                        onValueChange = {
                            CsenhaState.value = it
                        },
                        modifier = Modifier
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(30.dp),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Confirma senha"
                            )
                        },
                        label = {
                            Text("Comfirma senha")
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF2C91DE),
                            unfocusedBorderColor = Color(0xFF2C91DE),
                            focusedContainerColor = Color(0x65AEDCFF),
                            unfocusedContainerColor = Color(0x65AEDCFF)
                        ),
                    )

                    Spacer( modifier = Modifier .height(44.dp))
                    Column (
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        val context = LocalContext.current

                        Button(
                            onClick = {
                                val cliente = CadastroUser(
                                    id_user = 0,
                                    nome_user = nameState.value,
                                    email = emailState.value,
                                    senha = senhaState.value,
                                    id_tipo = 3,
                                )

                                Log.i("Cadastro", " Enviando dados para API: $cliente")


                                GlobalScope.launch(Dispatchers.IO) {
                                    try {
                                        val response = clienteApi.cadastrarUsuario(cliente).await()

                                        // Exibe tudo no Logcat
                                        Log.i("API_CADASTRO", "Resposta completa: $response")
                                        Log.i("API_CADASTRO", "Mensagem: ${response.message}")
                                        Log.i("API_CADASTRO", "ID do usuário: ${response.data.id_user}")

                                        // Salva o ID para usar depois
                                        SessionManager.saveUserId(context = context, userId = response.data.id_user)

                                    } catch (e: Exception) {
                                        Log.e("API_CADASTRO", "Erro ao cadastrar: ${e.message}")
                                    }
                                }
                                navegacao?.navigate("login")
                            },
                            colors = ButtonDefaults.buttonColors(Color(0xFFAEDCFF)),
                            shape = RoundedCornerShape(30.dp),
                            modifier = Modifier
                                .width(270.dp)
                                .border(
                                    width = 2.dp, // 👈 tamanho da borda
                                    color = Color(0xFF2C91DE),
                                    shape = RoundedCornerShape(38.dp)
                                ),
                        ) {
                            Text(
                                text = "CADASTRAR",
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                        Spacer( modifier = Modifier .height(3.dp))

                    }
                }
            }
        }
    }



}

@Preview
@Composable
private fun CadastroscreenPreview() {
    Cadastroscreen(navegacao = null)
}
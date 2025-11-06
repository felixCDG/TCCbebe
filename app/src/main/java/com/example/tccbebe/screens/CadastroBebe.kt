package com.example.tccbebe.screens

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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import br.senai.sp.jandira.foodrecipe.service.AzureUploadService.uploadImageToAzure
import coil.compose.AsyncImage
import com.example.tccbebe.R
import com.example.tccbebe.model.RegistroBebe
import com.example.tccbebe.model.RegistroResp
import com.example.tccbebe.service.Conexao
import com.example.tccbebe.service.AuthenticatedConexao
import com.example.tccbebe.utils.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.await


@Composable
fun CadastroBebe(navegacao: NavHostController?) {


    // 1) Estado para armazenar o URI da imagem escolhida
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    // 2) Estado para armazenar a URL retornada pelo Azure
    var imageUrl by remember { mutableStateOf<String?>(null) }

    // 3) Launcher para pegar o arquivo via Galeria
    val pickImageLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            imageUri = uri
        }

    val context = LocalContext.current

    val selectedOptionId = remember { mutableStateOf<Int?>(null) }
    val selectedOptionSanId = remember { mutableStateOf<Int?>(null) }

    val pictureState = remember { mutableStateOf("") }

    var nomeState = remember {
        mutableStateOf("")
    }
    var dataState = remember {
        mutableStateOf("")
    }
    var nacionalidadeState = remember {
        mutableStateOf("")
    }
    var naturalidadeState = remember {
        mutableStateOf("")
    }
    var pesoState = remember {
        mutableStateOf("")
    }
    var alturaState = remember {
        mutableStateOf("")
    }
    var CNState = remember {
        mutableStateOf("")
    }
    var cpfState = remember {
        mutableStateOf("")
    }
    var CSCState = remember {
        mutableStateOf("")
    }
    var obsState = remember {
        mutableStateOf("")
    }

    val expandedMenu = remember { mutableStateOf(false) }
    val selectedOption = remember { mutableStateOf("") }

    val expandedSangue = remember { mutableStateOf(false) }
    val selectSangue = remember { mutableStateOf("") }

    val clienteApi = AuthenticatedConexao(context).getRegistroBebeService()


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
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        IconButton(
                            onClick = {}
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "",
                                modifier = Modifier
                                    .size(400.dp)
                            )
                        }
                        Spacer(modifier = Modifier,)
                        Text(
                            modifier = Modifier
                                .padding(top = 12.dp),
                            text = "Seu Bebe",
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = 28.sp
                        )
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()),
                    ) {
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            text = "Nome completo *",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 20.sp,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(7.dp))
                        OutlinedTextField(
                            value = nomeState.value,
                            onValueChange = {
                                nomeState.value = it
                            },
                            modifier = Modifier
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(30.dp),
                            placeholder = {
                                Text("Digite o nome completo")
                            },
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            text = "Data de nascimento *",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 20.sp,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(7.dp))
                        OutlinedTextField(
                            value = dataState.value,
                            onValueChange = {
                                dataState.value = it
                            },
                            modifier = Modifier
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(30.dp),
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.DateRange,
                                    contentDescription = "Senha"
                                )
                            },
                            placeholder = {
                                Text("Selecione a data")
                            },
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            text = "Sexo*",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 20.sp,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(7.dp))
                        OutlinedTextField(
                            value = selectedOption.value,
                            onValueChange = {},
                            readOnly = true,
                            modifier = Modifier
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(30.dp),
                            trailingIcon = {
                                IconButton(onClick = { expandedMenu.value = !expandedMenu.value }) {
                                    Icon(
                                        imageVector = Icons.Default.ArrowDropDown,
                                        contentDescription = "Abrir menu"
                                    )
                                }
                            },
                            placeholder = {
                                Text("Selecione")
                            },
                        )
                        DropdownMenu(
                            expanded = expandedMenu.value,
                            onDismissRequest = { expandedMenu.value = false },
                            modifier = Modifier.background(Color(0xFFFFFFFF))
                        ) {
                            DropdownMenuItem(
                                text = { Text("Masculino") },
                                onClick = {
                                    selectedOption.value = "Masculino"
                                    selectedOptionId.value = 1
                                    expandedMenu.value = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Feminino") },
                                onClick = {
                                    selectedOption.value = "Feminino"
                                    selectedOptionId.value = 1
                                    expandedMenu.value = false
                                }
                            )
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ){
                            IconButton(
                                onClick = {}
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Favorite,
                                    contentDescription = "",
                                    modifier = Modifier
                                        .size(400.dp)
                                )
                            }
                            Spacer(modifier = Modifier,)
                            Text(
                                modifier = Modifier
                                    .padding(top = 12.dp),
                                text = "Saúde ao Nascer",
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                fontSize = 28.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        Row (modifier = Modifier .fillMaxWidth(),   horizontalArrangement = Arrangement.spacedBy(130.dp)){
                            Text(
                                text = "Peso *",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 20.sp,
                                color = Color.Black
                            )
                            Text(
                                text = "Altura ao nascer (cm)",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 15.sp,
                                color = Color.Black
                            )
                        }
                        Spacer(modifier = Modifier.height(7.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = pesoState.value,
                                onValueChange = {
                                    pesoState.value = it
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(56.dp),
                                shape = RoundedCornerShape(30.dp),
                                placeholder = { Text("Ex:3.2") }
                            )

                            OutlinedTextField(
                                value = alturaState.value,
                                onValueChange = {
                                    alturaState.value = it
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(56.dp),
                                shape = RoundedCornerShape(30.dp),
                                placeholder = { Text("Ex: 50") }
                            )
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            text = "Tipo Sanguineo",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 20.sp,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(7.dp))
                        OutlinedTextField(
                            value = selectSangue.value,
                            onValueChange = {},
                            readOnly = true,
                            modifier = Modifier
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(30.dp),
                            trailingIcon = {
                                IconButton(onClick = { expandedSangue.value = !expandedMenu.value }) {
                                    Icon(
                                        imageVector = Icons.Default.ArrowDropDown,
                                        contentDescription = "Abrir menu"
                                    )
                                }
                            },
                            placeholder = {
                                Text("Selecione")
                            },
                        )
                        DropdownMenu(
                            expanded = expandedSangue.value,
                            onDismissRequest = { expandedSangue.value = false },
                            modifier = Modifier.background(Color(0xFFFFFFFF))
                        ) {
                            DropdownMenuItem(
                                text = { Text("A+") },
                                onClick = {
                                    selectSangue.value = "Tipo sanguinio: A+"
                                    selectedOptionSanId.value = 1
                                    expandedSangue.value = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("A-") },
                                onClick = {
                                    selectSangue.value = "Tipo sanguinio: A-"
                                    selectedOptionSanId.value = 2
                                    expandedSangue.value = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("B+") },
                                onClick = {
                                    selectSangue.value = "Tipo sanguinio: B+"
                                    selectedOptionSanId.value = 3
                                    expandedSangue.value = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("B-") },
                                onClick = {
                                    selectSangue.value = "Tipo sanguinio: B-"
                                    selectedOptionSanId.value = 4
                                    expandedSangue.value = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("AB+") },
                                onClick = {
                                    selectSangue.value = "Tipo sanguinio: AB+"
                                    selectedOptionSanId.value = 5
                                    expandedSangue.value = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("AB-") },
                                onClick = {
                                    selectSangue.value = "Tipo sanguinio: AB-"
                                    selectedOptionSanId.value = 6
                                    expandedSangue.value = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("O+") },
                                onClick = {
                                    selectSangue.value = "Tipo sanguinio: O+"
                                    selectedOptionSanId.value = 7
                                    expandedSangue.value = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("O-") },
                                onClick = {
                                    selectSangue.value = "Tipo sanguinio: O-"
                                    selectedOptionSanId.value = 8
                                    expandedSangue.value = false
                                }
                            )
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ){
                            IconButton(
                                onClick = {}
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Email,
                                    contentDescription = "",
                                    modifier = Modifier
                                        .size(400.dp)
                                )
                            }
                            Spacer(modifier = Modifier,)
                            Text(
                                modifier = Modifier
                                    .padding(top = 12.dp),
                                text = "Documentos",
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                fontSize = 28.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            text = "Certidando de Nascimento *",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 20.sp,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(7.dp))
                        OutlinedTextField(
                            value = CNState.value,
                            onValueChange = {
                                CNState.value = it
                            },
                            modifier = Modifier
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(30.dp),
                            placeholder = {
                                Text("Numedo da certidao")
                            },
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
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(30.dp),
                            placeholder = {
                                Text("000.000.000.00")
                            },
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            text = "Cartão SUS/ Convênio *",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 20.sp,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(7.dp))
                        OutlinedTextField(
                            value = CSCState.value,
                            onValueChange = {
                                CSCState.value = it
                            },
                            modifier = Modifier
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(30.dp),
                            placeholder = {
                                Text("Numero")
                            },
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ){
                            Spacer(modifier = Modifier,)
                            Text(
                                modifier = Modifier
                                    .padding(top = 12.dp),
                                text = "Uploude de Arquivos",
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                fontSize = 28.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        Column (
                            modifier = Modifier.fillMaxWidth(),
                        ){
                            Spacer(modifier = Modifier,)
                            Text(
                                modifier = Modifier
                                    .padding(top = 12.dp),
                                text = "Uploude de Arquivos",
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp
                            )
                            Text(
                                modifier = Modifier
                                    .padding(top = 12.dp),
                                text = "Docs: RG,CNH,Certidao de Nascimento",
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        // Dentro da Column que você já tem, substitua este Card:
                        OutlinedTextField(
                            value = pictureState.value,
                            onValueChange = { },
                            shape = RoundedCornerShape(10.dp),
                            label = {
                                Text(
                                    text = "Imagem",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFF000000)
                                )
                            },
                            leadingIcon = {
                                Icon(imageVector = Icons.Default.Email,
                                    contentDescription = "",
                                    tint = Color(0xFF000000),
                                    modifier = Modifier
                                        .padding(start = 10.dp)
                                        .size(40.dp)
                                )
                            },
                            enabled = false,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    pickImageLauncher.launch("image/*")
                                }
                        )

                        imageUri?.let { uri ->
                            AsyncImage(
                                model = uri,
                                contentDescription = "Imagem Selecionada",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .border(2.dp, Color.Gray, shape = RoundedCornerShape(8.dp))
                                    .padding(horizontal = 4.dp),
                                contentScale = ContentScale.Crop
                            )
                        }

                        Spacer(modifier = Modifier.height(34.dp))
                        Column(
                            modifier = Modifier
                                .fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Button(
                                onClick = {

                                    if (imageUri != null) {
                                        GlobalScope.launch(Dispatchers.IO) {
                                            try {
                                                // 1️⃣ Faz o upload e pega a URL
                                                val urlRetornada = uploadImageToAzure(context, imageUri!!)

                                                withContext(Dispatchers.Main) {
                                                    pictureState.value = urlRetornada // atualiza o state
                                                }

                                                val idUser = SessionManager.getUserId(context)

                                                // 2️⃣ Cria o objeto com a URL correta
                                                val cliente = RegistroBebe(
                                                    id_bebe = 0,
                                                    nome = nomeState.value,
                                                    data_nascimento = dataState.value,
                                                    peso = pesoState.value,
                                                    altura = alturaState.value,
                                                    certidao_nascimento = CNState.value,
                                                    cpf = cpfState.value,
                                                    imagem_certidao = urlRetornada, // <-- usa a URL retornada
                                                    cartao_medico = CSCState.value,
                                                    idSexo = selectedOptionId.value ?: 0,
                                                    idSangue = selectedOptionSanId.value ?: 0,

                                                )

                                                val response = clienteApi.cadastroBabe(cliente).await()
                                                Log.i("API_CADASTRO", "Resposta: $response")

                                                withContext(Dispatchers.Main) {
                                                    navegacao?.navigate("home")
                                                }

                                            } catch (e: Exception) {
                                                Log.e("API_CADASTRO", "Erro: ${e.message}")
                                            }
                                        }
                                    } else {
                                        Log.e("UPLOAD", "Nenhuma imagem selecionada")
                                    }

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
                                Text(  
                                    text = "ADICIONAR BEBE",
                                    color = Color.Black,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
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
private fun CadastroBebePreview() {
    CadastroBebe(navegacao = null)
}
package com.example.tccbebe.screens

import android.R.id.input
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.tccbebe.R

@Composable
fun CadastroResponsavel(navegacao: NavHostController?) {

    val expandedMenupai = remember { mutableStateOf(false) }
    val selectedOptionpai = remember { mutableStateOf("") }

    var nomeState = remember {
        mutableStateOf("")
    }
    var dataNState = remember {
        mutableStateOf("")
    }
    var cpfState = remember {
        mutableStateOf("")
    }
    var profissaoState = remember {
        mutableStateOf("")
    }
    var telefoneState = remember {
        mutableStateOf("")
    }
    var cepState = remember {
        mutableStateOf("")
    }
    var CSCState = remember {
        mutableStateOf("")
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
                            text = "Responsavel",
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
                            value = dataNState.value,
                            onValueChange = {
                                dataNState.value
                            },
                            modifier = Modifier
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(30.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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
                            text = "CPF *",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 20.sp,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(7.dp))
                        OutlinedTextField(
                            value = cpfState.value,
                            onValueChange = {
                                cpfState.value
                            },
                            modifier = Modifier
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(30.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            placeholder = {
                                Text("000.000.000-00")
                            },
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            text = "TELEFONE *",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 20.sp,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(7.dp))
                        OutlinedTextField(
                            value = telefoneState.value,
                            onValueChange = {
                                telefoneState.value
                            },
                            modifier = Modifier
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(30.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Phone,
                                    contentDescription = "Telefone"
                                )
                            },
                            placeholder = {
                                Text("(00) 00000-0000")
                            },
                        )
                        Spacer(modifier = Modifier.height(24.dp))
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
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp),
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, Color.Black),
                            colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Email, // ícone de upload
                                    contentDescription = "Upload",
                                    modifier = Modifier.size(48.dp),
                                    tint = Color.Black
                                )
                            }
                        }
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
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            text = "CEP *",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 20.sp,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(7.dp))
                        OutlinedTextField(
                            value = cepState.value,
                            onValueChange = {
                                cepState.value
                            },
                            modifier = Modifier
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(30.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = "Telefone"
                                )
                            },
                            placeholder = {
                                Text("cep")
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
                            value = selectedOptionpai.value,
                            onValueChange = {},
                            readOnly = true,
                            modifier = Modifier
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(30.dp),
                            trailingIcon = {
                                IconButton(onClick = { expandedMenupai.value = !expandedMenupai.value }) {
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
                            expanded = expandedMenupai.value,
                            onDismissRequest = { expandedMenupai.value = false },
                            modifier = Modifier.background(Color(0xFFFFFFFF))
                        ) {
                            DropdownMenuItem(
                                text = { Text("Masculino") },
                                onClick = {
                                    selectedOptionpai.value = "Masculino"
                                    expandedMenupai.value = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Feminino") },
                                onClick = {
                                    selectedOptionpai.value = "Feminino"
                                    expandedMenupai.value = false
                                }
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
                                    navegacao?.navigate("cadastroB")
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
                                    text = "ADICIONAR RESPONSAVEL",
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
private fun CadastroResponsavelPreview() {
    CadastroResponsavel(navegacao = null)
}
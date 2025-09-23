package com.example.tccbebe.screens

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.tccbebe.R


@Composable
fun CadastroBebe(navegacao: NavHostController?) {

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
                        Row (modifier = Modifier .fillMaxWidth(),   horizontalArrangement = Arrangement.spacedBy(130.dp)){
                            Text(
                                text = "Sexo *",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 20.sp,
                                color = Color.Black
                            )
                            Text(
                                text = "Nacionalidade *",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 20.sp,
                                color = Color.Black
                            )
                        }
                        Spacer(modifier = Modifier.height(7.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = selectedOption.value,
                                onValueChange = {},
                                readOnly = true,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(56.dp),
                                shape = RoundedCornerShape(30.dp),
                                trailingIcon = {
                                    IconButton(onClick = { expandedMenu.value = !expandedMenu.value }) {
                                        Icon(
                                            imageVector = Icons.Default.ArrowDropDown,
                                            contentDescription = "Abrir menu"
                                        )
                                    }
                                },
                                placeholder = { Text("Selecione") }
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
                                        expandedMenu.value = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Feminino") },
                                    onClick = {
                                        selectedOption.value = "Feminino"
                                        expandedMenu.value = false
                                    }
                                )
                            }

                            OutlinedTextField(
                                value = nacionalidadeState.value,
                                onValueChange = {
                                    nacionalidadeState.value = it
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(56.dp),
                                shape = RoundedCornerShape(30.dp),
                                placeholder = { Text("Ex: Brasileiro") }
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            text = "Naturalidade *",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 20.sp,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(7.dp))
                        OutlinedTextField(
                            value = naturalidadeState.value,
                            onValueChange = {
                                naturalidadeState.value = it
                            },
                            modifier = Modifier
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(30.dp),

                            placeholder = {
                                Text("Cidade/Estado")
                            },
                        )
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
                                    expandedSangue.value = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("A-") },
                                onClick = {
                                    selectSangue.value = "Tipo sanguinio: A-"
                                    expandedSangue.value = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("B+") },
                                onClick = {
                                    selectSangue.value = "Tipo sanguinio: B+"
                                    expandedSangue.value = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("B-") },
                                onClick = {
                                    selectSangue.value = "Tipo sanguinio: B-"
                                    expandedSangue.value = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("AB+") },
                                onClick = {
                                    selectSangue.value = "Tipo sanguinio: AB+"
                                    expandedSangue.value = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("AB-") },
                                onClick = {
                                    selectSangue.value = "Tipo sanguinio: AB-"
                                    expandedSangue.value = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("O+") },
                                onClick = {
                                    selectSangue.value = "Tipo sanguinio: O+"
                                    expandedSangue.value = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("O-") },
                                onClick = {
                                    selectSangue.value = "Tipo sanguinio: O-"
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

                        Spacer(modifier = Modifier.height(34.dp))
                        Column(
                            modifier = Modifier
                                .fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Button(
                                onClick = {},
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
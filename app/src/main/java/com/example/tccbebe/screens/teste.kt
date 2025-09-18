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
import androidx.compose.foundation.layout.wrapContentWidth
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
fun Teste(navegacao: NavHostController?) {

    val expandedMenu = remember { mutableStateOf(false) }
    val selectedOption = remember { mutableStateOf("") }


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
                    Box {
                        OutlinedTextField(
                            value = selectedOption.value,
                            onValueChange = {},
                            readOnly = true, // impede digitação manual
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("Selecione uma opção") },
                            trailingIcon = {
                                IconButton(onClick = { expandedMenu.value = !expandedMenu.value }) {
                                    Icon(
                                        imageVector = Icons.Default.ArrowDropDown,
                                        contentDescription = "Abrir menu"
                                    )
                                }
                            }
                        )

                        DropdownMenu(
                            expanded = expandedMenu.value,
                            onDismissRequest = { expandedMenu.value = false },
                            modifier = Modifier.background(Color(0xFFFFDF87))
                        ) {
                            DropdownMenuItem(
                                text = { Text("Home") },
                                onClick = {
                                    selectedOption.value = "Home"
                                    expandedMenu.value = false
                                    navegacao?.navigate("home1")
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Perfil") },
                                onClick = {
                                    selectedOption.value = "Perfil"
                                    expandedMenu.value = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Cadastrar Receita") },
                                onClick = {
                                    selectedOption.value = "Cadastrar Receita"
                                    expandedMenu.value = false
                                    navegacao?.navigate("cadastroReceita")
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Favoritas") },
                                onClick = {
                                    selectedOption.value = "Favoritas"
                                    expandedMenu.value = false
                                }
                            )
                        }
                    }
                }
            }
        }
    }



}

@Preview
@Composable
private fun TestePreview() {
    Teste(navegacao = null)
}



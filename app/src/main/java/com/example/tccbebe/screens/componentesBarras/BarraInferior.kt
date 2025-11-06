package com.example.tccbebe.screens.componentesBarras

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController

@Composable
fun BarraInferior(ControleNavegacao: NavHostController?) {

    NavigationBar (
        containerColor = MaterialTheme.colorScheme.primaryContainer
    ){
        NavigationBarItem(
            onClick = {
                ControleNavegacao!!.navigate("conteudo")
            },
            selected = false,
            icon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = ""
                )
            },
            label = {
                Text(text = "Home")
            }
        )
        NavigationBarItem(
            onClick = {},
            selected = false,
            icon = {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = ""
                )
            },
            label = {
                Text(text = "Favorito")
            }
        )
        NavigationBarItem(
            onClick = {
                ControleNavegacao!!.navigate("cadastro")
            },
            selected = false,
            icon = {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = ""
                )
            },
            label = {
                Text(text = "Novo Cliente")
            }
        )
    }

}

@Preview
@Composable
private fun BarraInferiorPreview() {
        BarraInferior( null)
}
package com.example.tccbebe.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tccbebe.screens.componentesBarras.BarraInferior
import com.example.tccbebe.screens.componentesBarras.BarradeTitulo
import com.example.tccbebe.screens.componentesBarras.BotaoFlutuante
import com.example.tccbebe.screens.componentesBarras.ConteudoRotina

@Composable
fun RotinaScreensPt1(modifier: Modifier = Modifier) {

    var ControleNavegacao = rememberNavController()

    Scaffold (
        topBar = {
            BarradeTitulo()
        },
        bottomBar = {
            BarraInferior(ControleNavegacao)
        },
        floatingActionButton = {
            BotaoFlutuante(ControleNavegacao)
        },
        content = { padding ->
            NavHost(
                navController = ControleNavegacao,
                startDestination = "conteudo"
            ){
                composable(route = "conteudo") { ConteudoRotina(padding, ControleNavegacao) }
            }
        }
    )

}



@Preview
@Composable
private fun RotinaScreensPt1Preview() {
    RotinaScreensPt1()
}
package com.example.tccbebe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import br.senai.sp.jandira.telarotina.screens.CriarRotinaScreen
import com.example.tccbebe.screens.BabyIAScreen
import com.example.tccbebe.screens.CadastroBebe
import com.example.tccbebe.screens.CadastroBebeNovo
import com.example.tccbebe.screens.CadastroResponsavelNovo
import com.example.tccbebe.screens.Cadastroscreen
import com.example.tccbebe.screens.CalendarioScreen
import com.example.tccbebe.screens.ChatIndividualScreen
import com.example.tccbebe.screens.ContatosScreen
import com.example.tccbebe.screens.CriarConta
import com.example.tccbebe.screens.HomeScreen
import com.example.tccbebe.screens.Loginscreen
import com.example.tccbebe.screens.PerfilResp
import com.example.tccbebe.screens.SplashScreen
import com.example.tccbebe.screens.VideoChamadaScreen
import com.example.tccbebe.ui.theme.TCCBEBETheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val navegacao = rememberNavController()
            NavHost(
                navController = navegacao,
                startDestination = "splash"
            ){
               composable(route = "splash"){ SplashScreen(navegacao) }
               composable(route = "criarconta"){ CriarConta(navegacao) }
               composable(
                   route = "login",
                   enterTransition = { slideInHorizontally(animationSpec = tween(300), initialOffsetX = { -it }) },
                   exitTransition = { slideOutHorizontally(animationSpec = tween(300), targetOffsetX = { -it }) }
               ){ Loginscreen(navegacao) }
               composable(
                   route = "cadastro",
                   enterTransition = { slideInHorizontally(animationSpec = tween(300), initialOffsetX = { it }) },
                   exitTransition = { slideOutHorizontally(animationSpec = tween(300), targetOffsetX = { it }) }
               ){ Cadastroscreen(navegacao) }
               composable(route = "cadastroR"){ CadastroResponsavelNovo(navegacao) }
               composable(route = "cadastroB"){ CadastroBebeNovo(navegacao) }
               composable(route = "perfilresp",) { PerfilResp(navegacao = navegacao) }
               composable(route = "calendario",) { CalendarioScreen(navegacao = navegacao) }
               composable(route = "home",) { HomeScreen(navegacao = navegacao) }
               composable(route = "contatos") { ContatosScreen(navController = navegacao) }
               composable(route = "chat/{contatoId}/{contatoNome}") { backStackEntry ->
                   val contatoId = backStackEntry.arguments?.getString("contatoId") ?: ""
                   val contatoNome = backStackEntry.arguments?.getString("contatoNome") ?: ""
                   ChatIndividualScreen(navController = navegacao, contatoId = contatoId, contatoNome = contatoNome)
               }
               composable(route = "babyia") { BabyIAScreen(navegacao = navegacao) }
               composable(route = "videochamada/{roomName}") { backStackEntry ->
                   val roomName = backStackEntry.arguments?.getString("roomName")
                   VideoChamadaScreen(navegacao = navegacao, roomName = roomName)
               }
               composable(route = "videochamada") { VideoChamadaScreen(navegacao = navegacao) }
                composable(route = "itemR"){ CriarRotinaScreen( navegacao) }
            }

        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TCCBEBETheme {
        Greeting("Android")
    }
}
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
import com.example.tccbebe.screens.Bemvindo
import com.example.tccbebe.screens.CadastroBebe
import com.example.tccbebe.screens.CadastroResponsavel
import com.example.tccbebe.screens.Cadastroscreen
import com.example.tccbebe.screens.CalendarioScreen
import com.example.tccbebe.screens.CriarConta
import com.example.tccbebe.screens.Loginscreen
import com.example.tccbebe.screens.PerfilResp
import com.example.tccbebe.ui.theme.TCCBEBETheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val navegacao = rememberNavController()
            NavHost(
                navController = navegacao,
                startDestination = "calendario"
            ){
               composable(route = "bemvindo"){ Bemvindo(navegacao) }
               composable(route = "criarconta"){ CriarConta(navegacao) }
               composable(route = "login"){ Loginscreen(navegacao) }
               composable(route = "cadastro"){ Cadastroscreen(navegacao) }
               composable(route = "cadastroR"){ CadastroResponsavel(navegacao) }
               composable(route = "cadastroB"){ CadastroBebe(navegacao) }
               composable(route = "perfilresp",) { PerfilResp(navegacao = navegacao) }
               composable(route = "calendario",) { CalendarioScreen(navegacao = navegacao) }
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
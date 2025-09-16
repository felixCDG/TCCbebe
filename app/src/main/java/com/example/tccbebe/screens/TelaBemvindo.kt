package com.example.tccbebe.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.tccbebe.R

fun CurvedTopShape() = GenericShape { size, _ ->
    moveTo(0f, size.height) // canto inferior esquerdo
    lineTo(0f, size.height * 0.2f) // sobe até perto do topo

    // curva superior (você pode ajustar os pontos de controle)
    quadraticBezierTo(
        size.width * 0.25f, -8f,     // ponto de controle
        size.width * 0.5f, size.height * 0.1f // ponto final

    )
    quadraticBezierTo(
        size.width * 0.75f, size.height * 0.2f,
        size.width, 0f
    )

    lineTo(size.width, size.height) // desce lado direito
    close()
}

@Composable
fun Bemvindo(modifier: Modifier = Modifier) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xfffffffff))
    ) {
        Column(
            modifier = Modifier
                .zIndex(1f)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Image(
                painter = painterResource(R.drawable.logoremovedor),
                contentDescription = "",
                modifier = Modifier
                    .size(180.dp),

            )
        }
        Column (
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Bottom
        ){
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(670.dp),
                shape = CurvedTopShape() ,// aplica o shape
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF90CAF9)
                )
            ) {
                Column (
                    modifier = Modifier
                        .fillMaxSize()
                ){
                    Text(
                        modifier = Modifier
                            .padding(top = 66.dp)
                            .padding(38.dp),
                        text = "BEM VINDO!",
                        color = Color.Black,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 28.sp
                    )
                   Column (
                            modifier = Modifier
                                .padding(15.dp)
                                .fillMaxSize(),
                       horizontalAlignment = Alignment.CenterHorizontally
                   ) {
                       Text(
                           text = "Cuidar de um bebe pode ser desafiador!",
                           color = Color.Black,
                           fontWeight = FontWeight.Bold,
                           fontSize = 17.sp
                       )
                       Spacer(modifier = Modifier .height(17.dp))
                       Text(
                           text = "   Você não precisa passar por isso sozinho.\n         Apoiamos pais de primeira viagem,\n      oferecendo dicas, informações úteis e\n    uma rede de apoio para que a rotina com\n              seu filho seja mais tranquila.",
                           color = Color.Black,
                           fontWeight = FontWeight.Bold,
                           fontSize = 15.sp
                       )
                       Spacer(modifier = Modifier .height(68.dp))
                       Text(
                           text = "Benefícios",
                           color = Color.Black,
                           fontWeight = FontWeight.ExtraBold,
                           fontSize = 36.sp
                       )
                       Row (
                           modifier = Modifier
                               .fillMaxSize()
                       ){
                           Column (
                               modifier = Modifier
                                   .fillMaxSize()
                           ){
                               Spacer(modifier = Modifier .height(16.dp))
                               Icon(
                                   imageVector = Icons.Default.CheckCircle,
                                   contentDescription = ""
                               )
                               Spacer(modifier = Modifier .height(16.dp))
                               Icon(
                                   imageVector = Icons.Default.CheckCircle,
                                   contentDescription = ""
                               )
                               Spacer(modifier = Modifier .height(16.dp))
                               Icon(
                                   imageVector = Icons.Default.CheckCircle,
                                   contentDescription = ""
                               )
                               Spacer(modifier = Modifier .height(16.dp))
                               Icon(
                                   imageVector = Icons.Default.CheckCircle,
                                   contentDescription = ""
                               )
                           }
                           Column (
                               modifier = Modifier
                                   .fillMaxSize()
                           ) {
                               Text(
                                   text = "Cuidar de um bebe pode ser desafiador!",
                                   color = Color.Black,
                                   fontWeight = FontWeight.Bold,
                                   fontSize = 17.sp
                               )
                           }
                       }
                   }
                }
            }
        }
    }

}

@Preview
@Composable
private fun BemvindoPreview() {
    Bemvindo()
}
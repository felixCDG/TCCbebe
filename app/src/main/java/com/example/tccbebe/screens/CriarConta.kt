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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import androidx.navigation.Navigator
import com.example.tccbebe.R



@Composable
fun CriarConta(navegacao: NavHostController?) {


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
                    .size(220.dp),

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
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = painterResource(id = R.drawable.fundoback),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Column (modifier = Modifier .fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Text(
                            text = "BEM VINDO!",
                            color = Color.Black,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 38.sp
                        )
                        Spacer(modifier = Modifier .height(42.dp))
                        Button(
                            onClick = {},
                            modifier = Modifier
                                .width(260.dp)
                                .border(border = BorderStroke(1.dp, color = Color(0xFF000000)), shape = RoundedCornerShape(16.dp)),
                            colors = ButtonDefaults.buttonColors(Color(0xfffffffff)),
                            shape = RoundedCornerShape(16.dp),

                        ) {
                            Text(
                                text = "CRIAR CONTA",
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                fontSize = 23.sp
                            )
                        }
                        Spacer(modifier = Modifier .height(32.dp))
                        Button(
                            onClick = {},
                            modifier = Modifier
                                .padding(bottom = 45.dp)
                                .width(260.dp)
                                .border(border = BorderStroke(1.dp, color = Color(0xFF000000)), shape = RoundedCornerShape(16.dp)),
                            colors = ButtonDefaults.buttonColors(Color(0xfffffffff)),
                            shape = RoundedCornerShape(16.dp),

                            ) {
                            Text(
                                text = "ENTRAR",
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                fontSize = 23.sp
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
private fun CriarContaPreview() {
   CriarConta(navegacao = null)
}
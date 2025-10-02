package com.example.tccbebe.screens.componentesBarras

import android.adservices.topics.Topic
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarradeTitulo(modifier: Modifier = Modifier) {

    TopAppBar(
        title = {
             Row (verticalAlignment = Alignment.CenterVertically,
                 horizontalArrangement = Arrangement.SpaceBetween,
                 modifier = Modifier
                     .fillMaxWidth()

             ){
                 Column (
                     modifier = Modifier
                         .padding(6.dp),
                 ){
                     Text(text = "Olá, Guilherme",
                         style = MaterialTheme.typography.titleLarge
                     )

                 }
                 Column (
                     modifier = Modifier
                         .padding(6.dp)
                         .fillMaxWidth(),
                     horizontalAlignment = Alignment.End

                 ){
                     Text(text = "Olá, Guilherme",
                         style = MaterialTheme.typography.titleLarge
                     )
                 }

                     /* Card (
                          modifier = Modifier
                              .size(60.dp)
                              .padding(4.dp),
                          shape = CircleShape
                      ){}*/
             }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFFFFFFFF)
        )
    )

}

@Preview
@Composable
private fun BarradeTituloPreview() {


        BarradeTitulo()


}
package com.example.tccbebe.screens.componentesBarras

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.await

@Composable
fun ConteudoRotina(paddingValues: PaddingValues, ControleNavegacao: NavHostController?) {

    Column (
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
    ){
        Row (
            modifier = Modifier .padding(10.dp)
        ){
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Person"
            )
            Spacer(modifier = Modifier .padding(horizontal = 4.dp))
            Text(text = "Lista de Cliente",
                style = MaterialTheme.typography.titleLarge)
        }
    }

}

@Preview
@Composable
private fun ConteudoRotinaPreview() {

    ConteudoRotina(PaddingValues(16.dp), ControleNavegacao = null)

}
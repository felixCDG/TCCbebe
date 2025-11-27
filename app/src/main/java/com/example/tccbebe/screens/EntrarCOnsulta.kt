package com.example.tccbebe.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.tccbebe.R

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.TextField
import androidx.navigation.NavHostController
import androidx.compose.ui.platform.LocalContext
import android.widget.Toast
import java.net.URLEncoder
import java.net.URLDecoder
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import com.example.tccbebe.service.Conexao

@Composable
fun SalaConsulta(navegacao: NavHostController?) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Logo SOS BABY
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.logocerta),
                contentDescription = "Bebê",
                modifier = Modifier.height(42.dp)
            )
        }

        // Navegação central
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = "Home",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF333333),
                modifier = Modifier.clickable {
                    navegacao?.navigate("home")// Já estamos na Home, apenas scroll para o topo se necessário
                }
            )
            Text(
                text = "Chat",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF333333),
                modifier = Modifier.clickable {
                    navegacao?.navigate("contatos")
                }
            )
            Text(
                text = "BabyIA",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF333333),
                modifier = Modifier.clickable {
                    navegacao?.navigate("babyia")
                }
            )
        }

        // Ações do lado direito
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Ícone de notificação
            IconButton(
                onClick = {
                    // navController?.navigate("notificacoes")
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notifications",
                    tint = Color(0xFF7986CB),
                    modifier = Modifier.size(24.dp)
                )
            }

            // Foto de perfil
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF7986CB))
                    .clickable {
                        // navController?.navigate("perfil")
                    },
                contentAlignment = Alignment.Center
            ) {
                IconButton(
                    onClick = {
                        navegacao?.navigate("perfilresp")
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profile",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}


@Composable
fun CreateCallScreen(navegacao: NavHostController?) {
    Column(modifier = Modifier.fillMaxSize()) {
        // Reuse the existing top header
        SalaConsulta(navegacao)

        // Content: centered card with title, single-line input and primary button
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth(0.9f)) {
                Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Entrar na Chamada",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    val roomName = remember { mutableStateOf("") }
                    val context = LocalContext.current
                    val isLoading = remember { mutableStateOf(false) }
                    val scope = rememberCoroutineScope()

                    TextField(
                        value = roomName.value,
                        onValueChange = { if (it.length <= 100) roomName.value = it },
                        placeholder = { Text("Nome da sala") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            val trimmed = roomName.value.trim()
                            if (trimmed.isEmpty()) {
                                Toast.makeText(context, "Digite o nome da sala.", Toast.LENGTH_SHORT).show()
                                return@Button
                            }

                            // Encode for safe transport in route and path
                            val encoded = try {
                                URLEncoder.encode(trimmed, "utf-8")
                            } catch (_: Exception) {
                                trimmed
                            }

                            // Prevent double clicks
                            if (isLoading.value) return@Button

                            isLoading.value = true

                            scope.launch {
                                try {
                                    val conexao = Conexao()
                                    val service = conexao.getRoomService()
                                    val response = service.checkRoom(encoded)

                                    if (response.isSuccessful) {
                                        val body = response.body()
                                        if (body != null && body.exists) {
                                            // Room exists -> navigate
                                            navegacao?.navigate("video/$encoded")
                                        } else {
                                            Toast.makeText(context, "Sala não encontrada.", Toast.LENGTH_SHORT).show()
                                        }
                                    } else {
                                        if (response.code() == 404) {
                                            Toast.makeText(context, "Sala não encontrada.", Toast.LENGTH_SHORT).show()
                                        } else {
                                            Toast.makeText(context, "Erro ao verificar sala: ${response.code()}.", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                } catch (ex: Exception) {
                                    Toast.makeText(context, "Falha de rede: ${ex.localizedMessage}", Toast.LENGTH_SHORT).show()
                                } finally {
                                    isLoading.value = false
                                }
                            }

                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2F6BE4)),
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (isLoading.value) {
                            CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp, modifier = Modifier.size(18.dp))
                        } else {
                            Text(text = "Entrar na Chamada", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun VideoScreen(roomNameEncoded: String?) {
    // Decode the room name if it was encoded into the route
    val roomName = try {
        roomNameEncoded?.let { URLDecoder.decode(it, "utf-8") }
    } catch (_: Exception) {
        roomNameEncoded
    }

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text(text = "Video - Sala: ${roomName ?: "—"}", fontSize = 20.sp, fontWeight = FontWeight.Medium)
    }
}


@Preview(showBackground = true)
@Composable
fun CreateCallScreenPreview() {
    CreateCallScreen(navegacao = null)
}

@Preview
@Composable
private fun SalaConsultaPreview() {
    SalaConsulta(navegacao = null)
}
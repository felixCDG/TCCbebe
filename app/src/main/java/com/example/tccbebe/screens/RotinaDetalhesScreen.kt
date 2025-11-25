package br.senai.sp.jandira.telarotina.screens

import android.widget.Toast
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.tccbebe.R
import com.example.tccbebe.model.CadastroRotina
import com.example.tccbebe.service.AuthenticatedConexao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RotinaDetalhesScreen(navegacao: NavHostController?) {
    // Define font family - using system default for now
    val kronaOneFont = FontFamily.Default

    // State: lista de rotinas e loading
    val rotinas = remember { androidx.compose.runtime.mutableStateListOf<CadastroRotina>() }
    val isLoading = remember { mutableStateOf(true) }
    val context = LocalContext.current

    // Fetch rotinas once when the composable is first composed
    LaunchedEffect(Unit) {
        val client = AuthenticatedConexao(context).getRotinaService()
        try {
            // Execute network call on IO and capture response, then assign state on Main
            val response = withContext(Dispatchers.IO) {
                client.getAllRotinas().execute()
            }

            Log.i("RotinaDetalhes", "HTTP code=${response.code()} message=${response.message()}")

            if (response.isSuccessful) {
                val wrapper = response.body()
                Log.i("RotinaDetalhes", "wrapper==null? ${wrapper == null}")
                if (wrapper == null) {
                    try {
                        val raw = response.raw()
                        Log.i("RotinaDetalhes", "raw response: $raw")
                        val eb = response.errorBody()
                        if (eb != null) {
                            val s = eb.string()
                            Log.i("RotinaDetalhes", "errorBody: $s")
                        }
                    } catch (_: Exception) {
                        // ignore
                    }
                }
                val bodyList = wrapper?.data ?: emptyList()
                rotinas.clear()
                rotinas.addAll(bodyList)

                // Log and Toast for debugging
                Log.i("RotinaDetalhes", "Rotinas carregadas: ${bodyList.size}")
                Toast.makeText(context, "Rotinas carregadas: ${bodyList.size}", Toast.LENGTH_SHORT).show()
            } else {
                // handle non-successful response if needed
            }
        } catch (_: Exception) {
            // Log exception
        } finally {
            isLoading.value = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header
            TopAppBar(
                title = {
                    Image(
                        painter = painterResource(id = R.drawable.logo_baby),
                        contentDescription = "S♥S Baby Logo",
                        modifier = Modifier
                            .height(56.dp)
                            .width(100.dp)
                            .padding(start = 8.dp)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Voltar",
                            tint = Color.Gray
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notificações",
                            tint = Color.Blue
                        )
                    }
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Perfil",
                            tint = Color.Blue
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )

            // Content: cabeçalho fixo + lista rolável com LazyColumn
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Spacer(modifier = Modifier.height(40.dp))

                // Title
                Text(
                    text = "Bem vindo\na Rotina !!",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = kronaOneFont,
                    color = Color.Black,
                    lineHeight = 28.sp
                )

                Spacer(modifier = Modifier.height(40.dp))

                // Create Button
                Button(
                    onClick = {
                        navegacao?.navigate("itemR")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF6C7CE7)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Criar",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = kronaOneFont,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Description Text
                Text(
                    text = "Crie uma nova\nrotina ao seu Bebê",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = kronaOneFont,
                    color = Color.Black,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(40.dp))

                // Lista em LazyColumn para garantir renderização/rolagem
                if (isLoading.value) {
                    CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                } else {
                    if (rotinas.isEmpty()) {
                        Text(text = "Nenhuma rotina encontrada", color = Color.Gray)
                    } else {
                        LazyColumn(modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            item {
                                // Debug header
                                Text(text = "Encontradas: ${rotinas.size}", color = Color.Gray, modifier = Modifier.padding(bottom = 8.dp))
                                Text(text = rotinas.first().toString(), color = Color.DarkGray, fontSize = 12.sp, modifier = Modifier.padding(bottom = 12.dp))
                            }

                            itemsIndexed(rotinas) { index: Int, rotina: CadastroRotina ->
                                RotinaCard(rotina = rotina, index = index)
                            }
                        }
                    }
                }
            }
        }

        // Overlay debug badge com a contagem de rotinas (sempre visível)
        if (!isLoading.value) {
            Box(modifier = Modifier
                .fillMaxSize()
            ) {
                Text(
                    text = "Rotinas: ${rotinas.size}",
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp)
                        .background(Color.White.copy(alpha = 0.9f), shape = RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    color = Color.Black,
                    fontSize = 12.sp
                )
            }
        }
    }
}

private fun formatDateForDisplay(apiDate: String): String {
    // Expecting YYYY-MM-DD or already formatted
    return try {
        if (apiDate.contains("-")) {
            val parts = apiDate.split("-")
            if (parts.size >= 3) {
                val y = parts[0]
                val m = parts[1]
                val d = parts[2]
                return "$d/$m/$y"
            }
        }
        apiDate
    } catch (_: Exception) {
        apiDate
    }
}

@Composable
fun RotinaCard(rotina: CadastroRotina, index: Int) {
    // try parse color, fallback to a default
    val color = try {
        Color(android.graphics.Color.parseColor(rotina.cor))
    } catch (e: Exception) {
        Color(0xFF6C7CE7)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = color),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        // choose text color based on background luminance for contrast
        val textColor = if (color.luminance() > 0.6f) Color.Black else Color.White

        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
                Text(
                    text = rotina.titulo_rotina,
                    fontWeight = FontWeight.Bold,
                    color = textColor,
                    fontSize = 16.sp,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = { /* TODO: remover rotina */ }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Remover",
                        tint = Color.Red
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "TAREFA",
                color = textColor.copy(alpha = 0.9f),
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = rotina.titulo_item,
                color = textColor,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = rotina.descricao,
                color = textColor.copy(alpha = 0.9f),
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val dataExibicao = formatDateForDisplay(rotina.data_rotina)
                Text(text = dataExibicao, color = textColor)
                Text(text = rotina.hora, color = textColor)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RotinaDetalhesScreenPreview() {
    RotinaDetalhesScreen(navegacao = null)
}

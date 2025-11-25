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
import com.example.tccbebe.utils.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.LocalDateTime
import java.util.Locale

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
            // Get logged in user id from SessionManager
            val userId = SessionManager.getUserId(context)
            if (userId <= 0) {
                Log.w("RotinaDetalhes", "User id inválido: $userId")
                Toast.makeText(context, "Usuário não autenticado", Toast.LENGTH_SHORT).show()
            } else {
                // Execute network call on IO and capture response, then assign state on Main
                val response = withContext(Dispatchers.IO) {
                    client.getUserRotinas(userId).execute()
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
                    // Ordena da mais recente para a mais antiga usando heurísticas sobre data/hora retornadas
                    val sorted = bodyList.sortedWith(compareByDescending<CadastroRotina> {
                        parseRotinaToEpochMillis(it) ?: Long.MIN_VALUE
                    })
                    // Se houver uma rotina recém-criada salva em sessão, priorizá-la
                    val rotinaRecenteId = SessionManager.getRotinaId(context)
                    val reordered = if (rotinaRecenteId > 0) {
                        val mutable = sorted.toMutableList()
                        val idx = mutable.indexOfFirst { it.id_rotina == rotinaRecenteId }
                        if (idx > 0) {
                            val item = mutable.removeAt(idx)
                            mutable.add(0, item)
                        }
                        // limpa o id salvo para não repetir a priorização
                        try { SessionManager.clearRotinaId(context) } catch (_: Exception) { }
                        mutable.toList()
                    } else {
                        sorted
                    }

                    rotinas.clear()
                    rotinas.addAll(reordered)

                    // Log and Toast for debugging
                    Log.i("RotinaDetalhes", "Rotinas carregadas: ${bodyList.size}")
                    Toast.makeText(context, "Rotinas carregadas: ${bodyList.size}", Toast.LENGTH_SHORT).show()

                    // Log formatted values for cada rotina
                    try {
                        for (r in sorted) {
                            val formattedDate = formatDateForDisplay(r.data_rotina, r.hora)
                            val formattedTime = formatTimeForDisplay(r.hora, r.data_rotina)
                            Log.i("ROTINA_PARSE", "id=${r.id_rotina} rawDate='${r.data_rotina}' rawHora='${r.hora}' -> formatted='${formattedDate} ${formattedTime}'")
                        }
                    } catch (_: Exception) {
                    }
                } else {
                    // handle non-successful response if needed
                    val eb = response.errorBody()?.string()
                    Log.w("RotinaDetalhes", "Falha ao obter rotinas para user=$userId: code=${response.code()} body=$eb")
                }
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
                                // Removido texto de debug que imprimia o primeiro objeto de rotina
                            }

                            itemsIndexed(rotinas) { index: Int, rotina: CadastroRotina ->
                                RotinaCard(rotina = rotina, _index = index)
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

private fun parseRotinaToEpochMillis(rotina: CadastroRotina): Long? {
    try {
        val dateRaw = rotina.data_rotina.trim()
        val timeRaw = rotina.hora.trim()

        val isoInstantFull = "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}(?:\\.\\d+)?Z".toRegex()
        val timeRegex = "(\\d{2}):(\\d{2})(?::(\\d{2}))?".toRegex()

        // Try to combine a date field with a time field first (prefer this over a standalone time instant)
        // 1) Pattern like "22T00:00:00.000Z/10/2025" -> day T time Z / month / year
        val dayTtime = "(\\d{1,2})T(\\d{2}:\\d{2}:\\d{2}(?:\\.\\d+)?Z)".toRegex().find(dateRaw)
        if (dayTtime != null) {
            val day = dayTtime.groupValues[1].toIntOrNull() ?: return null
            val timeIsoPart = dayTtime.groupValues[2]
            val parts = dateRaw.split("/")
            val month = parts.getOrNull(1)?.toIntOrNull()
            val year = parts.getOrNull(2)?.toIntOrNull()
            if (month != null && year != null) {
                // Prefer time components from the hora field if it contains an ISO instant (even when date is 1970)
                val horaIso = isoInstantFull.find(timeRaw)?.value
                if (horaIso != null) {
                    // extract HH:mm:ss from horaIso and combine with the parsed date
                    val tmHora = "(\\d{2}):(\\d{2}):(\\d{2})".toRegex().find(horaIso)
                    if (tmHora != null) {
                        val h = tmHora.groupValues[1].toIntOrNull() ?: 0
                        val m = tmHora.groupValues[2].toIntOrNull() ?: 0
                        val s = tmHora.groupValues[3].toIntOrNull() ?: 0
                        val ldt = LocalDateTime.of(year, month, day, h, m, s)
                        return ldt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
                    }
                }

                // Fallback: take time from the dateRaw's embedded time
                val tm = "(\\d{2}):(\\d{2}):(\\d{2})".toRegex().find(timeIsoPart)
                if (tm != null) {
                    val h = tm.groupValues[1].toInt()
                    val m = tm.groupValues[2].toInt()
                    val s = tm.groupValues[3].toInt()
                    val ldt = LocalDateTime.of(year, month, day, h, m, s)
                    return ldt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
                }
            }
        }

        // 2) If data in YYYY-MM-DD and timeRaw contains HH:mm or HH:mm:ss -> combine
        val ymd = "(\\d{4})-(\\d{2})-(\\d{2})".toRegex().find(dateRaw)
        if (ymd != null) {
            val y = ymd.groupValues[1].toIntOrNull()
            val mo = ymd.groupValues[2].toIntOrNull()
            val d = ymd.groupValues[3].toIntOrNull()
            if (y != null && mo != null && d != null) {
                val tm = timeRegex.find(timeRaw)
                val h = tm?.groupValues?.get(1)?.toIntOrNull() ?: 0
                val mi = tm?.groupValues?.get(2)?.toIntOrNull() ?: 0
                val s = tm?.groupValues?.get(3)?.toIntOrNull() ?: 0
                val ldt = LocalDateTime.of(y, mo, d, h, mi, s)
                return ldt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
            }
        }

        // 3) If data in dd/MM/yyyy and timeRaw contains HH:mm or HH:mm:ss -> combine
        val dmy = "(\\d{1,2})/(\\d{1,2})/(\\d{4})".toRegex().find(dateRaw)
        if (dmy != null) {
            val day = dmy.groupValues[1].toIntOrNull() ?: return null
            val month = dmy.groupValues[2].toIntOrNull() ?: return null
            val year = dmy.groupValues[3].toIntOrNull() ?: return null
            val tm = timeRegex.find(timeRaw)
            val h = tm?.groupValues?.get(1)?.toIntOrNull() ?: 0
            val mi = tm?.groupValues?.get(2)?.toIntOrNull() ?: 0
            val s = tm?.groupValues?.get(3)?.toIntOrNull() ?: 0
            val ldt = LocalDateTime.of(year, month, day, h, mi, s)
            return ldt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        }

        // 4) If dateRaw contains numeric day/month/year in any order and timeRaw has HH:mm -> combine
        val anyDateNums = "(\\d{1,2}).*(\\d{1,2}).*(\\d{4})".toRegex().find(dateRaw)
        if (anyDateNums != null) {
            val d = anyDateNums.groupValues[1].toIntOrNull() ?: return null
            val mo = anyDateNums.groupValues[2].toIntOrNull() ?: return null
            val y = anyDateNums.groupValues[3].toIntOrNull() ?: return null
            val tm = timeRegex.find(timeRaw)
            val h = tm?.groupValues?.get(1)?.toIntOrNull() ?: 0
            val mi = tm?.groupValues?.get(2)?.toIntOrNull() ?: 0
            val s = tm?.groupValues?.get(3)?.toIntOrNull() ?: 0
            val ldt = LocalDateTime.of(y, mo, d, h, mi, s)
            return ldt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        }

        // 5) If dateRaw itself contains a full ISO instant -> use it
        isoInstantFull.find(dateRaw)?.value?.let { return Instant.parse(it).toEpochMilli() }

        // 6) If timeRaw is a full ISO instant but dateRaw is empty or doesn't contain a date, then use timeRaw
        isoInstantFull.find(timeRaw)?.value?.let {
            // If dateRaw is non-empty and contains something that looks like a valid date (not epoch 1970), prefer not to use standalone time instant
            val looksLikeDate = dateRaw.isNotEmpty() && (dateRaw.contains("-") || dateRaw.contains("/") || dateRaw.any { it.isDigit() })
            if (!looksLikeDate) {
                return Instant.parse(it).toEpochMilli()
            } else {
                // Extract time components from the time ISO and try to merge with dateRaw heuristics (fallback)
                try {
                    val tm = "(\\d{2}):(\\d{2}):(\\d{2})".toRegex().find(it)
                    if (tm != null) {
                        val h = tm.groupValues[1].toIntOrNull() ?: 0
                        val m = tm.groupValues[2].toIntOrNull() ?: 0
                        val s = tm.groupValues[3].toIntOrNull() ?: 0
                        // try combine with YYYY-MM-DD
                        val ymd2 = "(\\d{4})-(\\d{2})-(\\d{2})".toRegex().find(dateRaw)
                        if (ymd2 != null) {
                            val y = ymd2.groupValues[1].toIntOrNull()
                            val mo = ymd2.groupValues[2].toIntOrNull()
                            val d = ymd2.groupValues[3].toIntOrNull()
                            if (y != null && mo != null && d != null) {
                                val ldt = LocalDateTime.of(y, mo, d, h, m, s)
                                return ldt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
                            }
                        }
                        val dmy2 = "(\\d{1,2})/(\\d{1,2})/(\\d{4})".toRegex().find(dateRaw)
                        if (dmy2 != null) {
                            val day = dmy2.groupValues[1].toIntOrNull() ?: 0
                            val mo = dmy2.groupValues[2].toIntOrNull() ?: 0
                            val y = dmy2.groupValues[3].toIntOrNull() ?: 1970
                            val ldt = LocalDateTime.of(y, mo, day, h, m, s)
                            return ldt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
                        }
                    }
                } catch (_: Exception) {
                }
                // otherwise fallback to the standalone time instant
                return Instant.parse(it).toEpochMilli()
            }
        }

    } catch (_: Exception) {
        // ignore and return null
    }
    return null
}

private fun formatDateForDisplay(apiDate: String, apiTime: String = ""): String {
    try {
        // Direct handling for patterns like "22T00:00:00.000Z/10/2025"
        val dayTPattern = "(\\d{1,2})T.*?/(\\d{1,2})/(\\d{4})".toRegex()
        val dayTMatch = dayTPattern.find(apiDate)
        if (dayTMatch != null) {
            val d = dayTMatch.groupValues[1].padStart(2, '0')
            val m = dayTMatch.groupValues[2].padStart(2, '0')
            val y = dayTMatch.groupValues[3]
            return "$d/$m/$y"
        }

        val epoch = parseRotinaToEpochMillis(CadastroRotina(
            id_rotina = 0,
            titulo_rotina = "",
            cor = "#FFFFFF",
            idUser = 0,
            titulo_item = "",
            descricao = "",
            hora = apiTime,
            data_rotina = apiDate
        ))
        if (epoch != null) {
            val instant = Instant.ofEpochMilli(epoch)
            val zdt = instant.atZone(ZoneId.systemDefault())
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy").withLocale(Locale.getDefault())
            return zdt.format(formatter)
        }
    } catch (_: Exception) {
    }
    // Fallbacks already present: try common formats without creating full object
    return try {
        val trimmed = apiDate.trim()
        val isoRegex = "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}(?:\\.\\d+)?Z".toRegex()
        val isoMatch = isoRegex.find(trimmed)?.value
        if (isoMatch != null) {
            val instant = Instant.parse(isoMatch)
            val zdt = instant.atZone(ZoneId.systemDefault())
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy").withLocale(Locale.getDefault())
            return zdt.format(formatter)
        }
        if (trimmed.contains("-") && !trimmed.contains("T")) {
            val parts = trimmed.split("-")
            if (parts.size >= 3) {
                val y = parts[0]
                val m = parts[1]
                val d = parts[2]
                return "$d/$m/$y"
            }
        }
        if (trimmed.contains("/")) {
            val parts = trimmed.split("/")
            for (p in parts) {
                val sub = p.trim()
                val match = isoRegex.find(sub)?.value
                if (match != null) {
                    val instant = Instant.parse(match)
                    val zdt = instant.atZone(ZoneId.systemDefault())
                    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy").withLocale(Locale.getDefault())
                    return zdt.format(formatter)
                }
            }
        }
        trimmed
    } catch (_: Exception) {
        apiDate
    }
}

private fun formatTimeForDisplay(apiTime: String, apiDate: String = ""): String {
    try {
        // Direct handling for ISO timestamps like "1970-01-01T10:00:00.000Z"
        val isoRegex = "\\d{4}-\\d{2}-\\d{2}T(\\d{2}:\\d{2}:\\d{2})(?:\\.\\d+)?Z".toRegex()
        val isoMatch = isoRegex.find(apiTime)
        if (isoMatch != null) {
            val timePart = isoMatch.groupValues[1]
            return timePart.substring(0, 5)
        }

        val epoch = parseRotinaToEpochMillis(CadastroRotina(
            id_rotina = 0,
            titulo_rotina = "",
            cor = "#FFFFFF",
            idUser = 0,
            titulo_item = "",
            descricao = "",
            hora = apiTime,
            data_rotina = apiDate
        ))
        if (epoch != null) {
            val instant = Instant.ofEpochMilli(epoch)
            val zdt = instant.atZone(ZoneId.systemDefault())
            val formatter = DateTimeFormatter.ofPattern("HH:mm").withLocale(Locale.getDefault())
            return zdt.format(formatter)
        }
    } catch (_: Exception) {
    }

    return try {
        val trimmed = apiTime.trim()
        val isoRegex = "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}(?:\\.\\d+)?Z".toRegex()
        val isoMatch = isoRegex.find(trimmed)?.value
        if (isoMatch != null) {
            val instant = Instant.parse(isoMatch)
            val zdt = instant.atZone(ZoneId.systemDefault())
            val formatter = DateTimeFormatter.ofPattern("HH:mm").withLocale(Locale.getDefault())
            return zdt.format(formatter)
        }
        val timeRegex = "\\d{2}:\\d{2}(?::\\d{2})?".toRegex()
        val timeMatch = timeRegex.find(trimmed)?.value
        if (timeMatch != null) {
            return if (timeMatch.length >= 5) timeMatch.substring(0, 5) else timeMatch
        }
        trimmed
    } catch (_: Exception) {
        apiTime
    }
}

@Suppress("UNUSED_PARAMETER")
@Composable
fun RotinaCard(rotina: CadastroRotina, _index: Int) {
    // try parse color, fallback to a default
    val color = try {
        Color(android.graphics.Color.parseColor(rotina.cor))
    } catch (_: Exception) {
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
                val dataExibicao = formatDateForDisplay(rotina.data_rotina, rotina.hora)
                Text(text = dataExibicao, color = textColor)
                Text(text = formatTimeForDisplay(rotina.hora, rotina.data_rotina), color = textColor)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RotinaDetalhesScreenPreview() {
    RotinaDetalhesScreen(navegacao = null)
}

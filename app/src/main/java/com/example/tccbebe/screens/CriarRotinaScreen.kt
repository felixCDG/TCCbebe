package br.senai.sp.jandira.telarotina.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.tccbebe.R
import com.example.tccbebe.model.CadastroItemRotina
import com.example.tccbebe.model.CadastroRotina
import com.example.tccbebe.model.CadastroUser
import com.example.tccbebe.service.AuthenticatedConexao
import com.example.tccbebe.utils.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.await
import kotlin.String

// Data class para representar um item de rotina
data class ItemRotina(
    var tituloGeral: String = "", // Título geral acima do card
    var titulo: String = "",
    var descricao: String = "",
    var hora: String = "00:00",
    var data: String = "",
    var cor: String = "#6C7CE7", // Cor padrão
    var tituloGeralError: String = "", // Erro do título geral
    var tituloError: String = "",
    var descricaoError: String = "",
    var horaError: String = "",
    var dataError: String = ""
)

// VisualTransformation para formatação de data
class DateVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val digits = text.text.filter { it.isDigit() }
        val formatted = when {
            digits.length <= 2 -> digits
            digits.length <= 4 -> "${digits.substring(0, 2)}/${digits.substring(2)}"
            digits.length <= 8 -> "${digits.substring(0, 2)}/${digits.substring(2, 4)}/${digits.substring(4)}"
            else -> "${digits.substring(0, 2)}/${digits.substring(2, 4)}/${digits.substring(4, 8)}"
        }
        
        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                val digitsBeforeOffset = text.text.take(offset).count { it.isDigit() }
                return when {
                    digitsBeforeOffset <= 2 -> digitsBeforeOffset
                    digitsBeforeOffset <= 4 -> digitsBeforeOffset + 1 // +1 for first slash
                    digitsBeforeOffset <= 8 -> digitsBeforeOffset + 2 // +2 for both slashes
                    else -> formatted.length
                }
            }
            
            override fun transformedToOriginal(offset: Int): Int {
                val slashesBeforeOffset = formatted.take(offset).count { it == '/' }
                return (offset - slashesBeforeOffset).coerceAtMost(text.text.length)
            }
        }
        
        return TransformedText(AnnotatedString(formatted), offsetMapping)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CriarRotinaScreen(navegacao: NavHostController?) {
    // Lista de itens de rotina
    var itensRotina = remember { mutableStateOf(listOf(ItemRotina())) }
    
    // Estados para validação geral
    var showErrorDialog = remember { mutableStateOf(false) }
    var errorMessage = remember { mutableStateOf("") }

    val context = LocalContext.current
    val clienteApi = AuthenticatedConexao(context).getItemRotinaService()
    val rotinaApi = AuthenticatedConexao(context).getRotinaService()

    // Cores disponíveis para seleção
    val coresDisponiveis = listOf(
        "#E57373", // Vermelho
        "#FFB74D", // Laranja
        "#FFF176", // Amarelo
        "#81C784", // Verde
        "#64B5F6", // Azul
        "#9575CD", // Roxo
        "#BA68C8", // Rosa
        "#4FC3F7"  // Ciano
    )

    // Função para adicionar novo item de rotina
    fun adicionarNovoItem() {
        val novosItens = itensRotina.value.toMutableList()
        novosItens.add(ItemRotina())
        itensRotina.value = novosItens
    }
    
    // Função para remover item de rotina
    fun removerItem(index: Int) {
        if (itensRotina.value.size > 1) {
            val novosItens = itensRotina.value.toMutableList()
            novosItens.removeAt(index)
            itensRotina.value = novosItens
        }
    }
    
    // Função para atualizar item específico
    fun atualizarItem(index: Int, novoItem: ItemRotina) {
        val novosItens = itensRotina.value.toMutableList()
        novosItens[index] = novoItem
        itensRotina.value = novosItens
    }

    // Função para filtrar apenas dígitos da data
    fun filterDateDigits(input: String): String {
        return input.filter { it.isDigit() }.take(8) // Máximo 8 dígitos (DDMMAAAA)
    }
    
    // Função para formatar data apenas para exibição (usada na validação)
    fun formatDateForDisplay(digits: String): String {
        return when {
            digits.length <= 2 -> digits
            digits.length <= 4 -> "${digits.substring(0, 2)}/${digits.substring(2)}"
            digits.length <= 8 -> "${digits.substring(0, 2)}/${digits.substring(2, 4)}/${digits.substring(4)}"
            else -> "${digits.substring(0, 2)}/${digits.substring(2, 4)}/${digits.substring(4, 8)}"
        }
    }
    
    // Função para converter data DDMMAAAA para YYYY-MM-DD (formato da API)
    fun convertDateToApiFormat(dateDigits: String): String {
        if (dateDigits.length != 8) return dateDigits
        
        val day = dateDigits.substring(0, 2)
        val month = dateDigits.substring(2, 4)
        val year = dateDigits.substring(4, 8)
        
        return "$year-$month-$day"
    }
    
    // Função para validar formato de data
    fun isValidDate(dateDigits: String): Boolean {
        if (dateDigits.length != 8) return false
        
        val day = dateDigits.substring(0, 2).toIntOrNull() ?: return false
        val month = dateDigits.substring(2, 4).toIntOrNull() ?: return false
        val year = dateDigits.substring(4, 8).toIntOrNull() ?: return false
        
        return day in 1..31 && month in 1..12 && year >= 1900
    }

    // Função para validar todos os itens
    fun validarCampos(): Boolean {
        var isValid = true
        val erros = mutableListOf<String>()
        
        // Validar cada item da rotina
        itensRotina.value.forEachIndexed { index, item ->
            val itemAtualizado = item.copy()
            
            // Limpar erros anteriores do item
            itemAtualizado.tituloGeralError = ""
            itemAtualizado.tituloError = ""
            itemAtualizado.descricaoError = ""
            itemAtualizado.horaError = ""
            itemAtualizado.dataError = ""
            
            // Validar título geral
            if (item.tituloGeral.trim().isEmpty()) {
                itemAtualizado.tituloGeralError = "Título é obrigatório"
                erros.add("• Item ${index + 1}: Título é obrigatório")
                isValid = false
            } else if (item.tituloGeral.trim().length < 3) {
                itemAtualizado.tituloGeralError = "Título deve ter pelo menos 3 caracteres"
                erros.add("• Item ${index + 1}: Título deve ter pelo menos 3 caracteres")
                isValid = false
            }
            
            // Validar título
            if (item.titulo.trim().isEmpty()) {
                itemAtualizado.tituloError = "Título é obrigatório"
                erros.add("• Item ${index + 1}: Título é obrigatório")
                isValid = false
            } else if (item.titulo.trim().length < 3) {
                itemAtualizado.tituloError = "Título deve ter pelo menos 3 caracteres"
                erros.add("• Item ${index + 1}: Título deve ter pelo menos 3 caracteres")
                isValid = false
            }
            
            // Validar descrição
            if (item.descricao.trim().isEmpty()) {
                itemAtualizado.descricaoError = "Descrição é obrigatória"
                erros.add("• Item ${index + 1}: Descrição é obrigatória")
                isValid = false
            } else if (item.descricao.trim().length < 5) {
                itemAtualizado.descricaoError = "Descrição deve ter pelo menos 5 caracteres"
                erros.add("• Item ${index + 1}: Descrição deve ter pelo menos 5 caracteres")
                isValid = false
            }
            
            // Validar hora
            if (item.hora.trim().isEmpty() || item.hora == "00:00") {
                itemAtualizado.horaError = "Hora é obrigatória"
                erros.add("• Item ${index + 1}: Hora é obrigatória")
                isValid = false
            } else {
                // Validar formato da hora (HH:MM)
                val horaRegex = Regex("^([01]?[0-9]|2[0-3]):[0-5][0-9]$")
                if (!horaRegex.matches(item.hora)) {
                    itemAtualizado.horaError = "Formato de hora inválido (HH:MM)"
                    erros.add("• Item ${index + 1}: Formato de hora inválido (use HH:MM)")
                    isValid = false
                }
            }
            
            // Validar data
            if (item.data.trim().isEmpty()) {
                itemAtualizado.dataError = "Data é obrigatória"
                erros.add("• Item ${index + 1}: Data é obrigatória")
                isValid = false
            } else if (!isValidDate(item.data)) {
                itemAtualizado.dataError = "Data inválida (use DD/MM/AAAA)"
                erros.add("• Item ${index + 1}: Data inválida (use DD/MM/AAAA)")
                isValid = false
            }
            
            // Atualizar o item com os erros
            atualizarItem(index, itemAtualizado)
        }
        
        // Se houver erros, mostrar diálogo
        if (!isValid) {
            errorMessage.value = "Por favor, corrija os seguintes erros:\n\n${erros.joinToString("\n")}"
            showErrorDialog.value = true
        }
        
        return isValid
    }
    
    // Define font family - using system default for now
    val kronaOneFont = FontFamily.Default
    
    // Componente para renderizar um item de rotina
    @Composable
    fun ItemRotinaCard(
        item: ItemRotina,
        index: Int,
        onItemChange: (ItemRotina) -> Unit,
        onRemove: () -> Unit,
        showRemoveButton: Boolean
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Campo de título geral acima do card
            Text(
                text = "Título",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = kronaOneFont,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            
            OutlinedTextField(
                value = item.tituloGeral,
                onValueChange = { 
                    val novoItem = item.copy(tituloGeral = it)
                    if (item.tituloGeralError.isNotEmpty()) {
                        novoItem.tituloGeralError = ""
                    }
                    onItemChange(novoItem)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                placeholder = {
                    Text(
                        text = "Digite o título da rotina",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = if (item.tituloGeralError.isNotEmpty()) Color.Red else Color(0xFF6C7CE7),
                    unfocusedBorderColor = if (item.tituloGeralError.isNotEmpty()) Color.Red else Color.Gray
                ),
                shape = RoundedCornerShape(12.dp),
                isError = item.tituloGeralError.isNotEmpty()
            )
            
            if (item.tituloGeralError.isNotEmpty()) {
                Text(
                    text = item.tituloGeralError,
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
                )
            }
            
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(android.graphics.Color.parseColor(item.cor))
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header do item com número e botão remover
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Item ${index + 1}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = kronaOneFont,
                        color = Color.White
                    )
                    
                    if (showRemoveButton) {
                        IconButton(
                            onClick = onRemove,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Remover item",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
                
                // Título and Hora Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Título
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Tarefa",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            fontFamily = kronaOneFont,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = item.titulo,
                            onValueChange = { 
                                val novoItem = item.copy(titulo = it)
                                if (item.tituloError.isNotEmpty()) {
                                    novoItem.tituloError = ""
                                }
                                onItemChange(novoItem)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color(0xFF8A94F0),
                                unfocusedContainerColor = Color(0xFF8A94F0),
                                focusedBorderColor = if (item.tituloError.isNotEmpty()) Color.Red else Color.Transparent,
                                unfocusedBorderColor = if (item.tituloError.isNotEmpty()) Color.Red else Color.Transparent
                            ),
                            shape = RoundedCornerShape(24.dp),
                            isError = item.tituloError.isNotEmpty()
                        )
                        if (item.tituloError.isNotEmpty()) {
                            Text(
                                text = item.tituloError,
                                color = Color.Red,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(start = 8.dp, top = 2.dp)
                            )
                        }
                    }
                    
                    // Hora
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Hora",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            fontFamily = kronaOneFont,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = item.hora,
                            onValueChange = { 
                                val novoItem = item.copy(hora = it)
                                if (item.horaError.isNotEmpty()) {
                                    novoItem.horaError = ""
                                }
                                onItemChange(novoItem)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color(0xFF8A94F0),
                                unfocusedContainerColor = Color(0xFF8A94F0),
                                focusedBorderColor = if (item.horaError.isNotEmpty()) Color.Red else Color.Transparent,
                                unfocusedBorderColor = if (item.horaError.isNotEmpty()) Color.Red else Color.Transparent
                            ),
                            shape = RoundedCornerShape(24.dp),
                            isError = item.horaError.isNotEmpty()
                        )
                        if (item.horaError.isNotEmpty()) {
                            Text(
                                text = item.horaError,
                                color = Color.Red,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(start = 8.dp, top = 2.dp)
                            )
                        }
                    }
                }
                
                // Descrição and Data Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Descrição
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Descrição",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            fontFamily = kronaOneFont,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = item.descricao,
                            onValueChange = { 
                                val novoItem = item.copy(descricao = it)
                                if (item.descricaoError.isNotEmpty()) {
                                    novoItem.descricaoError = ""
                                }
                                onItemChange(novoItem)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color(0xFF8A94F0),
                                unfocusedContainerColor = Color(0xFF8A94F0),
                                focusedBorderColor = if (item.descricaoError.isNotEmpty()) Color.Red else Color.Transparent,
                                unfocusedBorderColor = if (item.descricaoError.isNotEmpty()) Color.Red else Color.Transparent
                            ),
                            shape = RoundedCornerShape(24.dp),
                            isError = item.descricaoError.isNotEmpty()
                        )
                        if (item.descricaoError.isNotEmpty()) {
                            Text(
                                text = item.descricaoError,
                                color = Color.Red,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(start = 8.dp, top = 2.dp)
                            )
                        }
                    }
                    
                    // Data
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Data",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            fontFamily = kronaOneFont,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedTextField(
                            value = item.data,
                            onValueChange = { newValue ->
                                val digitsOnly = filterDateDigits(newValue)
                                val novoItem = item.copy(data = digitsOnly)
                                if (item.dataError.isNotEmpty()) {
                                    novoItem.dataError = ""
                                }
                                onItemChange(novoItem)
                            },
                            visualTransformation = DateVisualTransformation(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            placeholder = {
                                Text(
                                    text = "DD/MM/AAAA",
                                    color = Color.White.copy(alpha = 0.7f),
                                    fontSize = 14.sp
                                )
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color(0xFF8A94F0),
                                unfocusedContainerColor = Color(0xFF8A94F0),
                                focusedBorderColor = if (item.dataError.isNotEmpty()) Color.Red else Color.Transparent,
                                unfocusedBorderColor = if (item.dataError.isNotEmpty()) Color.Red else Color.Transparent
                            ),
                            shape = RoundedCornerShape(24.dp),
                            isError = item.dataError.isNotEmpty()
                        )
                        if (item.dataError.isNotEmpty()) {
                            Text(
                                text = item.dataError,
                                color = Color.Red,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(start = 8.dp, top = 2.dp)
                            )
                        }
                    }
                }
                
                // Seletor de cor
                Column {
                    Text(
                        text = "Cor",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = kronaOneFont,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(4),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(coresDisponiveis) { cor ->
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .background(
                                        Color(android.graphics.Color.parseColor(cor)),
                                        CircleShape
                                    )
                                    .border(
                                        width = if (cor == item.cor) 3.dp else 0.dp,
                                        color = Color.White,
                                        shape = CircleShape
                                    )
                                    .clickable { 
                                        val novoItem = item.copy(cor = cor)
                                        onItemChange(novoItem)
                                    }
                            )
                        }
                    }
                }
            }
        }
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // Header
        TopAppBar(
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo_baby),
                        contentDescription = "S♥S Baby Logo",
                        modifier = Modifier
                            .size(80.dp)
                    )
                }
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
                    Image(
                        painter = painterResource(id = R.drawable.notificacoes),
                        contentDescription = "S♥S Baby Logo",
                        modifier = Modifier
                            .size(50.dp)
                    )
                }
                IconButton(onClick = { }) {
                    Image(
                        painter = painterResource(id = R.drawable.perfil),
                        contentDescription = "S♥S Baby Logo",
                        modifier = Modifier
                            .size(50.dp)

                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White
            )
        )
        
        // Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            
            // Title
            Text(
                text = "Crie uma\nnova Rotina",
                fontSize = 24.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = kronaOneFont,
                color = Color.Black,
                lineHeight = 28.sp
            )
            
            Spacer(modifier = Modifier.height(40.dp))
            
            // Lista de itens de rotina
            itensRotina.value.forEachIndexed { index, item ->
                ItemRotinaCard(
                    item = item,
                    index = index,
                    onItemChange = { novoItem ->
                        atualizarItem(index, novoItem)
                    },
                    onRemove = {
                        removerItem(index)
                    },
                    showRemoveButton = itensRotina.value.size > 1
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Botões de ação
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Add Button
                IconButton(
                    onClick = { adicionarNovoItem() },
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            Color(0xFF6C7CE7),
                            RoundedCornerShape(20.dp)
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Adicionar item",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
                
                // Criar Button
                Button(
                    onClick = {
                        // Validar campos antes de enviar
                        if (!validarCampos()) {
                            Log.w("ROTINA_DEBUG", "❌ Validação falhou - campos obrigatórios não preenchidos")
                            return@Button
                        }

                        // Primeiro criar a rotina principal com a cor do primeiro item
                        val primeiroItem = itensRotina.value.first()
                        val userId = SessionManager.getUserId(context)
                        
                        val rotinaPrincipal = CadastroRotina(
                            id_rotina = 0,
                            titulo = primeiroItem.tituloGeral.trim(),
                            cor = primeiroItem.cor, // Enviando a cor selecionada
                            idUser = userId
                        )

                        Log.i("ROTINA_DEBUG", "=== CRIANDO ROTINA PRINCIPAL ===")
                        Log.i("ROTINA_DEBUG", "Título: '${rotinaPrincipal.titulo}'")
                        Log.i("ROTINA_DEBUG", "Cor: '${rotinaPrincipal.cor}'")
                        Log.i("ROTINA_DEBUG", "User ID: ${rotinaPrincipal.idUser}")

                        GlobalScope.launch(Dispatchers.IO) {
                            try {
                                // Lista para coletar IDs dos itens criados
                                val itemIdsCriados = mutableListOf<Int>()

                                // Processar todos os itens para envio PRIMEIRO
                                itensRotina.value.forEachIndexed { index, item ->
                                    // Converter data para formato da API (YYYY-MM-DD)
                                    val dataFormatada = convertDateToApiFormat(item.data.trim())
                                    val dataExibicao = formatDateForDisplay(item.data.trim())
                                    Log.i("ROTINA_DEBUG", "Item ${index + 1} - Data dígitos: '${item.data.trim()}' -> Exibição: '$dataExibicao' -> API: '$dataFormatada'")
                                    
                                    val cliente = CadastroItemRotina(
                                        id_item = 0,
                                        titulo = item.titulo.trim(), // Usando o título do campo "Tarefa"
                                        descricao = item.descricao.trim(),
                                        data_rotina = dataFormatada,
                                        hora = item.hora.trim(),
                                    )

                                    // 🔍 LOGS DETALHADOS - DADOS DO BODY
                                    Log.i("ROTINA_DEBUG", "=== DADOS SENDO ENVIADOS - ITEM ${index + 1} ===")
                                    Log.i("ROTINA_DEBUG", "Body da requisição: $cliente")
                                    Log.i("ROTINA_DEBUG", "id_item: ${cliente.id_item}")
                                    Log.i("ROTINA_DEBUG", "titulo: '${cliente.titulo}'")
                                    Log.i("ROTINA_DEBUG", "descricao: '${cliente.descricao}'")
                                    Log.i("ROTINA_DEBUG", "data_rotina: '${cliente.data_rotina}'")
                                    Log.i("ROTINA_DEBUG", "hora: '${cliente.hora}'")

                                    try {
                                        Log.i("ROTINA_DEBUG", "🚀 Fazendo chamada para API - Item ${index + 1}...")
                                        val response = clienteApi.cadastrarItemR(cliente).await()

                                        Log.i("ROTINA_DEBUG", "=== RESPOSTA DA API - ITEM ${index + 1} ===")
                                        Log.i("ROTINA_DEBUG", "✅ Resposta recebida: $response")
                                        Log.i("ROTINA_DEBUG", "Status: ${response.status}")
                                        Log.i("ROTINA_DEBUG", "Status Code: ${response.status_code}")
                                        Log.i("ROTINA_DEBUG", "Mensagem: ${response.message}")
                                        Log.i("ROTINA_DEBUG", "Data: ${response.data}")

                                    } catch (e: Exception) {
                                        Log.e("ROTINA_DEBUG", "=== ERRO NA REQUISIÇÃO - ITEM ${index + 1} ===")
                                        Log.e("ROTINA_DEBUG", "❌ Erro ao cadastrar: ${e.message}")
                                        Log.e("ROTINA_DEBUG", "Tipo do erro: ${e.javaClass.simpleName}")
                                        Log.e("ROTINA_DEBUG", "Stack trace: ${e.stackTrace.contentToString()}")
                                        e.printStackTrace()
                                    }
                                }

                            } catch (e: Exception) {
                                Log.e("ROTINA_DEBUG", "=== ERRO NA CRIAÇÃO DA ROTINA PRINCIPAL ===")
                                Log.e("ROTINA_DEBUG", "❌ Erro ao criar rotina: ${e.message}")
                                Log.e("ROTINA_DEBUG", "Tipo do erro: ${e.javaClass.simpleName}")
                                e.printStackTrace()
                            }
                        }
                        
                        // Navegar após processar todos os itens
                        navegacao?.navigate("login")
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF6C7CE7),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(
                        text = "Criar Rotina",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        fontFamily = kronaOneFont
                    )
                }
            }
        }
    }
    
    // Diálogo de erro
    if (showErrorDialog.value) {
        AlertDialog(
            onDismissRequest = { showErrorDialog.value = false },
            title = {
                Text(
                    text = "Erro de Validação",
                    fontWeight = FontWeight.Bold,
                    color = Color.Red
                )
            },
            text = {
                Text(
                    text = errorMessage.value,
                    color = Color.Black
                )
            },
            confirmButton = {
                Button(
                    onClick = { showErrorDialog.value = false },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF6C7CE7)
                    )
                ) {
                    Text("OK", color = Color.White)
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CriarRotinaScreenPreview() {
    CriarRotinaScreen(
        navegacao = null
    )
}

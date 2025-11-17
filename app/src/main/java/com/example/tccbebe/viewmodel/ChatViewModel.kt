package com.example.tccbebe.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tccbebe.model.Mensagem
import com.example.tccbebe.repository.ChatRepository
import com.example.tccbebe.utils.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ChatUiState(
    val mensagens: List<Mensagem> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isEnviandoMensagem: Boolean = false
)

class ChatViewModel(private val context: Context) : ViewModel() {
    
    private val repository = ChatRepository(context)
    
    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()
    
    // ID do usuário atual obtido do SessionManager
    private fun getCurrentUserId(): String {
        val userId = SessionManager.getUserId(context)
        return if (userId != -1) userId.toString() else "1" // Fallback para ID 1 se não estiver logado
    }
    
    // Verificar se a mensagem foi enviada pelo usuário atual
    fun isMensagemEnviada(mensagem: Mensagem): Boolean {
        return mensagem.id_user == getCurrentUserId() || mensagem.remetente == "Você"
    }
    
    fun carregarMensagens(chatId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            println("📥 [CHAT] Carregando mensagens para chat ID: $chatId")
            println("📥 [CHAT] Usuario atual: ${getCurrentUserId()}")
            
            repository.getMessagesByChat(chatId)
                .onSuccess { mensagens ->
                    println("✅ [CHAT] Mensagens carregadas: ${mensagens.size} mensagens")
                    if (mensagens.isNotEmpty()) {
                        mensagens.forEach { msg ->
                            println("📝 [CHAT] Mensagem: '${msg.conteudo}' - User: ${msg.id_user} - Chat: ${msg.id_chat} - Criada: ${msg.created_at}")
                        }
                    } else {
                        println("ℹ️ [CHAT] Nenhuma mensagem encontrada para o chat $chatId")
                    }
                    
                    _uiState.value = _uiState.value.copy(
                        mensagens = mensagens.sortedBy { it.created_at },
                        isLoading = false
                    )
                }
                .onFailure { exception ->
                    println("❌ [CHAT] Erro ao carregar mensagens: ${exception.message}")
                    println("❌ [CHAT] Stack trace: ${exception.stackTrace?.take(3)?.joinToString()}")
                    
                    // Se for erro 404 ou chat sem mensagens, não mostrar erro - apenas chat vazio
                    if (exception.message?.contains("404") == true || 
                        exception.message?.contains("não encontrado") == true ||
                        exception.message?.contains("not found") == true ||
                        exception.message?.contains("Nenhuma mensagem") == true) {
                        println("ℹ️ [CHAT] Chat sem mensagens anteriores - iniciando chat vazio")
                        _uiState.value = _uiState.value.copy(
                            mensagens = emptyList(),
                            isLoading = false,
                            errorMessage = null
                        )
                    } else {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = "Erro ao carregar mensagens: ${exception.message}"
                        )
                    }
                }
        }
    }
    
    fun enviarMensagem(conteudo: String, chatId: String) {
        if (conteudo.isBlank()) {
            println("⚠️ [CHAT] Tentativa de enviar mensagem vazia")
            return
        }
        
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isEnviandoMensagem = true)
            
            val userId = getCurrentUserId()
            println("📤 [CHAT] Enviando mensagem: '$conteudo' para chat $chatId como usuário $userId")
            
            // Criar mensagem temporária para mostrar imediatamente na UI
            val mensagemTemporaria = Mensagem(
                id = "temp_${System.currentTimeMillis()}",
                conteudo = conteudo,
                id_chat = chatId,
                id_user = userId,
                created_at = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", java.util.Locale.getDefault()).format(java.util.Date()),
                remetente = "Você"
            )
            
            // Adicionar mensagem temporária à lista
            val mensagensAtuais = _uiState.value.mensagens.toMutableList()
            mensagensAtuais.add(mensagemTemporaria)
            _uiState.value = _uiState.value.copy(mensagens = mensagensAtuais)
            println("📝 [CHAT] Mensagem temporária adicionada à UI: ${mensagemTemporaria.id}")
            
            repository.enviarMensagem(conteudo, chatId, userId)
                .onSuccess { mensagem ->
                    println("✅ [CHAT] Mensagem enviada com sucesso: ${mensagem.id}")
                    println("✅ [CHAT] Conteúdo: '${mensagem.conteudo}' - Chat: ${mensagem.id_chat}")
                    _uiState.value = _uiState.value.copy(isEnviandoMensagem = false)
                    
                    // Remover mensagem temporária e recarregar para pegar a mensagem real
                    println("🔄 [CHAT] Recarregando mensagens após envio...")
                    carregarMensagens(chatId)
                }
                .onFailure { exception ->
                    println("❌ [CHAT] Erro ao enviar mensagem: ${exception.message}")
                    println("❌ [CHAT] Stack trace: ${exception.stackTrace?.take(3)?.joinToString()}")
                    
                    // Remover mensagem temporária em caso de erro
                    val mensagensSemTemp = _uiState.value.mensagens.filter { !it.id.startsWith("temp_") }
                    _uiState.value = _uiState.value.copy(
                        mensagens = mensagensSemTemp,
                        isEnviandoMensagem = false,
                        errorMessage = "Erro ao enviar mensagem: ${exception.message}"
                    )
                }
        }
    }
    
    fun criarOuBuscarChat(contatoId: String, contatoNome: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            // Para contatos de teste, usar o contatoId diretamente como chatId
            // Isso simplifica a lógica e funciona com o backend atual
            println("🔄 [CHAT] Iniciando chat com contato ID: $contatoId ($contatoNome)")
            println("🔄 [CHAT] Usuário atual: ${getCurrentUserId()}")
            carregarMensagens(contatoId)
        }
    }
    
    fun limparErro() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
    
    
    // Método para recarregar mensagens
    fun recarregarMensagens(chatId: String) {
        carregarMensagens(chatId)
    }
    
}

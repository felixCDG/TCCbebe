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
    
    fun carregarMensagens(chatId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            println("📥 Carregando mensagens para chat ID: $chatId")
            
            repository.getMessagesByChat(chatId)
                .onSuccess { mensagens ->
                    println("✅ Mensagens carregadas: ${mensagens.size} mensagens")
                    mensagens.forEach { msg ->
                        println("📝 Mensagem: '${msg.conteudo}' - User: ${msg.id_user} - Chat: ${msg.id_chat}")
                    }
                    
                    _uiState.value = _uiState.value.copy(
                        mensagens = mensagens.sortedBy { it.created_at },
                        isLoading = false
                    )
                }
                .onFailure { exception ->
                    println("❌ Erro ao carregar mensagens: ${exception.message}")
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = exception.message
                    )
                }
        }
    }
    
    fun enviarMensagem(conteudo: String, chatId: String) {
        if (conteudo.isBlank()) return
        
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isEnviandoMensagem = true)
            
            println("📤 Enviando mensagem: '$conteudo' para chat $chatId")
            
            repository.enviarMensagem(conteudo, chatId, getCurrentUserId())
                .onSuccess { mensagem ->
                    println("✅ Mensagem processada: ${mensagem.id}")
                    _uiState.value = _uiState.value.copy(isEnviandoMensagem = false)
                    
                    // Recarregar mensagens para mostrar a nova mensagem
                    carregarMensagens(chatId)
                }
                .onFailure { exception ->
                    println("❌ Erro ao processar mensagem: ${exception.message}")
                    _uiState.value = _uiState.value.copy(
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
            println("🔄 Carregando mensagens para chat ID: $contatoId")
            carregarMensagens(contatoId)
        }
    }
    
    fun limparErro() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
    
    // Método para verificar se uma mensagem foi enviada pelo usuário atual
    fun isMensagemEnviada(mensagem: Mensagem): Boolean {
        return mensagem.id_user == getCurrentUserId()
    }
    
    // Método para recarregar mensagens
    fun recarregarMensagens(chatId: String) {
        carregarMensagens(chatId)
    }
    
}

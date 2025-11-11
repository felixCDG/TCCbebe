package com.example.tccbebe.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.tccbebe.model.Contato
import com.example.tccbebe.repository.ChatRepository

data class ContatosUiState(
    val contatos: List<Contato> = emptyList(),
    val searchText: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class ContatosViewModel(private val context: Context) : ViewModel() {
    private val repository = ChatRepository(context)
    private val _uiState = MutableStateFlow(ContatosUiState())
    val uiState: StateFlow<ContatosUiState> = _uiState.asStateFlow()

    init {
        carregarContatos()
    }

    private fun carregarContatos() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            
            // Contatos de teste sempre disponíveis (IDs numéricos para compatibilidade com API)
            val contatosTeste = listOf(
                Contato(
                    id = "1",
                    nome = "Dr. Maria Santos",
                    email = "maria.santos@clinica.com",
                    tipo = "Pediatra - Teste",
                    status = "online"
                ),
                Contato(
                    id = "2",
                    nome = "Enfermeira Ana Silva",
                    email = "ana.silva@hospital.com",
                    tipo = "Enfermeira - Teste",
                    status = "online"
                ),
                Contato(
                    id = "3",
                    nome = "Dr. João Pereira",
                    email = "joao.pereira@clinica.com",
                    tipo = "Cardiologista - Teste",
                    status = "ocupado"
                ),
                Contato(
                    id = "4",
                    nome = "Clínica Pediatra Central",
                    email = "contato@pediatracentral.com",
                    tipo = "Clínica - Teste",
                    status = "online"
                ),
                Contato(
                    id = "5",
                    nome = "Mãe do Gabriel - Teste",
                    email = "mae.gabriel@email.com",
                    tipo = "Mãe - Teste",
                    status = "offline"
                )
            )
            
            // Primeiro, sempre mostrar os contatos de teste
            _uiState.value = _uiState.value.copy(
                contatos = contatosTeste,
                isLoading = false,
                errorMessage = null
            )
            
            // Depois, tentar carregar contatos da API em background
            try {
                repository.getAllContatos()
                    .onSuccess { contatosApi ->
                        val todosContatos = contatosTeste + contatosApi
                        _uiState.value = _uiState.value.copy(
                            contatos = todosContatos,
                            errorMessage = null
                        )
                    }
                    .onFailure { exception ->
                        // Manter os contatos de teste mesmo se a API falhar
                        println("Erro ao carregar contatos da API: ${exception.message}")
                    }
            } catch (e: Exception) {
                println("Erro ao tentar conectar com a API: ${e.message}")
                // Contatos de teste já estão carregados, não fazer nada
            }
        }
    }

    fun atualizarBusca(texto: String) {
        _uiState.value = _uiState.value.copy(searchText = texto)
    }

    fun contatosFiltrados(): List<Contato> {
        val searchText = _uiState.value.searchText
        return if (searchText.isBlank()) {
            _uiState.value.contatos
        } else {
            _uiState.value.contatos.filter { contato ->
                contato.nome.contains(searchText, ignoreCase = true) ||
                contato.tipo.contains(searchText, ignoreCase = true)
            }
        }
    }

    fun recarregarContatos() {
        carregarContatos()
    }
    
    // Método para debug - força carregar apenas contatos de teste
    fun carregarContatosTeste() {
        val contatosTeste = listOf(
            Contato(
                id = "1",
                nome = "Dr. Maria Santos",
                email = "maria.santos@clinica.com",
                tipo = "Pediatra - Teste",
                status = "online"
            ),
            Contato(
                id = "2",
                nome = "Enfermeira Ana Silva",
                email = "ana.silva@hospital.com",
                tipo = "Enfermeira - Teste",
                status = "online"
            ),
            Contato(
                id = "3",
                nome = "Dr. João Pereira",
                email = "joao.pereira@clinica.com",
                tipo = "Cardiologista - Teste",
                status = "ocupado"
            ),
            Contato(
                id = "4",
                nome = "Clínica Pediatra Central",
                email = "contato@pediatracentral.com",
                tipo = "Clínica - Teste",
                status = "online"
            ),
            Contato(
                id = "5",
                nome = "Mãe do Gabriel - Teste",
                email = "mae.gabriel@email.com",
                tipo = "Mãe - Teste",
                status = "offline"
            )
        )
        
        _uiState.value = _uiState.value.copy(
            contatos = contatosTeste,
            isLoading = false,
            errorMessage = null
        )
    }
}

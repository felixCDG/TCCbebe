package com.example.tccbebe.utils

import androidx.navigation.NavHostController
import java.util.UUID

object VideoChamadaUtils {
    
    /**
     * Navega para uma videochamada com nome de sala específico
     */
    fun iniciarVideoChamada(navegacao: NavHostController?, roomName: String) {
        navegacao?.navigate("videochamada/$roomName")
    }
    
    /**
     * Navega para uma videochamada gerando um nome de sala aleatório
     */
    fun iniciarVideoChamadaAleatoria(navegacao: NavHostController?) {
        val roomName = "sala-${UUID.randomUUID().toString().take(8)}"
        navegacao?.navigate("videochamada/$roomName")
    }
    
    /**
     * Navega para videochamada sem parâmetros (sala será gerada automaticamente)
     */
    fun iniciarVideoChamadaSimples(navegacao: NavHostController?) {
        navegacao?.navigate("videochamada")
    }
    
    /**
     * Gera um nome de sala baseado em dois usuários
     */
    fun gerarNomeSalaParaUsuarios(userId1: Int, userId2: Int): String {
        val ids = listOf(userId1, userId2).sorted()
        return "sala-${ids[0]}-${ids[1]}"
    }
}

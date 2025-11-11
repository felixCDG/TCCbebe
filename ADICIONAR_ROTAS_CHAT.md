# 🔧 Como Adicionar as Rotas de Chat no MainActivity.kt

## ⚠️ AÇÃO NECESSÁRIA

Para que o sistema de chat funcione completamente, você precisa adicionar as rotas no `MainActivity.kt`.

## 📝 Passos para Adicionar:

### 1. Adicionar Imports
No arquivo `MainActivity.kt`, adicione estes imports após a linha 30:

```kotlin
import com.example.tccbebe.screens.ContatosScreen
import com.example.tccbebe.screens.ChatIndividualScreen
```

### 2. Adicionar Rotas
Dentro do `NavHost`, após a linha 61 (`composable(route = "home",) { HomeScreen(navegacao = navegacao) }`), adicione:

```kotlin
// Rotas do Chat
composable(route = "contatos") { 
    ContatosScreen(navController = navegacao) 
}
composable(
    route = "chat/{contatoId}/{contatoNome}",
    arguments = listOf(
        navArgument("contatoId") { type = NavType.StringType },
        navArgument("contatoNome") { type = NavType.StringType }
    )
) { backStackEntry ->
    val contatoId = backStackEntry.arguments?.getString("contatoId") ?: ""
    val contatoNome = backStackEntry.arguments?.getString("contatoNome") ?: ""
    ChatIndividualScreen(
        navController = navegacao,
        contatoId = contatoId,
        contatoNome = contatoNome
    )
}
```

### 3. Adicionar Import para NavArgument
Também adicione este import no topo do arquivo:

```kotlin
import androidx.navigation.NavType
import androidx.navigation.navArgument
```

## 📱 Resultado Final

Após adicionar essas rotas, o fluxo de navegação funcionará:
- Home → Chat → Contatos → Chat Individual

## ✅ Como Testar

1. Execute o app
2. Vá para Home
3. Clique no botão "Chat" 
4. Deve navegar para a tela de contatos
5. Clique em um contato
6. Deve abrir o chat individual

## 🔍 Verificação

Se tudo estiver correto, você verá:
- Lista de contatos carregando da API
- Chat individual funcional
- Envio de mensagens funcionando

## 🆘 Se Houver Problemas

1. Verifique se todos os imports foram adicionados
2. Confirme se as rotas estão dentro do `NavHost`
3. Teste a navegação passo a passo
4. Verifique os logs no Logcat para erros

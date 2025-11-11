# 💬 Integração do Chat com Backend SOS Baby

## 📋 Resumo da Implementação

Integração completa do sistema de chat do app TCC Bebê com o backend Node.js + Express + Prisma.

## 🏗️ Arquivos Criados/Atualizados

### 📁 Modelos de Dados
- **`ChatModels.kt`** - Modelos: Chat, Mensagem, Usuario, Contato, ApiResponse

### 🌐 Camada de Rede
- **`ChatApiService.kt`** - Interface Retrofit com todos os endpoints
- **`RetrofitClient.kt`** - Cliente HTTP configurado com logging
- **`ApiConfig.kt`** - Configurações centralizadas da API

### 🏪 Repository
- **`ChatRepository.kt`** - Gerencia todas as chamadas da API com tratamento de erros

### 🎯 ViewModels
- **`ChatViewModel.kt`** - Gerencia chat individual e envio de mensagens
- **`ContatosViewModel.kt`** - Atualizado para consumir API real

### 🖥️ Telas
- **`ChatIndividualScreen.kt`** - Integrada com ViewModel e API
- **`ContatosScreen.kt`** - Já integrada anteriormente

## 🔗 Endpoints da API

### Chat
```
GET /v1/sosbaby/chats - Lista todos os chats
GET /v1/sosbaby/chat/{id} - Busca chat específico
POST /v1/sosbaby/chat/cadastro - Cria novo chat
```

### Mensagens
```
GET /v1/sosbaby/messages - Lista todas as mensagens
GET /v1/sosbaby/message/{id} - Busca mensagem específica
POST /v1/sosbaby/message/send - Envia nova mensagem
```

### Contatos
```
GET /v1/sosbaby/users - Lista usuários
GET /v1/sosbaby/doctors - Lista médicos
```

## ⚙️ Configuração

### 1. URL do Backend
Edite o arquivo `ApiConfig.kt`:

```kotlin
const val BASE_URL = "http://SEU_IP:3030/v1/sosbaby/"
```

**Opções:**
- Emulador: `http://10.0.2.2:3030/v1/sosbaby/`
- Dispositivo físico: `http://192.168.1.XXX:3030/v1/sosbaby/`
- Produção: `https://seu-backend.herokuapp.com/v1/sosbaby/`

### 2. Estrutura dos Dados

**Mensagem (Backend):**
```json
{
  "id": "string",
  "conteudo": "string",
  "id_chat": "string", 
  "id_user": "string",
  "created_at": "string"
}
```

**Response Padrão:**
```json
{
  "success": boolean,
  "data": any,
  "message": "string"
}
```

## 🚀 Funcionalidades Implementadas

### ✅ Lista de Contatos
- Carrega usuários e médicos da API
- Busca em tempo real
- Fallback com contato de teste
- Loading states e tratamento de erros

### ✅ Chat Individual
- Criação automática de chat se não existir
- Carregamento de mensagens por chat
- Envio de mensagens em tempo real
- Auto-scroll para última mensagem
- Indicadores de loading e erro
- Retry automático em caso de erro

### ✅ Tratamento de Erros
- Mensagens descritivas de erro
- Fallbacks para dados offline
- Retry buttons em telas de erro
- Logging detalhado das requisições

## 🔧 Como Usar

### 1. Iniciar o Backend
```bash
cd BackEnd_SOSBaby
npm install
npm start
# Backend rodará em http://localhost:3030
```

### 2. Configurar IP no App
- Abra `ApiConfig.kt`
- Configure a `BASE_URL` com o IP correto
- Para emulador: use `10.0.2.2`
- Para dispositivo físico: use o IP da sua máquina

### 3. Testar a Integração
1. Abra o app
2. Vá para "Chat" → "Contatos"
3. Clique em "Dr. Maria Santos" (contato de teste)
4. Teste o envio de mensagens

## 🐛 Troubleshooting

### Erro de Conexão
- Verifique se o backend está rodando
- Confirme o IP correto no `ApiConfig.kt`
- Teste a URL no navegador: `http://SEU_IP:3030/v1/sosbaby/users`

### Mensagens não aparecem
- Verifique os logs no Logcat
- Confirme se os endpoints estão retornando dados
- Teste com o contato de teste primeiro

### Erro de CORS
- O backend já tem CORS configurado
- Se necessário, adicione o IP do dispositivo nas configurações do backend

## 📱 Fluxo de Navegação

```
Home → Chat → Contatos → Chat Individual
     ↓         ↓           ↓
  TelaHome → ContatosScreen → ChatIndividualScreen
```

## 🔮 Próximos Passos

1. **Autenticação**: Implementar login real para obter userId
2. **Socket.io**: Chat em tempo real com WebSockets
3. **Cache Local**: Room Database para mensagens offline
4. **Push Notifications**: Notificações de novas mensagens
5. **Anexos**: Suporte a imagens e arquivos

## 📞 Suporte

Em caso de problemas:
1. Verifique os logs no Logcat
2. Teste os endpoints diretamente no navegador/Postman
3. Confirme se o backend está rodando corretamente
4. Verifique a configuração de rede (IP/porta)

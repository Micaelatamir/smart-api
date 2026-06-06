# Smart API — API Inteligente com Reconhecimento de Fala

> API REST com assistente financeiro via linguagem natural e transcrição de áudio. Construída com Spring Boot e Spring AI, usando **Groq** como provider de IA (gratuito) e **H2** como banco em memória (sem precisar de Docker).

---

## Stack

- Java 21
- Spring Boot 3.4.5
- Spring AI 1.0.0
- Groq API — `llama-3.3-70b-versatile` (chat) + `whisper-large-v3` (transcrição)
- H2 Database (em memória)
- Lombok

---

## Setup

### Pré-requisitos

- Java 21+
- IntelliJ IDEA
- Chave da API Groq — gratuita em [console.groq.com](https://console.groq.com)

### Configurando

1. Abre o arquivo `src/main/resources/application.properties`
2. Substitui `SUA_CHAVE_GROQ_AQUI` pela sua chave do Groq
3. Roda a classe `SmartApiApplication` no IntelliJ

Sem Docker, sem configuração de banco. O H2 sobe automaticamente junto com a aplicação.

---

## Endpoints

### Chat

| Método | Rota | Descrição |
|--------|------|-----------|
| POST | `/chat` | Envia mensagem de texto ao assistente |
| POST | `/chat/transcribe` | Envia arquivo de áudio, recebe transcrição + resposta |

### Transações

| Método | Rota | Descrição |
|--------|------|-----------|
| GET | `/transactions` | Lista todas as transações |
| GET | `/transactions/{id}` | Busca por ID |
| POST | `/transactions` | Cria transação manualmente |
| DELETE | `/transactions/{id}` | Remove transação |

### H2 Console

Acessível em `http://localhost:8080/h2-console` com:
- JDBC URL: `jdbc:h2:mem:smartapi`
- User: `sa`
- Password: (vazio)

---

## Exemplos

**Chat:**
```json
POST /chat
{ "message": "Gastei 50 reais no mercado hoje" }
```

**Resposta:**
```json
{ "response": "Registrei a despesa de R$ 50,00 na categoria mercado!" }
```

**Transcrição:**
```
POST /chat/transcribe
Content-Type: multipart/form-data
file: audio.mp3
```

---

Desenvolvido por [Micaela Tamir](https://github.com/Micaelatamir)

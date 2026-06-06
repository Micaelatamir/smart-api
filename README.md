# smart-api

API REST com assistente financeiro inteligente, reconhecimento de fala e processamento de linguagem natural. O projeto surgiu de um desafio de integrar Spring Boot com modelos de IA modernos — conectar transcrição de áudio, tool calling e um LLM num fluxo só foi bem interessante de montar.

O assistente consegue entender uma mensagem como "gastei 80 reais no mercado hoje" e automaticamente registrar a transação no banco, sem precisar de nenhum formulário.

## Tecnologias

- Java 21
- Spring Boot 3.4.5
- Spring AI 1.0.0
- Groq API — LLM `llama-3.3-70b-versatile` e transcrição `whisper-large-v3`
- H2 (banco em memória)
- Lombok

## Como rodar

Você vai precisar de:
- Java 21+
- IntelliJ IDEA
- Uma chave da API do Groq (gratuita em [console.groq.com](https://console.groq.com))

Clone o repositório e abra no IntelliJ. Antes de rodar, coloque sua chave no `application.properties`:

```properties
spring.ai.openai.api-key=sua_chave_aqui
```

Depois é só rodar a classe `SmartApiApplication`. Sem Docker, sem banco externo — o H2 já sobe junto com a aplicação.

## Endpoints

### Chat

**POST /chat**

Manda uma mensagem em texto e o assistente responde. Se você mencionar uma transação, ele registra automaticamente usando tool calling.

```json
{
  "message": "Gastei 50 reais no mercado hoje"
}
```

Resposta:
```json
{
  "response": "Anotei! Registrei uma despesa de R$ 50,00 na categoria mercado."
}
```

**POST /chat/transcribe**

Manda um arquivo de áudio (mp3, wav, m4a) e recebe de volta a transcrição e a resposta do assistente.

```
Content-Type: multipart/form-data
file: audio.mp3
```

Resposta:
```json
{
  "transcription": "gastei oitenta reais no mercado",
  "response": "Registrei a despesa de R$ 80,00!"
}
```

### Transações

| Método | Rota | Descrição |
|--------|------|-----------|
| GET | `/transactions` | Lista todas as transações |
| GET | `/transactions/{id}` | Busca uma transação por ID |
| POST | `/transactions` | Cria uma transação manualmente |
| DELETE | `/transactions/{id}` | Remove uma transação |

### H2 Console

Durante o desenvolvimento dá pra inspecionar o banco direto no navegador em `http://localhost:8080/h2-console`.

- JDBC URL: `jdbc:h2:mem:smartapi`
- User: `sa`
- Password: deixa vazio

## Como o tool calling funciona

Uma das partes mais interessantes do projeto foi implementar o tool calling. Em vez de só gerar texto, o modelo consegue chamar funções reais da aplicação.

Criei três ferramentas que o LLM pode invocar:

- `saveTransaction` — registra uma despesa ou receita no banco
- `getBudgetSummary` — retorna o resumo financeiro com receitas, despesas e saldo
- `listTransactions` — lista todas as transações cadastradas

Quando você manda "qual meu saldo?", o modelo identifica que precisa chamar `getBudgetSummary`, busca os dados reais e responde com base neles. Tudo isso de forma transparente pra quem usa a API.

## Estrutura do projeto

```
src/main/java/com/micaelatamir/smartapi/
├── config/
│   └── ChatConfig.java          # Configuração do ChatClient com system prompt e tools
├── controller/
│   ├── ChatController.java      # Endpoints de chat e transcrição
│   └── TransactionController.java
├── model/
│   └── Transaction.java
├── repository/
│   └── TransactionRepository.java
├── service/
│   ├── AssistantService.java    # Orquestra o chat com o LLM
│   └── TranscriptionService.java
└── tool/
    └── BudgetTools.java         # Funções que a IA pode chamar
```
Quando você manda "qual meu saldo?", o modelo identifica que precisa chamar getBudgetSummary, busca os dados reais e responde com base neles. Tudo isso de forma transparente pra quem usa a API.or [Micaela Tamir](https://github.com/Micaelatamir)

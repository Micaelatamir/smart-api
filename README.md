# smart-api

A REST API with an intelligent financial assistant, speech recognition, and natural language processing. The project came from the challenge of integrating Spring Boot with modern AI models — connecting audio transcription, tool calling, and an LLM into a single flow was genuinely fun to put together.

The assistant can understand a message like "I spent 80 dollars on groceries today" and automatically log the transaction to the database, no form or manual input needed.

## Tech Stack

- Java 21
- Spring Boot 3.4.5
- Spring AI 1.0.0
- Groq API — LLM `llama-3.3-70b-versatile` and transcription `whisper-large-v3`
- H2 (in-memory database)
- Lombok

## Getting Started

You'll need:
- Java 21+
- IntelliJ IDEA
- A Groq API key (free at [console.groq.com](https://console.groq.com))

Clone the repo and open it in IntelliJ. Before running, add your key to `application.properties`:

```properties
spring.ai.openai.api-key=your_key_here
```

Then just run `SmartApiApplication`. No Docker, no external database — H2 starts automatically with the application.

## Endpoints

### Chat

**POST /chat**

Send a text message and the assistant replies. If you mention a transaction, it gets logged automatically via tool calling.

```json
{
  "message": "I spent 50 dollars at the supermarket today"
}
```

Response:
```json
{
  "response": "Got it! Logged an expense of $50.00 under groceries."
}
```

**POST /chat/transcribe**

Send an audio file (mp3, wav, m4a) and get back the transcription plus the assistant's response.

```
Content-Type: multipart/form-data
file: audio.mp3
```

Response:
```json
{
  "transcription": "I spent eighty dollars at the supermarket",
  "response": "Logged the $80.00 expense!"
}
```

### Transactions

| Method | Route | Description |
|--------|-------|-------------|
| GET | `/transactions` | List all transactions |
| GET | `/transactions/{id}` | Get a transaction by ID |
| POST | `/transactions` | Create a transaction manually |
| DELETE | `/transactions/{id}` | Delete a transaction |

### H2 Console

During development you can inspect the database directly in the browser at `http://localhost:8080/h2-console`.

- JDBC URL: `jdbc:h2:mem:smartapi`
- User: `sa`
- Password: leave empty

## How Tool Calling Works

One of the most interesting parts of this project was implementing tool calling. Instead of just generating text, the model can invoke real functions in the application.

I built three tools the LLM can call:

- `saveTransaction` — logs an expense or income to the database
- `getBudgetSummary` — returns a financial summary with income, expenses, and balance
- `listTransactions` — lists all recorded transactions

When you ask "what's my balance?", the model identifies it needs to call `getBudgetSummary`, fetches the real data, and responds based on it. All transparent to whoever is consuming the API.

## Project Structure

```
src/main/java/com/micaelatamir/smartapi/
├── config/
│   └── ChatConfig.java            # ChatClient setup with system prompt and tools
├── controller/
│   ├── ChatController.java        # Chat and transcription endpoints
│   └── TransactionController.java
├── model/
│   └── Transaction.java
├── repository/
│   └── TransactionRepository.java
├── service/
│   ├── AssistantService.java      # Orchestrates the LLM chat flow
│   └── TranscriptionService.java
└── tool/
    └── BudgetTools.java           # Functions the AI can call
```ps://github.com/Micaelatamir)

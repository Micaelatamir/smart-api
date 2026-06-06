package com.micaelatamir.smartapi.config;

import com.micaelatamir.smartapi.tool.BudgetTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatConfig {

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder, BudgetTools budgetTools) {
        return builder
                .defaultSystem("""
                        Voce e um assistente financeiro pessoal chamado Finn.
                        Ajude o usuario a registrar receitas e despesas, consultar o saldo
                        e entender seus gastos de forma clara e amigavel.
                        Responda sempre em portugues do Brasil.
                        Quando o usuario mencionar uma transacao, use as ferramentas disponiveis para salva-la.
                        """)
                .defaultTools(budgetTools)
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(
                                MessageWindowChatMemory.builder().maxMessages(10).build()
                        ).build()
                )
                .build();
    }
}

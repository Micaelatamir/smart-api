package com.micaelatamir.smartapi.tool;

import com.micaelatamir.smartapi.model.Transaction;
import com.micaelatamir.smartapi.model.Transaction.TransactionType;
import com.micaelatamir.smartapi.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BudgetTools {

    private final TransactionRepository repository;

    @Tool(description = "Registra uma nova transacao financeira. Use para despesas ou receitas informadas pelo usuario.")
    public String saveTransaction(String description, BigDecimal amount, String type, String category) {
        var txType = type.equalsIgnoreCase("receita") ? TransactionType.INCOME : TransactionType.EXPENSE;

        var transaction = Transaction.builder()
                .description(description)
                .amount(amount)
                .type(txType)
                .category(category)
                .build();

        repository.save(transaction);
        return "Transacao registrada: %s - R$ %.2f (%s)".formatted(description, amount, category);
    }

    @Tool(description = "Retorna o resumo financeiro atual com total de receitas, despesas e saldo.")
    public String getBudgetSummary() {
        var totalIncome = repository.sumByType(TransactionType.INCOME);
        var totalExpense = repository.sumByType(TransactionType.EXPENSE);
        var balance = totalIncome.subtract(totalExpense);

        return """
                Resumo financeiro:
                - Receitas: R$ %.2f
                - Despesas: R$ %.2f
                - Saldo atual: R$ %.2f
                """.formatted(totalIncome, totalExpense, balance);
    }

    @Tool(description = "Lista todas as transacoes registradas.")
    public String listTransactions() {
        List<Transaction> all = repository.findAll();
        if (all.isEmpty()) {
            return "Nenhuma transacao registrada ainda.";
        }

        var sb = new StringBuilder("Transacoes:\n");
        for (var t : all) {
            sb.append("- [%s] %s: R$ %.2f (%s)\n"
                    .formatted(t.getType(), t.getDescription(), t.getAmount(), t.getCategory()));
        }
        return sb.toString();
    }
}

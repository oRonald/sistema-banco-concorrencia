package br.com.projeto.clima.model.transaction;

import br.com.projeto.clima.model.transaction.enums.TransactionType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.UUID;

public class Transaction implements Serializable {
    private static final long seriaVersionUID = 1L;

    private final String id;
    private final TransactionType type;
    private final BigDecimal amount;
    private final Long fromAccountId;
    private final Long toAccountId;
    private final Instant timestamp;

    public Transaction(TransactionType type, BigDecimal amount, Long fromAccountId, Long toAccountId, Instant timestamp) {
        this.id = UUID.randomUUID().toString();
        this.type = type;
        this.amount = amount.setScale(2, RoundingMode.HALF_EVEN);
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public TransactionType getType() {
        return type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Long getFromAccountId() {
        return fromAccountId;
    }

    public Long getToAccountId() {
        return toAccountId;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id='" + id + '\'' +
                ", type=" + type +
                ", amount=" + amount +
                ", fromAccountId=" + fromAccountId +
                ", toAccountId=" + toAccountId +
                ", timestamp=" + timestamp +
                '}';
    }
}

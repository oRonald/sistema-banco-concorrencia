package br.com.bank.system.model.account;

import br.com.bank.system.model.transaction.Transaction;
import br.com.bank.system.model.transaction.enums.TransactionType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class Account implements Serializable {

    private static final long seriaVersionUID = 1L;

    private final long id;
    private final long customerId;
    private BigDecimal balance;
    private final List<Transaction> history;

    private transient ReentrantLock lock = new ReentrantLock();

    public Account(long id, long customerId, BigDecimal initialBalance) {
        this.id = id;
        this.customerId = customerId;
        this.balance = initialBalance.setScale(2, RoundingMode.HALF_EVEN);
        this.history = new ArrayList<>();
        this.lock = new ReentrantLock();
    }

    public long getId() {
        return id;
    }

    public long getCustomerId() {
        return customerId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public List<Transaction> getHistory() {
        return history;
    }

    public ReentrantLock getLock() {
        return lock;
    }

    public void depositUnsafe(BigDecimal amount){
        balance = balance.add(amount);
        history.add(new Transaction(TransactionType.DEPOSIT, amount, null, id, Instant.now()));
    }

    public void withdrawUnsafe(BigDecimal amount){
        balance = balance.subtract(amount);
        history.add(new Transaction(TransactionType.WITHDRAW, amount, id, null, Instant.now()));
    }

    public void addTransferRecord(BigDecimal amount, Long fromAccountId, Long toAccountId){
        history.add(new Transaction(TransactionType.TRANSFER, amount, fromAccountId, toAccountId, Instant.now()));
    }

    private Object readResolve(){
        lock = new ReentrantLock();
        return this;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", customerId=" + customerId +
                ", balance=" + balance +
                '}';
    }
}

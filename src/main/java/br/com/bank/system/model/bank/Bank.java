package br.com.bank.system.model.bank;

import br.com.bank.system.model.account.Account;
import br.com.bank.system.model.configurations.PersistenceManager;
import br.com.bank.system.model.customer.Customer;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class Bank {

    private final Map<Long, Account> accounts = new ConcurrentHashMap<>();
    private final Map<Long, Customer> customers = new ConcurrentHashMap<>();
    private long accountSequence = 1;
    private long customerSequence = 1;

    private final PersistenceManager manager = new PersistenceManager();

    public Bank() {

    }

    // CUSTOMER OPERATIONS
    public synchronized Customer createCustomer(String name, String cpf){
        long id = customerSequence++;
        Customer customer = new Customer(id, name, cpf);
        customers.put(id, customer);
        return customer;
    }

    public Optional<Customer> getCustomer(long id){
        return Optional.ofNullable(customers.get(id));
    }

    public Collection<Customer> listCustomers(){
        return customers.values();
    }

    public synchronized void deleteCustomer(long id){
        accounts.values().removeIf(a -> a.getCustomerId() == id);
        customers.remove(id);
    }

    //ACCOUNT OPERATIONS
    public synchronized Account createAccount(long customerId, BigDecimal initialDeposit){
        if(!customers.containsKey(customerId)){
            throw new IllegalArgumentException("Customer does not exists: " + customerId);
        }

        long id = accountSequence++;
        Account acc = new Account(id, customerId, initialDeposit);
        accounts.put(id, acc);
        return acc;
    }

    public Optional<Account> getAccount(long id){
        return Optional.ofNullable(accounts.get(id));
    }

    public Collection<Account> listAccounts(){
        return accounts.values();
    }

    //FINANCIAL OPERATIOINS
    public void deposit(long accountId, BigDecimal amount){
        Account acc = getAccountFromMap(accountId);
        acc.getLock().lock();
        try{
            acc.depositUnsafe(amount);
        } finally {
            acc.getLock().unlock();
        }
    }

    public void withdraw(long accountId, BigDecimal amount){
        Account acc = getAccountFromMap(accountId);
        acc.getLock().lock();
        try{
            if(acc.getBalance().compareTo(amount) < 0)
                throw new IllegalStateException("Insufficient balance");
            acc.withdrawUnsafe(amount);
        } finally {
            acc.getLock().unlock();
        }
    }

    public void transfer(long fromId, long toId, BigDecimal amount){
        if(fromId == toId) throw new IllegalArgumentException("Same account");
        Account a = getAccountFromMap(fromId);
        Account b = getAccountFromMap(toId);

        Account first = a.getId() < b.getId() ? a : b;
        Account second = a.getId() < b.getId() ? b : a;

        first.getLock().lock();
        second.getLock().lock();
        try{
            if(a.getBalance().compareTo(amount) < 0)
                throw new IllegalStateException("Insufficient balance");
            a.withdrawUnsafe(amount);
            b.depositUnsafe(amount);
            a.addTransferRecord(amount, fromId, toId);
            b.addTransferRecord(amount, fromId, toId);
        } finally {
            second.getLock().unlock();
            first.getLock().unlock();
        }

    }

    private Account getAccountFromMap(long accountId){
        Account acc = accounts.get(accountId);
        if(acc == null) throw new IllegalArgumentException("Account not found: " + accountId);
        return acc;
    }

    //PERSISTENCE
    public void save() throws Exception{
        PersistenceManager.save(customers, accounts);
    }

    public void load() throws Exception{
        Map<Long, Customer> loadedCustomers = PersistenceManager.loadCustomers();
        Map<Long, Account> loadedAccounts = PersistenceManager.loadAccounts();

        customers.clear();
        customers.putAll(loadedCustomers);

        accounts.clear();
        accounts.putAll(loadedAccounts);

        this.customerSequence = customers.keySet().stream().max(Long::compareTo).orElse(0L);
        this.accountSequence = accounts.keySet().stream().max(Long::compareTo).orElse(0L);
    }

}

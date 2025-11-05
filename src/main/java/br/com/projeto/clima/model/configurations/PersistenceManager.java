package br.com.projeto.clima.model.configurations;

import br.com.projeto.clima.model.account.Account;
import br.com.projeto.clima.model.customer.Customer;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class PersistenceManager {

    private static final String DATA_DIR = "data";
    private static final String CUSTOMERS_FILE = DATA_DIR + "/customers.txt";
    private static final String ACCOUNTS_FILE = DATA_DIR + "/accounts.txt";

    private static final ReentrantLock lock = new ReentrantLock();

    public static class LoadResult {
        public Map<Long, Account> accounts;
        public Map<Long, Customer> customers;
        public long accountSequence;
        public long customerSequence;

        public LoadResult(Map<Long, Account> accounts, Map<Long, Customer> customers, long accountSequence, long customerSequence) {
            this.accounts = accounts;
            this.customers = customers;
            this.accountSequence = accountSequence;
            this.customerSequence = customerSequence;
        }
    }

    public static void save(Map<Long, Customer> customers, Map<Long, Account> accounts){
        lock.lock();
        try{
            Files.createDirectories(Paths.get(DATA_DIR));
            saveCustomers(customers);
            saveAccounts(accounts);
        } catch (IOException e) {
            throw new RuntimeException("Error saving data: " + e.getMessage(), e);
        } finally {
            lock.unlock();
        }
    }

    private static void saveCustomers(Map<Long, Customer> customers) throws IOException{
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(CUSTOMERS_FILE))){
            for(Customer c : customers.values()){
                writer.write(c.getId() + " | " + c.getName() + " | " + c.getCpf());
                writer.newLine();
            }
        }
    }

    private static void saveAccounts(Map<Long, Account> accounts) throws IOException{
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(ACCOUNTS_FILE))){
            for(Account a : accounts.values()){
                writer.write(a.getId() + " | " + a.getCustomerId() + " | " + a.getBalance());
                writer.newLine();
            }
        }
    }

    public static Map<Long, Customer> loadCustomers(){
        Map<Long, Customer> customers = new HashMap<>();
        lock.lock();
        try{
            Path path = Paths.get(CUSTOMERS_FILE);
            if(!Files.exists(path)) return customers;

            try(BufferedReader reader = Files.newBufferedReader(path)){
                String line;
                while((line = reader.readLine()) != null){
                    if(line.isBlank()) continue;
                    String[] parts = line.split("\\|");
                    long id = Long.parseLong(parts[0]);
                    String name = parts[1];
                    String cpf = parts[2];
                    customers.put(id, new Customer(id, name, cpf));
                }
            } catch (IOException e) {
                throw new RuntimeException("Error loading customers: " + e.getMessage(), e);
            }
        } finally {
            lock.unlock();
        }
        return customers;
    }

    public static Map<Long, Account> loadAccounts() {
        Map<Long, Account> accounts = new HashMap<>();
        lock.lock();
        try{
            Path path = Paths.get(ACCOUNTS_FILE);
            if(!Files.exists(path)) return accounts;

            try(BufferedReader reader = Files.newBufferedReader(path)){
                String line;
                while((line = reader.readLine()) != null){
                    if(line.isBlank()) continue;
                    String[] parts = line.split("\\|");
                    long id = Long.parseLong(parts[0]);
                    long customerId = Long.parseLong(parts[1]);
                    BigDecimal balance = BigDecimal.valueOf(Double.parseDouble(parts[2]));
                    accounts.put(id, new Account(id, customerId, balance));
                }
            }
        } catch (IOException e){
            throw new RuntimeException("Error loading accounts: " + e.getMessage(), e);
        } finally {
            lock.unlock();;
        }
        return accounts;
    }
}

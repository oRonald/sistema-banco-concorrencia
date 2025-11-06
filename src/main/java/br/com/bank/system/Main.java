package br.com.bank.system;

import br.com.bank.system.model.account.Account;
import br.com.bank.system.model.bank.Bank;
import br.com.bank.system.model.customer.Customer;

import java.math.BigDecimal;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {
        Bank bank = new Bank();
        Scanner sc = new Scanner(System.in);
        boolean running = true;

        while (running){
            System.out.println("\n========= MENU =========");
            System.out.println("1. Cadastrar cliente");
            System.out.println("2. Listar Clientes");
            System.out.println("3. Criar Conta");
            System.out.println("4. Listar Contas");
            System.out.println("5. Depositar");
            System.out.println("6. Sacar");
            System.out.println("7. Transferir");
            System.out.println("8. Salvar dados");
            System.out.println("0. Sair");
            System.out.print("Escolha: ");

            switch (sc.nextInt()){
                case 1 -> {
                    System.out.println("Name: ");
                    sc.nextLine();
                    String name = sc.nextLine();
                    System.out.println("CPF: ");
                    String cpf = sc.nextLine();
                    Customer c = bank.createCustomer(name, cpf);
                    System.out.println("Customer created: " + c);
                }
                case 2 -> bank.listCustomers().forEach(System.out::println);
                case 3 -> {
                    System.out.println("Customers ID");
                    long id = sc.nextLong();
                    System.out.println("Initial deposit: ");
                    BigDecimal deposit = sc.nextBigDecimal();
                    Account a = bank.createAccount(id, deposit);
                    System.out.println("Account created: " + a);
                }
                case 4 -> bank.listAccounts().forEach(System.out::println);
                case 5 -> {
                    System.out.println("Account ID: ");
                    long id = sc.nextLong();
                    System.out.println("Value: ");
                    BigDecimal value = sc.nextBigDecimal();
                    bank.deposit(id, value);
                    System.out.println("Deposit made");
                } case 6 -> {
                    System.out.println("Account ID: ");
                    long id = sc.nextLong();
                    System.out.println("Value: ");
                    BigDecimal value = sc.nextBigDecimal();
                    bank.withdraw(id, value);
                    System.out.println("Withdraw made");
                } case 7 -> {
                    System.out.println("From-Account: ");
                    long from = sc.nextLong();
                    System.out.println("To-Account: ");
                    long to = sc.nextLong();
                    System.out.println("Value: ");
                    BigDecimal value = sc.nextBigDecimal();
                    bank.transfer(from, to, value);
                    System.out.println("transfer made");
                } case 8 -> {
                    bank.save();
                    System.out.println("Data saved in disk");
                } case 0 -> {
                    System.out.println("Finishing...");
                    running = false;
                }
                default -> System.out.println("Invalid option");
            }
        }
        sc.close();
    }
}
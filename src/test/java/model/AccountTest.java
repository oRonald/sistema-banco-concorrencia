package model;

import br.com.bank.system.model.account.Account;
import br.com.bank.system.model.bank.Bank;
import br.com.bank.system.model.customer.Customer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.math.BigDecimal;

public class AccountTest {

    private Customer customer;
    private Account account;
    private Bank bank;

    @Before
    public void setUp(){
        bank = new Bank();
        customer = new Customer(1, "Ronald", "12345678901");
        customer = bank.createCustomer(customer.getName(), customer.getCpf());
        account = bank.createAccount(customer.getId(), BigDecimal.ZERO);
    }

    @Test
    public void testInitialBalanceIsZero(){
        assertEquals(0, account.getBalance().compareTo(BigDecimal.ZERO));
    }

    @Test
    public void testDepositIncreasesBalance(){
        bank.deposit(account.getId(), new BigDecimal("300.50"));
        assertEquals(0, account.getBalance().compareTo(new BigDecimal("300.50")));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDepositNegativeThrowsException(){
        bank.deposit(account.getId(), new BigDecimal("-10"));
    }

    @Test
    public void testWithdrawDecreasesBalance(){
        bank.deposit(account.getId(), new BigDecimal("500.00"));
        bank.withdraw(account.getId(), new BigDecimal("200.00"));
        assertEquals(0, account.getBalance().compareTo(new BigDecimal("300.00")));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithdrawNegativeFundsThrowsException(){
        bank.withdraw(customer.getId(), new BigDecimal("-100"));
    }

    @Test(expected = IllegalStateException.class)
    public void testWithdrawInsufficientFundsThrowsException(){
        bank.withdraw(customer.getId(), new BigDecimal("50.00"));
    }

    @Test
    public void testTransferToAnotherAccount(){
        Customer targetCustomer = new Customer(2, "Ryan", "09876543211");
        Account targetAccount = new Account(2, targetCustomer.getId(), BigDecimal.ZERO);

        bank.createCustomer("Ryan", "09876543211");
        bank.createAccount(targetAccount.getId(), BigDecimal.ZERO);

        bank.deposit(customer.getId(), new BigDecimal("400.00"));
        bank.transfer(account.getId(), targetAccount.getId(), new BigDecimal("150.00"));

        assertEquals(0, account.getBalance().compareTo(new BigDecimal("250.00")));
        assertEquals(-1, targetAccount.getBalance().compareTo(new BigDecimal("150.00")));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTransferNegativeAmountThrowsException(){
        Account targetAccount = new Account(2, customer.getId(), BigDecimal.ZERO);
        bank.transfer(targetAccount.getId(), account.getId(), new BigDecimal("-10"));
    }
}

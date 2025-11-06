package model;

import br.com.bank.system.model.account.Account;
import br.com.bank.system.model.bank.Bank;
import br.com.bank.system.model.customer.Customer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.math.BigDecimal;

public class BankTest {

    private Bank bank;
    private Customer customer;
    private Account account;

    @Before
    public void setUp(){
        bank = new Bank();
        customer = bank.createCustomer("Ronald", "12345678901");
        account = bank.createAccount(customer.getId(), BigDecimal.ZERO);
    }

    @Test
    public void testCreateCustomer(){
        assertNotNull(customer);
        assertEquals("Ronald", customer.getName());
        assertTrue(bank.listCustomers().contains(customer));
    }

    @Test
    public void testCreateAccount(){
        assertNotNull(account);
        assertEquals(customer.getId(), account.getCustomerId());
        assertTrue(bank.listAccounts().contains(account));
        assertEquals(0, account.getBalance().compareTo(BigDecimal.ZERO));
    }

    @Test
    public void testDeposit(){
        bank.deposit(account.getId(), new BigDecimal("150.75"));
        assertEquals(0, account.getBalance().compareTo(new BigDecimal("150.75")));
    }

    @Test
    public void testWithdraw(){
        bank.deposit(account.getId(), new BigDecimal("200.00"));
        bank.withdraw(account.getId(), new BigDecimal("50.00"));
        assertEquals(0, account.getBalance().compareTo(new BigDecimal("150.00")));
    }

    @Test(expected = IllegalStateException.class)
    public void testWithdrawMoreThanBalanceThrowsException(){
        bank.withdraw(account.getId(), new BigDecimal("10.00"));
    }

    @Test
    public void testTransferBetweenAccounts(){
        Customer ryan = bank.createCustomer("Ryan", "09876543211");
        Account account2 = bank.createAccount(ryan.getId(), BigDecimal.ZERO);

        bank.deposit(account.getId(), new BigDecimal("100.00"));
        bank.transfer(account.getId(), account2.getId(), new BigDecimal("40.00"));

        assertEquals(0, account.getBalance().compareTo(new BigDecimal("60.00")));
        assertEquals(0, account2.getBalance().compareTo(new BigDecimal("40.00")));
    }
}

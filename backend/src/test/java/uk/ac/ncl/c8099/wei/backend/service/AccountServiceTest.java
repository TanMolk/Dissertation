package uk.ac.ncl.c8099.wei.backend.service;

import jakarta.annotation.Resource;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.web3j.crypto.Credentials;
import uk.ac.ncl.c8099.wei.backend.BackendApplication;
import uk.ac.ncl.c8099.wei.backend.constants.TestAccount;
import uk.ac.ncl.c8099.wei.backend.controller.context.AddressContext;
import uk.ac.ncl.c8099.wei.backend.dao.AccountRepository;
import uk.ac.ncl.c8099.wei.backend.dao.entity.Account;
import uk.ac.ncl.c8099.wei.backend.utils.AccountUtil;

import java.security.SignatureException;
import java.util.List;

/**
 * @author wei tan
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BackendApplication.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AccountServiceTest {

    @Resource
    private Credentials credentials;

    @Resource
    private AccountService accountService;

    @Resource
    private AccountRepository accountRepository;

    @Test
    @Order(0)
    public void testVerifySignature() throws SignatureException {
        Credentials account = TestAccount.ACCOUNTS[0];
        String message = "123";
        String sign = AccountUtil.sign(account, message);

        AddressContext.set(account.getAddress());
        Assert.assertTrue(accountService.verifySignature(sign, message));
    }

    @Test
    @Order(1)
    public void testSign() throws SignatureException {
        String message = "123";
        String sign = accountService.sign(message);

        AddressContext.set(credentials.getAddress());
        Assert.assertTrue(accountService.verifySignature(sign, message));
    }

    @Test
    @Order(2)
    public void testAddAccount() {
        //1
        Credentials account = TestAccount.ACCOUNTS[0];
        List<Account> accounts = accountRepository.findByAddress(account.getAddress());

        Boolean result = accountService.addAccount(account.getAddress());
        Assert.assertEquals((accounts.size() == 0), result);

        //2
        account = TestAccount.ACCOUNTS[TestAccount.ACCOUNTS.length - 1];
        accounts = accountRepository.findByAddress(account.getAddress());
        result = accountService.addAccount(account.getAddress());
        Assert.assertEquals((accounts.size() == 0), result);

        //3
        result = accountService.addAccount(account.getAddress());
        Assert.assertEquals((accounts.size() == 0), result);
    }

    @Test
    @Order(3)
    public void testLogin() {
        Credentials account = TestAccount.ACCOUNTS[0];

        List<String> accounts = accountRepository.findByAddress(account.getAddress()).stream()
                .map(Account::getAddress).toList();

        AddressContext.set(account.getAddress());
        Assert.assertEquals(accountService.containAddress(), accounts.contains(account.getAddress()));
    }
}

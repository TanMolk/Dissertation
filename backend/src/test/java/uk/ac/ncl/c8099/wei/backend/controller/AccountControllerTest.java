package uk.ac.ncl.c8099.wei.backend.controller;

import jakarta.annotation.Resource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.web3j.crypto.Credentials;
import uk.ac.ncl.c8099.wei.backend.constants.AccountConstant;
import uk.ac.ncl.c8099.wei.backend.constants.TestAccount;
import uk.ac.ncl.c8099.wei.backend.controller.request.GeneralRequest;
import uk.ac.ncl.c8099.wei.backend.controller.request.LoginRequest;
import uk.ac.ncl.c8099.wei.backend.controller.request.SignInRequest;
import uk.ac.ncl.c8099.wei.backend.controller.response.GeneralResponse;
import uk.ac.ncl.c8099.wei.backend.dao.AccountRepository;
import uk.ac.ncl.c8099.wei.backend.utils.AccountUtil;
import uk.ac.ncl.c8099.wei.backend.utils.JsonUtil;

/**
 * @author wei tan
 */


@SpringBootTest
@RunWith(SpringRunner.class)
public class AccountControllerTest {

    @Resource
    private WebApplicationContext webApplicationContext;

    @Resource
    private AccountRepository accountRepository;

    //mock request
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testSignUp() throws Exception {
        String uri = "/account/sign-up";

        //1 add successfully
        Credentials account = TestAccount.ACCOUNTS[TestAccount.ACCOUNTS.length - 1];
        accountRepository.removeAccountByAddress(account.getAddress());

        GeneralRequest request = new GeneralRequest();
        SignInRequest data = new SignInRequest();
        data.setCipher(AccountConstant.SIGN_UP_CIPHER);
        request.setAccount(account.getAddress());
        String dataStr = JsonUtil.toJsonStr(data);

        request.setData(dataStr);
        request.setSignature(AccountUtil.sign(account, data));

        String responseString = mockMvc.perform(MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJsonStr(request))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse().getContentAsString();

        GeneralResponse response = JsonUtil.jsonStrToObj(responseString, GeneralResponse.class);
        Assert.assertTrue(Boolean.parseBoolean(response.getData()));

        //2 has contained this account
        account = TestAccount.ACCOUNTS[0];

        request = new GeneralRequest();
        data = new SignInRequest();
        data.setCipher(AccountConstant.SIGN_UP_CIPHER);

        dataStr = JsonUtil.toJsonStr(data);

        request.setAccount(account.getAddress());
        request.setData(dataStr);
        request.setSignature(AccountUtil.sign(account, dataStr));
        responseString = mockMvc.perform(MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJsonStr(request))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse().getContentAsString();

        response = JsonUtil.jsonStrToObj(responseString, GeneralResponse.class);

        Assert.assertFalse(Boolean.parseBoolean(response.getData()));
    }

    @Test
    public void testLogin() throws Exception {
        String uri = "/account/login";


        //1 doesn't exist
        Credentials account = TestAccount.ACCOUNTS[TestAccount.ACCOUNTS.length - 1];
        accountRepository.removeAccountByAddress(account.getAddress());

        GeneralRequest request = new GeneralRequest();
        LoginRequest data = new LoginRequest();
        data.setCipher(AccountConstant.LOGIN_CIPHER);
        request.setAccount(account.getAddress());
        request.setData(JsonUtil.toJsonStr(data));
        request.setSignature(AccountUtil.sign(account, request.getData()));


        String responseString = mockMvc.perform(MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJsonStr(request))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse().getContentAsString();

        GeneralResponse response = JsonUtil.jsonStrToObj(responseString, GeneralResponse.class);
        Assert.assertFalse(Boolean.parseBoolean(response.getData()));


        //2 contain this account
        account = TestAccount.ACCOUNTS[0];

        request = new GeneralRequest();
        request.setAccount(account.getAddress());
        request.setData(JsonUtil.toJsonStr(data));
        request.setSignature(AccountUtil.sign(account, request.getData()));


        responseString = mockMvc.perform(MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJsonStr(request))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse().getContentAsString();

        response = JsonUtil.jsonStrToObj(responseString, GeneralResponse.class);
        Assert.assertTrue(Boolean.parseBoolean(response.getData()));

        //3 wrong cipher
        request = new GeneralRequest();
        data.setCipher("123");
        request.setAccount(account.getAddress());
        request.setData(JsonUtil.toJsonStr(data));
        request.setSignature(AccountUtil.sign(account, request.getData()));

        responseString = mockMvc.perform(MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.toJsonStr(request))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse().getContentAsString();
        response = JsonUtil.jsonStrToObj(responseString, GeneralResponse.class);
        Assert.assertFalse(Boolean.parseBoolean(response.getData()));

    }
}

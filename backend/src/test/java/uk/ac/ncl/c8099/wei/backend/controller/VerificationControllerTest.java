package uk.ac.ncl.c8099.wei.backend.controller;

import cn.hutool.core.util.RandomUtil;
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
import uk.ac.ncl.c8099.wei.backend.constants.TestAccount;
import uk.ac.ncl.c8099.wei.backend.controller.request.GeneralRequest;
import uk.ac.ncl.c8099.wei.backend.controller.request.SetVerificationRequest;
import uk.ac.ncl.c8099.wei.backend.controller.response.GeneralResponse;
import uk.ac.ncl.c8099.wei.backend.enums.VerificationTypeEnum;
import uk.ac.ncl.c8099.wei.backend.utils.AccountUtil;
import uk.ac.ncl.c8099.wei.backend.utils.JsonUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wei tan
 */


@SpringBootTest
@RunWith(SpringRunner.class)
public class VerificationControllerTest {

    @Resource
    private WebApplicationContext webApplicationContext;

    //mock request
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testSet() throws Exception {
        String uri = "/verification/set";
        String hash = "9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08";


        //1 add successfully
        Credentials account = TestAccount.ACCOUNTS[0];

        GeneralRequest request = new GeneralRequest();
        SetVerificationRequest data = new SetVerificationRequest();
        data.setName("test-" + RandomUtil.randomString(4));
        data.setType(VerificationTypeEnum.TEXT);
        data.setOriginalHash(hash);
        data.setCalculateResult(AccountUtil.sign(account, hash));

        List<String> relatedParties = new ArrayList<>();
        relatedParties.add(account.getAddress());
        relatedParties.add(TestAccount.ACCOUNTS[1].getAddress());

        data.setRelatedParties(relatedParties);

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

        Assert.assertNotNull(response.getData());
    }
}

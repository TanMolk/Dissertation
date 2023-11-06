package uk.ac.ncl.c8099.wei.backend.service;

import jakarta.annotation.Resource;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.web3j.crypto.Credentials;
import uk.ac.ncl.c8099.wei.backend.constants.TestAccount;
import uk.ac.ncl.c8099.wei.backend.dao.VerificationRepository;
import uk.ac.ncl.c8099.wei.backend.dao.entity.Verification;
import uk.ac.ncl.c8099.wei.backend.enums.VerificationStatusEnum;
import uk.ac.ncl.c8099.wei.backend.enums.VerificationTypeEnum;
import uk.ac.ncl.c8099.wei.backend.utils.AccountUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wei tan
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class VerificationServiceTest {

    @Resource
    private VerificationService verificationService;

    @Resource
    private VerificationRepository verificationRepository;

    @Test
    public void testAddVerification() throws Exception {
        List<String> relatedParties =
                Arrays.stream(Arrays.copyOfRange(TestAccount.ACCOUNTS, 0, TestAccount.ACCOUNTS.length - 2))
                        .map(Credentials::getAddress)
                        .collect(Collectors.toList());

        Credentials creatorCredential = TestAccount.ACCOUNTS[TestAccount.ACCOUNTS.length - 1];
        String creator = creatorCredential.getAddress();

        String hash = "9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08";
        try {
            verificationService.addVerification(
                    "test",
                    VerificationTypeEnum.TEXT,
                    creator,
                    relatedParties,
                    hash,
                    AccountUtil.sign(creatorCredential, hash));
        } catch (Exception e) {
            e.printStackTrace();
            Assert.assertEquals("This list should contain creator's public key", e.getMessage());
        }

        relatedParties.add(creator);
        Verification verification = verificationService.addVerification(
                "test",
                VerificationTypeEnum.TEXT,
                creator,
                relatedParties,
                hash,
                AccountUtil.sign(creatorCredential, hash));
        Assert.assertNotNull(verification);
        Assert.assertEquals(VerificationStatusEnum.PENDING, verification.getStatus());
        Assert.assertEquals(relatedParties.size(), verification.getRelatedParties().size());


        relatedParties = new ArrayList<>();
        relatedParties.add(creator);
        verification = verificationService.addVerification(
                "test",
                VerificationTypeEnum.TEXT,
                creator,
                relatedParties,
                hash,
                AccountUtil.sign(creatorCredential, hash));
        Assert.assertNotNull(verification);
        Assert.assertEquals(VerificationStatusEnum.APPROVED, verification.getStatus());
        Assert.assertEquals(relatedParties.size(), verification.getRelatedParties().size());
    }

    @Test
    public void testUpdateVerificationState() {
        List<Verification> all = verificationRepository.findAll();
        Verification verification = all.get(0);

        VerificationStatusEnum updateState = VerificationStatusEnum.PENDING.equals(verification.getStatus())
                ? VerificationStatusEnum.REJECTED
                : VerificationStatusEnum.REJECTED.equals(verification.getStatus()) ? VerificationStatusEnum.APPROVED : VerificationStatusEnum.PENDING;

        Boolean result = verificationService.updateVerificationState(verification.getId(), updateState);

        Assert.assertTrue(
                verificationRepository.findById(verification.getId()).get().getStatus() == updateState
                        && result
        );


        result = verificationService.updateVerificationState(12824905, updateState);
        Assert.assertFalse(result);

    }
}

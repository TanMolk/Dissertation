package uk.ac.ncl.c8099.wei.backend.service;

import jakarta.annotation.Resource;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import uk.ac.ncl.c8099.wei.backend.BackendApplication;
import uk.ac.ncl.c8099.wei.backend.dao.RelatedPartyRepository;
import uk.ac.ncl.c8099.wei.backend.dao.VerificationRepository;
import uk.ac.ncl.c8099.wei.backend.dao.entity.RelatedParty;
import uk.ac.ncl.c8099.wei.backend.enums.VerificationStatusEnum;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author wei tan
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BackendApplication.class)
public class RelatedPartyServiceTest {

    @Resource
    private RelatedPartyService relatedPartyService;

    @Resource
    private RelatedPartyRepository relatedPartyRepository;

    @Resource
    private VerificationRepository verificationRepository;

    @Test
    public void testUpdateRelatedPartyState() throws Exception {
        List<RelatedParty> all = relatedPartyRepository.findAll();

        Map<Integer, List<RelatedParty>> collection = all.stream().collect(Collectors.groupingBy(RelatedParty::getVerificationId));

        int times = 0;
        int rejectVerificationId = 0;
        int approvedVerificationId = 0;
        for (List<RelatedParty> parties : collection.values()) {
            if (times == 0) {
                if (parties.stream().filter(p -> p.getStatus().equals(VerificationStatusEnum.PENDING)).count() > 1) {
                    RelatedParty rp = parties.get(0);
                    rejectVerificationId = rp.getVerificationId();
                    Boolean result = relatedPartyService.updateRelatedPartyState(null,
                            VerificationStatusEnum.REJECTED,
                            rp.getVerificationId(),
                            rp.getPartyAddress());
                    Assert.assertTrue(result);
                    Assert.assertEquals(verificationRepository.findById(rp.getVerificationId()).get()
                            .getStatus(), VerificationStatusEnum.REJECTED);
                    times++;
                }
            } else if (times == 1) {
                int finalRejectVerificationId = rejectVerificationId;
                parties.forEach(rp -> {
                    if (parties.stream().filter(p ->
                                    p.getStatus().equals(VerificationStatusEnum.PENDING)
                                            && finalRejectVerificationId != p.getVerificationId())
                            .count() > 1) {
                        Boolean result;
                        try {
                            result = relatedPartyService.updateRelatedPartyState("test",
                                    VerificationStatusEnum.APPROVED,
                                    rp.getVerificationId(),
                                    rp.getPartyAddress());
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        Assert.assertTrue(result);
                    }
                });

                Assert.assertEquals(verificationRepository.findById(parties.get(0).getVerificationId()).get()
                        .getStatus(), VerificationStatusEnum.APPROVED);
                times++;
            }
        }
    }
}

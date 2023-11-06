package uk.ac.ncl.c8099.wei.backend.service;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ncl.c8099.wei.backend.controller.context.AddressContext;
import uk.ac.ncl.c8099.wei.backend.dao.RelatedPartyRepository;
import uk.ac.ncl.c8099.wei.backend.dao.VerificationRepository;
import uk.ac.ncl.c8099.wei.backend.dao.entity.RelatedParty;
import uk.ac.ncl.c8099.wei.backend.dao.entity.Verification;
import uk.ac.ncl.c8099.wei.backend.enums.VerificationStatusEnum;

import java.util.List;
import java.util.Objects;

/**
 * @author wei tan
 */

@Service
public class RelatedPartyService {

    @Resource
    private RelatedPartyRepository relatedPartyRepository;

    @Resource
    private VerificationRepository verificationRepository;

    @Resource
    private BlockchainService blockchainService;

    @Resource
    private AccountService accountService;


    @Transactional(rollbackFor = Exception.class)
    public Boolean updateRelatedPartyState(
            String calculatingResult,
            VerificationStatusEnum state,
            Integer verificationId,
            String address) throws Exception {

        RelatedParty relatedParty = relatedPartyRepository.findByVerificationIdAndPartyAddress(
                verificationId,
                address);

        if (!VerificationStatusEnum.PENDING.equals(relatedParty.getStatus())) {
            return false;
        }

        if (VerificationStatusEnum.REJECTED.equals(state)) {
            if (relatedPartyRepository.updateStateAndCalculatingResultById(
                    relatedParty.getId(),
                    state,
                    null
            ) == 0) {
                throw new RuntimeException("Update related parties fails");
            }

            if (verificationRepository.updateStateById(verificationId, VerificationStatusEnum.REJECTED) == 0) {
                throw new RuntimeException("Update verification fails");
            }

            return true;
        } else if (VerificationStatusEnum.APPROVED.equals(state)) {
            Verification verification = verificationRepository.findById(verificationId).get();

            if (accountService.verifySignature(calculatingResult, verification.getHash())) {


                if (relatedPartyRepository.updateStateAndCalculatingResultById(
                        relatedParty.getId(),
                        state,
                        calculatingResult
                ) == 0) {
                    throw new RuntimeException("Update related parties fails");
                }

                if (relatedPartyRepository
                        .countByVerificationIdAndStatus(verificationId, VerificationStatusEnum.PENDING) == 0) {
                    if (verificationRepository.updateStateById(verificationId, VerificationStatusEnum.APPROVED) == 0) {
                        throw new RuntimeException("Update verification fails");
                    }

                    //check all related party calculating results with the original hash
                    List<RelatedParty> relatedOperations = relatedPartyRepository.findByVerificationId(verificationId);
                    for (RelatedParty relatedOperation : relatedOperations) {
                        if (!Objects.equals(relatedOperation.getVerificationId(), verificationId)
                                || !relatedOperation.getPartyAddress().equals(AddressContext.get()))
                            if (!accountService.verifySignatureWithAddress(
                                    relatedOperation.getResult(),
                                    verification.getHash(),
                                    relatedOperation.getPartyAddress())) {
                                throw new RuntimeException("Checking signature fails");
                            }
                    }


                    //synchronize data to blockchain network
                    Long certificationNumber = verification.getCertificationNumber();

                    String signatureOfCreator = relatedOperations.stream()
                            .filter(ro -> ro.getPartyAddress().equals(verification.getAccountAddress()))
                            .toList()
                            .get(0).getResult();

                    return blockchainService.addCertification(certificationNumber, signatureOfCreator);
                }

                return true;
            }
        }
        return false;
    }
}

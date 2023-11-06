package uk.ac.ncl.c8099.wei.backend.service;

import cn.hutool.core.util.RandomUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ncl.c8099.wei.backend.controller.context.AddressContext;
import uk.ac.ncl.c8099.wei.backend.dao.RelatedPartyRepository;
import uk.ac.ncl.c8099.wei.backend.dao.VerificationRepository;
import uk.ac.ncl.c8099.wei.backend.dao.entity.RelatedParty;
import uk.ac.ncl.c8099.wei.backend.dao.entity.Verification;
import uk.ac.ncl.c8099.wei.backend.enums.VerificationStatusEnum;
import uk.ac.ncl.c8099.wei.backend.enums.VerificationTypeEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class contains functions allowing to
 * - add verification
 * - change the state of a verification
 * - check the integrity of a document
 *
 * @author wei tan
 */

@Service
public class VerificationService {

    @Resource
    private VerificationRepository verificationRepository;

    @Resource
    private RelatedPartyRepository relatedPartyRepository;

    @Resource
    private AccountService accountService;

    @Resource
    private BlockchainService blockchainService;


    /**
     * @param type                    The type of this verification
     * @param creatorAccountAddress   The account's public key of the creator of this verification
     * @param relatedPartyAddressList related parties' public keys
     * @param firstCalculatingResult  the first calculating result by the creator.
     *                                This result is the hash of the document and encrypt by the private key of the creator.
     * @return The verification instance created.
     */

    @Transactional(rollbackFor = Exception.class)
    public Verification addVerification(String name,
                                        VerificationTypeEnum type,
                                        String creatorAccountAddress,
                                        List<String> relatedPartyAddressList,
                                        String originalHash,
                                        String firstCalculatingResult) throws Exception {

        //check signature of originalHash
        if (!accountService.verifySignature(firstCalculatingResult, originalHash)) {
            return null;
        }

        Verification verification = new Verification();
        verification.setStatus(VerificationStatusEnum.PENDING);

        verification.setName(name);
        verification.setType(type);
        verification.setAccountAddress(creatorAccountAddress);
        verification.setCertificationNumber(System.currentTimeMillis() + RandomUtil.randomInt(100000000));
        verification.setHash(originalHash);

        verification = verificationRepository.save(verification);
        Integer verificationId = verification.getId();
        if (verificationId == null) {
            throw new RuntimeException("Verification creating fails");
        }

        //create related parties
        //create record of creator
        List<RelatedParty> relatedParties = new ArrayList<>();
        RelatedParty creatorRecord = new RelatedParty();
        creatorRecord.setVerificationId(verificationId);
        creatorRecord.setResult(firstCalculatingResult);
        creatorRecord.setStatus(VerificationStatusEnum.APPROVED);
        creatorRecord.setPartyAddress(creatorAccountAddress);
        relatedParties.add(creatorRecord);
        //create other records
        relatedPartyAddressList.stream().distinct()
                .forEach(key -> {
                    if (!key.equals(creatorAccountAddress)) {
                        RelatedParty record = new RelatedParty();
                        record.setVerificationId(verificationId);
                        record.setStatus(VerificationStatusEnum.PENDING);
                        record.setPartyAddress(key);
                        relatedParties.add(record);
                    }
                });

        List<RelatedParty> relatedPartiesResult = relatedPartyRepository.saveAllAndFlush(relatedParties);
        relatedPartiesResult.forEach(party -> {
            if (party.getId() == null) {
                throw new RuntimeException("Related parties creating fails");
            }
        });

        //If only the creator joining this verification, synchronize to blockchain.
        if (relatedPartiesResult.size() == 1) {
            if (!blockchainService.addCertification(verification.getCertificationNumber(), firstCalculatingResult)) {
                throw new RuntimeException("Synchronizing to blockchain network fails");
            }
        }

        verification.setRelatedParties(relatedPartiesResult);
        return verification;
    }

    /**
     * Update the state of a verification by id
     *
     * @param verificationId the id of verification which need to update
     * @param state          the state after updating
     * @return if update successfully.
     */
    public Boolean updateVerificationState(Integer verificationId, VerificationStatusEnum state) {
        return verificationRepository.updateStateById(verificationId, state) == 1;
    }

    /**
     * Check the integrity of a document.
     * Get the original hash from smart contract by certificationNumber firstly, and compare it with input hash.
     *
     * @param hash                The hash of the document provided
     * @param certificationNumber The certification number of this verification
     * @return If the hash is matched.
     */
    public Boolean checkDocumentIntegrity(String hash, Long certificationNumber) throws Exception {
        String theSignatureOfCreator = blockchainService.getCertification(certificationNumber);
        Verification verification = verificationRepository.findByCertificationNumber(certificationNumber);

        if (!accountService.verifySignatureWithAddress(
                theSignatureOfCreator,
                verification.getHash(),
                verification.getAccountAddress())) {
            return false;
        }

        return verification.getHash().equalsIgnoreCase(hash);
    }

    /**
     * Return all verification related to a public key.
     *
     * @param address Address
     * @return all verification related to this public key.
     */
    public List<Verification> listAllVerificationByAddress(String address) {
        List<RelatedParty> related = relatedPartyRepository.findByPartyAddress(address);
        return verificationRepository.findAllById(related.stream()
                .map(RelatedParty::getVerificationId)
                .collect(Collectors.toList()));
    }

    public Verification queryVerification(Integer id) {
        RelatedParty relatedParty = relatedPartyRepository.findByVerificationIdAndPartyAddress(id, AddressContext.get());

        Verification result = null;
        if (relatedParty.getId() != null) {
            result = verificationRepository.findById(id).get();
        }
        return result;
    }
}

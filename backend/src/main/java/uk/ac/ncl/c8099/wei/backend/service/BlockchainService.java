package uk.ac.ncl.c8099.wei.backend.service;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import uk.ac.ncl.c8099.wei.backend.blockchain.contract.DocumentIntegrity;
import uk.ac.ncl.c8099.wei.backend.dao.VerificationRepository;
import uk.ac.ncl.c8099.wei.backend.dao.entity.Verification;
import uk.ac.ncl.c8099.wei.backend.enums.VerificationStatusEnum;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author wei tan
 */

@Slf4j
@Service
public class BlockchainService {

    @Resource
    private VerificationRepository verificationRepository;

    @Resource
    private DocumentIntegrity contract;

    private final Queue<KV> DATA = new ConcurrentLinkedDeque<>();

    private final ThreadPoolExecutor executor = new ThreadPoolExecutor(
            5,
            10,
            30, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>());

    //invoking smart contract thread
    {
        new Thread(() -> {
            while (true) {
                try {

                    //execute every second
                    Thread.sleep(1000);

                    if (DATA.isEmpty()) {
                        continue;
                    }

                    executor.execute(() -> {
                        try {
                            //gather data need to store
                            List<BigInteger> keyList = new ArrayList<>();
                            List<String> valueList = new ArrayList<>();
                            for (int i = 0; i < DATA.size(); i++) {
                                KV kv = DATA.poll();
                                if (kv == null) {
                                    break;
                                }
                                keyList.add(kv.key);
                                valueList.add(kv.value);
                            }

                            if (!keyList.isEmpty()) {
                                //call smart contract
                                TransactionReceipt receipt = contract.addCertifications(keyList, valueList).send();

                                //update verification data
                                List<Verification> verifications = verificationRepository.findByCertificationNumberIn(
                                        keyList.stream().map(BigInteger::longValue).toList());
                                for (Verification verification : verifications) {
                                    verification.setStatus(VerificationStatusEnum.APPROVED);
                                    verification.setBlockNumber(receipt.getBlockNumber().intValue());
                                    verification.setTxHash(receipt.getTransactionHash());
                                }
                                verificationRepository.saveAllAndFlush(verifications);
                            }
                        } catch (Exception e) {
                            log.error("Executor exception ", e);
                        }
                    });
                } catch (Exception e) {
                    log.error("Executor exception ", e);
                }
            }
        }).start();
    }

    public synchronized boolean addCertification(Long certificationNumber, String theSignatureOfCreator) {
        KV kv = new KV();
        kv.key = BigInteger.valueOf(certificationNumber);
        kv.value = theSignatureOfCreator;
        return DATA.add(kv);
    }

    public String getCertification(Long certificationNumber) throws Exception {
        return contract.getCertification(BigInteger.valueOf(certificationNumber))
                .send();
    }

    private static class KV {
        private BigInteger key;
        private String value;
    }
}

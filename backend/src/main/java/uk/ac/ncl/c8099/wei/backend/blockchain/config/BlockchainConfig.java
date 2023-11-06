package uk.ac.ncl.c8099.wei.backend.blockchain.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.StaticGasProvider;
import uk.ac.ncl.c8099.wei.backend.blockchain.contract.DocumentIntegrity;

import java.io.IOException;
import java.math.BigInteger;

/**
 * @author wei tan
 */

@Configuration
public class BlockchainConfig {

    @Value("${web3j.client-address}")
    private String entryPoint;

    @Value("${web3j.wallet.private-key}")
    private String privateKey;

    @Value("${web3j.contract.document-integrity.address}")
    private String documentIntegrityContractAddress;

    @Bean
    public Web3j web3j() {
        return Web3j.build(new HttpService(entryPoint));
    }

    @Bean
    public Credentials credentials() {
        return Credentials.create(privateKey);
    }

    @Bean
    public DocumentIntegrity documentIntegrity(Web3j web3j, Credentials credentials) throws IOException {
        BigInteger gasPrice = web3j.ethGasPrice().send().getGasPrice();

        return DocumentIntegrity.load(
                documentIntegrityContractAddress,
                web3j,
                credentials,
                new StaticGasProvider(gasPrice, new BigInteger("30000000")));
    }
}

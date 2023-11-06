package uk.ac.ncl.c8099.wei.backend.service;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Sign;
import org.web3j.utils.Numeric;
import uk.ac.ncl.c8099.wei.backend.controller.context.AddressContext;
import uk.ac.ncl.c8099.wei.backend.dao.AccountRepository;
import uk.ac.ncl.c8099.wei.backend.dao.entity.Account;
import uk.ac.ncl.c8099.wei.backend.utils.JsonUtil;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.SignatureException;
import java.util.Arrays;
import java.util.List;

/**
 * AccountService is used to encrypt response data, decrypt request data, add account and login.
 *
 * @author wei tan
 */

@Service
public class AccountService {

    @Resource
    private AccountRepository accountRepository;

    @Resource
    private Credentials credentials;

    /**
     * Add account.
     *
     * @param address account address
     * @return If add successful.
     */
    public Boolean addAccount(String address) {
        if (accountRepository.countAccountByAddress(address) == 0) {
            Account account = new Account();
            account.setAddress(address);
            account = accountRepository.save(account);
            return account.getId() != null;
        }
        return false;
    }

    /**
     * This method returns contain this public key and cipher is right
     *
     * @return if contain this address
     */
    public boolean containAddress() {
        List<String> addressList = accountRepository.findAll()
                .stream()
                .map(Account::getAddress).toList();

        String requestAddress = AddressContext.get();
        boolean result = false;
        for (String address : addressList) {
            if (address.equals(requestAddress)) {
                result = true;
                break;
            }
        }
        return result;
    }


    /**
     * <a href="https://blog.web3labs.com/web3development/how-to-sign-messages-with-web3j">Code Source</a>
     */
    public String sign(Object obj) {
        String jsonStr = JsonUtil.toJsonStr(obj);
        byte[] messageBytes = jsonStr.getBytes(StandardCharsets.UTF_8);
        Sign.SignatureData signature = Sign.signPrefixedMessage(messageBytes, credentials.getEcKeyPair());
        byte[] value = new byte[65];
        System.arraycopy(signature.getR(), 0, value, 0, 32);
        System.arraycopy(signature.getS(), 0, value, 32, 32);
        System.arraycopy(signature.getV(), 0, value, 64, 1);
        return Numeric.toHexString(value);
    }

    /**
     * <a href="https://gist.github.com/megamattron/94c05789e5ff410296e74dad3b528613?permalink_comment_id=4517910#gistcomment-4517910">Code Source</a>
     */
    public boolean verifySignature(String signature, String message) throws SignatureException {
        return verifySignatureWithAddress(signature, message, AddressContext.get());
    }

    public boolean verifySignatureWithAddress(String signature, String message, String address) throws SignatureException {
        byte[] signatureBytes = Numeric.hexStringToByteArray(signature);

        if (signatureBytes.length < 64 || signatureBytes.length > 65) {
            throw new RuntimeException("Illegal Input");
        }

        byte v = signatureBytes[64];
        if (v < 27) v += 27;

        byte[] r = Arrays.copyOfRange(signatureBytes, 0, 32);
        byte[] s = Arrays.copyOfRange(signatureBytes, 32, 64);

        Sign.SignatureData signatureData = new Sign.SignatureData(v, r, s);

        BigInteger recoveredKey = Sign.signedPrefixedMessageToKey(message.getBytes(), signatureData);
        return ("0x" + Keys.getAddress(recoveredKey)).equals(address);
    }

}
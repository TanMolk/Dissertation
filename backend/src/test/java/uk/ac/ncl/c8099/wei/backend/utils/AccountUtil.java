package uk.ac.ncl.c8099.wei.backend.utils;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Sign;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.SignatureException;
import java.util.Arrays;

/**
 * @author wei tan
 */
public class AccountUtil {

    public static String sign(Credentials credentials, Object obj) {
        String jsonStr = JsonUtil.toJsonStr(obj);
        byte[] messageBytes = jsonStr.getBytes(StandardCharsets.UTF_8);
        Sign.SignatureData signature = Sign.signPrefixedMessage(messageBytes, credentials.getEcKeyPair());
        byte[] value = new byte[65];
        System.arraycopy(signature.getR(), 0, value, 0, 32);
        System.arraycopy(signature.getS(), 0, value, 32, 32);
        System.arraycopy(signature.getV(), 0, value, 64, 1);
        return Numeric.toHexString(value);
    }

    public static boolean verifySignature(String address,String signature, String message) throws SignatureException {
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

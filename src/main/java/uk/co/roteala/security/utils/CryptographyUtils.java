package uk.co.roteala.security.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.asn1.sec.SECNamedCurves;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.asn1.x9.X9IntegerConverter;
import org.bouncycastle.math.ec.ECAlgorithms;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement;
import org.bouncycastle.math.ec.ECPoint;
import uk.co.roteala.common.MempoolTransaction;
import uk.co.roteala.common.SignatureModel;
import uk.co.roteala.security.PubKey;
import uk.co.roteala.security.PublicKey;
import uk.co.roteala.utils.Base58;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

@Slf4j
@UtilityClass
public class CryptographyUtils{
    private static final String MESSAGE_PREFIX = "\u0019Ethereum Signed Message:\n";

    private static final Pattern HEXADECIMAL_PATTERN = Pattern.compile("0x[a-fA-F0-9]{40}");

    private static byte[] getEthereumMessagePrefix(int messageLength) {
        return MESSAGE_PREFIX
                .concat(String.valueOf(messageLength))
                .getBytes(StandardCharsets.UTF_8);
    }

    private static byte[] getEthereumMessageHash(byte[] message) {
        byte[] prefix = getEthereumMessagePrefix(message.length);

        byte[] result = new byte[prefix.length + message.length];
        System.arraycopy(prefix, 0, result, 0, prefix.length);
        System.arraycopy(message, 0, result, prefix.length, message.length);

        return HashingService.sha3(result);
    }

    public List<PublicKey> recoverPublicKeys(MempoolTransaction transaction) {
        List<PublicKey> publicKeys = new ArrayList<>();

        X9ECParameters curveRaw = SECNamedCurves.getByName("secp256k1");

        ECCurve curve = curveRaw.getCurve();

        //Format signature to BigInteger
        BigInteger x = new BigInteger(transaction.getSignature().getR(), 16);
        BigInteger s = new BigInteger(transaction.getSignature().getS(), 16);
        BigInteger h = new BigInteger(1, getEthereumMessageHash(transaction
                .computeJSONstring().getBytes(StandardCharsets.UTF_8)));

        ECFieldElement xCoord = curve.fromBigInteger(x);
        ECPoint rPoint = solveYCoordinates(curve, xCoord);

        BigInteger sInverted = s.modInverse(curve.getOrder());
        BigInteger rInverted = x.modInverse(curve.getOrder());

        BigInteger gCoefficient = rInverted.multiply(h).mod(curve.getOrder());

        BigInteger rsInverted = x.multiply(sInverted).modInverse(curve.getOrder());

        ECPoint publicPoint1 = rPoint.multiply(rsInverted);
        ECPoint publicPoint1Negate = rPoint.multiply(rsInverted).negate();

        ECPoint publicPoint2 = curveRaw.getG().multiply(gCoefficient).negate();

        ECPoint publicKeyPoint = publicPoint1.add(publicPoint2).normalize();
        ECPoint publicKeyPointNegate = publicPoint1Negate.add(publicPoint2).normalize();

        publicKeys.add(PublicKey.builder().ecPoint(publicKeyPoint).build());
        publicKeys.add(PublicKey.builder().ecPoint(publicKeyPointNegate).build());

        return publicKeys;
    }


    public boolean checkKeyWithPubKeyHash(PublicKey key, String pubKeyHash) {
        return Objects.equals(key.getPubKeyHash(), pubKeyHash);
    }

    public static boolean isValidAddress(String address) {
        if (address == null || address.length() != 42) {
            return false;
        }
        return HEXADECIMAL_PATTERN.matcher(address).matches();
    }

    public boolean checkKeyWithAddress(PublicKey key, String address) {
        return Objects.equals(key.toAddress(), address);
    }

    public String bytesToHexString(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = String.format("%02x", b);
            hexString.append(hex);
        }
        return hexString.toString();
    }

    private static ECPoint solveYCoordinates(ECCurve curve, ECFieldElement x) {
        List<ECPoint> points = new ArrayList<>();

        BigInteger beta = x.toBigInteger()
                .pow(3)
                .add(x.toBigInteger().multiply(curve.getA().toBigInteger()))
                .add(curve.getB().toBigInteger());

        BigInteger y = beta
                .modPow(curve.getField().getCharacteristic()
                        .add(BigInteger.ONE)
                        .divide(BigInteger.valueOf(4)), curve.getField().getCharacteristic());


        points.add(curve.createPoint(x.toBigInteger(), y));

        return curve.createPoint(x.toBigInteger(), y);
    }
}

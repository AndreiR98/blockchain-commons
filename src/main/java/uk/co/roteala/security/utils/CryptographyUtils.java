package uk.co.roteala.security.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.asn1.sec.SECNamedCurves;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement;
import org.bouncycastle.math.ec.ECPoint;
import uk.co.roteala.common.MempoolTransaction;
import uk.co.roteala.security.PublicKey;
import uk.co.roteala.utils.Base58;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@UtilityClass
public class CryptographyUtils{
    public List<PublicKey> recoverPublicKeys(MempoolTransaction transaction) throws JsonProcessingException {
        //ECParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec("secp256k1");
        List<PublicKey> publicKeys = new ArrayList<>();

        X9ECParameters curveRaw = SECNamedCurves.getByName("secp256k1");

        ECCurve curve = curveRaw.getCurve();


        //Format signature to BigInteger
        BigInteger x = new BigInteger(transaction.getSignature().getR(), 16);
        BigInteger s = new BigInteger(transaction.getSignature().getS(), 16);
        BigInteger h = new BigInteger(transaction.computeSigningHash(), 16);

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

    public boolean checkKeyWithAddress(PublicKey key, String address) {
        return Objects.equals(key.toAddress(), address);
    }

    public String decodeAddress(String address){
        byte[] addressDecoded = Base58.decode(address);

        return bytesToHexString(addressDecoded);
    }

    public String bytesToHexString(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = String.format("%02x", b);
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public static ECPoint solveYCoordinates(ECCurve curve, ECFieldElement x) {
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

package uk.co.roteala.security;

import lombok.Getter;
import lombok.Setter;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.bouncycastle.math.ec.ECPoint;
import uk.co.roteala.common.UTXO;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.security.*;


public class ECKey {

    @Getter
    @Setter
    private PrivateKey privateKey;

    //Contains the public key point
    @Getter
    private PublicKey publicKey;

    //Implement the logic for the key

    public ECKey(String secretHex) {
        this.privateKey = PrivateKey.builder()
                    .d(secretHex)
                    .build();

        this.publicKey = this.privateKey.getPublicKey();
    }

    public ECKey() {
        generateRandomEC();

        this.publicKey = this.privateKey.getPublicKey();
    }

    private void generateRandomEC() {
        Security.addProvider(new BouncyCastleProvider());
        KeyPair keys = null;

        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EC", "BC");

            ECNamedCurveParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec("secp256k1");

            keyGen.initialize(ecSpec, new SecureRandom());

            keys =  keyGen.generateKeyPair();

            this.privateKey = PrivateKey.builder()
                    .d(((BCECPrivateKey) keys.getPrivate()).getD().toString(16))
                    .build();

        } catch (Exception e){
        }
    }

    public String signUTXO(UTXO utxo) throws NoSuchAlgorithmException {
        final String utxoHash = utxo.hashHeader();
        final String algorithm = "HmacSHA256";

        //Signature structure:
        //signature_r signature_s

        ECNamedCurveParameterSpec ecParameterSpec = ECNamedCurveTable.getParameterSpec("secp256k1");

        ECPoint ecPoint = ecParameterSpec.getG();
        BigInteger secretKey = this.getPrivateKey().getD();

        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.toByteArray(), algorithm);
        Mac mac = Mac.getInstance(algorithm);

        byte[] hmacBytes = mac.doFinal(utxoHash.getBytes());

        final String r = ecPoint.multiply(new BigInteger(bytesToHexString(hmacBytes), 16)).normalize().getAffineXCoord().toString();

        return r;
    }

    private static String bytesToHexString(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = String.format("%02x", b);
            hexString.append(hex);
        }
        return hexString.toString();
    }
}

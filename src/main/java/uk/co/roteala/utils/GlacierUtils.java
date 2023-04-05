package uk.co.roteala.utils;

import lombok.experimental.UtilityClass;
import org.bouncycastle.asn1.sec.SECNamedCurves;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.bouncycastle.jcajce.provider.digest.RIPEMD160;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.bouncycastle.jce.spec.ECNamedCurveSpec;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECPoint;
import org.springframework.util.SerializationUtils;
import uk.co.roteala.common.*;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.*;
import java.security.interfaces.ECKey;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;

@UtilityClass
public class GlacierUtils {
    public static String generateRandomSha256(Integer size) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");

        SecureRandom secureRandom = new SecureRandom();

        byte[] randomBytes = new byte[size];

        secureRandom.nextBytes(randomBytes);

        byte[] hash = messageDigest.digest(randomBytes);

        return bytesToHex(hash, size);
    }

    public static String generateSHA256Digest(byte[] bytes, Integer size) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");

        byte[] hash = messageDigest.digest(bytes);

        return bytesToHex(hash, size);
    }

    public static byte[] generateSHA256Digest(byte[] bytes) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");

        return messageDigest.digest(bytes);
    }

    public static byte[] generateRIPEDM160Digest(byte[] bytes) throws NoSuchAlgorithmException {
        Security.addProvider(new BouncyCastleProvider());


        MessageDigest messageDigest = MessageDigest.getInstance("RIPEMD160");



        return messageDigest.digest(bytes);
    }

    public static String bytesToHex(byte[] hash, int length) {
        StringBuilder hexString = new StringBuilder();

        for (int i = 0; i < length; i++) {

            String hex = Integer.toHexString(0xff & hash[i]);

            if (hex.length() == 1) {
                hexString.append('0');
            }

            hexString.append(hex);
        }

        return hexString.toString();
    }

    public static String bytesToHex(byte[] bytes) {
        final char[] HEX_ARRAY = "0123456789abcdef".toCharArray();

        char[] hexChars = new char[bytes.length * 2];

        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;

            hexChars[j * 2] = HEX_ARRAY[v >>> 4];

            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }

        return new String(hexChars);
    }
    private KeyPair generateKeyPair() throws NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException {
        Security.addProvider(new BouncyCastleProvider());

        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA", "BC");

        ECNamedCurveParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec("secp256k1");

        keyGen.initialize(ecSpec, new SecureRandom());

        return keyGen.generateKeyPair();
    }

    public SignatureModel signTransaction(byte[] message, String privateKey) throws NoSuchAlgorithmException, NoSuchProviderException, SignatureException, InvalidKeyException, InvalidKeySpecException {
        SignatureModel signature = new SignatureModel();
        Security.addProvider(new BouncyCastleProvider());
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA", "BC");

        //ECNamedCurveParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec("secp256k1");

        X9ECParameters curveParams = SECNamedCurves.getByName("secp256k1");
        ECCurve curve = curveParams.getCurve();

        ECParameterSpec ecSpec = new ECNamedCurveSpec("secp256k1", curve, curveParams.getG(), curveParams.getN());

        final BigInteger d = new BigInteger(privateKey, 16);

        ECPrivateKeySpec privateKeySpec = new ECPrivateKeySpec(d, ecSpec);

        Signature sig = Signature.getInstance("SHA256withECDSA", "BC");
        sig.initSign(KeyFactory.getInstance("ECDSA", "BC").generatePrivate(privateKeySpec));
        sig.update(message);

        byte[] signatureBytes = sig.sign();

        signature.setSignature(bytesToHex(signatureBytes));


        return signature;
    }

    public MockAccount getFromPrivateKey(String privateKey){
        MockAccount account = new MockAccount();

        account.setPrivateKey(privateKey);
        account.setScriptKey(addressToPubScript(privateKey));
        account.setAddress(formatAddressFromHex(privateKey));

        return account;
    }

    public String addressToPubScript(String privateKeyHex) {

        final BigInteger d = new BigInteger(privateKeyHex, 16);


        ECNamedCurveParameterSpec ecParameterSpec = ECNamedCurveTable.getParameterSpec("secp256k1");

        ECPoint ecPoint = ecParameterSpec.getG().multiply(d).normalize();


        final String hexX = ecPoint.getAffineXCoord().toBigInteger().toString(16);
        final String hexY = ecPoint.getAffineYCoord().toBigInteger().toString(16);

        return getScriptAddress(hexX, hexY);
    }

    private String formatAddressFromHex(String privateKeyHex){

        final BigInteger d = new BigInteger(privateKeyHex, 16);


        ECNamedCurveParameterSpec ecParameterSpec = ECNamedCurveTable.getParameterSpec("secp256k1");

        ECPoint ecPoint = ecParameterSpec.getG().multiply(d).normalize();


        final String hexX = ecPoint.getAffineXCoord().toBigInteger().toString(16);
        final String hexY = ecPoint.getAffineYCoord().toBigInteger().toString(16);

        // Combine the hex-encoded X and Y public keys into a single hex-encoded public key
        String hexPublicKey = "04" + hexX + hexY;
        // Hash the public key using SHA-256 and RIPEMD-160
        byte[] publicKeyBytes = hexStringToByteArray(hexPublicKey);
        byte[] sha256 = sha256Hash(publicKeyBytes);
        byte[] ripeMd160 = ripemd160Hash(sha256);
        // Add the version byte (0x00 for mainnet) to the front of the RIPEMD-160 hash
        byte[] extendedRipeMd160 = new byte[ripeMd160.length + 1];
        extendedRipeMd160[0] = 0x00;
        System.arraycopy(ripeMd160, 0, extendedRipeMd160, 1, ripeMd160.length);
        // Calculate the checksum by hashing the extended RIPEMD-160 hash twice using SHA-256
        byte[] checksum = doubleSHA256(extendedRipeMd160);
        // Concatenate the extended RIPEMD-160 hash and the checksum
        byte[] bytesToEncode = new byte[extendedRipeMd160.length + 4];
        System.arraycopy(extendedRipeMd160, 0, bytesToEncode, 0, extendedRipeMd160.length);
        System.arraycopy(checksum, 0, bytesToEncode, extendedRipeMd160.length, 4);
        // Base58Check encode the result
        return Base58.encode(bytesToEncode);
    }

    private String formatAddress(BCECPublicKey publicKey) {
        String hexX = publicKey.getQ().getAffineXCoord().toBigInteger().toString(16);
        String hexY = publicKey.getQ().getAffineYCoord().toBigInteger().toString(16);

        // Combine the hex-encoded X and Y public keys into a single hex-encoded public key
        String hexPublicKey = "04" + hexX + hexY;
        // Hash the public key using SHA-256 and RIPEMD-160
        byte[] publicKeyBytes = hexStringToByteArray(hexPublicKey);
        byte[] sha256 = sha256Hash(publicKeyBytes);
        byte[] ripeMd160 = ripemd160Hash(sha256);
        // Add the version byte (0x00 for mainnet) to the front of the RIPEMD-160 hash
        byte[] extendedRipeMd160 = new byte[ripeMd160.length + 1];
        extendedRipeMd160[0] = 0x00;
        System.arraycopy(ripeMd160, 0, extendedRipeMd160, 1, ripeMd160.length);
        // Calculate the checksum by hashing the extended RIPEMD-160 hash twice using SHA-256
        byte[] checksum = doubleSHA256(extendedRipeMd160);
        // Concatenate the extended RIPEMD-160 hash and the checksum
        byte[] bytesToEncode = new byte[extendedRipeMd160.length + 4];
        System.arraycopy(extendedRipeMd160, 0, bytesToEncode, 0, extendedRipeMd160.length);
        System.arraycopy(checksum, 0, bytesToEncode, extendedRipeMd160.length, 4);
        // Base58Check encode the result
        return Base58.encode(bytesToEncode);
    }

    private String getScriptAddress(String hexX, String hexY) {
        //String hexX = publicKey.getQ().getAffineXCoord().toBigInteger().toString(16);
        //String hexY = publicKey.getQ().getAffineYCoord().toBigInteger().toString(16);

        // Combine the hex-encoded X and Y public keys into a single hex-encoded public key
        String hexPublicKey = "04" + hexX + hexY;
        // Hash the public key using SHA-256 and RIPEMD-160
        byte[] publicKeyBytes = hexStringToByteArray(hexPublicKey);
        byte[] sha256 = sha256Hash(publicKeyBytes);
        byte[] ripeMd160 = ripemd160Hash(sha256);

        return bytesToHex(ripeMd160);
    }

    public MockAccount generateMockAccount() throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {
        KeyPair keyPair = generateKeyPair();

        BCECPublicKey publicKey = (BCECPublicKey) keyPair.getPublic();
        BCECPrivateKey privateKey = (BCECPrivateKey) keyPair.getPrivate();

        String address = formatAddress(publicKey);
        String scriptAddress = getScriptAddress(publicKey.getW().getAffineX().toString(16), publicKey.getW().getAffineY().toString(16));

        MockAccount account = new MockAccount();

        account.setAddress(address);
        account.setPrivateKey(privateKey.getD().toString(16));
        account.setWif(generateWif(keyPair));
        account.setScriptKey(scriptAddress);

        return account;
    }



    public String generateWif(KeyPair keyPair) throws NoSuchAlgorithmException {
        BCECPrivateKey privateKey = (BCECPrivateKey) keyPair.getPrivate();

        byte[] privateKeyBytes = privateKey.getD().toByteArray();

        // If the byte array has a leading zero, remove it
        if (privateKeyBytes[0] == 0) {
            byte[] tmp = new byte[privateKeyBytes.length - 1];
            System.arraycopy(privateKeyBytes, 1, tmp, 0, tmp.length);
            privateKeyBytes = tmp;
        }

        // Add the prefix byte 0x80 to the byte array
        byte[] prefixPrivateKeyBytes = new byte[privateKeyBytes.length + 1];
        prefixPrivateKeyBytes[0] = (byte) 0x80;
        System.arraycopy(privateKeyBytes, 0, prefixPrivateKeyBytes, 1, privateKeyBytes.length);

        // Take the SHA-256 hash of the byte array twice
        byte[] hash = doubleSHA256(prefixPrivateKeyBytes);

        // Take the first 4 bytes of the double SHA-256 hash as the checksum
        byte[] checksum = new byte[4];
        System.arraycopy(hash, 0, checksum, 0, 4);

        // Append the 4-byte checksum to the byte array from step 2
        byte[] privateKeyWithChecksum = new byte[prefixPrivateKeyBytes.length + 4];
        System.arraycopy(prefixPrivateKeyBytes, 0, privateKeyWithChecksum, 0, prefixPrivateKeyBytes.length);
        System.arraycopy(checksum, 0, privateKeyWithChecksum, prefixPrivateKeyBytes.length, 4);

        // Convert the resulting byte array to Base58Check encoding
        return Base58.encode(privateKeyWithChecksum);


    }

    private static byte[] doubleSHA256(byte[] input) {
        try{
            Security.addProvider(new BouncyCastleProvider());
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            byte[] firstHash = sha256.digest(input);
            return sha256.digest(firstHash);
        } catch (NoSuchAlgorithmException e){
            throw new RuntimeException(e);
        }
    }

    private static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    private static byte[] sha256Hash(byte[] input) {
        try {
            Security.addProvider(new BouncyCastleProvider());
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            return sha256.digest(input);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private static byte[] ripemd160Hash(byte[] input) {
        try {
            Security.addProvider(new BouncyCastleProvider());
            MessageDigest ripemd160 = MessageDigest.getInstance("RIPEMD160");
            return ripemd160.digest(input);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private static byte[] addVersionByte(byte[] input) {
        byte[] versionedHash = new byte[input.length + 1];
        versionedHash[0] = (byte) 0x00; // Version byte for mainnet
        System.arraycopy(input, 0, versionedHash, 1, input.length);
        return versionedHash;
    }

    private static byte[] concatenateByteArrays(byte[] a, byte[] b) {
        byte[] result = new byte[a.length + b.length];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }


    public static String parseIpAddress(String address) {
        return address
                .substring(1)
                .split(":")[0];
    }

    private BigDecimal formatZeroBalance(Chain chain) {
        final int decimalPoints = chain.getCoin().getDecimalPoints();

        StringBuilder after = new StringBuilder();

        for(int i = 0; i < decimalPoints; i++){
            after.append("0");
        }

        return new BigDecimal("0."+after);
    }

    /**
     * Default return windows == true
     * */
    public boolean getSystem() {
        if(System.getProperty("os.name").contains("linux")){
            return false;
        } else {
            return true;
        }
    }
}

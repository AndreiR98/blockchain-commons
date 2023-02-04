package uk.co.roteala.utils;

import lombok.Getter;
import lombok.experimental.UtilityClass;

import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.ECKey;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPrivateKeySpec;
import java.security.spec.EllipticCurve;

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

    public static ECPrivateKey generateECPrivateKey() {
        KeyPairGenerator keyPairGenerator = new K
    }

    public static ECPublicKey generateECPublicKey() {}

    public static String formatECKey(ECKey key) {}
}

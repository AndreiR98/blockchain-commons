package uk.co.roteala.security.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.crypto.digests.SHAKEDigest;
import org.bouncycastle.jcajce.provider.digest.Keccak;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;

@Slf4j
@UtilityClass
public class HashingService {
    public byte[] hexStringToByteArray(String hexString) {
        // Remove any spaces or other formatting characters from the input string
        hexString = hexString.replaceAll("\\s", "");

        // Create a byte array with half the length of the hexadecimal string
        byte[] byteArray = new byte[hexString.length() / 2];

        // Iterate through the hexadecimal string by pairs of characters
        for (int i = 0; i < hexString.length(); i += 2) {
            // Get the two characters representing one byte
            String byteString = hexString.substring(i, i + 2);

            // Convert the byte string to a byte value
            byte b = (byte) Integer.parseInt(byteString, 16);

            // Store the byte value in the byte array
            byteArray[i / 2] = b;
        }

        return byteArray;
    }

    public byte[] sha256Hash(byte[] input) {
        try {
            Security.addProvider(new BouncyCastleProvider());
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            return sha256.digest(input);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] ripemd160Hash(byte[] input) {
        try {
            Security.addProvider(new BouncyCastleProvider());
            MessageDigest ripemd160 = MessageDigest.getInstance("RIPEMD160");
            return ripemd160.digest(input);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] firstRoundAddressGenerator(byte[] hex) {
        return ripemd160Hash(sha256Hash(hex));
    }

    public byte[] doubleSHA256(byte[] input) {
        try {
            Security.addProvider(new BouncyCastleProvider());
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            byte[] firstHash = sha256.digest(input);
            return sha256.digest(firstHash);
        } catch (NoSuchAlgorithmException exception){
            throw new RuntimeException(exception);
        }
    }

    public String bytesToHexString(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = String.format("%02x", b);
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public byte[] computeSHA3(String input) {
        try {
            Security.addProvider(new BouncyCastleProvider());
            MessageDigest crypt = MessageDigest.getInstance("SHA3-384");

            crypt.update(input.getBytes(StandardCharsets.UTF_8));

            return crypt.digest();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] computeKeccak(byte[] bytes) {
        Keccak.DigestKeccak kecc = new Keccak.Digest256();

        kecc.update(bytes);

        return kecc.digest();
    }
}

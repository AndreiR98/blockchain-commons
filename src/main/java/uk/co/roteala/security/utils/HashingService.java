package uk.co.roteala.security.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.crypto.digests.SHAKEDigest;
import org.bouncycastle.jcajce.provider.digest.Keccak;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import uk.co.roteala.core.rlp.Strings;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;

@Slf4j
@UtilityClass
public class HashingService {
    private static final String HEX_PREFIX = "0x";
    private static final char[] HEX_CHAR_MAP = "0123456789abcdef".toCharArray();
//    public byte[] hexStringToByteArray(String hexString) {
//        // Remove any spaces or other formatting characters from the input string
//        hexString = hexString.replaceAll("\\s", "");
//
//        // Create a byte array with half the length of the hexadecimal string
//        byte[] byteArray = new byte[hexString.length() / 2];
//
//        // Iterate through the hexadecimal string by pairs of characters
//        for (int i = 0; i < hexString.length(); i += 2) {
//            // Get the two characters representing one byte
//            String byteString = hexString.substring(i, i + 2);
//
//            // Convert the byte string to a byte value
//            byte b = (byte) Integer.parseInt(byteString, 16);
//
//            // Store the byte value in the byte array
//            byteArray[i / 2] = b;
//        }
//
//        return byteArray;
//    }

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

    public static String toHexString(byte[] input) {
        return toHexString(input, 0, input.length, true);
    }

    public static String toHexString(byte[] input, int offset, int length, boolean withPrefix) {
        String output = new String(toHexCharArray(input, offset, length));
        return withPrefix ? "0x" + output : output;
    }

    private static char[] toHexCharArray(byte[] input, int offset, int length) {
        char[] output = new char[length << 1];
        int i = offset;

        for(int j = 0; i < length + offset; ++j) {
            int v = input[i] & 255;
            output[j++] = HEX_CHAR_MAP[v >>> 4];
            output[j] = HEX_CHAR_MAP[v & 15];
            ++i;
        }

        return output;
    }

    public String bytesToHexString(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = String.format("%02x", b);
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public static String computeSHA3(String input) {
        byte[] bytes = hexStringToByteArray(input);
        byte[] result = sha3(bytes);

        return toHexString(result);
//        try {
//            Security.addProvider(new BouncyCastleProvider());
//            MessageDigest crypt = MessageDigest.getInstance("SHA3-384");
//
//            crypt.update(input.getBytes(StandardCharsets.UTF_8));
//
//            return crypt.digest();
//        } catch (NoSuchAlgorithmException e) {
//            throw new RuntimeException(e);
//        }
    }

    public byte[] computeKeccak(byte[] bytes) {
        Keccak.DigestKeccak kecc = new Keccak.Digest256();

        kecc.update(bytes);

        return kecc.digest();
    }

    public static byte[] sha3(byte[] input) {
        return sha3(input, 0, input.length);
    }

    public static byte[] sha3(byte[] input, int offset, int length) {
        Keccak.DigestKeccak kecc = new Keccak.Digest256();
        kecc.update(input, offset, length);

        return kecc.digest();
    }

    public static byte[] hexStringToByteArray(String input) {
        String cleanInput = cleanHexPrefix(input);

        int len = cleanInput.length();

        if (len == 0) {
            return new byte[0];
        } else {
            byte[] data;
            byte startIdx;
            if (len % 2 != 0) {
                data = new byte[len / 2 + 1];
                data[0] = (byte)Character.digit(cleanInput.charAt(0), 16);
                startIdx = 1;
            } else {
                data = new byte[len / 2];
                startIdx = 0;
            }

            for(int i = startIdx; i < len; i += 2) {
                data[(i + 1) / 2] = (byte)((Character.digit(cleanInput.charAt(i), 16) << 4) + Character.digit(cleanInput.charAt(i + 1), 16));
            }

            return data;
        }
    }

    public static String cleanHexPrefix(String input) {
        return containsHexPrefix(input) ? input.substring(2) : input;
    }

    public static boolean containsHexPrefix(String input) {
        return !Strings.isEmpty(input) && input.length() > 1 && input.charAt(0) == '0' && input.charAt(1) == 'x';
    }
}

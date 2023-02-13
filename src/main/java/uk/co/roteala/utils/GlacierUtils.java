package uk.co.roteala.utils;

import lombok.experimental.UtilityClass;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.bouncycastle.jcajce.provider.digest.RIPEMD160;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
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

    /**
     * Create key paor from secure random or from imported key
     * TO DO: Fully implement with boucy castle
     * */
    public KeyPair generateKeyPair(SecureRandom random) throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {

        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EC");

        ECGenParameterSpec ecSpec = new ECGenParameterSpec("secp256k1");

        if (random == null) {
            SecureRandom randomGenBytes = SecureRandom.getInstanceStrong();

            keyGen.initialize(ecSpec, randomGenBytes);

        } else {
            keyGen.initialize(ecSpec, random);
        }

        return keyGen.genKeyPair();
    }

    private KeyPair generateKeyPair() throws NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException {
        Security.addProvider(new BouncyCastleProvider());

        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA", "BC");

        ECNamedCurveParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec("secp256k1");

        keyGen.initialize(ecSpec, new SecureRandom());

        return keyGen.generateKeyPair();
    }

    private String formatAddress(BCECPublicKey publicKey) throws NoSuchAlgorithmException {
        StringBuilder builder = new StringBuilder();

        //Add 0x20 byte to hex value of X
        builder.append("20"+publicKey.getQ().getAffineXCoord().toBigInteger().toString(16));

        //Check if Y is odd or even
        if(publicKey.getQ().getAffineXCoord().toBigInteger().mod(new BigInteger("2")).equals(new BigInteger("0"))){
            builder.append("02");
        } else {
            builder.append("03");
        }

        final byte[] firstRound = generateSHA256Digest(SerializationUtils.serialize(builder.toString()));

        //Get the checkshum
        final byte[] checkSum = new byte[4];
        System.arraycopy(firstRound, 0, checkSum, 0, 4);

        final byte[] secondRound = generateRIPEDM160Digest(firstRound);

        final byte[] addressInBytes = new byte[secondRound.length + checkSum.length];

        System.arraycopy(secondRound, 0, addressInBytes, 0, secondRound.length);
        System.arraycopy(checkSum, 0, addressInBytes, secondRound.length, checkSum.length);

        final String address = Base58.encode(addressInBytes);

        return address;
    }

    public AccountModel generateGenesisAccount(BigDecimal value) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {
        KeyPair keyPair = generateKeyPair();

        BCECPublicKey publicKey = (BCECPublicKey) keyPair.getPublic();

        String address = formatAddress(publicKey);

        AccountModel account = new AccountModel();

        account.setTimeStamp(String.valueOf(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)));
        account.setBalance(value);
        account.setAddress(AddressBaseModel.builder().hexEncoded(address).build());
        account.setType(AccountType.GENESIS);

        return account;
    }

    /**
     * Create new account
     * */
    public AccountModel createAccount(BCECPublicKey publicKey, Chain chain) throws NoSuchAlgorithmException {
        AccountModel account = new AccountModel();

        String address = formatAddress(publicKey);

        account.setTimeStamp(String.valueOf(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)));
        account.setBalance(formatZeroBalance(chain));
        account.setAddress(AddressBaseModel.builder().hexEncoded(address).build());
        account.setType(AccountType.PERSONAL);

        return account;
    }

    private BigDecimal formatZeroBalance(Chain chain) {
        final int decimalPoints = chain.getCoin().getDecimalPoints();

        StringBuilder after = new StringBuilder();

        for(int i = 0; i < decimalPoints; i++){
            after.append("0");
        }

        return new BigDecimal("0."+after);
    }

//    public TransactionBaseModel generateGenesisTransactions(AccountModel account) {
//        final byte[] genesisAddress = SerializationUtils.serialize("GENESIS");
//    }
}

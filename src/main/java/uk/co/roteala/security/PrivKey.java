package uk.co.roteala.security;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;

public interface PrivKey extends Key{

    BigInteger getD();

    String getHex();

    byte[] getEncoded();
    String toAddress();

    String toWIF();

    PublicKey getPublicKey();
}

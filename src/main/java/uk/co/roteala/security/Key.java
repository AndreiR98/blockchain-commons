package uk.co.roteala.security;

import java.security.NoSuchAlgorithmException;

public interface Key {
    KeyType getType();

    String toAddress();
}

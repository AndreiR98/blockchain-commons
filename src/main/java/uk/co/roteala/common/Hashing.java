package uk.co.roteala.common;

import java.security.NoSuchAlgorithmException;

public interface Hashing {
    String hashHeader() throws NoSuchAlgorithmException;

    byte[] serialized();
}

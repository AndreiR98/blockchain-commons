package uk.co.roteala.common;

import java.security.NoSuchAlgorithmException;

public interface HashingHeader {
    String hashHeader() throws NoSuchAlgorithmException;
}

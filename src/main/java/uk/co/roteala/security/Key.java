package uk.co.roteala.security;

public interface Key {
    KeyType getType();

    String toAddress();
}

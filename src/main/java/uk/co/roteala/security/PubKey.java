package uk.co.roteala.security;

public interface PubKey extends Key{
    String getX();

    String getY();

    byte[] encodedX();

    byte[] encodedY();
}

package uk.co.roteala.core.rlp;

import java.util.Arrays;

public class Bytes {
    public static byte[] trimLeadingBytes(byte[] bytes, byte b) {
        int offset;

        for(offset = 0; offset < bytes.length - 1 && bytes[offset] == b; ++offset) {
        }

        return Arrays.copyOfRange(bytes, offset, bytes.length);
    }

    public static byte[] trimLeadingZeroes(byte[] bytes) {
        return trimLeadingBytes(bytes, (byte)0);
    }
}

package uk.co.roteala;

import java.math.BigInteger;
import java.util.Arrays;

public class RlpString implements RlpType{
    private static final byte[] EMPTY = new byte[0];

    private final byte[] value;

    private RlpString(byte[] value) {
        this.value = value;
    }

    public byte[] getBytes() {
        return this.value;
    }

    public BigInteger asPositiveBigInteger() {
        return this.value.length == 0 ? BigInteger.ZERO : new BigInteger(1, this.value);
    }

    public String asString() {
        return toHexString(this.value);
    }

    public static RlpString create(byte[] value) {
        return new RlpString(value);
    }

    public static RlpString create(byte value) {
        return new RlpString(new byte[]{value});
    }

    public static RlpString create(BigInteger value) {
        if (value != null && value.signum() >= 1) {
            byte[] bytes = value.toByteArray();
            return bytes[0] == 0 ? new RlpString(Arrays.copyOfRange(bytes, 1, bytes.length)) : new RlpString(bytes);
        } else {
            return new RlpString(EMPTY);
        }
    }

    public static RlpString create(long value) {
        return create(BigInteger.valueOf(value));
    }

    public static RlpString create(String value) {
        return new RlpString(value.getBytes());
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            RlpString rlpString = (RlpString)o;
            return Arrays.equals(this.value, rlpString.value);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Arrays.hashCode(this.value);
    }

    public static String toHexString(byte[] input, int offset, int length, boolean withPrefix) {
        StringBuilder stringBuilder = new StringBuilder();
        if (withPrefix) {
            stringBuilder.append("0x");
        }

        for(int i = offset; i < offset + length; ++i) {
            stringBuilder.append(String.format("%02x", input[i] & 255));
        }

        return stringBuilder.toString();
    }

    public static String toHexString(byte[] input) {
        return toHexString(input, 0, input.length, true);
    }

    public static byte[] toBytesPadded(BigInteger value, int length) {
        byte[] result = new byte[length];
        byte[] bytes = value.toByteArray();
        int bytesLength;
        byte srcOffset;
        if (bytes[0] == 0) {
            bytesLength = bytes.length - 1;
            srcOffset = 1;
        } else {
            bytesLength = bytes.length;
            srcOffset = 0;
        }

        if (bytesLength > length) {
            throw new RuntimeException("Input is too large to put in byte array of size " + length);
        } else {
            int destOffset = length - bytesLength;
            System.arraycopy(bytes, srcOffset, result, destOffset, bytesLength);
            return result;
        }
    }

}

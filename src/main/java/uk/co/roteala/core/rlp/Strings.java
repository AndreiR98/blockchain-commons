package uk.co.roteala.core.rlp;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Strings {
    public static boolean isEmpty(String s) {
        return s == null || s.length() == 0;
    }

    private static String toHexString(byte[] input, int offset, int length, boolean withPrefix) {
        StringBuilder stringBuilder = new StringBuilder();

        if(withPrefix) {
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
}

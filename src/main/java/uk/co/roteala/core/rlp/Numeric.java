package uk.co.roteala.core.rlp;

import lombok.experimental.UtilityClass;

import java.math.BigInteger;
import java.nio.ByteBuffer;

@UtilityClass
public class Numeric {
    int CHAIN_ID_INC = 35;
    int LOWER_REAL_V = 27;
    public static byte[] hexStringToByteArray(String input) {
        String cleanInput = cleanHexPrefix(input);

        int len = cleanInput.length();

        if (len == 0) {
            return new byte[0];
        } else {
            byte[] data;
            byte startIdx;
            if (len % 2 != 0) {
                data = new byte[len / 2 + 1];
                data[0] = (byte)Character.digit(cleanInput.charAt(0), 16);
                startIdx = 1;
            } else {
                data = new byte[len / 2];
                startIdx = 0;
            }

            for(int i = startIdx; i < len; i += 2) {
                data[(i + 1) / 2] = (byte)((Character.digit(cleanInput.charAt(i), 16) << 4) + Character.digit(cleanInput.charAt(i + 1), 16));
            }

            return data;
        }
    }

    public static String cleanHexPrefix(String input) {
        return containsHexPrefix(input) ? input.substring(2) : input;
    }

    public static String prependHexPrefix(String input) {
        return !containsHexPrefix(input) ? "0x" + input : input;
    }

    public static boolean containsHexPrefix(String input) {
        return !Strings.isEmpty(input) && input.length() > 1 && input.charAt(0) == '0' && input.charAt(1) == 'x';
    }

    public static byte[] longToBytes(long x) {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.putLong(x);
        return buffer.array();
    }

    public static BigInteger toBigInt(byte[] value) {
        return new BigInteger(1, value);
    }
}

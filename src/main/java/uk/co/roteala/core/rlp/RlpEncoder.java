package uk.co.roteala.core.rlp;

import uk.co.roteala.RlpList;
import uk.co.roteala.RlpString;
import uk.co.roteala.RlpType;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static uk.co.roteala.RlpDecoder.OFFSET_SHORT_LIST;
import static uk.co.roteala.RlpDecoder.OFFSET_SHORT_STRING;

public class RlpEncoder {

    public static byte[] encode(RlpType values) {
        return values instanceof RlpString ? encodeString((RlpString) values) : encodeList((RlpList) values);
    }

    private static byte[] encode(byte[] bytesValues, int offset) {
        if (bytesValues.length == 1 && offset == OFFSET_SHORT_STRING && bytesValues[0] >= 0 && bytesValues[0] <= 127) {
            return bytesValues;
        } else {
            byte[] encodedStringLength;
            if (bytesValues.length <= 55) {
                encodedStringLength = new byte[bytesValues.length + 1];
                encodedStringLength[0] = (byte)(offset + bytesValues.length);
                System.arraycopy(bytesValues, 0, encodedStringLength, 1, bytesValues.length);
                return encodedStringLength;
            } else {
                encodedStringLength = toMinimalByteArray(bytesValues.length);
                byte[] result = new byte[bytesValues.length + encodedStringLength.length + 1];
                result[0] = (byte)(offset + 55 + encodedStringLength.length);
                System.arraycopy(encodedStringLength, 0, result, 1, encodedStringLength.length);
                System.arraycopy(bytesValues, 0, result, encodedStringLength.length + 1, bytesValues.length);
                return result;
            }
        }
    }

    public static byte[] encodeString(RlpString value) {
        return encode(value.getBytes(), OFFSET_SHORT_STRING);
    }

    private static byte[] toMinimalByteArray(int value) {
        byte[] encoded = toByteArray(value);

        for(int i = 0; i < encoded.length; ++i) {
            if (encoded[i] != 0) {
                return Arrays.copyOfRange(encoded, i, encoded.length);
            }
        }

        return new byte[0];
    }

    private static byte[] toByteArray(int value) {
        return new byte[]{(byte)(value >> 24 & 255), (byte)(value >> 16 & 255), (byte)(value >> 8 & 255), (byte)(value & 255)};
    }

    public static byte[] encodeList(RlpList value) {
        List<RlpType> values = value.getValues();
        if (values.isEmpty()) {
            return encode(new byte[0], OFFSET_SHORT_LIST);
        } else {
            byte[] result = new byte[0];

            RlpType entry;
            for(Iterator var3 = values.iterator(); var3.hasNext(); result = concat(result, encode(entry))) {
                entry = (RlpType)var3.next();
            }

            return encode(result, OFFSET_SHORT_LIST);
        }
    }

    private static byte[] concat(byte[] b1, byte[] b2) {
        byte[] result = Arrays.copyOf(b1, b1.length + b2.length);
        System.arraycopy(b2, 0, result, b1.length, b2.length);
        return result;
    }
}

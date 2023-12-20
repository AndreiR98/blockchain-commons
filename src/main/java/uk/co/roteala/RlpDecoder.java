package uk.co.roteala;

import java.util.ArrayList;

public class RlpDecoder {
    public static int OFFSET_SHORT_STRING = 128;
    public static int OFFSET_LONG_STRING = 183;
    public static int OFFSET_SHORT_LIST = 192;
    public static int OFFSET_LONG_LIST = 247;

    public RlpDecoder() {
    }

    public static RlpList decode(byte[] rlpEncoded) {
        RlpList rlpList = new RlpList(new ArrayList<>());
        traverse(rlpEncoded, 0, rlpEncoded.length, rlpList);

        return  rlpList;
    }

    private static void traverse(byte[] data, int startPos, int endPos, RlpList rlpList) {
        try {
            if (data != null && data.length != 0) {
                while(true) {
                    while(startPos < endPos) {
                        int prefix = data[startPos] & 255;
                        if (prefix < OFFSET_SHORT_STRING) {
                            byte[] rlpData = new byte[]{(byte)prefix};
                            rlpList.getValues().add(RlpString.create(rlpData));
                            ++startPos;
                        } else if (prefix == OFFSET_SHORT_STRING) {
                            rlpList.getValues().add(RlpString.create(new byte[0]));
                            ++startPos;
                        } else {
                            byte lenOfListLen;
                            if (prefix > OFFSET_SHORT_STRING && prefix <= OFFSET_LONG_STRING) {
                                lenOfListLen = (byte)(prefix - OFFSET_SHORT_STRING);
                                byte[] rlpData = new byte[lenOfListLen];
                                System.arraycopy(data, startPos + 1, rlpData, 0, lenOfListLen);
                                rlpList.getValues().add(RlpString.create(rlpData));
                                startPos += 1 + lenOfListLen;
                            } else {
                                int listLen;
                                if (prefix > OFFSET_LONG_STRING && prefix < OFFSET_SHORT_LIST) {
                                    lenOfListLen = (byte)(prefix - OFFSET_LONG_STRING);
                                    listLen = calcLength(lenOfListLen, data, startPos);
                                    byte[] rlpData = new byte[listLen];
                                    System.arraycopy(data, startPos + lenOfListLen + 1, rlpData, 0, listLen);
                                    rlpList.getValues().add(RlpString.create(rlpData));
                                    startPos += lenOfListLen + listLen + 1;
                                } else if (prefix >= OFFSET_SHORT_LIST && prefix <= OFFSET_LONG_LIST) {
                                    lenOfListLen = (byte)(prefix - OFFSET_SHORT_LIST);
                                    RlpList newLevelList = new RlpList(new ArrayList());
                                    traverse(data, startPos + 1, startPos + lenOfListLen + 1, newLevelList);
                                    rlpList.getValues().add(newLevelList);
                                    startPos += 1 + lenOfListLen;
                                } else if (prefix > OFFSET_LONG_LIST) {
                                    lenOfListLen = (byte)(prefix - OFFSET_LONG_LIST);
                                    listLen = calcLength(lenOfListLen, data, startPos);
                                    RlpList newLevelList = new RlpList(new ArrayList());
                                    traverse(data, startPos + lenOfListLen + 1, startPos + lenOfListLen + listLen + 1, newLevelList);
                                    rlpList.getValues().add(newLevelList);
                                    startPos += lenOfListLen + listLen + 1;
                                }
                            }
                        }
                    }

                    return;
                }
            }
        } catch (Exception var8) {
            throw new RuntimeException("RLP wrong encoding", var8);
        }
    }

    private static int calcLength(int lengthOfLength, byte[] data, int pos) {
        byte pow = (byte)(lengthOfLength - 1);
        int length = 0;

        for(int i = 1; i <= lengthOfLength; ++i) {
            length += (data[pos + i] & 255) << 8 * pow;
            --pow;
        }

        return length;
    }
}

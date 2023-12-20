package uk.co.roteala.core.rlp;

import lombok.experimental.UtilityClass;
import uk.co.roteala.RlpList;
import uk.co.roteala.RlpString;
import uk.co.roteala.RlpType;
import uk.co.roteala.common.MempoolTransaction;
import uk.co.roteala.common.RawTransaction;
import uk.co.roteala.common.Signature;
import uk.co.roteala.common.SignatureModel;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class RlpUtils {
    public static byte[] encode(RawTransaction transaction) {
        return encodeData(transaction, (Signature) null);
    }

    public static byte[] encode(RawTransaction transaction, long chainId) {
        Signature signature = new Signature(Numeric.longToBytes(chainId), new byte[0], new byte[0]);
        return encodeData(transaction, signature);
    }

    private static byte[] encodeData(RawTransaction transaction, Signature signature) {
        List<RlpType> values = asRlpValues(transaction, signature);
        RlpList rlpList = new RlpList(values);

        return RlpEncoder.encode(rlpList);
    }

    private static List<RlpType> asRlpValues(RawTransaction transaction, Signature signature) {
        List<RlpType> result = new ArrayList();

        result.add(RlpString.create(transaction.getNonce()));
        result.add(RlpString.create(transaction.getGasPrice()));
        result.add(RlpString.create(transaction.getGasLimit()));

        String to = transaction.getTo();

        if(to != null && to.length() > 0) {
            result.add(RlpString.create(Numeric.hexStringToByteArray(to)));
        } else {
            result.add(RlpString.create(""));
        }

        result.add(RlpString.create(transaction.getValue()));

        byte[] data = Numeric.hexStringToByteArray(transaction.getData());
        result.add(RlpString.create(data));

        if(signature != null) {
            result.add(RlpString.create(Bytes.trimLeadingZeroes(signature.getV())));
            result.add(RlpString.create(Bytes.trimLeadingZeroes(signature.getR())));
            result.add(RlpString.create(Bytes.trimLeadingZeroes(signature.getS())));
        }

        return result;
    }
}

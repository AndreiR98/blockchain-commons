package uk.co.roteala.common;

import lombok.*;
import org.springframework.util.SerializationUtils;
import uk.co.roteala.utils.GlacierUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class TransactionBaseModel extends BaseEmptyModel implements HashingHeader {
    private String hash;
    private String blockHash;
    private Integer blockNumber;
    private Integer transactionIndex;
    private String from;
    private String to;
    private List<UTXO> in;
    private List<UTXO> out;
    private long timeStamp;
    private TransactionStatus status;
    private TransactionType type;
    private SignatureModel signature;

    @Override
    public String hashHeader() throws NoSuchAlgorithmException {
        List<Object> map = new ArrayList<>();
        map.add(hash);
        map.add(from);
        map.add(to);
        map.add(in);
        map.add(out);
        map.add(timeStamp);
        map.add(blockNumber);
        return GlacierUtils.bytesToHex(GlacierUtils.generateSHA256Digest(SerializationUtils.serialize(map)));
    }
}

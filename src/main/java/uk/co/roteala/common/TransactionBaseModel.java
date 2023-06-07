package uk.co.roteala.common;

import lombok.*;
import org.springframework.util.SerializationUtils;
import uk.co.roteala.common.monetary.Coin;
import uk.co.roteala.utils.GlacierUtils;

import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class TransactionBaseModel extends BaseEmptyModel implements Hashing {
    private String hash;
    private String blockHash;
    private Integer blockNumber;
    private String from;
    private String to;
    private Coin fees;
    private Integer version;
    private Integer transactionIndex;
    private List<UTXO> in;
    private List<UTXO> out;
    private long timeStamp;
    private long confirmations;
    private long blockTime;
    private TransactionStatus status;

    @Override
    public String hashHeader() throws NoSuchAlgorithmException {
        List<Object> map = new ArrayList<>();
        map.add(in);
        map.add(out);
        map.add(fees);
        map.add(timeStamp);
        map.add(blockNumber);
        map.add(version);
        map.add(from);
        map.add(to);
        return GlacierUtils.bytesToHex(GlacierUtils.generateSHA256Digest(GlacierUtils.generateSHA256Digest(SerializationUtils.serialize(map))));
    }

    @Override
    public byte[] serialized() {
        List<Object> map = new ArrayList<>();
        map.add(hash);
        map.add(blockHash);
        map.add(blockNumber);
        map.add(fees);
        map.add(version);
        map.add(transactionIndex);
        map.add(in);
        map.add(out);
        map.add(timeStamp);
        map.add(confirmations);
        map.add(blockTime);
        map.add(status);

        return SerializationUtils.serialize(map);
    }
}

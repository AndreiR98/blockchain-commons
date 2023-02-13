package uk.co.roteala.common;

import lombok.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class BaseBlockModel extends BaseEmptyModel {
    //header that is going to be hashed
    private String version;
    private String markleRoot;
    private String timeStamp;
    private BigInteger nonce;
    private String previousHash;
    private Integer numberOfBits;
    //end of header

    private Integer index;
    private String hash;
    private String transactionsRoot;
    private String stateRoot;
    private String miner;
    private List<String> transactions;
    private Integer confirmations;
    private Integer size;
    private Integer height;
    private BlockStatus status;
    private BigDecimal difficulty;
}

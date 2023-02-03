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
    private Integer number;
    private String hash;
    private String previousHash;
    private String nextHash;
    private BigInteger nonce;
    private String transactionsRoot;
    private String stateRoot;
    private String mine;
    private List<String> transactions;
    private Integer confirmations;
    private Integer size;
    private Integer height;
    private Integer version;
    private String markleRoot;
    private String timeStamp;
    private BigDecimal difficulty;
    private String chainWork;
}

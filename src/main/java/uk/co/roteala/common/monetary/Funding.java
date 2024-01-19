package uk.co.roteala.common.monetary;

import lombok.*;
import uk.co.roteala.common.Account;
import uk.co.roteala.common.BasicModel;

import java.math.BigInteger;

@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Funding {
    private String targetAccountAddress;
    private String sourceAccountAddress;
    private BigInteger amount;
    private BigInteger networkFees;
    private BigInteger processingFees;
    private String hash;
    private String minerAddress;
    private TransactionType type;

    public enum TransactionType {
        MEMPOOL,
        TRANSACTION;
    }
}

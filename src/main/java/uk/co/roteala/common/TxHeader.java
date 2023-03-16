package uk.co.roteala.common;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class TxHeader implements Serializable {
    private String from;
    private String to;
    private Integer blockNumber;
    private BigDecimal amount;
    private long timeStamp;
}

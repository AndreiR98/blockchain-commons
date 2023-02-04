package uk.co.roteala.common;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class BaseCoinModel implements Serializable {
    private String name;
    private String nickName;
    private BigDecimal maxAmount;
    private Integer decimalPoints;
}

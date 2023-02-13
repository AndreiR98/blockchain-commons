package uk.co.roteala.common;

import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class AccountEntry extends BaseEmptyModel {
    private String address;
    private BigDecimal value;
}

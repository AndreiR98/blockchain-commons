package uk.co.roteala.common.monetary;

import lombok.*;

@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class AmountDTO {
    private Coin rawAmount;
}

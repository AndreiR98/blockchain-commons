package uk.co.roteala.common.monetary;

import lombok.*;
import uk.co.roteala.common.Fees;

@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class AmountDTO {
    private Coin rawAmount;
    private Fees fees;
}

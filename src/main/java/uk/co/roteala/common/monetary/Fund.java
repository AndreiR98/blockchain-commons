package uk.co.roteala.common.monetary;

import lombok.*;
import uk.co.roteala.common.AccountModel;

@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Fund {
    private AccountModel targetAccount;
    private AccountModel sourceAccount;
    private Coin amount;
    private boolean isProcessed;
}

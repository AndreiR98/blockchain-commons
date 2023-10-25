package uk.co.roteala.common.monetary;

import lombok.*;
import uk.co.roteala.common.Account;

@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Fund {
    private String targetAccountAddress;
    private Account sourceAccount;
    //private AmountDTO amount;
    private boolean isProcessed;
}

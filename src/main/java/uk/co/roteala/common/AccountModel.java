package uk.co.roteala.common;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class AccountModel extends BaseEmptyModel {
    private AddressBaseModel address;
    private String password;
    private String timeStamp;
    private BigDecimal balance;
    private AccountType type;
}

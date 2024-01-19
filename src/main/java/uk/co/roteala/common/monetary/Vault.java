package uk.co.roteala.common.monetary;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import uk.co.roteala.common.BasicModel;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeName("VAULT")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Vault extends BasicModel implements Serializable {
    private String vaultId;
    private Integer numberOfCoins;
    private List<Coin> coins;
    private boolean closed;

    @Override
    public byte[] getKey() {
        return null;
    }
}

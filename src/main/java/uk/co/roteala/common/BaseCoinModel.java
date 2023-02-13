package uk.co.roteala.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
//@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class BaseCoinModel implements Serializable {
    @JsonProperty("name")
    private String name;

    @JsonProperty("nick_name")
    private String nickName;

    @JsonProperty("max_amount")
    private BigDecimal maxAmount;

    @JsonProperty("decimal_points")
    private Integer decimalPoints;
}

package uk.co.roteala.common;

import lombok.*;

@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Spender {
    private Integer input;
    private String txid;
}

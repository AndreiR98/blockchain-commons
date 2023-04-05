package uk.co.roteala.common;

import lombok.*;

import java.io.Serializable;

@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Spender implements Serializable {
    private Integer input;
    private String txId;
}

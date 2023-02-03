package uk.co.roteala.common;

import lombok.*;

import java.io.Serializable;

@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class SignatureModel implements Serializable {
    //hex encoded values
    private String rComponent;
    private String sComponent;
}

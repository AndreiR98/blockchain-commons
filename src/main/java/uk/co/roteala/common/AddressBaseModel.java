package uk.co.roteala.common;

import lombok.*;

import java.io.Serializable;

@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class AddressBaseModel implements Serializable {
    private String hexEncoded;
}

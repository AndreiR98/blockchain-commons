package uk.co.roteala.common;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ContractBaseModel implements Serializable {
    private String hash;
    private List<String> transactions;
    private String timeStamp;
    private SignatureModel senderSignature;
    private SignatureModel receiverSignature;
}

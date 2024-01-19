package uk.co.roteala.common.messenger;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.*;
import uk.co.roteala.common.BasicModel;
import uk.co.roteala.security.PublicKey;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeName("RESPONSE")
public class Response extends BasicModel {
    private boolean status;
    private String location;
    private EventTypes type;
    private BasicModel payload;

    @Override
    public byte[] getKey() {
        return null;
    }

    @Override
    public String serialize() {
        return super.serialize();
    }
}

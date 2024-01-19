package uk.co.roteala.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import uk.co.roteala.common.messenger.Response;
import uk.co.roteala.common.monetary.Vault;
import uk.co.roteala.exceptions.SerializationException;
import uk.co.roteala.exceptions.errorcodes.SerializationErrorCode;
import uk.co.roteala.net.Peer;

import java.io.Serializable;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = BlockHeader.class, name = "BLOCKHEADER"),
        @JsonSubTypes.Type(value = Block.class, name = "BLOCK"),
        @JsonSubTypes.Type(value = Transaction.class, name = "TRANSACTION"),
        @JsonSubTypes.Type(value = MempoolTransaction.class, name = "MEMPOOLTRANSACTION"),
        @JsonSubTypes.Type(value = ChainState.class, name="STATECHAIN"),
        @JsonSubTypes.Type(value = NodeState.class, name="NODESTATE"),
        @JsonSubTypes.Type(value = Account.class, name="ACCOUNT"),
        @JsonSubTypes.Type(value = Peer.class, name="PEER"),
        @JsonSubTypes.Type(value = PeersContainer.class, name="PEERSCONTAINER"),
        @JsonSubTypes.Type(value = Vault.class, name="VAULT"),
        @JsonSubTypes.Type(value = Response.class, name = "RESPONSE")
})
public abstract class BasicModel implements Serializable {
    private final long serialVersionUID = 1L;

    @JsonIgnore
    protected ObjectMapper mapper = new ObjectMapper();

    @JsonIgnore
    public abstract byte[] getKey();

    @JsonIgnore
    protected String serialize() {
        try {
            return mapper.writeValueAsString(this);
        } catch (Exception e) {
            throw new SerializationException(SerializationErrorCode.SERIALIZATION_FAILED);
        }
    }
}

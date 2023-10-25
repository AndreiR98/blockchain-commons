package uk.co.roteala.common;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import uk.co.roteala.common.events.PeersContainer;
import uk.co.roteala.common.monetary.Vault;

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
        @JsonSubTypes.Type(value = PeersContainer.class, name="PEERSCONTAINER"),
        @JsonSubTypes.Type(value = Vault.class, name="VAULT")
})
public abstract class BasicModel implements Serializable {
    private final long serialVersionUID = 1L;
}

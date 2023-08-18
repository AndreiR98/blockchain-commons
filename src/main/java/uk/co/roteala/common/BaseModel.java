package uk.co.roteala.common;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import uk.co.roteala.common.events.AccountMessage;
import uk.co.roteala.common.events.PeersContainer;

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
        @JsonSubTypes.Type(value = PseudoTransaction.class, name = "PSEUDOTRANSACTION"),
        @JsonSubTypes.Type(value = ChainState.class, name="STATECHAIN"),
        @JsonSubTypes.Type(value = NodeState.class, name="NODESTATE"),
        @JsonSubTypes.Type(value = AccountModel.class, name="ACCOUNT"),
        @JsonSubTypes.Type(value = PeersContainer.class, name="PEERSCONTAINER")
})
public abstract class BaseModel implements Serializable {
    private final long serialVersionUID = 1L;
}

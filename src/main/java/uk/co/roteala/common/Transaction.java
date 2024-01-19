package uk.co.roteala.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import uk.co.roteala.exceptions.SerializationException;
import uk.co.roteala.exceptions.errorcodes.SerializationErrorCode;
import uk.co.roteala.security.utils.HashingService;

import java.io.Serializable;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.*;


@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeName("TRANSACTION")
public class Transaction extends BasicModel {
    private String hash;
    private Integer blockNumber;
    private String from;
    private String to;
    private BigInteger networkFees;
    private BigInteger processingFees;
    private BigInteger amount;
    private Integer version;
    private Integer transactionIndex;
    private String nonce;
    private long timeStamp;
    private long confirmations;
    private long blockTime;
    private String pubKeyHash;
    private TransactionStatus status;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private SignatureModel signature;

    @Override
    public String serialize() {
        return super.serialize();
    }

    @Override
    public byte[] getKey() {
        return this.hash.getBytes(StandardCharsets.UTF_8);
    }
}

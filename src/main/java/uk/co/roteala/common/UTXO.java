package uk.co.roteala.common;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.util.SerializationUtils;
import uk.co.roteala.common.monetary.Coin;
import uk.co.roteala.utils.GlacierUtils;

import javax.persistence.Transient;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class UTXO extends BaseEmptyModel implements Hashing {
    private boolean coinbase;
    private String txid;
    private String sigScript;
    private String pubKeyScript;
    private Coin value;
    private String address;
    private boolean spent;
    private Spender spender;

    @Override
    public String hashHeader() throws NoSuchAlgorithmException {
        List<Object> map = new ArrayList<>();
        map.add(value);
        map.add(pubKeyScript);
        map.add(sigScript);
        map.add(txid);
        map.add(coinbase);
        map.add(address);
        return GlacierUtils.bytesToHex(GlacierUtils.generateSHA256Digest(SerializationUtils.serialize(map)));
    }

    @Override
    public byte[] message() {
        return null;
    }
}

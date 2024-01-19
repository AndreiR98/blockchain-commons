package uk.co.roteala.core;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.rocksdb.WriteOptions;
import org.springframework.core.io.ClassPathResource;
import uk.co.roteala.RlpDecoder;
import uk.co.roteala.RlpList;
import uk.co.roteala.RlpString;
import uk.co.roteala.common.*;
import uk.co.roteala.common.monetary.Coin;
import uk.co.roteala.common.storage.ColumnFamilyTypes;
import uk.co.roteala.common.storage.Operator;
import uk.co.roteala.common.storage.SearchField;
import uk.co.roteala.common.storage.Storage;
import uk.co.roteala.core.rlp.Numeric;
import uk.co.roteala.core.rlp.RlpUtils;
import uk.co.roteala.core.rlp.Strings;
import uk.co.roteala.security.ECKey;
import uk.co.roteala.security.PublicKey;
import uk.co.roteala.security.utils.HashingService;
import uk.co.roteala.utils.Constants;

import java.io.InputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@UtilityClass
public class Blockchain {
    int CHAIN_ID_INC = 35;
    int LOWER_REAL_V = 27;
    public static void initializeGenesisState(Storage stateStorage) {
        if(!stateStorage.has(ColumnFamilyTypes.STATE, Constants.DEFAULT_STATE_NAME
                .getBytes(StandardCharsets.UTF_8))) {

            stateStorage.put(true, ColumnFamilyTypes.STATE, Constants.DEFAULT_STATE_NAME
                    .getBytes(StandardCharsets.UTF_8), Constants.GENESIS_STATE);

            stateStorage.put(true, ColumnFamilyTypes.NODE, Constants.DEFAULT_NODE_STATE
                    .getBytes(StandardCharsets.UTF_8), Constants.NODE_STATE);
            log.info("Node state created: {}", Constants.NODE_STATE);

            setUpGenesisAccounts(stateStorage);
        }
    }

    public static void initializeGenesisBlock(Storage blockchainStorage) {
        if(!blockchainStorage.has(ColumnFamilyTypes.BLOCKS, Constants.GENESIS_BLOCK.getHeader()
                .getIndex().toString()
                .getBytes(StandardCharsets.UTF_8))) {

            blockchainStorage.put(true, ColumnFamilyTypes.BLOCKS, Constants.GENESIS_BLOCK.getHeader()
                            .getIndex().toString()
                    .getBytes(StandardCharsets.UTF_8), Constants.GENESIS_BLOCK);
        }
    }

    private static void setUpGenesisAccounts(Storage stateStorage) {
        try {
            ClassPathResource resource = new ClassPathResource("genesis.json");
            InputStream inputStream = resource.getInputStream();

            ObjectMapper mapper = new ObjectMapper();

            List<Account> accounts = mapper.readValue(inputStream, new TypeReference<List<Account>>() {});

            for(Account account : accounts) {
                log.info("Creating genesis account:{}", account.getAddress());
                stateStorage.put(true, ColumnFamilyTypes.ACCOUNTS, account.getAddress()
                        .getBytes(StandardCharsets.UTF_8), account);
            }
        } catch (Exception e) {
            log.error("Failed to create genesis accounts!:{}", e);
        }
    }

    private Long getChainId(byte[] bytes) {
        BigInteger bv = Numeric.toBigInt(bytes);
        long v = bv.longValue();
        if (v == LOWER_REAL_V || v == (LOWER_REAL_V + 1)) {
            return null;
        }
        return (v - CHAIN_ID_INC) / 2;
    }

    public byte getRealV(BigInteger bv) {
        long v = bv.longValue();
        if (v == LOWER_REAL_V || v == (LOWER_REAL_V + 1)) {
            return (byte) v;
        }
        byte realV = (byte) LOWER_REAL_V;
        int inc = 0;
        if ((int) v % 2 == 0) {
            inc = 1;
        }
        return (byte) (realV + inc);
    }

    public byte[] decodeHexToBytes(String input) {
        String cleanInput = cleanHexPrefix(input);
        int len = cleanInput.length();
        if (len == 0) {
            return new byte[0];
        } else {
            byte[] data;
            byte startIdx;
            if (len % 2 != 0) {
                data = new byte[len / 2 + 1];
                data[0] = (byte)Character.digit(cleanInput.charAt(0), 16);
                startIdx = 1;
            } else {
                data = new byte[len / 2];
                startIdx = 0;
            }

            for(int i = startIdx; i < len; i += 2) {
                data[(i + 1) / 2] = (byte)((Character.digit(cleanInput.charAt(i), 16) << 4) + Character.digit(cleanInput.charAt(i + 1), 16));
            }

            return data;
        }
    }

    private static String cleanHexPrefix(String input) {
        return containsHexPrefix(input) ? input.substring(2) : input;
    }

    public static boolean containsHexPrefix(String input) {
        return !isEmpty(input) && input.length() > 1 &&
                input.charAt(0) == '0' && input.charAt(1) == 'x';
    }

    public static boolean isEmpty(String s) {
        return s == null || s.length() == 0;
    }
}

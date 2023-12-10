package uk.co.roteala.core;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.rocksdb.WriteOptions;
import org.springframework.core.io.ClassPathResource;
import uk.co.roteala.common.Account;
import uk.co.roteala.common.Transaction;
import uk.co.roteala.common.storage.ColumnFamilyTypes;
import uk.co.roteala.common.storage.Storage;
import uk.co.roteala.utils.Constants;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@UtilityClass
public class Blockchain {
    public static void initializeGenesisState(Storage stateStorage) {
        if(!stateStorage.has(ColumnFamilyTypes.STATE, Constants.DEFAULT_STATE_NAME
                .getBytes(StandardCharsets.UTF_8))) {

            stateStorage.put(true, ColumnFamilyTypes.STATE, Constants.DEFAULT_STATE_NAME
                    .getBytes(StandardCharsets.UTF_8), Constants.GENESIS_STATE);

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
                stateStorage.put(true, ColumnFamilyTypes.ACCOUNTS, account.getAddress()
                        .getBytes(StandardCharsets.UTF_8), account);
            }
        } catch (Exception e) {
            log.error("Failed to create genesis accounts!:{}", e);
        }
    }

    //public Transaction decodeRawTransaction(String hexData) {}
}

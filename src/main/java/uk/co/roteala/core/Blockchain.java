package uk.co.roteala.core;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import uk.co.roteala.common.storage.ColumnFamilyTypes;
import uk.co.roteala.common.storage.Storage;
import uk.co.roteala.utils.Constants;

import java.nio.charset.StandardCharsets;

@Slf4j
@UtilityClass
public class Blockchain {
    public static void initializeGenesisState(Storage stateStorage) {
        if(!stateStorage.has(ColumnFamilyTypes.STATE, Constants.DEFAULT_STATE_NAME
                .getBytes(StandardCharsets.UTF_8))) {
            stateStorage.put(ColumnFamilyTypes.STATE, Constants.DEFAULT_STATE_NAME
                    .getBytes(StandardCharsets.UTF_8), Constants.GENESIS_STATE);
        }
    }
}

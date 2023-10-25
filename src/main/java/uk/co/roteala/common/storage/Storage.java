package uk.co.roteala.common.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.rocksdb.ColumnFamilyHandle;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;
import uk.co.roteala.common.BasicModel;
import uk.co.roteala.exceptions.StorageException;
import uk.co.roteala.exceptions.errorcodes.StorageErrorCode;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class Storage implements KeyValueStorage {

    private final StorageTypes storageType;
    private final RocksDB rocksDB;
    private final List<ColumnFamilyHandle> columnFamilyHandles;

    /**
     * Retrieves a model by its key.
     *
     * @param key The unique key associated with the model.
     * @return The retrieved model or null if not found.
     */
    @Override
    public BasicModel get(byte[] key) {
        byte[] data = null;

        try {
            data = this.rocksDB.get(key);
        } catch (RocksDBException e) {
            log.error("Error accessing RocksDB!", e);
        }

        return (data != null) ? deserializer(data) : null;
    }

    /**
     * Inserts a model with the provided key into the storage.
     *
     * @param key   The unique key to associate with the model.
     * @param value The model to insert.
     */
    @Override
    public void put(byte[] key, BasicModel value) {
        try {
            if(has(key)) {
                throw new StorageException(StorageErrorCode.DATA_ALREADY);
            }

            this.rocksDB.put(key, serializer(value));
        } catch (Exception e) {
            log.error("Error while adding to storage!", e);
        }
    }

    /**
     * Checks if the provided key exists in the storage.
     *
     * @param key The unique key to check.
     * @return True if the key exists, otherwise false.
     */
    @Override
    public boolean has(byte[] key) {
        try {
            return this.rocksDB.get(key) != null;
        } catch (RocksDBException e) {
            log.error("Error checking key existence in RocksDB!", e);
            return false;
        }
    }

    /**
     * Deletes a model by its key.
     *
     * @param key The unique key of the model to be deleted.
     */
    @Override
    public void delete(byte[] key) {
        try {
            this.rocksDB.delete(key);
        } catch (RocksDBException e) {
            log.error("Error deleting key in RocksDB!", e);
        }
    }

    /**
     * Retrieves a model from a specific column family by its key.
     *
     * @param columnFamilyType The handle of the column family.
     * @param key    The unique key associated with the model.
     * @return The retrieved model or null if not found.
     */
    @Override
    public BasicModel get(ColumnFamilyTypes columnFamilyType, byte[] key) {
        byte[] data = null;

        try {
            data = this.rocksDB.get(getHandler(columnFamilyType), key);
        } catch (RocksDBException e) {
            log.error("Error accessing RocksDB!", e);
        }

        return (data != null) ? deserializer(data) : null;
    }

    /**
     * Inserts a model with the provided key into a specific column family.
     *
     * @param columnFamilyType The handle of the column family.
     * @param key    The unique key to associate with the model.
     * @param value  The model to insert.
     */
    @Override
    public void put(ColumnFamilyTypes columnFamilyType, byte[] key, BasicModel value) {
        try {
            if(has(columnFamilyType, key)) {
                throw new StorageException(StorageErrorCode.DATA_ALREADY);
            }

            this.rocksDB.put(getHandler(columnFamilyType), key, serializer(value));
        } catch (Exception e) {
            log.error("Error while adding to storage!", e);
        }
    }

    /**
     * Checks if the provided key exists in a specific column family.
     *
     * @param columnFamilyType The handle of the column family.
     * @param key    The unique key to check.
     * @return True if the key exists in the specified column family, otherwise false.
     */
    @Override
    public boolean has(ColumnFamilyTypes columnFamilyType, byte[] key) {
        try {
            return this.rocksDB.get(getHandler(columnFamilyType), key) != null;
        } catch (RocksDBException e) {
            log.error("Error checking key existence in RocksDB!", e);
            return false;
        }
    }

    /**
     * Deletes a model in a specific column family by its key.
     *
     * @param columnFamilyType The handle of the column family.
     * @param key    The unique key of the model to be deleted.
     */
    @Override
    public void delete(ColumnFamilyTypes columnFamilyType, byte[] key) {
        try {
            this.rocksDB.delete(getHandler(columnFamilyType), key);
        } catch (RocksDBException e) {
            log.error("Error deleting key in RocksDB!", e);
        }
    }

    /**
     * Retrieves the ColumnFamilyHandle for the given column family type.
     *
     * @param columnFamilyTypes The type of the column family.
     * @return The handle for the column family.
     * @throws StorageException if the handle is not found.
     */
    public ColumnFamilyHandle getHandler(ColumnFamilyTypes columnFamilyTypes) {
        return this.columnFamilyHandles.stream()
                .filter(handle -> {
                    try {
                        return Arrays.equals(handle.getName(), columnFamilyTypes.getName()
                                .getBytes(StandardCharsets.UTF_8));
                    } catch (RocksDBException e) {
                        return false;
                    }
                })
                .findFirst()
                .orElseThrow(() -> {
                    log.error("Column family handler with name: {} not found in storage: {}",
                            columnFamilyTypes.getName(),
                            this.storageType.getName());
                    return new StorageException(StorageErrorCode.HANDLER_NOT_FOUND);
                });
    }
}

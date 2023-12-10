package uk.co.roteala.common.storage;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SerializationUtils;
import org.rocksdb.*;
import org.springframework.stereotype.Repository;
import uk.co.roteala.common.BasicModel;
import uk.co.roteala.exceptions.StorageException;
import uk.co.roteala.exceptions.errorcodes.StorageErrorCode;
import uk.co.roteala.net.Peer;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Repository
@NoArgsConstructor
public class Storage implements KeyValueStorage {

    private StorageTypes storageType;
    private RocksDB rocksDB;
    private List<ColumnFamilyHandle> columnFamilyHandles;

    public Storage(StorageTypes storageType, RocksDB rocksDB, List<ColumnFamilyHandle> columnFamilyHandles) {
        this.storageType = storageType;
        this.rocksDB = rocksDB;
        this. columnFamilyHandles = columnFamilyHandles;
    }

    /**
     * Retrieves a model by its key.
     *
     * @param key The unique key associated with the model.
     * @return The retrieved model or null if not found.
     */
    @Override
    public BasicModel get(byte[] key) {
        RocksDB.loadLibrary();

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
    public synchronized void put(byte[] key, BasicModel value) {
        RocksDB.loadLibrary();
        try {
            if(has(key) && !(value instanceof Peer)) {
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
        RocksDB.loadLibrary();
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
    public synchronized void delete(byte[] key) {
        RocksDB.loadLibrary();
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
        RocksDB.loadLibrary();
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
    public synchronized void put(ColumnFamilyTypes columnFamilyType, byte[] key, BasicModel value) {
        RocksDB.loadLibrary();
        try {
            if(has(columnFamilyType, key) && !(value instanceof Peer)) {
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
        RocksDB.loadLibrary();
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
    public synchronized void delete(ColumnFamilyTypes columnFamilyType, byte[] key) {
        RocksDB.loadLibrary();
        try {
            this.rocksDB.delete(getHandler(columnFamilyType), key);
        } catch (RocksDBException e) {
            log.error("Error deleting key in RocksDB!", e);
        }
    }

    /**
     * Adds a key-value pair to the RocksDB database in the specified column family with the option
     * to control data persistence behavior, including immediate writing or delayed writing.
     *
     * @param persistent Set to true for immediate synchronous writing, or false for asynchronous writing.
     * @param columnFamilyType The type of the column family in which the data will be added.
     * @param key The key to add to the database.
     * @param value The value associated with the key.
     * @throws StorageException if the key already exists in the database.
     */
    @Override
    public synchronized void put(boolean persistent, ColumnFamilyTypes columnFamilyType, byte[] key, BasicModel value) {
        RocksDB.loadLibrary();
        try {
            if(has(columnFamilyType, key) && !(value instanceof Peer)) {
                throw new StorageException(StorageErrorCode.DATA_ALREADY);
            }

            if(persistent) {
                this.rocksDB.put(getHandler(columnFamilyType), DefaultStorage.defaultPersistantWriteOptions(),
                        key, serializer(value));
            } else {
                this.put(columnFamilyType,key, value);
            }
        } catch (Exception e) {
            log.error("Error while adding to storage!", e);
        }
    }

    /**
     * Adds a key-value pair to the RocksDB database in the default column family and ensures immediate data persistence.
     *
     * @param persistent Set to true for immediate synchronous writing, or false for asynchronous writing.
     * @param key The key to add to the database.
     * @param value The value associated with the key.
     * @throws StorageException if the key already exists in the database.
     */
    @Override
    public synchronized void put(boolean persistent, byte[] key, BasicModel value) {
        RocksDB.loadLibrary();
        try {
            if(has(key) && !(value instanceof Peer)) {
                throw new StorageException(StorageErrorCode.DATA_ALREADY);
            }

            if(persistent) {
                this.rocksDB.put(DefaultStorage.defaultPersistantWriteOptions(), key, serializer(value));
            } else {
                this.put(key, value);
            }
        } catch (Exception e) {
            log.error("Error while adding to storage!", e);
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
        RocksDB.loadLibrary();
        try {
            byte[] targetNameBytes = columnFamilyTypes.getName().getBytes(StandardCharsets.UTF_8);

            for (ColumnFamilyHandle handle : columnFamilyHandles) {
                byte[] handleNameBytes = handle.getName();

                if (Arrays.equals(handleNameBytes, targetNameBytes)) {
                    return handle;
                }
            }

            log.error("Column family handler with name: {} not found in storage: {}", columnFamilyTypes.getName(), this.storageType.getName());
            throw new StorageException(StorageErrorCode.HANDLER_NOT_FOUND);
        } catch (RocksDBException e) {
            log.error("Column family handler with name: {} not found in storage: {}",
                    columnFamilyTypes.getName(),
                    this.storageType.getName());
            throw new StorageException(StorageErrorCode.HANDLER_NOT_FOUND);
        }
    }
}

package uk.co.roteala.common.storage;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.rocksdb.*;
import org.springframework.stereotype.Repository;
import uk.co.roteala.common.*;
import uk.co.roteala.common.Transaction;
import uk.co.roteala.exceptions.StorageException;
import uk.co.roteala.exceptions.errorcodes.StorageErrorCode;
import uk.co.roteala.net.Peer;
import uk.co.roteala.utils.Constants;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Repository
@NoArgsConstructor
public class Storage extends AbstractStorageOperation implements KeyValueStorage {

    private StorageTypes storageType;
    private RocksDB rocksDB;
    private List<ColumnFamilyHandle> columnFamilyHandles;

    private Pagination pagination;

    public Storage(StorageTypes storageType, RocksDB rocksDB, List<ColumnFamilyHandle> columnFamilyHandles) {
        this.storageType = storageType;
        this.rocksDB = rocksDB;
        this.columnFamilyHandles = columnFamilyHandles;
        this.pagination = new Pagination();
    }

    protected RocksDB getRocksDB() {
        return this.rocksDB;
    }

    protected RocksIterator getIterator() {
        return this.rocksDB.newIterator();
    }

    protected RocksIterator getIterator(ColumnFamilyTypes columnFamilyTypes) {
        return this.rocksDB.newIterator(getHandler(columnFamilyTypes));
    }
    @Override
    public boolean putIfAbsent(boolean persistent, byte[] key, BasicModel value) {
        if(has(key)) {
            return false;
        }

        put(persistent, key, value);
        return true;
    }

    @Override
    public boolean putIfAbsent(boolean persistent, ColumnFamilyTypes columnFamilyTypes, byte[] key, BasicModel value) {
        if (has(columnFamilyTypes, key)) {
            return false;
        }
        put(persistent, columnFamilyTypes, key, value);
        return true;
    }


    @Override
    public boolean putIfAbsent(ColumnFamilyTypes columnFamilyTypes, byte[] key, BasicModel value) {
        if (has(columnFamilyTypes, key)) {
            return false;
        }
        put(columnFamilyTypes, key, value);
        return true;
    }

    @Override
    public boolean putIfAbsent(byte[] key, BasicModel value) {
        if(has(key)) {
            return false;
        }

        put(key, value);
        return true;
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

    @Override
    public synchronized void modify(ColumnFamilyTypes columnFamilyTypes, byte[] key, BasicModel model) {
        RocksDB.loadLibrary();
        try {
            //Blocks must be immutable
            if(model instanceof Block) {
                throw new StorageException(StorageErrorCode.IMMUTABILITY);
            }
            //Modify method only applies to data already existent except account, peer, you can't modify a transaction
            // or mempool transaction if they dont exists
            //If non existant Transaction, MempoolTransaction, StateChain, NodeState dont modify
            if((model instanceof Transaction) && !has(ColumnFamilyTypes.TRANSACTIONS, ((Transaction) model).getHash()
                    .getBytes(StandardCharsets.UTF_8))) {
                throw new StorageException(StorageErrorCode.MODIFY_NON_EXISTANT);
            }

            if((model instanceof MempoolTransaction) && !has(((MempoolTransaction) model).getHash()
                    .getBytes(StandardCharsets.UTF_8))) {
                throw new StorageException(StorageErrorCode.MODIFY_NON_EXISTANT);
            }

            if((model instanceof ChainState) && !has(ColumnFamilyTypes.STATE, Constants.DEFAULT_STATE_NAME
                    .getBytes(StandardCharsets.UTF_8))) {
                throw new StorageException(StorageErrorCode.MODIFY_NON_EXISTANT);
            }

            if((model instanceof NodeState) && !has(ColumnFamilyTypes.NODE, Constants.DEFAULT_NODE_STATE
                    .getBytes(StandardCharsets.UTF_8))) {
                throw new StorageException(StorageErrorCode.MODIFY_NON_EXISTANT);
            }

            if((model instanceof Transaction)) {
                Transaction transaction = (Transaction) model;
                this.rocksDB.put(getHandler(ColumnFamilyTypes.TRANSACTIONS), DefaultStorage.defaultPersistantWriteOptions(),
                        transaction.getHash()
                                .getBytes(StandardCharsets.UTF_8), serializer(transaction));
            }

            if((model instanceof MempoolTransaction)) {
                MempoolTransaction mempoolTransaction = (MempoolTransaction) model;
                this.rocksDB.put(getHandler(ColumnFamilyTypes.NODE), DefaultStorage.defaultPersistantWriteOptions(),
                        mempoolTransaction.getHash()
                                .getBytes(StandardCharsets.UTF_8), serializer(mempoolTransaction));
             }

            if((model instanceof ChainState)) {
                ChainState chainState = (ChainState) model;
                this.rocksDB.put(getHandler(ColumnFamilyTypes.NODE), DefaultStorage.defaultPersistantWriteOptions(),
                        Constants.DEFAULT_STATE_NAME
                                .getBytes(StandardCharsets.UTF_8), serializer(chainState));
            }

            if((model instanceof NodeState)) {
                NodeState nodeState = (NodeState) model;
                this.rocksDB.put(getHandler(ColumnFamilyTypes.NODE), DefaultStorage.defaultPersistantWriteOptions(),
                        Constants.DEFAULT_NODE_STATE
                                .getBytes(StandardCharsets.UTF_8), serializer(nodeState));
            }

            if((model) instanceof Account) {
                Account account = (Account) model;
                this.rocksDB.put(getHandler(ColumnFamilyTypes.ACCOUNTS), DefaultStorage.defaultPersistantWriteOptions(),
                        account.getAddress()
                                .getBytes(StandardCharsets.UTF_8), serializer(account));
            }

            if((model) instanceof Peer) {
                Peer peer = (Peer) model;
                this.rocksDB.put(DefaultStorage.defaultPersistantWriteOptions(),
                        peer.getKey(), serializer(peer));
                this.put(true, peer.getKey(), peer);
            }
        } catch (Exception e) {
            log.error("Failed to modify model");
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

    public List<String> asString() {
        List<String> hashes = new ArrayList<>();

        RocksDB.loadLibrary();

        int pageSize = DEFAULT_PAGE_SIZE;
        int pageNumber = this.pageNumber != null && this.pageNumber > 0 ? this.pageNumber : 1; // Ensure pageNumber is at least 1
        int skip = pageSize * (pageNumber);

        try (final RocksIterator iterator = this.getIterator(this.handler)) {

            // Move the iterator to the start of the page
            for (int i = 0; i < skip && iterator.isValid(); i++) {
                if (this.isReversed) {
                    iterator.prev();
                } else {
                    iterator.next();
                }
            }

            // Retrieve transactions for the page
            int count = 0;
            while (iterator.isValid() && count < pageSize) {
                BasicModel model = deserializer(iterator.value());
                hashes.add(model.getHash());

                count++;

                if (this.isReversed) {
                    iterator.prev();
                } else {
                    iterator.next();
                }
            }
        }

        return hashes;
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

    @Override
    public List<BasicModel> asBasicModel() {
        Object response = processCriteria();

        return (List<BasicModel>) response;
    }

    @Override
    public List<String> asKey() {
        return null;
    }

    /**
     * Process based on a criteria
     * */
    private Object processCriteria() {
        log.info("Operator: {}, Field:{}, Value:{}", operator, searchField, value);
        switch (operator) {
            case EQ:
                return operatorEQ();
            default:
                return null;
        }
    }

    /**
     * Logic for operator EQ(=)
     * */
    private Object operatorEQ() {
        List<BasicModel> multiQueryResponse = new ArrayList<>();

        switch (searchField) {
            case STATUS:
                if(this.storageType == StorageTypes.PEERS) {
                    RocksIterator iterator = this.getIterator();

                    for(iterator.seekToLast(); iterator.isValid(); iterator.prev()) {
                        Peer peer = (Peer) deserializer(iterator.value());

                        if(peer.isActive() == (boolean) value) {
                            multiQueryResponse.add(peer);
                        }
                    }
                }
        }

        return multiQueryResponse;
    }
}

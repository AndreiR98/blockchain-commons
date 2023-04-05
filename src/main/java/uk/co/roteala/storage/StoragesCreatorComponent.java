package uk.co.roteala.storage;

import org.rocksdb.RocksDBException;

public interface StoragesCreatorComponent {
    StorageComponent mempool() throws RocksDBException;

    StorageComponent wallet() throws RocksDBException;

    StorageComponent tx() throws RocksDBException;

    StorageComponent blocks() throws RocksDBException;

    StorageComponent peers() throws RocksDBException;

    StorageComponent chainState() throws RocksDBException;
}

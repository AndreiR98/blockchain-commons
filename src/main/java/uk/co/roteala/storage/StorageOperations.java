package uk.co.roteala.storage;

import org.rocksdb.RocksDBException;
import uk.co.roteala.common.BaseEmptyModel;

public interface StorageOperations {
    void add(byte[] key, BaseEmptyModel data) throws RocksDBException;

    BaseEmptyModel get(byte[] key) throws RocksDBException;

    void delete(byte[] key) throws RocksDBException;

    Long count() throws RocksDBException;
}

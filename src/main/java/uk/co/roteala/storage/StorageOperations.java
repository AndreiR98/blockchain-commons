package uk.co.roteala.storage;

import org.rocksdb.RocksDBException;
import uk.co.roteala.common.BaseModel;

public interface StorageOperations {
    void add(byte[] key, BaseModel data) throws RocksDBException;

    BaseModel get(byte[] key) throws RocksDBException;

    void delete(byte[] key) throws RocksDBException;

    Long count() throws RocksDBException;
}

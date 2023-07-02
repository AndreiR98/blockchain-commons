package uk.co.roteala.storage;

import org.rocksdb.FlushOptions;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;
import org.springframework.util.SerializationUtils;
import uk.co.roteala.common.BaseModel;

public class StorageComponent implements StorageOperations{

    private RocksDB db;

    public StorageComponent(RocksDB db) {
        this.db = db;
    }

    public RocksDB getRaw(){
        return this.db;
    }

    @Override
    public void add(byte[] key, BaseModel data) throws RocksDBException {
        final byte[] serializedData = SerializationUtils.serialize(data);
        this.db.put(key, serializedData);
        this.db.flush(new FlushOptions().setWaitForFlush(true));
    }

    @Override
    public BaseModel get(byte[] key) throws RocksDBException {
        if(key != null) {
            final byte[] serializedData = this.db.get(key);

            if(serializedData != null) {
                return (BaseModel) SerializationUtils.deserialize(serializedData);
            }
        }

        return null;
    }

    @Override
    public void delete(byte[] key) throws RocksDBException {
        if(key != null) {
            this.db.delete(key);
        }
    }

    @Override
    public Long count() throws RocksDBException {
        return Long.parseLong(this.db.getProperty("rocksdb.estimate-num-keys"));
    }
}

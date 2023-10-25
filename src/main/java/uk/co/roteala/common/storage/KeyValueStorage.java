package uk.co.roteala.common.storage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Utf8;
import org.apache.commons.lang3.SerializationUtils;
import org.rocksdb.ColumnFamilyHandle;
import uk.co.roteala.common.BasicModel;
import uk.co.roteala.exceptions.StorageException;
import uk.co.roteala.exceptions.errorcodes.StorageErrorCode;

import java.nio.charset.StandardCharsets;

public interface KeyValueStorage {
    BasicModel get(byte[] key);

    void put(byte[] key, BasicModel value);

    boolean has(byte[] key);

    void delete(byte[] key);

    BasicModel get(ColumnFamilyTypes columnFamilyTypes, byte[] key);

    void put(ColumnFamilyTypes columnFamilyTypes, byte[] key, BasicModel value);

    boolean has(ColumnFamilyTypes columnFamilyTypes, byte[] key);

    void delete(ColumnFamilyTypes columnFamilyTypes, byte[] key);

    ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    default byte[] serializer(BasicModel model) {
        try {
            String jsonData = OBJECT_MAPPER.writeValueAsString(model);
            return jsonData.getBytes(StandardCharsets.UTF_8);
        } catch (JsonProcessingException e) {
            throw new StorageException(StorageErrorCode.SERIALIZATION);
        }
    }

    default BasicModel deserializer(byte[] value) throws StorageException {
        try {
            return OBJECT_MAPPER.readValue(new String(value, StandardCharsets.UTF_8), BasicModel.class);
        } catch (JsonProcessingException e) {
            throw new StorageException(StorageErrorCode.SERIALIZATION);
        }
    }
}

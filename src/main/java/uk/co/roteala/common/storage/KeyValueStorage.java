package uk.co.roteala.common.storage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.SerializationUtils;
import uk.co.roteala.common.BasicModel;
import uk.co.roteala.exceptions.StorageException;
import uk.co.roteala.exceptions.errorcodes.StorageErrorCode;

public interface KeyValueStorage {
    BasicModel get(byte[] key);

    void put(byte[] key, BasicModel value);

    boolean has(byte[] key);

    void delete(byte[] key);

    BasicModel get(ColumnFamilyTypes columnFamilyTypes, byte[] key);

    void put(ColumnFamilyTypes columnFamilyTypes, byte[] key, BasicModel value);

    boolean has(ColumnFamilyTypes columnFamilyTypes, byte[] key);

    void delete(ColumnFamilyTypes columnFamilyTypes, byte[] key);

    void put(boolean persistent, ColumnFamilyTypes columnFamilyTypes, byte[] key, BasicModel value);

    void put(boolean persistent, byte[] key, BasicModel value);

    boolean putIfAbsent(boolean persistent, byte[] key, BasicModel value);

    boolean putIfAbsent(boolean persistent, ColumnFamilyTypes columnFamilyTypes, byte[] key, BasicModel value);

    boolean putIfAbsent(ColumnFamilyTypes columnFamilyTypes, byte[] key, BasicModel value);

    boolean putIfAbsent(byte[] key, BasicModel value);

    ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    default byte[] serializer(BasicModel model) {
        try {
            String jsonData = OBJECT_MAPPER.writeValueAsString(model);
            return SerializationUtils.serialize(jsonData);
        } catch (JsonProcessingException e) {
            throw new StorageException(StorageErrorCode.SERIALIZATION);
        }
    }

    default BasicModel deserializer(byte[] value) throws StorageException {
        try {

            return OBJECT_MAPPER.readValue((String) SerializationUtils.deserialize(value), BasicModel.class);
        } catch (JsonProcessingException e) {
            throw new StorageException(StorageErrorCode.SERIALIZATION);
        }
    }
}

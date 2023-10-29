package uk.co.roteala.common.storage;

import lombok.extern.slf4j.Slf4j;
import org.rocksdb.*;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class DefaultStorage {

    /**
     * Ensures the given storage path exists by creating directories if they don't already exist.
     * Logs a message if directories are created.
     *
     * @param path The storage path to ensure.
     */
    public static void ensurePathExists(File path) {
        if(path.mkdirs()) {
            log.info("Creating storage at: {}", path.getAbsolutePath());
        }
    }

    /**
     * Configures and returns a new DBOptions instance for a RocksDB database.
     * The configuration includes enabling concurrent writes, creating missing column families,
     * and ensuring the database is created if missing.
     *
     * @param createMissingColumn Determines whether to create missing column families.
     * @return A configured DBOptions instance.
     */
    public static DBOptions setupDatabaseOptions(boolean createMissingColumn) {
        DBOptions dbOptions = new DBOptions();
        dbOptions.setCreateIfMissing(true)
                .setAllowConcurrentMemtableWrite(true)
                .setCreateMissingColumnFamilies(createMissingColumn)
                .setAtomicFlush(true);

        return dbOptions;
    }

    /**
     * Configures and returns a new Options instance for a RocksDB database.
     * The configuration includes enabling concurrent writes, creating missing column families,
     * and ensuring the database is created if missing.
     *
     * @param createMissingColumn Determines whether to create missing column families.
     * @return A configured Options instance.
     */
    public static Options setupOptions(boolean createMissingColumn) {
        Options options = new Options();
        options.setCreateIfMissing(true)
                .setAllowConcurrentMemtableWrite(true)
                .setCreateMissingColumnFamilies(createMissingColumn)
                .setAtomicFlush(true);

        return options;
    }

    /**
     * Configures and returns a list of ColumnFamilyDescriptor instances for RocksDB.
     * This list includes the default column family and any additional column families provided.
     *
     * @param columnFamilyOptions The column family options used for each descriptor.
     * @param columnFamilies The list of column family names to create descriptors for.
     * @return A list of configured ColumnFamilyDescriptor instances.
     */
    public static List<ColumnFamilyDescriptor> setupColumnFamilyDescriptors(ColumnFamilyOptions columnFamilyOptions,
                                                                     List<String> columnFamilies) {
        List<ColumnFamilyDescriptor> columnFamilyDescriptors = new ArrayList<>();
        columnFamilyDescriptors.add(new ColumnFamilyDescriptor(RocksDB.DEFAULT_COLUMN_FAMILY));

        for(String cfName : columnFamilies) {
            columnFamilyDescriptors.add(new ColumnFamilyDescriptor(cfName.getBytes(StandardCharsets.UTF_8), columnFamilyOptions));
        }

        return columnFamilyDescriptors;
    }

    /**
     * Configures and returns a new ColumnFamilyOptions instance for a RocksDB database column family.
     * The configuration includes setting the compression type to SNAPPY compression and enabling blob garbage collection.
     *
     * @return A configured ColumnFamilyOptions instance.
     */
    public static ColumnFamilyOptions setupColumnFamilyOptions() {
        ColumnFamilyOptions columnFamilyOptions = new ColumnFamilyOptions();

        columnFamilyOptions.setCompressionType(CompressionType.SNAPPY_COMPRESSION);
        columnFamilyOptions.enableBlobGarbageCollection();

        return columnFamilyOptions;
    }

    /**
     * Configures and returns a new ColumnFamilyOptions instance for a RocksDB database column family.
     * The configuration includes setting the compression type to SNAPPY compression and enabling blob garbage collection.
     *
     * @return A configured ColumnFamilyOptions instance with SNAPPY compression and blob garbage collection enabled.
     */
    public static WriteOptions defaultPersistantWriteOptions() {
        return new WriteOptions()
                .setSync(true);
    }
}

package uk.co.roteala.common.storage;

import uk.co.roteala.common.BasicModel;

import java.util.List;

public interface StorageRetrieval<T> {
    List<T> as();

    StorageRetrieval<T> page();
}

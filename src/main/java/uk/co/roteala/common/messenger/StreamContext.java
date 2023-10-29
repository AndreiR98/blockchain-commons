package uk.co.roteala.common.messenger;

import java.util.List;
public interface StreamContext<V> {
    List<SourceSupplier<V>> sourceSuppliers();
}

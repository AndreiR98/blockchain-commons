package uk.co.roteala.common.messenger;

public interface AssemblerSupplier<V> {
    Assembler<V> get();
}

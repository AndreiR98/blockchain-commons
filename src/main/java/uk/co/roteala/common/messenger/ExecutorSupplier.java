package uk.co.roteala.common.messenger;

public interface ExecutorSupplier<V> {
    Executor<V> get();
}

package uk.co.roteala.common.messenger;

public interface Executor<V> {
    void init();

    void process(V message);

    void close();
}

package uk.co.roteala.common.events.transformer;

public interface ScriptTransformer<E, R> {
    void init();

    R transform(E event);

    void close();
}

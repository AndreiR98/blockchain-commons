package uk.co.roteala.common.events.transformer;

/**
 * Supply an event to the transformer
 * */
public interface ScriptTransformerSupplier<E, R> {
    ScriptTransformer<E, R> get();
}

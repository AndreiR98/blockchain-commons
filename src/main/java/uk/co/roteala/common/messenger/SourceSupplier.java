package uk.co.roteala.common.messenger;

public interface SourceSupplier<I> {
    I source();

    ReaderSupplier reader();
}

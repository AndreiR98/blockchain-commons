package uk.co.roteala.common.events;

public interface EventParser<E> {
    void processor(Event e);

    void executor();
}

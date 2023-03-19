package uk.co.roteala.common.events;

import uk.co.roteala.common.events.enums.ActionTypes;
import uk.co.roteala.common.events.enums.SubjectTypes;

public abstract class EventsMessageFactory {
    public static Event peers(){
        final String message = ActionTypes.FETCH+" "+ SubjectTypes.PEER+" "+"50";
        Event event = new Event();
        event.setMessage(message);

        return event;
    }
}

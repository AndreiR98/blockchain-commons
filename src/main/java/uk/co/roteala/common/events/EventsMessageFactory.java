package uk.co.roteala.common.events;

import uk.co.roteala.common.events.enums.ActionTypes;
import uk.co.roteala.common.events.enums.SubjectTypes;

public abstract class EventsMessageFactory {
    public static Event peers(){
        final String message = ActionTypes.FETCH.getCode()+" "+ SubjectTypes.PEER.getCode()+" "+"50";
        Event event = new Event();
        event.setAction(ActionTypes.FETCH);
        event.setSubject(SubjectTypes.PEER);
        event.setExtra("50");
        return event;
    }

    public static Event genesisTX() {
        final String message = ActionTypes.FETCH.getCode()+" "+ SubjectTypes.TRANSACTION.getCode()+" "+"genesis";
        Event event = new Event();
        event.setAction(ActionTypes.FETCH);
        event.setSubject(SubjectTypes.TRANSACTION);
        event.setExtra("genesis");

        return event;
    }

    public static Event genesisBL() {
        final String message = ActionTypes.FETCH.getCode()+" "+ SubjectTypes.BLOCK.getCode()+" "+"genesis";
        Event event = new Event();
        event.setAction(ActionTypes.FETCH);
        event.setSubject(SubjectTypes.BLOCK);
        event.setExtra("genesis");

        return event;
    }
}

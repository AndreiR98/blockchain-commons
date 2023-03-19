package uk.co.roteala.common.events;

import lombok.*;
import uk.co.roteala.common.events.enums.ActionTypes;

@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Action extends BaseEvent {
    private ActionTypes action;
}

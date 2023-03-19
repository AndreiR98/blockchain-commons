package uk.co.roteala.common.events;

import lombok.*;

@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class MessengerScript {
    private Action action;
    private Subject subject;

    private Extra extra;
}

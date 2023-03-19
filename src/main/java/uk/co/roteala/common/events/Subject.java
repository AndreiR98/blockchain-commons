package uk.co.roteala.common.events;

import lombok.*;
import uk.co.roteala.common.events.enums.SubjectTypes;

@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Subject extends BaseEvent {
    private SubjectTypes subject;
}

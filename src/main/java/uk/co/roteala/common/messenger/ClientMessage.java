package uk.co.roteala.common.messenger;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@Slf4j
@AllArgsConstructor
@JsonTypeName("TRANSACTION")
public class ClientMessage extends Message{
}

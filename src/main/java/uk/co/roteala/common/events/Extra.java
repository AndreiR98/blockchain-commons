package uk.co.roteala.common.events;

import lombok.*;

@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Extra extends BaseEvent implements ExtraHandler {
    private String extra;

    @Override
    public boolean forFields() {
        String[] components = this.extra.split("_");

        return components[1] != null;
    }

    @Override
    public String getField() {
        String[] components = this.extra.split("_");
        if(this.forFields()){
            return components[0];
        }
        return null;
    }

    @Override
    public String getModifier() {
        String[] components = this.extra.split("_");

        if(this.forFields()){
            return components[1];
        } else {
            return components[0];
        }
    }
}

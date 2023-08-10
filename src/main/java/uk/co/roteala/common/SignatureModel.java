package uk.co.roteala.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class SignatureModel implements Serializable {
    //hex encoded values
    private String r;
    private String s;
    //private Integer v;
    @JsonIgnore
    public Map<String, String> format() {
        Map<String, String> map = new TreeMap<>();
        map.put("r", this.r);
        map.put("s", this.s);
        //map.put("v", String.valueOf(this.v));

        return map;
    }
}

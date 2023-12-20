package uk.co.roteala.common;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Signature {
    private byte[] v;
    private byte[] r;
    private byte[] s;

    public Signature(byte v, byte[] r, byte[] s) {
        this.v = new byte[] {v};
        this.r = r;
        this.s = s;
    }

    public Signature(byte[] v, byte[] r, byte[] s) {
        this.v = v;
        this.r = r;
        this.s = s;
    }
}

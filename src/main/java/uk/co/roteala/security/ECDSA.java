package uk.co.roteala.security;

import lombok.RequiredArgsConstructor;
import org.bouncycastle.math.ec.ECCurve;

@RequiredArgsConstructor
public class ECDSA {
    private final ECKey ecKey;
    private final ECCurve ecCurve;

//    public boolean verify(PseudoTransaction pseudoTransaction){
//        String hash = pseudoTransaction.computeHash();
//    }
}

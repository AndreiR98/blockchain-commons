package uk.co.roteala.security;

import lombok.Getter;
import lombok.Setter;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;

import java.security.*;


public class ECKey {

    @Getter
    @Setter
    private PrivateKey privateKey;

    //Contains the public key point
    @Getter
    private PublicKey publicKey;

    //Implement the logic for the key

    public ECKey(String secretHex) {
        this.privateKey = PrivateKey.builder()
                    .d(secretHex)
                    .build();

        this.publicKey = this.privateKey.getPublicKey();
    }

    public ECKey() {
        generateRandomEC();

        this.publicKey = this.privateKey.getPublicKey();
    }

    private void generateRandomEC() {
        Security.addProvider(new BouncyCastleProvider());
        KeyPair keys = null;

        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EC", "BC");

            ECNamedCurveParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec("secp256k1");

            keyGen.initialize(ecSpec, new SecureRandom());

            keys =  keyGen.generateKeyPair();

            this.privateKey = PrivateKey.builder()
                    .d(((BCECPrivateKey) keys.getPrivate()).getD().toString(16))
                    .build();

        } catch (Exception e){
        }
    }
}

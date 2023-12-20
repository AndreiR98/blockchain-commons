package uk.co.roteala.security;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.asn1.sec.SECNamedCurves;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.asn1.x9.X9IntegerConverter;
import org.bouncycastle.math.ec.ECAlgorithms;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECPoint;
import uk.co.roteala.common.RawTransaction;
import uk.co.roteala.common.Signature;
import uk.co.roteala.core.Blockchain;
import uk.co.roteala.core.rlp.Numeric;
import uk.co.roteala.core.rlp.RlpEncoder;
import uk.co.roteala.core.rlp.RlpUtils;
import uk.co.roteala.security.utils.CryptographyUtils;
import uk.co.roteala.security.utils.HashingService;
import uk.co.roteala.utils.Base58;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

@Slf4j
@Builder
public class PublicKey implements PubKey{

    @Setter
    private final ECPoint ecPoint;

    @Override
    public KeyType getType() {
        return KeyType.PUBLIC;
    }

    @Override
    public String toAddress() {
        return generateETHAddress();
    }

    @Override
    public String getX() {
        return this.ecPoint.getAffineXCoord()
                .toBigInteger()
                .toString(16);
    }

    @Override
    public String getY() {
        return this.ecPoint.getAffineYCoord()
                .toBigInteger()
                .toString(16);
    }

    @Override
    public byte[] encodedX() {
        return this.ecPoint.getAffineXCoord()
                .getEncoded();
    }

    @Override
    public byte[] encodedY() {
        return this.ecPoint.getAffineYCoord()
                .getEncoded();
    }

    /**
     * Return Double hash of pubKey.x||pubKey.y
     * @return String
     * */
    public String getPubKeyHash() {

//        final byte[] bX = this.encodedX();
//        final byte[] bY = this.encodedY();
//
//        byte[] newBxBy = new byte[bX.length + bY.length];
//        System.arraycopy(bX, 0, newBxBy, 0, bX.length);
//        System.arraycopy(bY, 0, newBxBy, bX.length, bY.length);
        final byte[] address = this.toAddress().getBytes(StandardCharsets.UTF_8);

        return HashingService.computeSHA3(HashingService.computeSHA3(this.toAddress()));
    }

    private String generateAddress() {
        final byte[] bX = this.encodedX();
        final byte[] bY = this.encodedY();

        byte[] newBX = new byte[bX.length + 1];
        newBX[0] = 0x04;

        System.arraycopy(bX, 0, newBX, 1, bX.length);

        byte[] newBxBy = new byte[newBX.length + bY.length];

        System.arraycopy(newBX, 0, newBxBy, 0, newBX.length);
        System.arraycopy(bY, 0, newBxBy, newBX.length, bY.length);

        byte[] ripedMdString = HashingService.firstRoundAddressGenerator(newBxBy);

        byte[] extendedRipeMd160 = new byte[ripedMdString.length + 1];
        extendedRipeMd160[0] = 0x00;
        System.arraycopy(ripedMdString, 0, extendedRipeMd160, 1, ripedMdString.length);

        //Calculate the checksum by hashing the extended ripedMD twice using SHA256
        byte[] checkSum = HashingService.doubleSHA256(extendedRipeMd160);

        byte[] bytesToEncode = new byte[extendedRipeMd160.length + 4];

        System.arraycopy(extendedRipeMd160, 0, bytesToEncode, 0, extendedRipeMd160.length);
        System.arraycopy(checkSum, 0, bytesToEncode, extendedRipeMd160.length, 4);

        return Base58.encode(bytesToEncode);
    }

    private String generateETHAddress() {
        final byte[] bX = this.encodedX();
        final byte[] bY = this.encodedY();

        byte[] newBxBy = new byte[bX.length + bY.length];

        System.arraycopy(bX, 0, newBxBy, 0, bX.length);
        System.arraycopy(bY, 0, newBxBy, bX.length, bY.length);

        byte[] keccak256hash = HashingService.computeKeccak(newBxBy);

        byte[] addrBytes = new byte[20];
        System.arraycopy(keccak256hash, keccak256hash.length - 20, addrBytes, 0, 20);
        return "0x" + CryptographyUtils
                .bytesToHexString(addrBytes);
    }
}

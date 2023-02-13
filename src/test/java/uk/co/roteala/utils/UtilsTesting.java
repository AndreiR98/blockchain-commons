package uk.co.roteala.utils;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@SpringBootTest
@DirtiesContext
@Slf4j
public class UtilsTesting {

    @Test
    void testSecureRandom() throws InvalidAlgorithmParameterException, NoSuchAlgorithmException {
        final SecureRandom secureRandom = new SecureRandom();

        KeyPair kp = GlacierUtils.generateKeyPair(null);

        log.info("KeyPaire:{}", kp.getPublic());

        Assertions.assertTrue(true);
    }
}

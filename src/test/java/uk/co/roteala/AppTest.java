package uk.co.roteala;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@DirtiesContext
class AppTest
{
    @Test
    void contextLoads() {
        Assertions.assertTrue(true);
    }
}

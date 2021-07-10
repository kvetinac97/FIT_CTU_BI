package kvetinac97;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class FirstTest {

    private static Main main;

    @BeforeAll
    // nebo void sBeforeEach
    static void setup () {
        main = new Main();
    }

    @Test
    void firstMethodAssertion () {
        Assertions.assertEquals("First", main.test());
    }

}

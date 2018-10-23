package bachelorarbeit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class ExampleTest {
    private java.util.Set<Integer> set;

    @BeforeEach
    public void setUp() {
        set = new java.util.HashSet<>();
    }

    @AfterEach
    public void tearDown() {
        set = null;
    }

    @Test
    public void shouldAnswerWithTrue() {
        set.add(1);
        assertTrue(set.contains(1));
    }
}

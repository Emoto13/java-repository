package bg.sofia.uni.fmi.mjt.myfitnesspal.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class UnknownFoodExceptionTest {
    @Test
    void test() {
        assertThrows(UnknownFoodException.class, () -> {
            throw new UnknownFoodException("Message", new IllegalArgumentException("Invalid"));
        });
    }

}

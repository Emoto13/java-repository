package bg.sofia.uni.fmi.mjt.myfitnesspal.nutrition;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class NutritionInfoTest {
    @Test
    void test() {
        assertThrows(IllegalArgumentException.class, () -> {
            new NutritionInfo(-1, 50, 50);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new NutritionInfo(50, -1, 50);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new NutritionInfo(50, 50, -1);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new NutritionInfo(1, 50, 1);
        });

        NutritionInfo ni = new NutritionInfo(30, 30, 40);
        assertEquals(550.0, ni.calories());
    }

}

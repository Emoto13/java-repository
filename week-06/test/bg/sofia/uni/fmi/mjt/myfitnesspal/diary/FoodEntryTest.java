package bg.sofia.uni.fmi.mjt.myfitnesspal.diary;

import bg.sofia.uni.fmi.mjt.myfitnesspal.nutrition.NutritionInfo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class FoodEntryTest {
    @Test
    void test() {
        assertThrows(IllegalArgumentException.class, () -> {
            new FoodEntry(null, 1.0, new NutritionInfo(10.0, 10.0, 80));
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new FoodEntry("food", 0.0, new NutritionInfo(10.0, 10.0, 80));
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new FoodEntry("food", 1.0, null);
        });
    }
}

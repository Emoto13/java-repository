package bg.sofia.uni.fmi.mjt.myfitnesspal.diary;

import bg.sofia.uni.fmi.mjt.myfitnesspal.exception.UnknownFoodException;
import bg.sofia.uni.fmi.mjt.myfitnesspal.nutrition.NutritionInfo;
import bg.sofia.uni.fmi.mjt.myfitnesspal.nutrition.NutritionInfoAPI;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DailyFoodDiaryTest {
    @Test
    void test() {
        DailyFoodDiary d = new DailyFoodDiary(new NutritionINFOAPIStub());
        assertThrows(IllegalArgumentException.class, () -> {
            d.addFood(null, "", 0);
        });

        assertThrows(IllegalArgumentException.class, () -> d.addFood(Meal.BREAKFAST, "", 0));

        assertThrows(IllegalArgumentException.class, () -> {
            d.addFood(Meal.BREAKFAST, "food", 0);
        });


        assertDoesNotThrow(() -> {
            d.addFood(Meal.BREAKFAST, "food", 10);
        });

        assertEquals(1, d.getAllFoodEntries().size());
        assertNotEquals(0.0, d.getDailyCaloriesIntake());
        assertNotEquals(0.0, d.getDailyCaloriesIntakePerMeal(Meal.BREAKFAST));
        assertThrows(IllegalArgumentException.class, () -> d.getDailyCaloriesIntakePerMeal(null));
    }

    public static class NutritionINFOAPIStub implements NutritionInfoAPI {

        @Override
        public NutritionInfo getNutritionInfo(String foodName) throws UnknownFoodException {
            if (foodName == null || foodName.isBlank()) throw new IllegalArgumentException("Invalid food name");
            if (foodName.equals("unknown")) throw new UnknownFoodException("unknown");

            return new NutritionInfo(10, 10, 80);
        }
    }
}

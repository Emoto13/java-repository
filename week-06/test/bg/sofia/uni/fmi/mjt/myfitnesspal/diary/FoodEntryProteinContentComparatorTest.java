package bg.sofia.uni.fmi.mjt.myfitnesspal.diary;

import bg.sofia.uni.fmi.mjt.myfitnesspal.nutrition.NutritionInfo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FoodEntryProteinContentComparatorTest {
    @Test
    void test() {
        FoodEntryProteinContentComparator comparator = new FoodEntryProteinContentComparator();
        FoodEntry fe = new FoodEntry("f1", 1, new NutritionInfo(96, 2, 2));
        assertEquals(0, comparator.compare(fe, fe));
    }

}

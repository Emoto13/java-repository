public class TourGuide {
    public static int getBestSightseeingPairScore(int[] places) {
        int max = - 1 << 32;
        for (int i = 0; i < places.length; i++) {
            for (int j = i + 1; j < places.length; j++) {
                int currDist = places[i] + places[j] + i - j;
                if (currDist > max) {
                    max =  currDist;          
                }
            }
        }

        return max;
    }
}

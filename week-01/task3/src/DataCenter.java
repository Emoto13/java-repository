public class DataCenter {
    public static int getCommunicatingServersCount(int[][] map) {
        if (map.length == 0) return 0;

        int[] rows = new int[map.length];
        int[] cols = new int[map[0].length];
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j] == 1) {
                    rows[i]++;
                    cols[j]++;
                }
            }
        }

        int total = 0;
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j] == 1 && (rows[i] > 1 || cols[j] > 1)) {
                    total++;
                }
            }
        }

        return total;
    }
}

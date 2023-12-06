public class PrefixExtractor {
    public static String getLongestCommonPrefix(String[] words) {
        if (words == null || words.length == 0) {
            return "";
        }
 
        String bestPrefix = words[0];
        for (String word : words) {
            if (word == "") {
                return word;
            }

            for (int i = 0; i < word.length(); i++) {
                if (i >= bestPrefix.length()) {
                    bestPrefix = word.substring(0, i);
                    break;
                }

                if (word.charAt(i) != bestPrefix.charAt(i)) {
                    bestPrefix = word.substring(0, i);
                    break;
                }
            }
        }

        return bestPrefix;
    }
} 
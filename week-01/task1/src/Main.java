public class Main {
    public static void main(String[] args) {
        System.out.println(PrefixExtractor.getLongestCommonPrefix(new String[]{"flower", "flow", "flight"}));
        System.out.println(PrefixExtractor.getLongestCommonPrefix(new String[]{"dog", "racecar", "car"}));
        System.out.println(PrefixExtractor.getLongestCommonPrefix(new String[]{"cat"}));
        System.out.println(PrefixExtractor.getLongestCommonPrefix(new String[]{"", "a"}));        
    }
}

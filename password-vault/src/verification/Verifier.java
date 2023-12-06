package verification;

public class Verifier {
    public static boolean isEmptyOrNull(String value) {
        return value == null || value.isBlank();
    }

    public static void assertNotEmptyOrNull(String value, String message) {
        if (isEmptyOrNull(value)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void assertEqual(String first, String second, String message) {
        if (!first.equals(second)) {
            throw new IllegalArgumentException(message);
        }
    }
}

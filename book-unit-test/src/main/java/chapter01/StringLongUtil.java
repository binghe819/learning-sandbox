package chapter01;

public class StringLongUtil {
    private static boolean WAS_LAST_STRING_LONG = false;

    private StringLongUtil() {
    }

    public static boolean isStringLong(String input) {
        return input.length() > 5;
//        if (input.length() > 5) {
//            return true;
//        }
//        return false;
    }

    public static boolean isStringLongV2(String input) {
        boolean response = input.length() > 5;
        WAS_LAST_STRING_LONG = response;
        return response;
    }

    public static int parse(String integer) {
        return Integer.parseInt(integer);
    }
}

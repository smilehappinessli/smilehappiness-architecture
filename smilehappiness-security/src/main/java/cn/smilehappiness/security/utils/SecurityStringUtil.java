package cn.smilehappiness.security.utils;

/**
 * <p>
 * security string util
 * <p/>
 *
 * @author
 * @Date 2023/3/7 11:53
 */
public class SecurityStringUtil {

    private SecurityStringUtil() {

    }

    public static boolean isEmpty(String value) {
        int strLen;
        if (value != null && (strLen = value.length()) != 0) {
            for (int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(value.charAt(i))) {
                    return false;
                }
            }
        }

        return true;
    }

    public static boolean areNotEmpty(String... values) {
        boolean result = true;
        if (values != null && values.length != 0) {
            int var1 = values.length;
            for (int var2 = 0; var2 < var1; ++var2) {
                String value = values[var2];
                result &= !isEmpty(value);
            }
        } else {
            result = false;
        }

        return result;
    }

    public static boolean areEmpty(String... values) {
        return !areNotEmpty(values);
    }
}

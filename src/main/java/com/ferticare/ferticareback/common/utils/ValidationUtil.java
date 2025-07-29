package com.ferticare.ferticareback.common.utils;

import java.lang.reflect.Field;
import java.text.Normalizer;
import java.util.Random;
import java.util.regex.Pattern;

public class ValidationUtil {
    private static final Pattern NON_LATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");
    private static final Pattern EDGES_DASHES = Pattern.compile("(^-)|(-$)");
    private static final int RANDOM_SUFFIX_LENGTH = 8;
    private static final String RANDOM_CHARS = "abcdefghijklmnopqrstuvwxyz0123456789";
    private static final Random RANDOM = new Random();

    public static String toSlug(String input) {
        if (input == null) {
            return "";
        }

        input = input.replaceAll("đ", "d").replaceAll("Đ", "D");

        String nowhitespace = WHITESPACE.matcher(input).replaceAll("-");
        String normalized = Normalizer.normalize(nowhitespace, Normalizer.Form.NFD);
        String slug = NON_LATIN.matcher(normalized).replaceAll("");
        slug = slug.toLowerCase();
        slug = EDGES_DASHES.matcher(slug).replaceAll("");
        return slug;
    }

    /**
     * Generate a random alphanumeric string of the specified length
     * @param length length of the random string
     * @return random alphanumeric string
     */
    public static String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(RANDOM_CHARS.charAt(RANDOM.nextInt(RANDOM_CHARS.length())));
        }
        return sb.toString();
    }

    /**
     * Append a random string to a slug to make it unique
     * @param slug the original slug
     * @return the slug with a random suffix
     */
    public static String appendRandomSuffix(String slug) {
        return slug + "-" + generateRandomString(RANDOM_SUFFIX_LENGTH);
    }

    public static boolean isFieldExist(Class<?> clazz, String fieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            return true;
        } catch (NoSuchFieldException e) {
            Class<?> superClass = clazz.getSuperclass();
            if (superClass != null) {
                try {
                    Field superField = superClass.getDeclaredField(fieldName);
                    return true;
                } catch (NoSuchFieldException ex) {
                    return false;
                }
            }
            return false;
        }
    }
}


package net.minecraft.util;

import java.util.regex.Pattern;
import javax.annotation.Nullable;

public class StringUtils
{
    private static final Pattern PATTERN_CONTROL_CODE = Pattern.compile("(?i)\\u00A7[0-9A-FK-OR]");

    /**
     * Returns a value indicating whether the given string is null or empty.
     */
    public static boolean isNullOrEmpty(@Nullable String string)
    {
        return org.apache.commons.lang3.StringUtils.isEmpty(string);
    }
}

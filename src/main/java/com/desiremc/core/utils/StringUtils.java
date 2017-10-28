package com.desiremc.core.utils;

public class StringUtils
{

    public static String implode(String[] strings, int start, int end)
    {
        StringBuilder sb = new StringBuilder();

        for (int i = start; i < end; i++)
        {
            sb.append(strings[i] + " ");
        }

        return sb.toString().trim();
    }

    public static String compile(String[] strings)
    {
        return implode(strings, 0, strings.length);
    }

    public static String capitalize(final String str)
    {
        int strLen;
        if (str == null || (strLen = str.length()) == 0)
        {
            return str;
        }

        final int firstCodepoint = str.codePointAt(0);
        final int newCodePoint = Character.toTitleCase(firstCodepoint);
        if (firstCodepoint == newCodePoint)
        {
            // already capitalized
            return str;
        }

        final int newCodePoints[] = new int[strLen]; // cannot be longer than
                                                    // the char array
        int outOffset = 0;
        newCodePoints[outOffset++] = newCodePoint; // copy the first codepoint
        for (int inOffset = Character.charCount(firstCodepoint); inOffset < strLen;)
        {
            final int codepoint = str.codePointAt(inOffset);
            newCodePoints[outOffset++] = codepoint; // copy the remaining ones
            inOffset += Character.charCount(codepoint);
        }
        return new String(newCodePoints, 0, outOffset);
    }

    public static boolean contains(String[] values, String search)
    {
        for (String val : values)
        {
            if (val.equalsIgnoreCase(search))
            {
                return true;
            }
        }
        return false;
    }

    public static boolean isNullOrEmpty(String str)
    {
        return str == null || str.equals("") || str.equals("\0");
    }

}

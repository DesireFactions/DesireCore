package com.desiremc.core.utils;

import java.lang.reflect.Field;

import com.desiremc.core.DesireCore;

public class ReflectionUtils
{

    private static Class<?> stringClass = String.class;
    private static Field stringValueField;

    private static Class<Integer> integerClass = Integer.class;
    private static Field integerValueField;

    public static void setStringConents(String original, String content)
    {
        if (stringClass == null || stringValueField == null)
        {
            try
            {
                stringValueField = stringClass.getDeclaredField("value");
                stringValueField.setAccessible(true);
                stringValueField.set(original, content.toCharArray());
            }
            catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex)
            {
                ex.printStackTrace();
            }
        }
    }

    public static void setIntegerValue(Integer original, int value)
    {
        if (integerValueField == null)
        {
            try
            {
                integerValueField = integerClass.getDeclaredField("value");
                integerValueField.setAccessible(true);
            }
            catch (NoSuchFieldException | SecurityException ex)
            {
                ex.printStackTrace();
            }
        }
        if (integerValueField == null)
        {
            return;
        }
        try
        {
            integerValueField.set(original, value);
        }
        catch (IllegalArgumentException | IllegalAccessException ex)
        {
            ex.printStackTrace();
        }
    }

    public static void setValue(Object o, Field f, Object value)
    {
        try
        {
            f.set(o, value);
        }
        catch (IllegalArgumentException | IllegalAccessException ex)
        {
            ChatUtils.sendStaffMessage(ex, DesireCore.getInstance());
        }
    }

    public static class NMSClasses
    {
        public static Class<?> gameProfile;

        public static void initialize()
        {
            gameProfile = net.minecraft.util.com.mojang.authlib.GameProfile.class;
        }
    }

    public static class NMSFields
    {
        public static Field gameProfileId;
        public static Field gameProfileName;

        public static void initialize()
        {
            try
            {
                gameProfileId = NMSClasses.gameProfile.getDeclaredField("id");
                gameProfileName = NMSClasses.gameProfile.getDeclaredField("name");

                gameProfileId.setAccessible(true);
                gameProfileName.setAccessible(true);
            }
            catch (NoSuchFieldException | SecurityException ex)
            {
                ChatUtils.sendStaffMessage(ex, DesireCore.getInstance());
            }
        }
    }

}

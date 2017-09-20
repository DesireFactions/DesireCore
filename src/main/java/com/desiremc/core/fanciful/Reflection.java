package com.desiremc.core.fanciful;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class Reflection
{
    public static String getVersion()
    {
        final String name = Bukkit.getServer().getClass().getPackage().getName();
        final String version = name.substring(name.lastIndexOf('.') + 1) + ".";
        return version;
    }

    public static Class<?> getNMSClass(String className)
    {
        final String fullName = "net.minecraft.server." + getVersion() + className;
        Class<?> clazz = null;
        try
        {
            clazz = Class.forName(fullName);
        }
        catch (final Exception e)
        {
            e.printStackTrace();
        }
        return clazz;
    }

    public static Class<?> getNMSClassWithException(String className) throws Exception
    {
        final String fullName = "net.minecraft.server." + getVersion() + className;
        Class<?> clazz = Class.forName(fullName);
        return clazz;
    }

    public static Class<?> getOBCClass(String className)
    {
        final String fullName = "org.bukkit.craftbukkit." + getVersion() + className;
        Class<?> clazz = null;
        try
        {
            clazz = Class.forName(fullName);
        }
        catch (final Exception e)
        {
            e.printStackTrace();
        }
        return clazz;
    }

    public static Object getHandle(Object obj)
    {
        try
        {
            return getMethod(obj.getClass(), "getHandle", new Class[0]).invoke(obj, new Object[0]);
        }
        catch (final Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static Object getHandleWithException(Object obj) throws Exception
    {
        return getMethod(obj.getClass(), "getHandle", new Class[0]).invoke(obj, new Object[0]);
    }

    public static Entity getBukkitEntity(Object obj)
    {
        try
        {
            return (Entity) getMethod(obj.getClass(), "getBukkitEntity", new Class[0]).invoke(obj, new Object[0]);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static Field getField(Class<?> clazz, String name)
    {
        try
        {
            final Field field = clazz.getDeclaredField(name);
            field.setAccessible(true);
            return field;
        }
        catch (final Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static Method getMethod(Class<?> clazz, String name, Class<?>... args)
    {
        for (final Method m : clazz.getMethods())
        {
            if (m.getName().equals(name) && (args.length == 0 || ClassListEqual(args, m.getParameterTypes())))
            {
                m.setAccessible(true);
                return m;
            }
        }
        return null;
    }

    public static boolean ClassListEqual(Class<?>[] l1, Class<?>[] l2)
    {
        boolean equal = true;
        if (l1.length != l2.length) { return false; }
        for (int i = 0; i < l1.length; i++)
        {
            if (l1[i] != l2[i])
            {
                equal = false;
                break;
            }
        }
        return equal;
    }
}
package com.desiremc.core.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CollectionUtils
{

    @SuppressWarnings("unchecked")
    public static <T> T getLast(List<T> list)
    {
        if (list == null || list.size() == 0)
        {
            return null;
        }
        if (list instanceof Deque)
        {
            return ((Deque<T>) list).peekLast();
        }
        return list.get(list.size() - 1);
    }

    /**
     * Safely checks if the given collection is immutable. If the collection is mutable, the data will not be affected
     * unless the collection in question keeps track of total number of operations. The test is done by calling
     * {@link Collection#removeIf(java.util.function.Predicate)} with the predicate of {@code 1 == 2}.
     * 
     * @param values the collection to check.
     * @return {@code true} if the collection is immutable.
     */
    public static boolean isImmutable(Collection<?> values)
    {
        try
        {
            values.removeIf(x -> 1 == 2);
            return true;
        }
        catch (UnsupportedOperationException ex)
        {
            return false;
        }
    }

    /**
     * Converts the given values into their string counterpart. This is done by calling {@link Object#toString()} on
     * every object. More specific use cases like {@link org.bukkit.entity.Player#getName()} etc are not compatible.
     * 
     * @param values
     * @return
     */
    public static <T> List<String> getStringList(Collection<T> values)
    {
        if (values == null || values.size() == 0)
        {
            return Arrays.asList();
        }
        List<String> list = new LinkedList<>();
        for (Object o : values)
        {
            if (o != null)
            {
                list.add(o.toString());
            }
            else
            {
                list.add(null);
            }
        }
        return list;
    }

    private final static Map<Class<?>, Method> nameMethods = new HashMap<>();

    /**
     * Get the names of every single object passed in the values parameter. This method requires the method "getName()"
     * to exist within whatever type is passed. If it does not exist, an empty list is returned. However, in the future
     * there is a potential that it will be changed to throwing an {@link IllegalArgumentException}.
     * 
     * @param values the values to get the name of.
     * @param type the type of the object.
     * @return the list of names.
     */
    public static <T> List<String> getNames(Collection<T> values, Class<T> type)
    {
        if (values == null || values.size() == 0)
        {
            return Arrays.asList();
        }
        List<String> list = new LinkedList<>();

        Method method = getNameMethod(type);

        if (method == null)
        {
            return Arrays.asList();
        }

        for (Object obj : values)
        {
            try
            {
                list.add((String) method.invoke(obj));
            }
            catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex)
            {
                // this exception is actually going to be printed as it should never happen.
                // the method was set to accessible previously, and it should also never have any arguments.
                ex.printStackTrace();
            }
        }
        return list;
    }

    private static Method getNameMethod(Class<?> clazz)
    {
        Method method = nameMethods.get(clazz);
        if (method == null)
        {
            try
            {
                method = clazz.getDeclaredMethod("getName");
                method.setAccessible(true);
                nameMethods.put(clazz, method);
            }
            catch (NoSuchMethodException | SecurityException ex)
            {
                // ignored
            }
        }
        return method;
    }

}

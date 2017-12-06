package com.desiremc.core.utils;

import java.util.Deque;
import java.util.List;

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

}

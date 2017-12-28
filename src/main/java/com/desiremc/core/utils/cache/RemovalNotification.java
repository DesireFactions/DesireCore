package com.desiremc.core.utils.cache;

import java.util.Map.Entry;

public class RemovalNotification<K, V> implements Entry<K, V>
{

    private K key;
    private V value;
    private Cause cause;

    public RemovalNotification(K key, V value, Cause cause)
    {
        this.key = key;
        this.value = value;
        this.cause = cause;
    }

    @Override
    public K getKey()
    {
        return key;
    }

    @Override
    public V getValue()
    {
        return value;
    }

    @Override
    public V setValue(V value)
    {
        return this.value = value;
    }

    public Cause getCause()
    {
        return cause;
    }

    public static enum Cause
    {
        EXPIRE,
        REMOVE,
        OVERWRITE;
    }

}

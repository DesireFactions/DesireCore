package com.desiremc.core.utils.cache;

public abstract class RemovalListener<K, V>
{

    public abstract void onRemoval(RemovalNotification<K, V> entry);

}

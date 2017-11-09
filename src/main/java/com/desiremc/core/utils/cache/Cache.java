package com.desiremc.core.utils.cache;

import java.util.AbstractMap.SimpleEntry;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import com.desiremc.core.utils.cache.RemovalNotification.Cause;

public class Cache<K, V> implements Map<K, V>
{

    private JavaPlugin plugin;

    private HashMap<K, Data<K, V>> base;

    private LinkedList<V> values;

    private int expirationTime;

    private TimeUnit unit;

    private long defaultExpirationTimeMillis;

    private RemovalListener<K, V> removalListener;

    private boolean debug;

    /**
     * Create a new cache with given values.
     * 
     * @param expirationTime the amount of time based on the given unit.
     * @param unit the time unit of expirationTime.
     * @param removalListener the event that fires when an entry expires.
     * @param plugin the plugin that handles the removal timer
     */
    public Cache(int expirationTime, TimeUnit unit, RemovalListener<K, V> removalListener, JavaPlugin plugin)
    {
        this(expirationTime, unit, removalListener, plugin, false);
    }

    /**
     * Create a new cache with given values.
     * 
     * @param expirationTime the amount of time based on the given unit.
     * @param unit the time unit of expirationTime.
     * @param removalListener the event that fires when an entry expires.
     * @param plugin the plugin that handles the removal timer
     */
    public Cache(int expirationTime, TimeUnit unit, RemovalListener<K, V> removalListener, JavaPlugin plugin, boolean debug)
    {
        this.expirationTime = expirationTime;
        this.unit = unit;
        this.removalListener = removalListener;
        this.plugin = plugin;
        base = new HashMap<>();
        values = new LinkedList<>();
        this.debug = debug;
    }

    /**
     * Creates a new cache with given values that has no listener for removal.
     * 
     * @param expirationTime the amount of time based on the given unit.
     * @param unit the time unit of expirationTime.
     * @param plugin the plugin that handles the removal timer
     */
    public Cache(int expirationTime, TimeUnit unit, JavaPlugin plugin)
    {
        this(expirationTime, unit, new EmptyRemovalListener<>(), plugin);
    }

    /**
     * Creates a new cache that expires after the given number of ticks with no listener for removal. Assumes optimal
     * TPS of 20.
     * 
     * @param ticks the amount of ticks before removal.
     * @param plugin the plugin that handles the removal timer
     */
    public Cache(int ticks, RemovalListener<K, V> removalListener, JavaPlugin plugin)
    {
        this(ticks * 50, TimeUnit.MILLISECONDS, removalListener, plugin);
    }

    /**
     * Creates a new cache that expires after the given number of ticks with no listener for removal. Assumes optimal
     * TPS of 20.
     * 
     * @param ticks the amount of ticks before removal.
     * @param plugin the plugin that handles the removal timer
     */
    public Cache(int ticks, JavaPlugin plugin)
    {
        this(ticks * 50, TimeUnit.MILLISECONDS, plugin);
    }

    public void toRemove(K k)
    {
        Bukkit.getScheduler().runTask(plugin, new Runnable()
        {
            @Override
            public void run()
            {
                remove(k);
            }
        });
    }

    @Override
    public int size()
    {
        return base.size();
    }

    @Override
    public boolean isEmpty()
    {
        return base.isEmpty();
    }

    @Override
    public boolean containsKey(Object key)
    {
        return base.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value)
    {
        return base.containsValue(value);
    }

    @Override
    public V get(Object key)
    {
        Data<K, V> data = base.get(key);
        if (data == null)
        {
            return null;
        }
        if (data.getExpirationTime() < System.currentTimeMillis())
        {
            remove(data.getKey(), data.getValue());
            return null;
        }
        return data == null ? null : data.getValue();
    }

    @Override
    public V put(K key, V value)
    {
        if (debug)
        {
            System.out.println(getExpirationTimeTicks());
        }
        BukkitTask task = Bukkit.getScheduler().runTaskLater(plugin, new Runnable()
        {
            @Override
            public void run()
            {
                removalListener.onRemoval(new RemovalNotification<>(key, value, Cause.EXPIRE));
                base.remove(key);
            }
        }, getExpirationTimeTicks());

        Data<K, V> prev = base.put(key, new Data<>(key, value, System.currentTimeMillis() + getDefaultExpirationTimeMillis(), task));
        if (prev != null)
        {
            prev.getTask().cancel();
            removalListener.onRemoval(new RemovalNotification<K, V>(prev.getKey(), prev.getValue(), Cause.OVERWRITE));
        }

        values.add(value);

        return prev == null ? null : prev.getValue();
    }

    @Override
    public V remove(Object key)
    {
        Data<K, V> data = base.remove(key);
        if (data != null)
        {
            removalListener.onRemoval(new RemovalNotification<K, V>(data.getKey(), data.getValue(), Cause.REMOVE));
        }
        return data == null ? null : data.getValue();
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m)
    {
        int numKeysToBeAdded = m.size();
        if (numKeysToBeAdded == 0)
        {
            return;
        }

        for (Iterator<? extends Entry<? extends K, ? extends V>> i = m.entrySet().iterator(); i.hasNext();)
        {
            Entry<? extends K, ? extends V> e = i.next();
            put(e.getKey(), e.getValue());
        }
    }

    @Override
    public void clear()
    {
        base.clear();
    }

    @Override
    public Set<K> keySet()
    {
        return base.keySet();
    }

    @Override
    public Collection<V> values()
    {
        return values();
    }

    @Override
    public Set<Entry<K, V>> entrySet()
    {
        Set<Entry<K, V>> set = new LinkedHashSet<>();
        for (Entry<K, Data<K, V>> entry : base.entrySet())
        {
            set.add(new SimpleEntry<K, V>(entry.getKey(), entry.getValue() != null ? entry.getValue().getValue() : null));
        }
        return set;
    }

    public int getDefaultExpirationTime()
    {
        return expirationTime;
    }

    public TimeUnit getDefaultExpirationTimeUnit()
    {
        return unit;
    }

    public long getDefaultExpirationTimeMillis()
    {
        if (defaultExpirationTimeMillis == 0)
        {
            defaultExpirationTimeMillis = getExpirationTimeMillis(expirationTime, unit);
        }
        return defaultExpirationTimeMillis;
    }

    public long getTimeLeftMillis(Object key)
    {
        Data<K, V> data = base.get(key);
        if (data == null)
        {
            return 0;
        }
        return data.getExpirationTime() - System.currentTimeMillis();
    }

    private long getExpirationTimeMillis(int expirationTime, TimeUnit unit)
    {
        return TimeUnit.MILLISECONDS.convert(expirationTime, unit);
    }

    private long getExpirationTimeTicks()
    {
        return TimeUnit.SECONDS.convert(expirationTime, unit) * 20;
    }

    private static class Data<K, V>
    {

        private K key;
        private V value;
        private long expirationTime;
        private BukkitTask task;

        public Data(K key, V value, long expirationTime, BukkitTask task)
        {
            this.value = value;
            this.expirationTime = expirationTime;
            this.task = task;
        }

        private K getKey()
        {
            return key;
        }

        public V getValue()
        {
            return value;
        }

        public long getExpirationTime()
        {
            return expirationTime;
        }

        public BukkitTask getTask()
        {
            return task;
        }

    }

    private static class EmptyRemovalListener<K, V> extends RemovalListener<K, V>
    {

        @Override
        public void onRemoval(RemovalNotification<K, V> entry)
        {
        }
    }

}

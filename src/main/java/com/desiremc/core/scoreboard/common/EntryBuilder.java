package com.desiremc.core.scoreboard.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import com.desiremc.core.scoreboard.EntryRegistry.PlayerEntry;
import com.desiremc.core.scoreboard.type.Entry;

/**
 * An utility to make pretty entries for the scoreboards, without calculating the positions by yourself.
 *
 * @author TigerHix
 */
public final class EntryBuilder
{

    private static final HashMap<String, UUID> keyHistory = new HashMap<>();
    private static final ArrayList<UUID> blanks = new ArrayList<>();

    private final LinkedList<Entry> entries = new LinkedList<>();

    private int blankCount;

    /**
     * Append a blank line.
     *
     * @return this
     */
    public EntryBuilder blank()
    {
        UUID uuid;
        if (blankCount >= blanks.size())
        {
            uuid = UUID.randomUUID();
            blanks.add(uuid);
        }
        else
        {
            uuid = blanks.get(blankCount);
        }
        blankCount++;
        return next(uuid.toString(), "", uuid);
    }

    /**
     * Append a new line with specified text.
     *
     * @param string text
     * @return this
     */
    public EntryBuilder next(String key, String value)
    {
        UUID uuid;

        uuid = keyHistory.get(key);
        if (uuid == null)
        {
            uuid = UUID.randomUUID();
            keyHistory.put(key, uuid);
        }

        return next(key, value, uuid);
    }

    private EntryBuilder next(String key, String value, UUID uuid)
    {
        entries.add(new Entry(adapt(value), uuid, entries.size()));
        return this;
    }

    /**
     * Returns a map of entries.
     *
     * @return map
     */
    public List<Entry> build()
    {
        for (Entry entry : entries)
        {
            entry.setPosition(entries.size() - entry.getPosition());
        }
        return entries;
    }

    private String adapt(String entry)
    {
        // Cut off the exceeded part if needed
        if (entry.length() > 48)
            entry = entry.substring(0, 47);
        return Strings.format(entry);
    }

    public static List<Entry> build(PlayerEntry playerEntry)
    {
        EntryBuilder builder = new EntryBuilder();
        for (java.util.Map.Entry<String, String> entry : playerEntry.getEntryMap().entrySet())
        {
            builder.blank();
            builder.next(entry.getKey(), entry.getValue());
        }
        return builder.build();
    }

}

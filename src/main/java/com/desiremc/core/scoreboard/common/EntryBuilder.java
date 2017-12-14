package com.desiremc.core.scoreboard.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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
    private static final ArrayList<UUID> lines = new ArrayList<>();

    private final LinkedList<Entry> entries = new LinkedList<>();

    private int blankCount;
    private int lineCount;

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
        return next("", "", uuid);
    }

    private EntryBuilder line()
    {
        UUID uuid;
        if (lineCount >= lines.size())
        {
            uuid = UUID.randomUUID();
            lines.add(uuid);
        }
        else
        {
            uuid = lines.get(lineCount);
        }
        entries.add(new Entry("§7§m---------" + (lineCount != 0 ? "§r" : ""), "§7§m----------", uuid, entries.size() + 1));
        lineCount++;
        return this;
    }

    /**
     * Append a new line with specified text.
     *
     * @param key text
     * @param value the value to associate with the text
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
        entries.add(new Entry(key, "§c" + value, uuid, entries.size() + 1));
        return this;
    }

    /**
     * Returns a map of entries.
     *
     * @return map
     */
    public List<Entry> build()
    {
        return entries;
    }

    public int size()
    {
        return entries.size();
    }

    public static List<Entry> build(PlayerEntry playerEntry)
    {
        EntryBuilder builder = new EntryBuilder();
        if (playerEntry.getEntryMap().size() > 0)
        {
            builder.line();
            Map<String, String> entries;
            entries = playerEntry.getEntryMap();
            for (Map.Entry<String, String> entry : entries.entrySet())
            {
                if (builder.size() > 1)
                {
                    builder.blank();
                }
                builder.next(entry.getKey(), entry.getValue());
            }
            builder.line();
        }
        return builder.build();
    }

}

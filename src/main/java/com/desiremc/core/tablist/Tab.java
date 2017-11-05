package com.desiremc.core.tablist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

import com.desiremc.core.tablist.events.TabCreateEvent;

import net.minecraft.server.v1_7_R4.PacketPlayOutPlayerInfo;

public class Tab
{

    private static final boolean DEBUG = true;

    private static ConcurrentHashMap<Tab, Boolean> playerTabs = new ConcurrentHashMap<>();
    private Player player;
    private Scoreboard scoreboard;
    private HashMap<UUID, Entry> entries;

    public Tab(final Player player)
    {
        this.player = player;
        this.entries = new HashMap<>();
        this.clear();
        if (!player.getScoreboard().equals(Bukkit.getScoreboardManager().getMainScoreboard()))
        {
            this.scoreboard = player.getScoreboard();
            this.assemble();
        }
        else
        {
            this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
            player.setScoreboard(this.scoreboard);
            this.assemble();
        }

        Tab.playerTabs.put(this, true);
    }

    public void clear()
    {
        Iterator<Entry> iterator = this.entries.values().iterator();

        while (iterator.hasNext())
        {
            Entry online = iterator.next();

            if (online.getNms() != null)
            {
                PacketPlayOutPlayerInfo packet = PacketPlayOutPlayerInfo.removePlayer(online.getNms());

                ((CraftPlayer) this.player).getHandle().playerConnection.sendPacket(packet);
            }
        }

        for (Player player : Bukkit.getOnlinePlayers())
        {
            PacketPlayOutPlayerInfo packet1 = PacketPlayOutPlayerInfo.removePlayer(((CraftPlayer) player).getHandle());
            ((CraftPlayer) this.player).getHandle().playerConnection.sendPacket(packet1);
        }

        this.entries.clear();
    }

    public void assemble()
    {
        for (int i = 0; i < 60; ++i)
        {
            int x = i % 3;
            int y = i / 3;

            new Entry(this, this.getNextBlank(), x, y).send();
        }

        Bukkit.getPluginManager().callEvent(new TabCreateEvent(this));
    }

    public Entry getEntry(int x, int y)
    {
        Iterator<Entry> iterator = this.entries.values().iterator();

        Entry tabEntry;

        do
        {
            if (!iterator.hasNext())
            {
                return null;
            }

            tabEntry = iterator.next();
        } while (tabEntry.getX() != x || tabEntry.getY() != y);

        return tabEntry;
    }

    public String getNextBlank()
    {
        Iterator<String> iterator = getAllBlanks().iterator();

        label23: while (iterator.hasNext())
        {
            String string = iterator.next();
            Iterator<Entry> iterator1 = this.entries.values().iterator();

            while (iterator1.hasNext())
            {
                Entry tabEntry = iterator1.next();

                if (tabEntry.getText() != null && tabEntry.getText().startsWith(string))
                {
                    continue label23;
                }
            }

            return string;
        }
        if (DEBUG)
        {
            System.out.println("Tab.getNextBlank() returned null.");
        }
        return null;
    }

    private static List<String> getAllBlanks()
    {
        ArrayList<String> toReturn = new ArrayList<>();
        ChatColor[] achatcolor;
        int i = (achatcolor = ChatColor.values()).length;

        for (int j = 0; j < i; ++j)
        {
            ChatColor chatColor = achatcolor[j];

            toReturn.add("" + chatColor + ChatColor.RESET);
            ChatColor[] achatcolor1;
            int k = (achatcolor1 = ChatColor.values()).length;

            for (int l = 0; l < k; ++l)
            {
                ChatColor chatColor1 = achatcolor1[l];

                if (toReturn.size() >= 60)
                {
                    return toReturn;
                }

                toReturn.add("" + chatColor + chatColor1 + ChatColor.RESET);
            }
        }

        return toReturn;
    }

    public static Tab getByPlayer(Player player)
    {
        for (Tab t : playerTabs.keySet())
        {
            if (t.getPlayer().getName().equals(player.getName()))
            {
                return t;
            }
        }
        return null;
    }

    public static Set<Tab> getPlayerTabs()
    {
        return Tab.playerTabs.keySet();
    }

    public Player getPlayer()
    {
        return this.player;
    }

    public Scoreboard getScoreboard()
    {
        return this.scoreboard;
    }

    public void addEntry(Entry entry)
    {
        if (entry.getUniqueId() == null)
        {
            throw new IllegalArgumentException("Entry has null UUID.");
        }
        this.entries.put(entry.getUniqueId(), entry);
    }

    public void removeEntry(UUID uuid)
    {
        Entry entry = this.entries.remove(uuid);
        entry.setText("").send();
    }
}
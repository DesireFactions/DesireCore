package com.desiremc.core.tablist;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

import com.desiremc.core.DesireCore;
import com.desiremc.core.tablist.events.TabCreateEvent;

import net.minecraft.server.v1_7_R4.PacketPlayOutPlayerInfo;

public class Tab
{

    private static Set<Tab> playerTabs = new HashSet<>();
    private Player player;
    private Scoreboard scoreboard;
    private List<Entry> entries;

    public Tab(final Player player)
    {
        this.player = player;
        this.entries = new ArrayList<>();
        this.clear();
        if (!player.getScoreboard().equals(Bukkit.getScoreboardManager().getMainScoreboard()))
        {
            this.scoreboard = player.getScoreboard();
            this.assemble();
        }
        else
        {
            Bukkit.getScheduler().runTask(DesireCore.getInstance(), new Runnable()
            {
                public void run()
                {
                    Tab.this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
                    player.setScoreboard(Tab.this.scoreboard);
                    Tab.this.assemble();
                }
            });
        }

        Tab.playerTabs.add(this);
    }

    public void clear()
    {
        Iterator<Entry> iterator = this.entries.iterator();

        while (iterator.hasNext())
        {
            Entry online = iterator.next();

            if (online.nms() != null)
            {
                PacketPlayOutPlayerInfo packet = PacketPlayOutPlayerInfo.removePlayer(online.nms());

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

    private void assemble()
    {
        for (int i = 0; i < 60; ++i)
        {
            int x = i % 3;
            int y = i / 3;

            (new Entry(this, this.getNextBlank(), x, y)).send();
        }

        Bukkit.getPluginManager().callEvent(new TabCreateEvent(this));
    }

    public Entry getByPosition(int x, int y)
    {
        Iterator<Entry> iterator = this.entries.iterator();

        Entry tabEntry;

        do
        {
            if (!iterator.hasNext())
            {
                return null;
            }

            tabEntry = iterator.next();
        } while (tabEntry.x() != x || tabEntry.y() != y);

        return tabEntry;
    }

    public String getNextBlank()
    {
        Iterator<String> iterator = getAllBlanks().iterator();

        label23: while (iterator.hasNext())
        {
            String string = iterator.next();
            Iterator<Entry> iterator1 = this.entries.iterator();

            while (iterator1.hasNext())
            {
                Entry tabEntry = iterator1.next();

                if (tabEntry.text() != null && tabEntry.text().startsWith(string))
                {
                    continue label23;
                }
            }

            return string;
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
        Iterator<Tab> iterator = Tab.playerTabs.iterator();

        while (iterator.hasNext())
        {
            Tab playerTab = (Tab) iterator.next();

            if (playerTab.getPlayer().getName().equals(player.getName()))
            {
                return playerTab;
            }
        }

        return null;
    }

    public static Set<Tab> getPlayerTabs()
    {
        return Tab.playerTabs;
    }

    public Player getPlayer()
    {
        return this.player;
    }

    public Scoreboard getScoreboard()
    {
        return this.scoreboard;
    }

    public List<Entry> getEntries()
    {
        return this.entries;
    }
}
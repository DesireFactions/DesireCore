package com.desiremc.core.tablist;

import java.util.HashSet;
import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.Team;

import com.desiremc.core.tablist.events.TabDeleteEvent;

public class TabList implements Listener
{

    private static TabList instance;
    private TabListOptions options;

    public TabList()
    {
        this(TabListOptions.getDefaultOptions());
    }

    public TabList(TabListOptions options)
    {
        if (Bukkit.getMaxPlayers() < 60)
        {
            throw new NumberFormatException("Player limit must be at least 60!");
        }
        else
        {
            TabList.instance = this;
            this.options = options;
            for (Player p : Bukkit.getOnlinePlayers())
            {
                TabList.this.checkPlayer(p);
            }
        }
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event)
    {
        TabList.this.checkPlayer(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event)
    {
        Player player = event.getPlayer();
        Tab playerTab = Tab.getByPlayer(player);

        if (playerTab != null)
        {
            Iterator<Team> iterator = (new HashSet<>(playerTab.getScoreboard().getTeams())).iterator();

            while (iterator.hasNext())
            {
                Team team = iterator.next();

                team.unregister();
            }

            Tab.getPlayerTabs().remove(playerTab);
            Bukkit.getPluginManager().callEvent(new TabDeleteEvent(playerTab));
        }

    }

    private void checkPlayer(Player player)
    {
        Tab playerTab = Tab.getByPlayer(player);

        if (playerTab == null)
        {
            long time = System.currentTimeMillis();

            new Tab(player);

            if (this.options.sendCreationMessage())
            {
                player.sendMessage(ChatColor.GRAY + "We created your tab list in a time of " + (System.currentTimeMillis() - time) + " ms.");
            }
        }
        else
        {
            playerTab.clear();
        }

    }

    public static TabList getInstance()
    {
        return TabList.instance;
    }

    public TabListOptions getOptions()
    {
        return this.options;
    }
}

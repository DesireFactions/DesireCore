package com.desiremc.core.scoreboard;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.inventivetalent.tabapi.TabAPI;
import org.inventivetalent.tabapi.TabItem;

import com.desiremc.core.DesireCore;

public class Scoreboard implements Listener
{

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent e)
    {
        Player p = e.getPlayer();
        TabAPI.addItem(p, new TabItem[] { new TabItem(""), new TabItem(""), new TabItem(""), new TabItem(""), new TabItem("§l§3Location"), new TabItem(DesireCore.getCurrentServer()) });
    }

}

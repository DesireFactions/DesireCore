package com.desiremc.core.oldtablist.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.desiremc.core.oldtablist.Tab;

public class TabCreateEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private Tab playerTab;
    private Player player;

    public TabCreateEvent(Tab playerTab) {
        this.player = playerTab.getPlayer();
        this.playerTab = playerTab;
    }

    public HandlerList getHandlers() {
        return TabCreateEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return TabCreateEvent.handlers;
    }

    public Tab getPlayerTab() {
        return this.playerTab;
    }

    public Player getPlayer() {
        return this.player;
    }
}

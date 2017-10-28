package com.desiremc.core.tablist.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.desiremc.core.tablist.Tab;

public class TabDeleteEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private Tab playerTab;
    private Player player;

    public TabDeleteEvent(Tab playerTab) {
        this.player = playerTab.getPlayer();
        this.playerTab = playerTab;
    }

    public HandlerList getHandlers() {
        return TabDeleteEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return TabDeleteEvent.handlers;
    }

    public Tab getPlayerTab() {
        return this.playerTab;
    }

    public Player getPlayer() {
        return this.player;
    }
}

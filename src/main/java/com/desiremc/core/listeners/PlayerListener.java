package com.desiremc.core.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import com.desiremc.core.DesireCore;
import com.desiremc.core.events.PlayerBlockMoveEvent;
import com.desiremc.core.events.PlayerChunkMoveEvent;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;
import com.desiremc.core.session.SessionSetting;
import com.desiremc.core.staff.StaffHandler;
import com.desiremc.core.utils.BukkitUtils;

import net.md_5.bungee.api.ChatColor;

public class PlayerListener implements Listener
{
    private static final boolean DEBUG = false;

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onBlockChange(PlayerMoveEvent event)
    {
        PlayerBlockMoveEvent blockMoveEvent = null;
        if (BukkitUtils.differentChunk(event.getFrom(), event.getTo()))
        {
            blockMoveEvent = new PlayerChunkMoveEvent(event.getPlayer(), event.getFrom(), event.getTo());
        }
        else if (BukkitUtils.differentBlock(event.getFrom(), event.getTo()))
        {
            blockMoveEvent = new PlayerBlockMoveEvent(event.getPlayer(), event.getFrom(), event.getTo());
        }
        if (blockMoveEvent != null)
        {
            Bukkit.getPluginManager().callEvent(blockMoveEvent);
            if (blockMoveEvent.isCancelled())
            {
                event.setCancelled(true);
            }
            event.setFrom(blockMoveEvent.getFrom());
            event.setTo(blockMoveEvent.getTo());
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onFrozenPlayerMove(PlayerBlockMoveEvent event)
    {
        if (StaffHandler.getInstance().isFrozen(event.getPlayer()))
        {
            event.getPlayer().teleport(event.getFrom());
        }
    }

    @EventHandler
    public void onFrozenCommand(PlayerCommandPreprocessEvent event)
    {
        if (StaffHandler.getInstance().isFrozen(event.getPlayer()))
        {
            String command = event.getMessage();
            if (!command.startsWith("/msg") && !command.startsWith("/r"))
            {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onCPSInteract(PlayerInteractEvent event)
    {
        StaffHandler.getInstance().handleCPSTest(event);
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onChat(AsyncPlayerChatEvent event)
    {
        if (DEBUG)
        {
            System.out.println("PlayerListener.onChat() called.");
        }
        Player player = event.getPlayer();
        Session session = SessionHandler.getSession(player);

        if (StaffHandler.getInstance().inStaffChat(player))
        {
            event.setCancelled(true);
            String message = DesireCore.getLangHandler().renderMessageNoPrefix("staff.staff-chat-format", "{name}", player.getName(), "{message}", ChatColor.translateAlternateColorCodes('&', event.getMessage()));
            for (Session target : SessionHandler.getStaff())
            {
                target.getPlayer().sendMessage(message);
            }
            return;
        }

        if (StaffHandler.getInstance().chatDisabled() && !session.getRank().isStaff())
        {
            event.setCancelled(true);
            return;
        }

        for (Session s : SessionHandler.getStaff())
        {
            if (s.getRank().isStaff() && event.getMessage().contains(s.getName()) && s.getSetting(SessionSetting.MENTIONS))
            {
                s.getPlayer().playSound(s.getPlayer().getLocation(), Sound.LEVEL_UP, 1.0F, 1.0F);
            }
        }
    }

}

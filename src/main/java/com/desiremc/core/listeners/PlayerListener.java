package com.desiremc.core.listeners;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import com.desiremc.core.DesireCore;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;
import com.desiremc.core.session.SessionSetting;
import com.desiremc.core.staff.StaffHandler;
import com.desiremc.core.utils.StringUtils;

import net.md_5.bungee.api.ChatColor;

public class PlayerListener implements Listener
{
    private static final boolean DEBUG = false;

    @EventHandler
    public void onFrozenPlayerMove(PlayerMoveEvent event)
    {
        if (StaffHandler.getInstance().isFrozen(event.getPlayer()))
        {
            Player p = event.getPlayer();

            p.teleport(event.getFrom());
        }
    }

    @EventHandler
    public void onCPSInteract(PlayerInteractEvent event)
    {
        StaffHandler.getInstance().handleCPSTest(event);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onChat(AsyncPlayerChatEvent event)
    {
        if (DEBUG)
        {
            System.out.println("PlayerListener.onChat() called.");
        }
        Player p = event.getPlayer();
        Session session = SessionHandler.getSession(p);

        if (StaffHandler.getInstance().inStaffChat(p))
        {
            event.setCancelled(true);
            String message = DesireCore.getLangHandler().renderMessageNoPrefix("staff.staff-chat-format", "{name}", p.getName(), "{message}", ChatColor.translateAlternateColorCodes('&', event.getMessage()));
            for (Session target : SessionHandler.getInstance().getStaff())
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

        for (Session s : SessionHandler.getInstance().getStaff())
        {
            if (s.getRank().isStaff() && event.getMessage().contains(s.getName()) && s.getSetting(SessionSetting.MENTIONS))
            {
                s.getPlayer().playSound(s.getPlayer().getLocation(), Sound.LEVEL_UP, 1.0F, 1.0F);
            }
        }
    }

    @EventHandler
    public void onOreBreak(BlockBreakEvent event)
    {
        Player p = event.getPlayer();

        String name = StringUtils.capitalize(event.getBlock().getType().name().toLowerCase().replace("_", ""));

        if (!DesireCore.getConfigHandler().getStringList("xray-ores").contains(event.getBlock().getType().name()))
        {
            return;
        }

        Set<Block> vein = getVein(event.getBlock());

        for (Session session : SessionHandler.getInstance().getStaff())
        {
            if (session.getSetting(SessionSetting.FINDORE))
            {
                DesireCore.getLangHandler().sendRenderMessage(session, "alerts.xray.message", "{player}", p.getName(), "{count}", vein.size() + "", "{oreName}", name);
            }
        }
    }

    private Set<Block> getVein(Block block)
    {
        Set<Block> vein = new HashSet<>();
        vein.add(block);
        getVein(block, vein);
        return vein;
    }

    private void getVein(Block block, Set<Block> vein)
    {
        for (int i = -1; i < 2; i++)
        {
            for (int j = -1; j < 2; j++)
            {
                for (int k = -1; k < 2; k++)
                {
                    Block relative = block.getRelative(i, j, k);
                    if (!vein.contains(relative) && block.equals(relative) && (i != 0 || j != 0 || k != 0))
                    {
                        vein.add(relative);
                        getVein(relative, vein);
                    }
                }
            }
        }
    }
}

package com.desiremc.core.listeners;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import com.desiremc.core.DesireCore;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;
import com.desiremc.core.session.StaffHandler;

public class PlayerListener implements Listener
{
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

    @EventHandler
    public void onStaffChat(AsyncPlayerChatEvent event)
    {
        Player p = event.getPlayer();
        Session session = SessionHandler.getSession(p);

        if (StaffHandler.getInstance().inStaffChat(p))
        {
            event.setCancelled(true);
            for (UUID target : StaffHandler.getInstance().getAllInStaffChat())
            {
                String message = DesireCore.getLangHandler().renderMessage("staff.staff-chat-format", "{prefix}", session.getRank().getPrefix(), "{name}", p.getName(), "{message}", event.getMessage());
                Bukkit.getPlayer(target).sendMessage(message);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onMention(AsyncPlayerChatEvent event)
    {
        Session sender = SessionHandler.getSession(event.getPlayer());
        if (StaffHandler.getInstance().isChatEnabled() || sender.getRank().isStaff())
        {
            for (Player p : Bukkit.getOnlinePlayers())
            {
                Session session = SessionHandler.getSession(p);
                if (session.getRank().isStaff() && event.getMessage().contains(p.getName()) && session.getSettings().hasMentionsEnabled())
                {
                    p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
                }
            }
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event)
    {
        Session session = SessionHandler.getSession(event.getPlayer());
        if (!StaffHandler.getInstance().isChatEnabled())
        {
            if (!session.getRank().isStaff())
            {
                event.setCancelled(true);
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
            if (session.getSettings().hasXrayNotification())
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
                    if (!vein.contains(relative) &&
                            block.equals(relative) &&
                            (i != 0 || j != 0 || k != 0))
                    {
                        vein.add(relative);
                        getVein(relative, vein);
                    }
                }
            }
        }
    }
}

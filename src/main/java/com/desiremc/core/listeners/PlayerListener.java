package com.desiremc.core.listeners;

<<<<<<< HEAD
import com.desiremc.core.DesireCore;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;
import com.desiremc.core.staff.StaffHandler;
import com.desiremc.core.utils.StringUtils;
import net.md_5.bungee.api.ChatColor;
=======
>>>>>>> origin/1.7.10
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

<<<<<<< HEAD
import java.util.HashSet;
import java.util.Set;
=======
import com.desiremc.core.DesireCore;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;
import com.desiremc.core.session.SessionSetting;
import com.desiremc.core.staff.StaffHandler;

import net.md_5.bungee.api.ChatColor;
>>>>>>> origin/1.7.10

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
        Player player = event.getPlayer();
        Session session = SessionHandler.getSession(player);

        if (StaffHandler.getInstance().inStaffChat(player))
        {
            event.setCancelled(true);
            String message = DesireCore.getLangHandler().renderMessageNoPrefix("staff.staff-chat-format", "{name}", player.getName(), "{message}", ChatColor.translateAlternateColorCodes('&', event.getMessage()));
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

}

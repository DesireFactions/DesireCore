package com.desiremc.core.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.desiremc.core.DesireCore;
import com.desiremc.core.punishment.Punishment;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;
import com.desiremc.core.staff.StaffHandler;
import com.desiremc.core.utils.DateUtils;
import com.desiremc.core.utils.PlayerUtils;

public class ConnectionListener implements Listener
{

    private static final boolean DEBUG = false;
    
    @EventHandler
    public void onLogin(final PlayerLoginEvent event)
    {
        if (DEBUG)
        {
            System.out.println("onLogin(PlayerLoginEvent) called in ConnectionListener.");
        }
        Punishment p = SessionHandler.getBan(event.getPlayer().getUniqueId());
        if (p != null)
        {
            event.disallow(PlayerLoginEvent.Result.KICK_BANNED,
                    (DesireCore.getLangHandler().getPrefix() + "\n" + "\n" + "&c&lYou are banned from the network!\n"
                            + "\n" + "&cReason: &7{reason}\n" + "&cUntil: &7{until}\n" + "&cBanned By: &7{issuer}\n"
                            + "\n" + "&7Visit &ehttps://desirehcf.net/rules&7 for our terms and rules")
                                    .replace("{reason}", p.getReason())
                                    .replace("{until}", DateUtils.formatDateDiff(p.getExpirationTime()))
                                    .replace("{issuer}", PlayerUtils.getName(p.getIssuer()))
                                    .replace("&", "§"));
            return;
        }

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event)
    {
        if (DEBUG)
        {
            System.out.println("onJoin(PlayerJoinEvent) called in ConnectionListener.");
        }
        Player player = event.getPlayer();
        PlayerUtils.addPlayer(player);
        
        String ip = player.getAddress().getAddress().getHostAddress();
        Session session = SessionHandler.initializeSession(event.getPlayer().getUniqueId(), true);

        if (!session.getIp().equalsIgnoreCase(ip))
        {
            session.getIpList().add(ip);
            session.setIp(ip);
        }

        if (!session.getName().equalsIgnoreCase(player.getName()))
        {
            session.getNameList().add(player.getName());
            session.setName(player.getName());
        }
        
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onLogout(PlayerQuitEvent e)
    {
        if (DEBUG)
        {
            System.out.println("onLougout(PlayerQuitEvent) called in ConnectionListener.");
        }

        Player player = e.getPlayer();
        Session session = SessionHandler.getSession(player);
        SessionHandler.endSession(session);

        StaffHandler.getInstance().disableStaffMode(player);
        StaffHandler.getInstance().unFreeze(player);
        e.setQuitMessage(DesireCore.getLangHandler().renderMessage("leave.message", "{player}", player.getName()));
        
        PlayerUtils.removePlayer(player);
    }
}

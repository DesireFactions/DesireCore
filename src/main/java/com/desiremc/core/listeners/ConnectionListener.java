package com.desiremc.core.listeners;

import org.bukkit.ChatColor;
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
import com.desiremc.core.session.StaffHandler;
import com.desiremc.core.utils.DateUtils;
import com.desiremc.core.utils.PlayerUtils;

public class ConnectionListener implements Listener
{

    @EventHandler
    public void onLogin(final PlayerLoginEvent event)
    {
        if (DesireCore.DEBUG)
        {
            System.out.println("onLogin(PlayerLoginEvent) called in ConnectionListener.");
        }
        Session session = SessionHandler.initializeSession(event.getPlayer().getUniqueId(), false);
        Punishment p;
        if ((p = session.isBanned()) != null)
        {

            event.disallow(PlayerLoginEvent.Result.KICK_BANNED,
                    (DesireCore.getLangHandler().getPrefix() + "\n" + "\n" + "&c&lYou are banned from the network!\n" + "\n" + "&cReason: &7{reason}\n" + "&cUntil: &7{until}\n" + "&cBanned By: &7{issuer}\n" + "\n" + "&7Visit &ehttps://desirehcf.net/rules&7 for our terms and rules").replace("{reason}", p.getReason())
                            .replace("{until}", DateUtils.formatDateDiff(p.getExpirationTime())).replace("{issuer}", PlayerUtils.getName(p.getIssuer()).replace("&", "§")));
            return;
        }

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event)
    {
        if (DesireCore.DEBUG)
        {
            System.out.println("onJoin(PlayerJoinEvent) called in ConnectionListener.");
        }
        Player player = event.getPlayer();
        String ip = player.getAddress().getHostName();
        Session session = SessionHandler.initializeSession(event.getPlayer().getUniqueId(), true);
        boolean noColor = session.getRank().getId() == 1;
        boolean justColor = session.getRank().getId() == 2;
        event.getPlayer().setPlayerListName(noColor ? ChatColor.GRAY + event.getPlayer().getName() : justColor ? session.getRank().getMain() + event.getPlayer().getName() : session.getRank().getPrefix() + " " + ChatColor.GRAY + event.getPlayer().getName());
        
        if(!session.getIp().equalsIgnoreCase(ip))
        {
            session.getIpList().add(ip);
            session.setIp(ip);
        }

        if(!session.getName().equalsIgnoreCase(player.getName()))
        {
            session.getNameList().add(player.getName());
            session.setName(player.getName());
        }
    }

    @EventHandler
    public void onLogout(PlayerQuitEvent e)
    {
        Session session = SessionHandler.getSession(e.getPlayer());
        session.setTotalPlayed(session.getTotalPlayed() + System.currentTimeMillis() - session.getLastLogin());
        session.setLastLogin(System.currentTimeMillis());
        SessionHandler.endSession(session);

        StaffHandler.getInstance().disableStaffMode(e.getPlayer());
        StaffHandler.getInstance().unfreezePlayer(e.getPlayer());
        e.setQuitMessage(DesireCore.getLangHandler().getString("leave.message").replace("{player}", e.getPlayer().getName()));
    }

}

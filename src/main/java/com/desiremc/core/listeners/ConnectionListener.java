package com.desiremc.core.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.desiremc.core.DesireCore;
import com.desiremc.core.punishment.Punishment;
import com.desiremc.core.punishment.Punishment.Type;
import com.desiremc.core.punishment.PunishmentHandler;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;
import com.desiremc.core.staff.StaffHandler;
import com.desiremc.core.utils.DateUtils;
import com.desiremc.core.utils.PlayerUtils;

public class ConnectionListener implements Listener
{

    @EventHandler
    public void onLogin(final AsyncPlayerPreLoginEvent event)
    {
        String ip = event.getAddress().getHostAddress();

        if (PunishmentHandler.getInstance().getAllIpBans().contains(ip))
        {
            Punishment ipban = PunishmentHandler.getInstance().getPunishment(ip);

            event.disallow(Result.KICK_BANNED,
                    ("\n" + "&c&lYour IP is permanently banned from" +
                            " the network!\n"
                            + "&cReason: &7{reason}\n" + "&cBanned By: &7{issuer}\n"
                            + "&7Visit &ehttps://desirehcf.com/rules&7 for our terms and rules")
                                    .replace("{reason}", ipban.getReason())
                                    .replace("{issuer}", PlayerUtils.getName(ipban.getIssuer()))
                                    .replace("&", "§"));
            return;
        }

        Punishment ban = PunishmentHandler.getInstance().getPunishment(event.getUniqueId(), Type.BAN);
        if (ban != null && !ban.isRepealed())
        {
            if (ban.isPermanent())
            {
                event.disallow(Result.KICK_BANNED,
                        (DesireCore.getLangHandler().getPrefix() + "\n" + "\n" + "&c&lYou are permanently banned from" +
                                " the network!\n"
                                + "&cReason: &7{reason}\n" + "&cBanned By: &7{issuer}\n"
                                + "&7Visit &ehttps://desirehcf.com/rules&7 for our terms and rules")
                                        .replace("{reason}", ban.getReason())
                                        .replace("{issuer}", PlayerUtils.getName(ban.getIssuer()))
                                        .replace("&", "§"));
            }
            else
            {
                event.disallow(Result.KICK_BANNED,
                        (DesireCore.getLangHandler().getPrefix() + "\n" + "\n" + "&c&lYou are banned from the " +
                                "network!\n"
                                + "&cReason: &7{reason}\n" + "&cUntil: &7{until}\n" + "&cBanned By: &7{issuer}\n"
                                + "&7Visit &ehttps://desirehcf.com/rules&7 for our terms and rules")
                                        .replace("{reason}", ban.getReason())
                                        .replace("{until}", DateUtils.formatDateDiff(ban.getExpirationTime()))
                                        .replace("{issuer}", PlayerUtils.getName(ban.getIssuer()))
                                        .replace("&", "§"));
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent event)
    {
        event.setJoinMessage(null);
        Player player = event.getPlayer();
        PlayerUtils.addPlayer(player);

        SessionHandler.initializeSession(player);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onLogout(PlayerQuitEvent e)
    {
        e.setQuitMessage(null);

        Player player = e.getPlayer();
        Session session = SessionHandler.getSession(player);
        SessionHandler.endSession(session);

        StaffHandler.getInstance().disableStaffMode(player);
        PlayerUtils.removePlayer(player);
    }


    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event)
    {
        if (!event.getResult().equals(PlayerLoginEvent.Result.KICK_FULL))
        {
            return;
        }

        Player player = event.getPlayer();

        if (player.hasPermission("desirehcf.joinfull"))
        {
            event.allow();
        }
    }
}

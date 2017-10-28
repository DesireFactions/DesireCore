package com.desiremc.core.listeners;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import com.desiremc.core.DesireCore;
import com.desiremc.core.fanciful.FancyMessage;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;
import com.warrenstrange.googleauth.GoogleAuthenticator;

public class AuthListener implements Listener
{

    private static GoogleAuthenticator auth = new GoogleAuthenticator();
    public static List<UUID> authBlocked = new ArrayList<>();

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent event)
    {
        if (DesireCore.DEBUG)
        {
            System.out.println("onJoin(PlayerJoinEvent) called in AuthListener.");
        }
        Player p = event.getPlayer();
        Session session = SessionHandler.getSession(p.getUniqueId());

        if (!session.getRank().isStaff() || session.getName().equals("Doctor_Zee") || session.getName().equals("MewtwoUsedSplash") || session.getName().equals("Dapkin") || session.getName().equals("Swatted"))
            return;

        if (session.getIpList().contains(event.getPlayer().getAddress().getAddress().getHostAddress()))
        {
            return;
        }

        if (session.getAuthkey() == null || session.getAuthkey().equalsIgnoreCase(""))
        {
            session.setAuthKey(auth.createCredentials().getKey());

            FancyMessage message = new FancyMessage(DesireCore.getLangHandler().getPrefix() + "Your Google Auth code " +
                    "is ")
                            .color(ChatColor.WHITE)
                            .then(session.getAuthkey())
                            .link(getQRUrl(session.getName(), session.getAuthkey()))
                            .color(ChatColor.RED)
                            .then(". Click it for a QR code.")
                            .color(ChatColor.WHITE);

            message.send(p);

            forceAuth(session);
        }
        else
        {
            if (!session.getIp().equalsIgnoreCase(p.getAddress().getHostName()))
            {
                forceAuth(session);
                return;
            }
        }

        authBlocked.add(p.getUniqueId());

        DesireCore.getLangHandler().sendRenderMessage(session, "auth.must-login");
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event)
    {
        Player player = event.getPlayer();
        if (authBlocked.contains(player.getUniqueId()))
        {
            event.setTo(event.getFrom());
        }
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event)
    {
        Player player = event.getPlayer();
        if (authBlocked.contains(player.getUniqueId()))
        {
            if (!event.getMessage().contains("login"))
            {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(AsyncPlayerChatEvent event)
    {
        Player player = event.getPlayer();
        if (authBlocked.contains(player.getUniqueId()))
        {
            event.setCancelled(true);
            System.out.println("The event was cancelled in AuthListener");
        }
        System.out.println("The event was not cancelled in AuthListener");
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event)
    {
        Player player = event.getPlayer();
        if (authBlocked.contains(player.getUniqueId()))
        {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event)
    {
        Player player = event.getPlayer();
        if (authBlocked.contains(player.getUniqueId()))
        {
            event.setCancelled(true);
        }
    }

    private void forceAuth(Session session)
    {
        authBlocked.add(session.getUniqueId());

        DesireCore.getLangHandler().sendRenderMessage(session, "auth.must-login");
    }

    private static final String googleFormat = "https://www.google.com/chart?chs=200x200&chld=M%%7C0&cht=qr&chl=" +
            "otpauth://totp/%s@%s%%3Fsecret%%3D%s";

    private String getQRUrl(String username, String secret)
    {
        if (secret == null)
        {
            return null;
        }
        try
        {
            return String.format(googleFormat, username, URLEncoder.encode("144.217.11.123", "UTF-8"), secret);
        }
        catch (UnsupportedEncodingException ex)
        {
            return null;
        }
    }

}

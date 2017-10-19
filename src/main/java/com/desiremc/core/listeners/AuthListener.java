package com.desiremc.core.listeners;

import com.desiremc.core.DesireCore;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AuthListener implements Listener
{

    public static List<UUID> authBlocked = new ArrayList<>();

    @EventHandler
    public void onJoin(PlayerJoinEvent event)
    {
        Player p = event.getPlayer();
        Session session = SessionHandler.getSession(p.getUniqueId());

        if (!session.getRank().isStaff()) return;

        if (session.getAuthkey().equalsIgnoreCase("") || session.getAuthkey() == null)
        {
            GoogleAuthenticator auth = new GoogleAuthenticator();
            GoogleAuthenticatorKey key = auth.createCredentials();

            session.setAuthKey(key.getKey());

            DesireCore.getLangHandler().sendRenderMessage(session, "auth.setup", "{code}", key.getKey());
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
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event)
    {
        Player player = event.getPlayer();
        if (authBlocked.contains(player.getUniqueId()))
        {
            if(!event.getMessage().contains("login"))
            {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event)
    {
        Player player = event.getPlayer();
        if (authBlocked.contains(player.getUniqueId()))
        {
            event.setCancelled(true);
        }
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
}

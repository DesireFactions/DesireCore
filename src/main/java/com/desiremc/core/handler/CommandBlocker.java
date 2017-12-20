package com.desiremc.core.handler;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.FieldAccessException;
import com.desiremc.core.DesireCore;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.Arrays;
import java.util.List;

public class CommandBlocker implements Listener
{

    private List<String> blocked;

    public CommandBlocker()
    {
        blocked = Arrays.asList("pl", "plugins", "ver", "version", "about", "?");

        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(DesireCore.getInstance(), ListenerPriority.NORMAL, new PacketType[] {PacketType.Play.Client.TAB_COMPLETE})
        {
            public void onPacketReceiving(PacketEvent event)
            {
                if (event.getPacketType() == PacketType.Play.Client.TAB_COMPLETE)
                {
                    try
                    {
                        PacketContainer packet = event.getPacket();
                        String message = (packet.getSpecificModifier(String.class).read(0)).toLowerCase();
                        if (((message.startsWith("/")) && (!message.contains(" "))) || ((message.startsWith("/ver")) && (!message.contains("  "))) || ((message.startsWith("/version")) && (!message.contains("  "))) || ((message.startsWith("/?")) && (!message.contains("  "))) || ((message.startsWith("/about")) && (!message.contains("  "))) || ((message
                                .startsWith("/help")) && (!message.contains("  "))))
                        {
                            event.setCancelled(true);
                        }
                    } catch (FieldAccessException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCommandPreProcess(PlayerCommandPreprocessEvent event)
    {
        Player player = event.getPlayer();
        Session session = SessionHandler.getSession(player);
        String[] msg = event.getMessage().split(" ");

        for (String s : blocked)
        {
            if (msg[0].equalsIgnoreCase("/" + s))
            {
                DesireCore.getLangHandler().sendRenderMessage(session, "no_permissions");
                event.setCancelled(true);
            }
        }
    }
}

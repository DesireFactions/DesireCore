package com.desiremc.core.tablistfive;

import org.bukkit.entity.Player;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.desiremc.core.DesireCore;

public class PacketListener extends PacketAdapter
{

    public PacketListener()
    {
        super(DesireCore.getInstance(), PacketType.Play.Server.PLAYER_INFO, PacketType.Play.Server.LOGIN);
        TabAPI.getProtocolManager().addPacketListener(this);
    }

    @Override
    public void onPacketSending(PacketEvent event)
    {
        if (event.isCancelled())
        {
            return;
        }
        PacketContainer packet = event.getPacket();
        Player player = event.getPlayer();

        if (event.getPacketType() == PacketType.Play.Server.PLAYER_INFO)
        {
            int ping = packet.getIntegers().read(0);
            if (ping != -1)
            {
                try
                {
                    ping = packet.getIntegers().read(2);
                }
                catch (Exception ex)
                {
                }
            }
            if (ping == -1)
            {
                TabList list = TabAPI.getPlayerTabList(player);
                ping = list.getDefaultPing();
                String name = packet.getStrings().read(0);

                for (int i = 0; i < 60; i++)
                {
                    TabSlot slot = list.getSlot(i);
                    if (slot != null && slot.getName().equals(name))
                    {
                        ping = slot.getPing();
                        break;
                    }
                }

                try
                {
                    packet.getIntegers().write(2, ping);
                }
                catch (Exception ex)
                {
                    packet.getIntegers().write(0, ping);
                }
                event.setPacket(packet);
                return;
            }
            else
            {
                event.setCancelled(true);
            }
        }

    }

}
package com.desiremc.core.tablist;

import org.bukkit.entity.Player;

import com.comphenix.protocol.Packets;
import com.comphenix.protocol.events.ConnectionSide;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.desiremc.core.DesireCore;

@SuppressWarnings("deprecation")
public class PacketListener extends PacketAdapter
{

    public PacketListener()
    {
        super(DesireCore.getInstance(), ConnectionSide.SERVER_SIDE, Packets.Server.PLAYER_INFO, Packets.Server.LOGIN);
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

        if (TabAPI.getProtocolManager().getProtocolVersion(player) >= 47)
        {
            return;
        }

        if (event.getPacketID() == Packets.Server.PLAYER_INFO)
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

        if (event.getPacketID() == Packets.Server.LOGIN)
        {
            packet.getIntegers().write(2, 60); //Force maximum TabList size
            event.setPacket(packet);
        }
    }

}
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
        super(DesireCore.getInstance(), PacketType.Play.Server.PLAYER_INFO);
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
            int ping = packet.getIntegers().read(TabList.PACKET_INFO_PING);
            if (ping == -1)
            {
                TabList list = TabAPI.getPlayerTabList(player);
                ping = list.getDefaultPing();
                String name = packet.getStrings().read(0);

                for (TabSlot slot : list.getSlots())
                {
                    if (slot != null && slot.getName() != null && slot.getName().equals(name))
                    {
                        ping = slot.getPing();
                        break;
                    }
                }

                packet.getIntegers().write(TabList.PACKET_INFO_PING, ping);

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
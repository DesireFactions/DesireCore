package com.desiremc.core.utils;

import org.bukkit.entity.Player;

import com.desiremc.core.DesireCore;
import com.desiremc.core.bungee.StatusManager;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

public class BungeeUtils
{

    public static void sendToHub(Player player)
    {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(StatusManager.getLeastPopulous());
        player.sendPluginMessage(DesireCore.getInstance(), "BungeeCord", out.toByteArray());
    }
    
}

package com.desiremc.core.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PlayerUtils
{

    @SuppressWarnings("deprecation")
    public static boolean hasPlayed(String name)
    {
        return Bukkit.getOfflinePlayer(name) != null;
    }

    public static UUID getUUIDFromName(String name)
    {
        if (!hasPlayed(name))
        {
            return null;
        }
        Session s = SessionHandler.getInstance().findOne("name", name);
        if (s == null)
        {
            return null;
        }
        return s.getUniqueId();
    }

    /**
     * Get a player's name from their UUID.
     * 
     * @param uuid the uuid of the player in question.
     * @return the last seen username of the player.
     */
    public static String getName(UUID uuid)
    {
        OfflinePlayer op = Bukkit.getOfflinePlayer(uuid);
        return op == null ? null : op.getName();
    }

    public static List<Player> getPlayersInRange(Player player, int range)
    {
        List<Player> inRange = new ArrayList<>();

        for (Player p : Bukkit.getOnlinePlayers())
        {
            if (player.getLocation().distanceSquared(p.getLocation()) <= (range * range))
            {
                inRange.add(player);
            }
        }

        return inRange;
    }

    public static boolean hasEffect(Player player, PotionEffectType type)
    {
        for (PotionEffect effect : player.getActivePotionEffects())
        {
            if (effect.getType().equals(type))
                return true;
        }
        return false;
    }

}

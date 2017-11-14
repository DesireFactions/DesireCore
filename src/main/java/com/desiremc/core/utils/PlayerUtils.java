package com.desiremc.core.utils;

import com.desiremc.core.DesireCore;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class PlayerUtils
{

    private static final boolean DEBUG = false;

    private static HashMap<UUID, Player> playerCache = new HashMap<>();

    public static Player getPlayer(UUID uuid)
    {
        if (uuid == null)
        {
            if (DEBUG)
            {
                System.out.println("getPlayer(UUID) called with null UUID.");
            }
            return null;
        }
        if (DEBUG)
        {
            System.out.println("getPlayer(UUID) called with value " + uuid.toString() + ".");
        }
        return playerCache.get(uuid);
    }

    public static void addPlayer(Player player)
    {
        if (DEBUG)
        {
            System.out.println("addPlayer(Player) called with player " + player.getName() + ".");
        }
        playerCache.put(player.getUniqueId(), player);
    }

    public static void removePlayer(Player player)
    {
        if (DEBUG)
        {
            System.out.println("removePlayer(Player) called with player " + player.getName() + ".");
        }
        playerCache.remove(player.getUniqueId());
    }

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

    public static UUID getUUIDFromSender(CommandSender sender)
    {
        if (sender instanceof Player)
        {
            return ((Player) sender).getUniqueId();
        }
        else
        {
            return DesireCore.getConsoleUUID();
        }
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

        if (uuid.equals(DesireCore.getConsoleUUID()))
        {
            return "CONSOLE";
        }
        else if (op != null)
        {
            return op.getName();
        }

        return null;
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

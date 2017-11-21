package com.desiremc.core.utils;

import java.text.DecimalFormat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;

public class Utils
{
    
    private static DecimalFormat format = new DecimalFormat("#.###");
    
    public static String chat(String s)
    {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static String toString(Location l)
    {
        return l.getWorld().getName() + "," + format.format(l.getX()) + "," + format.format(l.getY()) + "," + format.format(l.getZ());
    }

    public static Location toLocation(String s)
    {
        String[] sp = s.split(",");
        return new Location(Bukkit.getWorld(sp[0]), Double.parseDouble(sp[1]), Double.parseDouble(sp[2]), Double.parseDouble(sp[3]));
    }

}

package com.desiremc.core.utils;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;

import com.desiremc.core.DesireCore;

public class BukkitUtils
{

    private static final BlockingDeque<Runnable> syncMethods = new LinkedBlockingDeque<>();

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

    /**
     * Check if the two given locations are different blocks.
     * 
     * @param loc1 the first location.
     * @param loc2 the second location.
     * @return {@code true} if the locations are different blocks.<br>
     *         {@code false otherwise}
     */
    public static boolean differentBlock(Location loc1, Location loc2)
    {
        return loc1.getBlockX() != loc2.getBlockX() || loc1.getBlockY() != loc2.getBlockY() || loc1.getBlockZ() != loc2.getBlockZ() || loc1.getWorld() != loc2.getWorld();
    }

    /**
     * Check if the two given locations are different chunks.
     * 
     * @param loc1 the first location.
     * @param loc2 the second location.
     * @return {@code true} if the locations are different chunks.<br>
     *         {@code false otherwise}
     */
    public static boolean differentChunk(Location loc1, Location loc2)
    {
        return loc1.getBlockX() >> 4 != loc2.getBlockX() >> 4 || loc1.getBlockZ() >> 4 != loc2.getBlockZ() >> 4 || loc1.getWorld() != loc2.getWorld();
    }

    private static int ticks = 0;
    private static long startTime = System.currentTimeMillis();

    private static double tps = 20;

    public static void initialize()
    {
        Bukkit.getScheduler().runTaskTimer(DesireCore.getInstance(), new Runnable()
        {
            @Override
            public void run()
            {
                ticks++;
                if (ticks == 20)
                {
                    tps = ((System.currentTimeMillis() - startTime) / 1000.0) * 20.0;
                    startTime = System.currentTimeMillis();
                    ticks = 0;
                }
                List<Runnable> tasks = new LinkedList<>();
                syncMethods.drainTo(tasks);
                tasks.forEach(task -> task.run());
            }
        }, 0, 0);
    }

    public static double getTPS()
    {
        return tps;
    }

    public static synchronized void addSyncMethod(Runnable runnable)
    {
        syncMethods.add(runnable);
    }

}

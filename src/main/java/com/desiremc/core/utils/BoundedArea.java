package com.desiremc.core.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Transient;

@Embedded
public class BoundedArea
{

    private int minX;
    private int minZ;
    private int maxX;
    private int maxZ;

    private String world;

    @Transient
    private World parsedWorld;

    /**
     * @return the minX
     */
    public int getMinX()
    {
        return minX;
    }

    /**
     * @param minX the minX to set
     */
    public void setMinX(int minX)
    {
        this.minX = minX;
    }

    /**
     * @return the minZ
     */
    public int getMinZ()
    {
        return minZ;
    }

    /**
     * @param minZ the minZ to set
     */
    public void setMinZ(int minZ)
    {
        this.minZ = minZ;
    }

    /**
     * @return the maxX
     */
    public int getMaxX()
    {
        return maxX;
    }

    /**
     * @param maxX the maxX to set
     */
    public void setMaxX(int maxX)
    {
        this.maxX = maxX;
    }

    /**
     * @return the maxZ
     */
    public int getMaxZ()
    {
        return maxZ;
    }

    /**
     * @param maxZ the maxZ to set
     */
    public void setMaxZ(int maxZ)
    {
        this.maxZ = maxZ;
    }

    /**
     * Check if the given location is within the bounded area.
     * 
     * @param location the location to check.
     * @return {@code true} if this location is within the bounded area.<br>
     *         {@code false} otherwise.
     */
    public boolean contains(Location location)
    {
        if (location.getWorld() != getWorld())
        {
            return false;
        }
        if (location.getX() >= getMinX() && location.getX() <= getMaxX() && location.getZ() >= getMinZ() && location.getZ() <= getMaxZ())
        {
            return true;
        }
        return false;
    }

    /**
     * @return the world
     */
    public World getWorld()
    {
        if (parsedWorld == null)
        {
            parsedWorld = Bukkit.getWorld(world);
        }
        return parsedWorld;
    }

    /**
     * @param world the world to set
     */
    public void setWorld(World parsedWorld)
    {
        this.parsedWorld = parsedWorld;
        this.world = parsedWorld.getName();
    }

}

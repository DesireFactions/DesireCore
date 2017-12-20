package com.desiremc.core.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Transient;

import com.github.davidmoten.rtree.geometry.Geometry;
import com.github.davidmoten.rtree.geometry.Rectangle;

@Embedded
public class BoundedArea implements Rectangle
{

    private int x1;
    private int z1;
    private int x2;
    private int z2;

    private String world;

    @Transient
    private World parsedWorld;

    public BoundedArea(int x1, int x2, int z1, int z2)
    {
        this.x1 = Math.min(x1, x2);
        this.x2 = Math.max(x1, x2);
        this.z1 = Math.min(z1, z2);
        this.z2 = Math.max(z1, z2);
    }

    public BoundedArea(BlockColumn pointOne, BlockColumn pointTwo)
    {
        if (pointOne == null || pointTwo == null)
        {
            throw new IllegalArgumentException("Neither point can be null.");
        }
        this.x1 = Math.min(pointOne.getX(), pointTwo.getX());
        this.x2 = Math.max(pointOne.getX(), pointTwo.getX());
        this.z1 = Math.min(pointOne.getZ(), pointTwo.getZ());
        this.z2 = Math.max(pointOne.getZ(), pointTwo.getZ());
    }

    public BoundedArea(float min, float min2, float max, float max2)
    {
        this((int) min, (int) min2, (int) max, (int) max2);
    }

    /**
     * @return the minX
     */
    public int getMinX()
    {
        return x1;
    }

    /**
     * @param minX the minX to set
     */
    public void setMinX(int minX)
    {
        this.x1 = minX;
    }

    /**
     * @return the minZ
     */
    public int getMinZ()
    {
        return z1;
    }

    /**
     * @param minZ the minZ to set
     */
    public void setMinZ(int minZ)
    {
        this.z1 = minZ;
    }

    /**
     * @return the maxX
     */
    public int getMaxX()
    {
        return x2;
    }

    /**
     * @param maxX the maxX to set
     */
    public void setMaxX(int maxX)
    {
        this.x2 = maxX;
    }

    /**
     * @return the maxZ
     */
    public int getMaxZ()
    {
        return z2;
    }

    /**
     * @param maxZ the maxZ to set
     */
    public void setMaxZ(int maxZ)
    {
        this.z2 = maxZ;
    }

    /**
     * Check if the given location is within the bounded area.
     * 
     * @param location the location to check.
     * @return {@code true} if this location is within the bounded area.
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
     * Check if the given block column is within the bounded area.
     * 
     * @param location the block column to check.
     * @return {@code true} if this block column is within the bounded area.
     */
    public boolean contains(BlockColumn blockColumn)
    {
        if (blockColumn.getWorld() != getWorld())
        {
            return false;
        }
        if (blockColumn.getX() >= getMinX() && blockColumn.getX() <= getMaxX() && blockColumn.getZ() >= getMinZ() && blockColumn.getZ() <= getMaxZ())
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

    @Override
    public double distance(Rectangle r)
    {
        return GeometryUtils.distance(x1(), y1(), x2(), y2(), r.x1(), r.y1(), r.x2(), r.y2());
    }

    @Override
    public boolean intersects(Rectangle r)
    {
        return GeometryUtils.intersects(x1(), y1(), x2(), y2(), r.x1(), r.y1(), r.x2(), r.y2());
    }

    @Override
    public Rectangle mbr()
    {
        return this;
    }

    @Override
    public Geometry geometry()
    {
        return this;
    }

    @Override
    public Rectangle add(Rectangle r)
    {
        return new BoundedArea(Math.min(x1(), r.x1()), Math.min(y1(), r.y1()), Math.max(x2(), r.x2()), Math.max(y2(), r.y2()));
    }

    @Override
    public float area()
    {
        return (x2() - x1()) * (y2() - y1());
    }

    @Override
    public boolean contains(double x, double y)
    {
        return x >= x1() && x <= x2() && y >= y1() && y <= y2();
    }

    @Override
    public float intersectionArea(Rectangle r)
    {
        if (!intersects(r))
            return 0;
        else
            return GeometryUtils.create(Math.max(x1(), r.x1()), Math.max(y1(), r.y1()), Math.min(x2, r.x2()), Math.min(y2(), r.y2())).area();
    }

    @Override
    public float perimeter()
    {
        return 2 * (x2() - x1()) + 2 * (y2() - y1());
    }

    @Override
    public float x1()
    {
        return x1;
    }

    @Override
    public float x2()
    {
        return x2;
    }

    @Override
    public float y1()
    {
        return z1;
    }

    @Override
    public float y2()
    {
        return z2;
    }

    public float getLength()
    {
        return y2() - y1() + 1;
    }

    public float getWidth()
    {
        return x2() - x1() + 1;
    }

}

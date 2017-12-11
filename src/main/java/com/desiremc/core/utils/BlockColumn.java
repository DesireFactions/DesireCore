package com.desiremc.core.utils;

import org.bukkit.World;
import org.mongodb.morphia.annotations.Embedded;

/**
 * Used to wrap a column of blocks. It is an easy to use wrapper for whenever a chunk is not wanted. This can be easily
 * stored in the database as it has the {@link Embedded} attribute.
 * 
 * @author Michael Ziluck
 */
@Embedded
public class BlockColumn
{

    private int x;

    private int z;

    private World world;

    /**
     * @param x
     * @param z
     * @param world
     */
    public BlockColumn(int x, int z, World world)
    {
        super();
        this.x = x;
        this.z = z;
        this.world = world;
    }

    /**
     * @return the x
     */
    public int getX()
    {
        return x;
    }

    /**
     * @param x the x to set
     */
    public void setX(int x)
    {
        this.x = x;
    }

    /**
     * @return the z
     */
    public int getZ()
    {
        return z;
    }

    /**
     * @param z the z to set
     */
    public void setZ(int z)
    {
        this.z = z;
    }

    /**
     * @return the world
     */
    public World getWorld()
    {
        return world;
    }

    /**
     * @param world the world to set
     */
    public void setWorld(World world)
    {
        this.world = world;
    }

    @Override
    public BlockColumn clone()
    {
        return new BlockColumn(x, z, world);
    }

}

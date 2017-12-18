package com.desiremc.core.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.World;
import org.bukkit.block.Block;
import org.mongodb.morphia.annotations.Embedded;

import com.github.davidmoten.rtree.geometry.Geometry;
import com.github.davidmoten.rtree.geometry.Rectangle;

/**
 * Used to wrap a column of blocks. It is an easy to use wrapper for whenever a chunk is not wanted. This can be easily
 * stored in the database as it has the {@link Embedded} attribute.
 * 
 * @author Michael Ziluck
 */
@Embedded
public class BlockColumn implements Rectangle
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

    /**
     * @return all blocks in the column.
     */
    public List<Block> getAllBlocks()
    {
        List<Block> blocks = new ArrayList<>(257);
        for (int i = 0; i < 256; i++)
        {
            blocks.add(world.getBlockAt(x, i, z));
        }
        return blocks;
    }

    @Override
    public BlockColumn clone()
    {
        return new BlockColumn(x, z, world);
    }

    @Override
    public double distance(Rectangle r)
    {
        return GeometryUtils.distanceSquared(x, z, x, z, r.x1(), r.y1(), r.x2(), r.y2());
    }

    @Override
    public boolean intersects(Rectangle r)
    {
        return r.x1() <= x && x <= r.x2() && r.y1() <= z && z <= r.y2();
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
        return GeometryUtils.create(Math.min(x, r.x1()), Math.min(z, r.y1()), Math.max(x, r.x2()), Math.max(z, r.y2()));
    }

    @Override
    public float area()
    {
        return 0;
    }

    @Override
    public boolean contains(double x, double y)
    {
        return this.x == x && this.z == y;
    }

    @Override
    public float intersectionArea(Rectangle r)
    {
        return 0;
    }

    @Override
    public float perimeter()
    {
        return 0;
    }

    @Override
    public float x1()
    {
        return x;
    }

    @Override
    public float x2()
    {
        return x;
    }

    @Override
    public float y1()
    {
        return z;
    }

    @Override
    public float y2()
    {
        return z;
    }

}

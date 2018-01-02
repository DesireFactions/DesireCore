package com.desiremc.core.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Transient;

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

    private String world;

    @Transient
    private World parsedWorld;

    /**
     * @param x
     * @param z
     * @param world
     */
    public BlockColumn(int x, int z, World world)
    {
        this.x = x;
        this.z = z;
        this.parsedWorld = world;
        this.world = world.getName();
    }

    /**
     * @param block the block
     */
    public BlockColumn(Block block)
    {
        this.x = block.getX();
        this.z = block.getZ();
        this.parsedWorld = block.getWorld();
        this.world = block.getWorld().getName();
    }

    public BlockColumn(Location location)
    {
        this.x = location.getBlockX();
        this.z = location.getBlockZ();
        this.parsedWorld = location.getWorld();
        this.world = location.getWorld().getName();
    }

    public BlockColumn()
    {
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
        if (parsedWorld == null)
        {
            this.parsedWorld = Bukkit.getWorld(world);
        }
        return parsedWorld;
    }

    /**
     * @param world the world to set
     */
    public void setWorld(World world)
    {
        this.parsedWorld = world;
        this.world = world.getName();
    }

    /**
     * @return all blocks in the column.
     */
    public List<Block> getAllBlocks()
    {
        List<Block> blocks = new ArrayList<>(257);
        for (int i = 0; i < 256; i++)
        {
            blocks.add(getWorld().getBlockAt(x, i, z));
        }
        return blocks;
    }

    /**
     * @return all blocks that are air in the column
     */
    public List<Block> getAirBlocks()
    {
        List<Block> blocks = getAllBlocks();
        blocks.removeIf(block -> block.getType() == Material.AIR);
        return blocks;
    }

    @Override
    public BlockColumn clone()
    {
        return new BlockColumn(x, z, getWorld());
    }

    @Override
    public double distance(Rectangle r)
    {
        if (r instanceof BoundedArea)
        {
            BoundedArea area = (BoundedArea) r;
            if (area.getWorld() != getWorld())
            {
                return Integer.MAX_VALUE;
            }
        }
        else if (r instanceof BlockColumn)
        {
            BlockColumn area = (BlockColumn) r;
            if (area.getWorld() != getWorld())
            {
                return Integer.MAX_VALUE;
            }
        }
        return GeometryUtils.distance(x, z, x, z, r.x1(), r.y1(), r.x2(), r.y2());
    }

    @Override
    public boolean intersects(Rectangle r)
    {
        if (r instanceof BoundedArea)
        {
            BoundedArea area = (BoundedArea) r;
            if (area.getWorld() != getWorld())
            {
                return false;
            }
        }
        else if (r instanceof BlockColumn)
        {
            BlockColumn area = (BlockColumn) r;
            if (area.getWorld() != getWorld())
            {
                return false;
            }
        }
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

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof BlockColumn)
        {
            return ((BlockColumn) obj).getX() == getX() && ((BlockColumn) obj).getZ() == getZ() && ((BlockColumn) obj).getWorld() == getWorld();
        }
        else if (obj instanceof Rectangle)
        {
            return ((Rectangle) obj).x1() == x1() && ((Rectangle) obj).x2() == x2() && ((Rectangle) obj).y1() == y1() && ((Rectangle) obj).y2() == y2();
        }
        else
        {
            return false;
        }
    }

}

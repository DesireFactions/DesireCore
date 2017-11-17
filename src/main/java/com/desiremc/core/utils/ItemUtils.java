package com.desiremc.core.utils;

import java.lang.reflect.Field;

import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import net.minecraft.server.v1_7_R4.NBTTagCompound;

public class ItemUtils
{
    private static Field itemStackHandle = null;

    public static CraftItemStack getCraftItemStack(ItemStack item)
    {
        return item instanceof CraftItemStack ? (CraftItemStack) item : CraftItemStack.asCraftCopy(item);
    }

    public static net.minecraft.server.v1_7_R4.ItemStack getHandle(CraftItemStack item)
    {
        if (item == null)
        {
            throw new IllegalArgumentException("Item can't be null.");
        }
        if (itemStackHandle == null)
        {
            populateReflection();
        }
        try
        {
            return (net.minecraft.server.v1_7_R4.ItemStack) itemStackHandle.get((CraftItemStack) item);
        }
        catch (IllegalArgumentException | IllegalAccessException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public static NBTTagCompound getTag(net.minecraft.server.v1_7_R4.ItemStack item)
    {
        if (item == null)
        {
            throw new IllegalArgumentException("Item can't be null");
        }
        if (!item.hasTag())
        {
            item.setTag(new NBTTagCompound());
        }
        return item.getTag();
    }

    private static void populateReflection()
    {
        try
        {
            itemStackHandle = CraftItemStack.class.getDeclaredField("handle");
            itemStackHandle.setAccessible(true);
        }
        catch (NoSuchFieldException | SecurityException e)
        {
            e.printStackTrace();
        }
    }
}

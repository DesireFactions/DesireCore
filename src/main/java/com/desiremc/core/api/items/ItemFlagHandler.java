package com.desiremc.core.api.items;

import java.util.EnumSet;
import java.util.Set;

import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import net.minecraft.server.v1_7_R4.NBTTagCompound;

public class ItemFlagHandler
{

    private static final String HIDEFLAGS = "HideFlags";

    public static ItemStack addItemFlags(ItemStack item, ItemFlag... hideFlags)
    {
        return addItemFlags(item, hideFlags);
    }

    public static ItemStack addItemFlags(ItemStack item, Iterable<ItemFlag> hideFlags)
    {
        NBTTagCompound tag = CraftItemStack.asNMSCopy(item).getTag();
        if (tag == null)
        {
            tag = new NBTTagCompound();
        }
        int hideFlag = getHideFlag(tag);
        for (ItemFlag flag : hideFlags)
        {
            hideFlag |= getBitModifier(flag);
        }
        tag.setInt(HIDEFLAGS, hideFlag);
        return CraftItemStack.asBukkitCopy(net.minecraft.server.v1_7_R4.ItemStack.createStack(tag));
    }

    public static ItemStack removeItemFlags(ItemStack item, ItemFlag... hideFlags)
    {
        NBTTagCompound tag = CraftItemStack.asNMSCopy(item).getTag();
        if (tag == null)
        {
            tag = new NBTTagCompound();
        }
        int hideFlag = getHideFlag(tag);
        for (ItemFlag flag : hideFlags)
        {
            hideFlag &= ~getBitModifier(flag);
        }
        tag.setInt(HIDEFLAGS, hideFlag);
        return CraftItemStack.asBukkitCopy(net.minecraft.server.v1_7_R4.ItemStack.createStack(tag));
    }

    public static Set<ItemFlag> getItemFlags(ItemStack item)
    {
        Set<ItemFlag> currentFlags = EnumSet.noneOf(ItemFlag.class);

        for (ItemFlag f : ItemFlag.values())
        {
            if (hasItemFlag(item, f))
            {
                currentFlags.add(f);
            }
        }

        return currentFlags;
    }

    public static boolean hasItemFlag(ItemStack item, ItemFlag flag)
    {
        int bitModifier = getBitModifier(flag);
        NBTTagCompound tag = CraftItemStack.asNMSCopy(item).getTag();
        if (tag == null)
        {
            tag = new NBTTagCompound();
        }
        return (getHideFlag(tag) & bitModifier) == bitModifier;
    }

    private static int getHideFlag(NBTTagCompound tag)
    {
        return tag.getInt(HIDEFLAGS);
    }

    private static byte getBitModifier(ItemFlag hideFlag)
    {
        return (byte) (1 << hideFlag.ordinal());
    }

}

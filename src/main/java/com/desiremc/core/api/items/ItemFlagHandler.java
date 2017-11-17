package com.desiremc.core.api.items;

import static com.desiremc.core.utils.ItemUtils.getCraftItemStack;
import static com.desiremc.core.utils.ItemUtils.getHandle;
import static com.desiremc.core.utils.ItemUtils.getTag;

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
        CraftItemStack craft = getCraftItemStack(item);
        NBTTagCompound tag = getTag(getHandle(craft));
        int hideFlag = getHideFlag(tag);
        for (ItemFlag flag : hideFlags)
        {
            hideFlag |= getBitModifier(flag);
        }
        tag.setInt(HIDEFLAGS, hideFlag);
        return craft;
    }

    public static ItemStack removeItemFlags(ItemStack item, ItemFlag... hideFlags)
    {
        CraftItemStack craft = getCraftItemStack(item);
        NBTTagCompound tag = getTag(getHandle(craft));
        int hideFlag = getHideFlag(tag);
        for (ItemFlag flag : hideFlags)
        {
            hideFlag &= ~getBitModifier(flag);
        }
        tag.setInt(HIDEFLAGS, hideFlag);
        return craft;
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
        return (getHideFlag(getTag(getHandle(getCraftItemStack(item)))) & bitModifier) == bitModifier;
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

package com.desiremc.core.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;

import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import net.minecraft.server.v1_7_R4.NBTCompressedStreamTools;
import net.minecraft.server.v1_7_R4.NBTTagCompound;
import net.minecraft.util.com.google.common.io.BaseEncoding;

public class ItemUtils
{
    private static Field itemStackHandle = null;

    public static CraftItemStack getCraftItemStack(ItemStack item)
    {
        return item instanceof CraftItemStack ? (CraftItemStack) item : CraftItemStack.asCraftCopy(item);
    }

    public static String serializeItem(ItemStack item)
    {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        NBTTagCompound tag = getTag(getHandle(getCraftItemStack(item)));
        NBTCompressedStreamTools.a(tag, output);

        return BaseEncoding.base64().encode(output.toByteArray());
    }

    public static ItemStack deserializeItem(String str)
    {
        ByteArrayInputStream input = new ByteArrayInputStream(BaseEncoding.base64().decode(str));

        NBTTagCompound tag = NBTCompressedStreamTools.a(input);
        net.minecraft.server.v1_7_R4.ItemStack nms = net.minecraft.server.v1_7_R4.ItemStack.createStack(tag);

        return CraftItemStack.asBukkitCopy(nms);
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

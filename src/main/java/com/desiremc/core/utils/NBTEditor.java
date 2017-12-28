package com.desiremc.core.utils;

import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import net.minecraft.server.v1_7_R4.NBTTagCompound;

public class NBTEditor
{

    public ItemStack setTag(ItemStack item, String tag, int val)
    {
        CraftItemStack craft = (CraftItemStack) item;
        net.minecraft.server.v1_7_R4.ItemStack nms = CraftItemStack.asNMSCopy(craft);
        NBTTagCompound compound = nms.getTag();
        if (compound == null)
        {
            compound = new NBTTagCompound();
        }
        compound.setInt(tag, val);
        return CraftItemStack.asBukkitCopy(nms);
    }

    public ItemStack setTag(ItemStack item, String tag, long val)
    {
        CraftItemStack craft = (CraftItemStack) item;
        net.minecraft.server.v1_7_R4.ItemStack nms = CraftItemStack.asNMSCopy(craft);
        NBTTagCompound compound = nms.getTag();
        if (compound == null)
        {
            compound = new NBTTagCompound();
        }
        compound.setLong(tag, val);
        return CraftItemStack.asBukkitCopy(nms);
    }

    public ItemStack setTag(ItemStack item, String tag, boolean val)
    {
        CraftItemStack craft = (CraftItemStack) item;
        net.minecraft.server.v1_7_R4.ItemStack nms = CraftItemStack.asNMSCopy(craft);
        NBTTagCompound compound = nms.getTag();
        if (compound == null)
        {
            compound = new NBTTagCompound();
        }
        compound.setBoolean(tag, val);
        return CraftItemStack.asBukkitCopy(nms);
    }

    public ItemStack setTag(ItemStack item, String tag, double val)
    {
        CraftItemStack craft = (CraftItemStack) item;
        net.minecraft.server.v1_7_R4.ItemStack nms = CraftItemStack.asNMSCopy(craft);
        NBTTagCompound compound = nms.getTag();
        if (compound == null)
        {
            compound = new NBTTagCompound();
        }
        compound.setDouble(tag, val);
        return CraftItemStack.asBukkitCopy(nms);
    }

    public ItemStack setTag(ItemStack item, String tag, float val)
    {
        CraftItemStack craft = (CraftItemStack) item;
        net.minecraft.server.v1_7_R4.ItemStack nms = CraftItemStack.asNMSCopy(craft);
        NBTTagCompound compound = nms.getTag();
        if (compound == null)
        {
            compound = new NBTTagCompound();
        }
        compound.setFloat(tag, val);
        return CraftItemStack.asBukkitCopy(nms);
    }

    public ItemStack setTag(ItemStack item, String tag, String val)
    {
        CraftItemStack craft = (CraftItemStack) item;
        net.minecraft.server.v1_7_R4.ItemStack nms = CraftItemStack.asNMSCopy(craft);
        NBTTagCompound compound = nms.getTag();
        if (compound == null)
        {
            compound = new NBTTagCompound();
        }
        compound.setString(tag, val);
        return CraftItemStack.asBukkitCopy(nms);
    }

}

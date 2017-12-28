package com.desiremc.core.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Collection;

import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import net.minecraft.server.v1_7_R4.NBTCompressedStreamTools;
import net.minecraft.server.v1_7_R4.NBTTagCompound;
import net.minecraft.util.com.google.common.io.BaseEncoding;

public class ItemUtils
{

    public static ItemStack[] toArray(Collection<ItemStack> items)
    {
        return items.toArray(new ItemStack[items.size()]);
    }

    public static CraftItemStack getCraftItemStack(ItemStack item)
    {
        return item instanceof CraftItemStack ? (CraftItemStack) item : CraftItemStack.asCraftCopy(item);
    }

    public static String serializeItem(ItemStack item)
    {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        NBTTagCompound tag = new NBTTagCompound();
        net.minecraft.server.v1_7_R4.ItemStack nms = CraftItemStack.asNMSCopy(item);
        nms.save(tag);

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

}

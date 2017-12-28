package com.desiremc.core.staff;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.desiremc.core.DesireCore;

public class GadgetHandler
{

    public HashMap<String, Gadget> gadgets;
    private static GadgetHandler instance;

    public GadgetHandler()
    {
        gadgets = new HashMap<>();
    }

    public static void initialize()
    {
        instance = new GadgetHandler();

        for (String key : DesireCore.getConfigHandler().getConfigurationSection("gadgets").getKeys(false))
        {
            boolean enabled = DesireCore.getConfigHandler().getBoolean("gadgets." + key + ".enabled");
            int slot = DesireCore.getConfigHandler().getInteger("gadgets." + key + ".slot");
            Material type = Material.getMaterial(DesireCore.getConfigHandler().getString("gadgets." + key + ".item")
                    .split(":")[0]);
            short data = (short) Integer.parseInt(DesireCore.getConfigHandler().getString("gadgets." + key + ".item")
                    .split(":")[1]);
            String displayName = DesireCore.getConfigHandler().getString("gadgets." + key + ".name");
            List<String> lore = DesireCore.getConfigHandler().getStringList("gadgets." + key + ".lore");

            Gadget gadget = new Gadget(key, enabled, slot - 1, type, data, displayName, lore);
            getInstance().gadgets.put(key, gadget);
        }
    }

    public static GadgetHandler getInstance()
    {
        return instance;
    }

    public Gadget getGadget(String name)
    {
        return gadgets.get(name);
    }

    public ItemStack buildGadget(String name, boolean status)
    {
        return buildGadget(gadgets.get(name), status);
    }

    public ItemStack buildGadget(Gadget gadget, boolean status)
    {
        ItemStack item;

        if (gadget.getName().equalsIgnoreCase("vanish") && status)
        {
            Material material = Material.matchMaterial(DesireCore.getConfigHandler().getString("gadgets.vanish" +
                    ".item-on").split(":")[0]);
            byte data = (byte) Integer.parseInt(DesireCore.getConfigHandler().getString("gadgets.vanish.item-on")
                    .split(":")[1]);
            item = new ItemStack(material, 1, data);
        }
        else
        {
            if (gadget.getData() != -1)
            {
                item = new ItemStack(gadget.getType(), 1, gadget.getData());
            }
            else
            {
                item = new ItemStack(gadget.getType(), 1);
            }
        }

        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', gadget.getDisplayName()));

        List<String> lore = new ArrayList<>();

        for (String s : gadget.getLore())
        {
            lore.add(ChatColor.translateAlternateColorCodes('&', s));
        }

        meta.setLore(lore);
        item.setItemMeta(meta);

        return item;
    }
}
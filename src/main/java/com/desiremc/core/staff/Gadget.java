package com.desiremc.core.staff;

import org.bukkit.Material;

import java.util.List;

public class Gadget
{

    private String name;

    private boolean enabled;

    private int slot;

    private Material type;

    private short data;

    private String displayName;

    private List<String> lore;

    public Gadget(String name, boolean enabled, int slot, Material type, short data, String displayName, List<String> lore)
    {
        this.name = name;
        this.enabled = enabled;
        this.slot = slot;
        this.type = type;
        this.data = data;
        this.displayName = displayName;
        this.lore = lore;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }

    public boolean isEnabled()
    {
        return enabled;
    }

    public void setSlot(int slot)
    {
        this.slot = slot;
    }

    public int getSlot()
    {
        return slot;
    }

    public void setType(Material type)
    {
        this.type = type;
    }

    public Material getType()
    {
        return type;
    }

    public void setData(short data)
    {
        this.data = data;
    }

    public short getData()
    {
        return data;
    }

    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }

    public String getDisplayName()
    {
        return displayName;
    }

    public void setLore(List<String> lore)
    {
        this.lore = lore;
    }

    public List<String> getLore()
    {
        return lore;
    }
}
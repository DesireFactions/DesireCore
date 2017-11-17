package com.desiremc.core.api.items;

public class ItemMetaKey {

    public final String BUKKIT;
    public final String NBT;

    public ItemMetaKey(final String both) {
        this(both, both);
    }

    public ItemMetaKey(final String nbt, final String bukkit) {
        this.NBT = nbt;
        this.BUKKIT = bukkit;
    }
}
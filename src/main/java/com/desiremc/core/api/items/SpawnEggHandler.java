package com.desiremc.core.api.items;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.SpawnEgg;

public class SpawnEggHandler
{

    public static boolean isSpawnEgg(ItemStack item)
    {
        return item != null && item.getType() == Material.MONSTER_EGG;
    }

    public static EntityType getType(ItemStack item)
    {
        if (item.getData() instanceof SpawnEgg)
        {
            SpawnEgg egg = (SpawnEgg) item.getData();
            return egg.getSpawnedType();
        }
        return null;
    }

    public static ItemStack setType(ItemStack item, EntityType type)
    {
        if (item.getData() instanceof SpawnEgg)
        {
            SpawnEgg egg = (SpawnEgg) item.getData();
            egg.setSpawnedType(type);
            item.setData(egg);
        }
        return item;
    }

    public static ItemStack getItem(EntityType type, int amount)
    {
        if (type == null)
        {
            throw new IllegalArgumentException("Type can't be null.");
        }
        if (!type.isAlive())
        {
            throw new IllegalArgumentException("Type must be living mob.");
        }
        ItemStack item = new ItemStack(Material.MONSTER_EGG, amount);
        item.setData(new SpawnEgg(type));
        return item;
    }

}

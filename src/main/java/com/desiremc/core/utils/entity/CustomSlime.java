package com.desiremc.core.utils.entity;

import java.util.List;

import org.bukkit.World;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;

import net.minecraft.server.v1_7_R4.EntitySlime;
import net.minecraft.server.v1_7_R4.PathfinderGoalSelector;

public class CustomSlime extends EntitySlime
{

    @SuppressWarnings("rawtypes")
    public CustomSlime(World world)
    {
        super(((CraftWorld) world).getHandle());

        setSize(1);

        List goalB = (List) EntityTypes.getPrivateField("b", PathfinderGoalSelector.class, goalSelector);
        goalB.clear();
        List goalC = (List) EntityTypes.getPrivateField("c", PathfinderGoalSelector.class, goalSelector);
        goalC.clear();
        List targetB = (List) EntityTypes.getPrivateField("b", PathfinderGoalSelector.class, targetSelector);
        targetB.clear();
        List targetC = (List) EntityTypes.getPrivateField("c", PathfinderGoalSelector.class, targetSelector);
        targetC.clear();
        
    }

    @Override
    public void h()
    {

    }
    
    @Override
    protected void bq()
    {
        
    }

    @Override
    protected void bS()
    {
        
    }
    
}

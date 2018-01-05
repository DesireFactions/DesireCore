package com.desiremc.core.tasks;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.desiremc.core.DesireCore;
import com.desiremc.core.staff.StaffHandler;

public class FrozenTask extends BukkitRunnable
{

    private Player target;

    public FrozenTask(Player target)
    {
        this.target = target;
    }

    @Override
    public void run()
    {
        if (!StaffHandler.getInstance().isFrozen(target))
        {
            cancel();
            return;
        }

        DesireCore.getLangHandler().sendRenderList(target, "staff.frozen", false, false);
    }
}

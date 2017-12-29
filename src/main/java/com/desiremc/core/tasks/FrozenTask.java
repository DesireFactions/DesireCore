package com.desiremc.core.tasks;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.desiremc.core.DesireCore;
import com.desiremc.core.staff.StaffHandler;

public class FrozenTask extends BukkitRunnable
{

    private Player player;
    private Player target;

    public FrozenTask(Player player, Player target)
    {
        this.player = player;
        this.target = target;
    }

    @Override
    public void run()
    {

        DesireCore.getLangHandler().sendRenderList(player, "staff.frozen", true, false, "{player}", target.getName());

        if (!StaffHandler.getInstance().isFrozen(player))
        {
            cancel();
            return;
        }

        runTaskLater(DesireCore.getInstance(), 140L);
    }
}

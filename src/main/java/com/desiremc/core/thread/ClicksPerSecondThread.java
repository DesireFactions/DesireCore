package com.desiremc.core.thread;

import com.desiremc.core.DesireCore;
import com.desiremc.core.session.StaffHandler;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class ClicksPerSecondThread extends BukkitRunnable
{

    private Player player;
    private Player target;

    private StaffHandler instance;

    private int count;

    public ClicksPerSecondThread(Player player, Player target, StaffHandler instance)
    {
        this.player = player;
        this.target = target;
        this.instance = instance;
    }

    @Override
    public void run()
    {
        HashMap<UUID,Integer> cps = instance.getCPS();

        switch (count)
        {
            case 0:
                DesireCore.getLangHandler().sendRenderMessage(player, "staff.cps-start", "{player}", target.getName());
                break;
            case 9:
                int amount = cps.get(target.getUniqueId()) / 20;
                DesireCore.getLangHandler().sendRenderMessage(player, "staff.cps-finish", "{player}", target.getName(), "{amount}", amount+"");
                cps.remove(target.getUniqueId());
                instance.decreaseNumCPSTests();
                cancel();
        }

        count++;
    }
}

package com.desiremc.core.thread;

import com.desiremc.core.DesireCore;
import com.desiremc.core.staff.StaffHandler;
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
            case 9:
                int amount = cps.get(target.getUniqueId()) / 10;
                DesireCore.getLangHandler().sendRenderMessage(player, "staff.cps-finish", true, false, "{player}", target.getName(), "{amount}", amount + "");
                cps.remove(target.getUniqueId());
                instance.decreaseNumCPSTests();
                cancel();
        }

        count++;
    }
}

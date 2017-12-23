package com.desiremc.core.api;

import org.bukkit.entity.Player;

import com.desiremc.core.DesireCore;
import com.desiremc.core.session.Session;
import com.desiremc.core.staff.StaffHandler;

public class StaffAPI
{

    private static final LangHandler LANG = DesireCore.getLangHandler();
    private static final StaffHandler STAFF = StaffHandler.getInstance();

    public static void freeze(Session sender, Player player)
    {
        STAFF.toggleFreeze(player, sender);
    }

    public static void toggleStaffMode(Player sender)
    {
        STAFF.toggleStaffMode(sender);
    }

    public static void clicksPerSecondTest(Session sender, Player player)
    {
        if (STAFF.CPSTest(player))
        {
            LANG.sendRenderMessage(sender, "staff.cps-already-running", "{player}", player.getDisplayName());
            return;
        }

        STAFF.startCPSTestForPlayer((Player) sender, player);
        LANG.sendRenderMessage(sender, "staff.cps-start", "{player}", player.getDisplayName());
    }

    public static void toggleInvisibility(Player player, boolean item)
    {
        STAFF.toggleInvisibility(player, item);
    }

    public static void mount(Player passenger, Player target)
    {
        STAFF.mount(passenger, target);
    }

}

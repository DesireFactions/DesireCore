package com.desiremc.core.validators;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.newcommands.SenderValidator;
import com.desiremc.core.session.Session;

/**
 * This assumes the sender is a player. If the sender is not a player, it will fail gracefully but will not send an
 * error message to the player.
 * 
 * @author Michael Ziluck
 */
public class SenderHasFreeSlotValidator implements SenderValidator
{

    @Override
    public boolean validate(Session sender)
    {
        System.out.println("Free slot start");
        if (!sender.isPlayer())
        {
            return false;
        }
        if (sender.getPlayer().getInventory().firstEmpty() == -1)
        {
            DesireCore.getLangHandler().sendRenderMessage(sender, "no_free_slots",
                    "{target}", "You",
                    "{have/has}", "have");
            return false;
        }
        System.out.println("Free slot pass");
        return true;
    }

}

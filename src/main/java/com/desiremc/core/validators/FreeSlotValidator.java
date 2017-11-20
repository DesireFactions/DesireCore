package com.desiremc.core.validators;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.desiremc.core.DesireCore;

public class FreeSlotValidator extends PlayerValidator
{

    @Override
    public boolean validateArgument(CommandSender sender, String label, Object arg)
    {
        if (!super.validateArgument(sender, label, arg))
        {
            return false;
        }
        Player p = (Player)sender;
        if (p.getInventory().firstEmpty() == -1)
        {
            DesireCore.getLangHandler().sendRenderMessage(sender, "no_free_slots");
            return false;
        }
        return true;
    }

}

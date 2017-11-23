package com.desiremc.core.validators;

import com.desiremc.core.DesireCore;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ItemInHandValidator extends PlayerValidator
{

    @Override
    public boolean validateArgument(CommandSender sender, String label, Object arg)
    {
        if (!super.validateArgument(sender, label, arg))
        {
            return false;
        }

        Player p = (Player) sender;
        if (p.getItemInHand() == null || p.getItemInHand().getType().equals(Material.AIR))
        {
            DesireCore.getLangHandler().sendString(sender, "item_in_hand");
            return false;
        }

        return true;
    }

}

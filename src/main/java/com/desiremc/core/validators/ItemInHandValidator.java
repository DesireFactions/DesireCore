package com.desiremc.core.validators;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.command.CommandValidator;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ItemInHandValidator extends CommandValidator
{

    @Override
    public boolean validateArgument(CommandSender sender, String label, Object arg)
    {
        Player p = (Player) sender;
        if (p.getItemInHand() == null || p.getItemInHand().getType().equals(Material.AIR))
        {
            DesireCore.getLangHandler().sendString(sender, "item_in_hand");
            return false;
        }

        return true;
    }

}

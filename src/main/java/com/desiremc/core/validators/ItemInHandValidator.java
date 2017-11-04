package com.desiremc.core.validators;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.desiremc.core.DesireCore;

public class ItemInHandValidator extends PlayerValidator {

    @Override
    public boolean validateArgument(CommandSender sender, String label, Object arg) {
        boolean first = super.validateArgument(sender, label, arg);
        if (!first) {
            return false;
        }
        
        Player p = (Player) sender;
        if (p.getItemInHand() == null) {
            DesireCore.getLangHandler().sendString(sender, "item_in_hand");
            return false;
        }
        
        return true;
    }
    
}

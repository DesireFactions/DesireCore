package com.desiremc.core.newvalidators;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.newcommands.SenderValidator;
import com.desiremc.core.session.Session;

public class ItemInHandValidator implements SenderValidator
{

    @Override
    public boolean validate(Session sender)
    {
        Player player = sender.getPlayer();
        if (player == null)
        {
            return false;
        }
        if (player.getItemInHand() == null || player.getItemInHand().getType() == Material.AIR)
        {
            DesireCore.getLangHandler().sendRenderMessage(sender, "item_in_hand");
            return false;
        }

        return true;

    }

}

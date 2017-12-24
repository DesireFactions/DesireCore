package com.desiremc.core.validators;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.newcommands.SenderValidator;
import com.desiremc.core.session.Session;
import org.bukkit.Material;
import org.bukkit.entity.Player;

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
            DesireCore.getLangHandler().sendRenderMessage(sender, "item_in_hand", true, false);
            return false;
        }

        return true;

    }

}

package com.desiremc.core.validators;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.newcommands.SenderValidator;
import com.desiremc.core.session.Session;
import com.sk89q.worldedit.bukkit.selections.Selection;
import org.bukkit.entity.Player;

/**
 * Assumes the sender is a player. If they are not it fails gracefully, but does not send an error message.
 * 
 * @author Michael Ziluck
 */
public class SelectedAreaValidator implements SenderValidator
{

    @Override
    public boolean validate(Session sender)
    {
        if (!sender.isPlayer())
        {
            return false;
        }
        Player player = sender.getPlayer();
        Selection s = DesireCore.getWorldEdit().getSelection(player);
        if (s != null && s.getArea() >= 1)
        {
            return true;
        }
        DesireCore.getLangHandler().sendRenderMessage(sender, "need_selection", true, false);
        return false;
    }

}

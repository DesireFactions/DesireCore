package com.desiremc.core.validators;

import org.bukkit.material.MaterialData;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.newcommands.Validator;
import com.desiremc.core.session.Session;

public class ItemBlockValidator implements Validator<MaterialData>
{

    @Override
    public boolean validateArgument(Session sender, String[] label, MaterialData arg)
    {
        if (!arg.getItemType().isBlock())
        {
            DesireCore.getLangHandler().sendRenderMessage(sender, "not_block");
            return false;
        }
        return true;
    }

}

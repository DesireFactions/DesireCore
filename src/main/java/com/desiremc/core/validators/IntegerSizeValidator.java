package com.desiremc.core.validators;

import org.bukkit.command.CommandSender;

import com.desiremc.core.api.command.CommandValidator;

public class IntegerSizeValidator extends CommandValidator
{

    private int minSize;
    private int maxSize;

    public IntegerSizeValidator(int minSize, int maxSize)
    {
        this.minSize = minSize;
        this.maxSize = maxSize;
    }

    @Override
    public boolean validateArgument(CommandSender sender, String label, Object arg)
    {
        int i = (Integer) arg;

        if (i < minSize)
        {
            LANG.sendString(sender, "integer.too_small");
            return false;
        }
        if (i > maxSize)
        {
            LANG.sendString(sender, "integer.too_large");
            return false;
        }

        return true;
    }

}

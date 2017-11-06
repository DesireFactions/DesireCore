package com.desiremc.core.validators;

import org.bukkit.command.CommandSender;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.command.CommandValidator;

public class DoubleSizeValidator extends CommandValidator
{

    private double minSize;
    private double maxSize;

    public DoubleSizeValidator(double minSize, double maxSize)
    {
        this.minSize = minSize;
        this.maxSize = maxSize;
    }

    @Override
    public boolean validateArgument(CommandSender sender, String label, Object arg)
    {
        double i = (Double) arg;

        if (i < minSize)
        {
            DesireCore.getLangHandler().sendString(sender, "number.too_small");
            return false;
        }
        if (i > maxSize)
        {
            DesireCore.getLangHandler().sendString(sender, "number.too_large");
            return false;
        }

        return true;
    }

}

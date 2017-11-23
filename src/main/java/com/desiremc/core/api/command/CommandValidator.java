package com.desiremc.core.api.command;

import java.util.Map;

import org.bukkit.command.CommandSender;

/**
 * @author Ryan Radomski
 */
public abstract class CommandValidator
{

    private int[] argsToValdate;

    /**
     * @param sender
     * @param label
     * @param arg
     * @return
     */
    public abstract boolean validateArgument(CommandSender sender, String label, Object arg);

    public void setArgsToValidate(Map<String, Integer> argsMap, String... argsToValidate)
    {
        this.argsToValdate = new int[argsToValidate.length];

        for (int i = 0; i < argsToValdate.length; i++)
        {
            this.argsToValdate[i] = argsMap.get(argsToValidate[i]);
        }
    }

    public boolean validate(CommandSender sender, String label, Object[] args)
    {
        if (argsToValdate == null)
        {
            throw new IllegalStateException("Did not call setArgsToValidateIndices yet.");
        }

        // support for validators not tied to any arguments
        if (argsToValdate.length == 0)
        {
            return validateArgument(sender, label, null);
        }

        for (int toValidate : argsToValdate)
        {
            try
            {
                if (!validateArgument(sender, label, args[toValidate]))
                {
                    return false;
                }
            }
            catch (IndexOutOfBoundsException ex)
            {

            }
        }

        return true;
    }

}
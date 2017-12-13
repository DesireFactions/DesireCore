package com.desiremc.core.newvalidators;

import java.text.DecimalFormat;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.newcommands.Validator;
import com.desiremc.core.session.Session;

public class NumberSizeValidator<T extends Number> implements Validator<T>
{

    private static final DecimalFormat format = new DecimalFormat("0.##");

    private T low;
    private T high;

    private String tooLow;
    private String tooHigh;

    /**
     * Constructs a new NumberSizeValidator with default low and high error messages.
     * 
     * @param low the lower bound.
     * @param high the upper bound.
     */
    public NumberSizeValidator(T low, T high)
    {
        this(low, high, "number.invalid");
    }

    /**
     * Constructs a new NumberSizeValidator with the same error message for the low and high end.
     * 
     * @param low the lower bound.
     * @param high the upper bound.
     * @param message the universal error message.
     */
    public NumberSizeValidator(T low, T high, String message)
    {
        this(low, high, message, message);
    }

    /**
     * Constructs a new NumberSizeValidator. The low and high messages are references to language file values. Potential
     * placeholders are {low_end}, {high_end}, and {value}.
     * 
     * @param low
     * @param high
     * @param tooLow
     * @param tooHigh
     */
    public NumberSizeValidator(T low, T high, String tooLow, String tooHigh)
    {
        this.low = low;
        this.high = high;
        this.tooLow = tooLow;
        this.tooHigh = tooHigh;
        if (tooLow == null)
        {
            tooLow = "number.too_low";
        }
        if (tooHigh == null)
        {
            tooHigh = "number.too_high";
        }
    }

    @Override
    public boolean validateArgument(Session sender, String[] label, T arg)
    {
        double val = arg.doubleValue();

        if (val < low.doubleValue())
        {
            DesireCore.getLangHandler().sendRenderMessage(sender, tooLow,
                    "{low_end}", format.format(low),
                    "{value}", format.format(arg));
            return false;
        }

        if (val > high.doubleValue())
        {
            DesireCore.getLangHandler().sendRenderMessage(sender, tooHigh,
                    "{high_end}", format.format(high),
                    "{value}", format.format(arg));
            return false;
        }

        return true;
    }

}

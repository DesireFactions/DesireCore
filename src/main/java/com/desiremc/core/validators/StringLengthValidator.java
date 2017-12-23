package com.desiremc.core.validators;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.newcommands.Validator;
import com.desiremc.core.session.Session;

public class StringLengthValidator implements Validator<String>
{

    private int minLength;
    private int maxLength;

    public StringLengthValidator(int minLength, int maxLength)
    {
        this.minLength = minLength;
        this.maxLength = maxLength;
    }

    @Override
    public boolean validateArgument(Session sender, String[] label, String arg)
    {
        int length = ((String) arg).length();

        if (length < minLength)
        {
            DesireCore.getLangHandler().sendRenderMessage(sender, "string.too_short");
            return false;
        }
        if (length > maxLength)
        {
            DesireCore.getLangHandler().sendRenderMessage(sender, "string.too_long");
            return false;
        }

        return true;
    }

}

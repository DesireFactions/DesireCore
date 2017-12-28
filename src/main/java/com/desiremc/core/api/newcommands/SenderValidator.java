package com.desiremc.core.api.newcommands;

import com.desiremc.core.session.Session;

public interface SenderValidator
{

    /**
     * Validates that the sender is in the proper state.
     * 
     * @param sender the person sending the command.
     * @return {@code true} if the sender is valid.
     */
    public boolean validate(Session sender);

}

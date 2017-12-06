package com.desiremc.core.api.newcommands;

import com.desiremc.core.session.Session;

public interface Validator<T>
{

    /**
     * Validates that the argument is in the correct state. This should also send any and all error messages associated
     * with the problem with the argument.
     * 
     * @param sender the sender of the command.
     * @param label the label of the command
     * @param arg the argument to be validated.
     * @return {@code true} if the argument is valid. Otherwise returns {@code false}.
     */
    public boolean validateArgument(Session sender, String[] label, T arg);

}

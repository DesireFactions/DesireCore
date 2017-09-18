package com.desiremc.core.api.command.arity;

public interface CommandArity {

    public boolean validateArity(int sentArgsLength, int commandArgsLength);

}

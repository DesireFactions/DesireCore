package com.desiremc.core.api.command.arity;

public class OptionalCommandArity implements CommandArity {

    @Override
    public boolean validateArity(int sentArgsLength, int commandArgsLength) {
        return sentArgsLength == commandArgsLength || sentArgsLength == commandArgsLength - 1;
    }

    @Override
    public boolean hasOptional()
    {
        return true;
    }

}

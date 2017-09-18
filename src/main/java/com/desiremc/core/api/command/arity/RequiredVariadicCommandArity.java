package com.desiremc.core.api.command.arity;

public class RequiredVariadicCommandArity implements CommandArity {

    @Override
    public boolean validateArity(int sentArgsLength, int commandArgsLength) {
        return sentArgsLength >= commandArgsLength;
    }

}

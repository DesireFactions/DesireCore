package com.desiremc.core.api.command.arity;

import java.util.List;

public class OptionalCommandArity implements CommandArity
{

    @Override
    public boolean validateArity(List<String> args, int commandArgsLength)
    {
        return args.size() == commandArgsLength || args.size() == commandArgsLength - 1;
    }

    @Override
    public boolean hasOptional()
    {
        return true;
    }

    @Override
    public boolean isVariadic()
    {
        return false;
    }

}

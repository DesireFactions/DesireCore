package com.desiremc.core.api.command.arity;

import java.util.List;

public class StrictCommandArity implements CommandArity {

    @Override
    public boolean validateArity(List<String> args, int commandArgsLength) {
        return args.size() == commandArgsLength;
    }

    @Override
    public boolean hasOptional()
    {
        return false;
    }

    @Override
    public boolean isVariadic()
    {
        return false;
    }

}

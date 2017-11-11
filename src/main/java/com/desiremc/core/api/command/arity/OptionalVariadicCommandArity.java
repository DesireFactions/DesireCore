package com.desiremc.core.api.command.arity;

import java.util.ArrayList;
import java.util.List;

import com.desiremc.core.utils.StringUtils;

public class OptionalVariadicCommandArity implements CommandArity
{

    @Override
    public boolean validateArity(List<String> args, int commandArgsLength)
    {
        if (args.size() == commandArgsLength - 1 || args.size() == commandArgsLength)
        {
            return true;
        }
        else if (args.size() > commandArgsLength - 1)
        {
            List<String> backup = new ArrayList<String>(args);
            args.subList(commandArgsLength, args.size()).clear();
            args.set(args.size() - 1, StringUtils.compile(backup.subList(commandArgsLength - 1, backup.size()).toArray(new String[0])));

            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    public boolean hasOptional()
    {
        return true;
    }

    @Override
    public boolean isVariadic()
    {
        return true;
    }

}

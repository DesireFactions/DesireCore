package com.desiremc.core.api.command.arity;

import java.util.List;

public interface CommandArity {

    public boolean validateArity(List<String> args, int commandArgsLength);

    public boolean hasOptional();
    
    public boolean isVariadic();
    
}

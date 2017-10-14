package com.desiremc.core.api.command;

import org.bukkit.command.CommandSender;

public interface ArgumentParser {
    
    public Object parseArgument(CommandSender sender, String label, String arg);
    
}

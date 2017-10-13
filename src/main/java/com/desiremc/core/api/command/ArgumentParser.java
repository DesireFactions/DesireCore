package com.desiremc.core.api.command;

import org.bukkit.command.CommandSender;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.LangHandler;

public interface ArgumentParser {
    
    public Object parseArgument(CommandSender sender, String label, String arg);
    
}

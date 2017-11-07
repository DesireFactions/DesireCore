package com.desiremc.core.parsers;

import org.bukkit.command.CommandSender;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.command.ArgumentParser;

public class IntegerParser implements ArgumentParser {

    @Override
    public Integer parseArgument(CommandSender sender, String label, String arg) {
        if (!arg.matches("\\d+")) {
            DesireCore.getLangHandler().sendString(sender, "arg_not_number");
            return null;
        }

        return Integer.parseInt(arg);
    }

}

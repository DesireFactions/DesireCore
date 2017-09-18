package com.desiremc.core.parsers;

import org.bukkit.command.CommandSender;

import com.desiremc.core.api.command.ArgumentParser;

public class StringParser implements ArgumentParser {

    @Override
    public String parseArgument(CommandSender sender, String label, String arg) {
        return arg;
    }

}

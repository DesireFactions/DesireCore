package com.desiremc.core.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;

import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.parsers.IntegerParser;
import com.desiremc.core.session.Rank;
import com.desiremc.core.utils.entity.CustomSlime;
import com.desiremc.core.utils.entity.EntityTypes;

public class SuperSlimeCommand extends ValidCommand
{

    public SuperSlimeCommand()
    {
        super("superslime", "fuck holos", Rank.DEVELOPER, new String[] { "x", "y", "z" });

        addParser(new IntegerParser(), "x", "y", "z");
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        int x = (int) args[0];
        int y = (int) args[1];
        int z = (int) args[2];

        EntityTypes.spawnEntity(new CustomSlime(Bukkit.getWorld("world")), new Location(Bukkit.getWorld("world"), x, y, z));
    }

}

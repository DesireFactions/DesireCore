package com.desiremc.core.commands;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.parsers.PlayerParser;
import com.desiremc.core.session.Rank;
import com.desiremc.core.validators.PlayerValidator;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class PingCommand extends ValidCommand
{
    public PingCommand(String name, String... aliases)
    {
        super(name, "List your ping.", Rank.GUEST, ARITY_OPTIONAL_VARIADIC, new String[] {"target"}, aliases);
        addValidator(new PlayerValidator());

        addParser(new PlayerParser(), "target");
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        Player player;

        if (args.length == 0)
        {
            player = (Player) sender;
            DesireCore.getLangHandler().sendRenderMessage(sender, "ping.self", "{ping}", ((CraftPlayer) player).getHandle().ping);
        }
        else
        {
            player = (Player) args[0];
            DesireCore.getLangHandler().sendRenderMessage(sender, "ping.other", "{ping}", ((CraftPlayer) player).getHandle().ping, "{player}", player.getName());
        }


    }
}

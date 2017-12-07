package com.desiremc.core.commands;

import java.util.List;

import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.newparsers.PlayerParser;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;

public class PingCommand extends ValidCommand
{
    public PingCommand()
    {
        super("ping", "View your ping.", Rank.GUEST, true, new String[] { "ms" });

        addArgument(CommandArgumentBuilder.createBuilder(Player.class).setName("target").setParser(new PlayerParser()).setOptional().setAllowsConsole().build());
    }

    @Override
    public void validRun(Session sender, String[] label, List<CommandArgument<?>> args)
    {
        Player player;
        if (args.get(0).hasValue())
        {
            player = (Player) args.get(0).getValue();
            DesireCore.getLangHandler().sendRenderMessage(sender, "ping.other", "{ping}", ((CraftPlayer) player).getHandle().ping, "{player}", player.getName());
        }
        else
        {
            player = sender.getPlayer();
            DesireCore.getLangHandler().sendRenderMessage(sender, "ping.self", "{ping}", ((CraftPlayer) player).getHandle().ping);
        }
    }
}

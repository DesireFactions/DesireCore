package com.desiremc.core.commands.staff;

import java.util.List;

import org.bukkit.entity.Player;

import com.desiremc.core.api.StaffAPI;
import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.parsers.PlayerParser;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;

public class StaffFollowCommand extends ValidCommand
{

    public StaffFollowCommand()
    {
        super("follow", "Follow a player", Rank.HELPER, true, new String[] { "mount", "ride", "leash", "lead" });

        addArgument(CommandArgumentBuilder.createBuilder(Player.class)
                .setName("target")
                .setParser(new PlayerParser())
                .build());
    }

    @Override
    public void validRun(Session sender, String label[], List<CommandArgument<?>> args)
    {
        StaffAPI.mount(sender.getPlayer(), (Player) args.get(0).getValue());
    }

}

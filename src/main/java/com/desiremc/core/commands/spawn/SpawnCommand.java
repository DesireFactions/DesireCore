package com.desiremc.core.commands.spawn;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.FileHandler;
import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.parsers.PlayerParser;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;

public class SpawnCommand extends ValidCommand
{

    public SpawnCommand()
    {
        super("spawn", "Teleport to the server spawn.", Rank.HELPER, true);

        addArgument(CommandArgumentBuilder.createBuilder(Player.class)
                .setName("target")
                .setParser(new PlayerParser())
                .setOptional()
                .build());
    }

    @Override
    public void validRun(Session sender, String label[], List<CommandArgument<?>> args)
    {
        Player player = args.get(0).hasValue() ? (Player) args.get(0).getValue() : sender.getPlayer();

        player.teleport(getSpawnLocation());

        if (args.get(0).hasValue())
        {
            DesireCore.getLangHandler().sendRenderMessage(sender, "spawn.target", true, false,
                    "{target}", player.getName());
            DesireCore.getLangHandler().sendRenderMessage(player, "spawn.force", true, false);
        }
        else
        {
            DesireCore.getLangHandler().sendRenderMessage(sender, "spawn.confirm", true, false);
        }
    }

    public static Location getSpawnLocation()
    {
        FileHandler config = DesireCore.getConfigHandler();
        return new Location(Bukkit.getWorld(config.getString("spawn.world")),
                config.getDouble("spawn.x"),
                config.getDouble("spawn.y"),
                config.getDouble("spawn.z"),
                config.getDouble("spawn.yaw").floatValue(),
                config.getDouble("spawn.pitch").floatValue());
    }
}
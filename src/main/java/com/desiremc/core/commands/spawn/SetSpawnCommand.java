package com.desiremc.core.commands.spawn;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.FileHandler;
import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

public class SetSpawnCommand extends ValidCommand
{

    public SetSpawnCommand()
    {
        super("setspawn", "Set the server spawn.", Rank.ADMIN, true);
    }

    @Override
    public void validRun(Session sender, String label[], List<CommandArgument<?>> args)
    {
        Player p = sender.getPlayer();
        FileHandler config = DesireCore.getConfigHandler();
        Location loc = p.getLocation();

        config.setDouble("spawn.x", loc.getX());
        config.setDouble("spawn.y", loc.getY());
        config.setDouble("spawn.z", loc.getZ());
        config.setDouble("spawn.pitch", loc.getPitch());
        config.setDouble("spawn.yaw", loc.getYaw());
        config.setString("spawn.world", loc.getWorld().getName());

        DesireCore.getLangHandler().sendRenderMessage(sender, "spawn.set", true, false);
    }
}

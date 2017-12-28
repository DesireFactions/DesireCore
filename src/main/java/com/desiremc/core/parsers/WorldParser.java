package com.desiremc.core.parsers;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.newcommands.Parser;
import com.desiremc.core.session.Session;
import com.desiremc.core.utils.CollectionUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.LinkedList;
import java.util.List;

public class WorldParser implements Parser<World>
{

    @Override
    public World parseArgument(Session sender, String[] label, String rawArgument)
    {
        World world = Bukkit.getWorld(rawArgument);
        if (world == null)
        {
            DesireCore.getLangHandler().sendRenderMessage(sender, "not_world", true, false);
            return null;
        }
        return world;
    }

    @Override
    public List<String> getRecommendations(Session sender, String lastWord)
    {
        List<String> options = new LinkedList<>(CollectionUtils.getNames(Bukkit.getWorlds(), World.class));
        Parser.pruneSuggestions(options, lastWord);
        return options;
    }

}

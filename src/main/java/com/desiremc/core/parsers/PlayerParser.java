package com.desiremc.core.parsers;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.newcommands.Parser;
import com.desiremc.core.session.Session;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class PlayerParser implements Parser<Player>
{

    @Override
    public Player parseArgument(Session sender, String[] label, String rawArgument)
    {
        Player player = Bukkit.getPlayerExact(rawArgument);
        if (player == null)
        {
            DesireCore.getLangHandler().sendRenderMessage(sender, "player_not_found", true, false);
            return null;
        }
        return player;
    }

    @Override
    public List<String> getRecommendations(Session sender, String lastWord)
    {
        return null;
    }

}

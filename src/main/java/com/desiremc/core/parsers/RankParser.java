package com.desiremc.core.parsers;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.LangHandler;
import org.bukkit.command.CommandSender;

import com.desiremc.core.api.command.ArgumentParser;
import com.desiremc.core.session.Rank;

public class RankParser implements ArgumentParser {

    private static final LangHandler LANG = DesireCore.getLangHandler();

    @Override
    public Rank parseArgument(CommandSender sender, String label, String arg) {
        Rank rank = Rank.getRank(arg);

        if (rank == null) {
            LANG.sendString(sender, "rank.invalid");
            return null;
        }

        return rank;
    }

}

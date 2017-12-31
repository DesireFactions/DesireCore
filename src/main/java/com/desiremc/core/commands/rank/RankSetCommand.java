package com.desiremc.core.commands.rank;

import java.util.List;

import org.bukkit.entity.Player;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.parsers.RankParser;
import com.desiremc.core.parsers.SessionParser;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;
import com.desiremc.core.utils.PlayerUtils;
import com.desiremc.core.validators.rank.RankSetValidator;

import ru.tehkode.permissions.PermissionGroup;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class RankSetCommand extends ValidCommand
{

    public RankSetCommand()
    {
        super("set", "Sets a user's rank.", Rank.ADMIN, new String[] { "update" });

        addArgument(CommandArgumentBuilder.createBuilder(Session.class)
                .setName("target")
                .setParser(new SessionParser())
                .build());

        addArgument(CommandArgumentBuilder.createBuilder(Rank.class)
                .setName("rank")
                .setParser(new RankParser())
                .addValidator(new RankSetValidator())
                .build());

    }

    @Override
    public void validRun(Session sender, String[] label, List<CommandArgument<?>> args)
    {
        Session target = (Session) args.get(0).getValue();
        Rank rank = (Rank) args.get(1).getValue();

        if (target.getRank().isStaff() && !rank.isStaff())
        {
            SessionHandler.removeStaff(target.getUniqueId());
        }

        PermissionUser user = PermissionsEx.getUser(target.getName());

        for (PermissionGroup group : user.getGroups())
        {
            user.removeGroup(group);
        }

        user.addGroup(rank.name().toUpperCase());

        target.setRank(rank);
        target.save();

        DesireCore.getLangHandler().sendRenderMessage(sender, "rank.set", true, false,
                "{player}", target.getName(),
                "{rank}", target.getRank().getDisplayName());

        Player player = PlayerUtils.getPlayer(target.getUniqueId());

        if (player != null)
        {
            DesireCore.getLangHandler().sendRenderMessage(player, "rank.inform", true, false,
                    "{rank}", target.getRank().getDisplayName());
        }
    }
}

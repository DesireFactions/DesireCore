package com.desiremc.core.commands.staff;

import java.util.List;

import org.bukkit.ChatColor;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.fanciful.FancyMessage;
import com.desiremc.core.parsers.SessionParser;
import com.desiremc.core.punishment.PunishmentHandler;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;
import com.desiremc.core.utils.DateUtils;

public class StaffAltsCommand extends ValidCommand
{

    public StaffAltsCommand(String name, String... aliases)
    {
        super(name, "List all alts of a player.", Rank.HELPER, aliases);

        addArgument(CommandArgumentBuilder.createBuilder(Session.class)
                .setName("target")
                .setParser(new SessionParser())
                .build());
    }

    @Override
    public void validRun(Session sender, String label[], List<CommandArgument<?>> args)
    {
        Session target = (Session) args.get(0).getValue();

        List<Session> alts = getUUIDFromIP(target.getIp());

        if (alts.size() == 1)
        {
            DesireCore.getLangHandler().sendRenderMessage(sender, "alts.none", "{player}", target.getName());
            return;
        }

        DesireCore.getLangHandler().sendRenderMessage(sender, "alts.header", "{player}", target.getName());
        DesireCore.getLangHandler().sendRenderMessageNoPrefix(sender, "alts.spacer");

        alts.removeIf(session -> session.getName().equalsIgnoreCase(target.getName()));

        for (Session session : alts)
        {
            new FancyMessage(session.getName())
                    .color(ChatColor.BLUE)
                    .tooltip(PunishmentHandler.getInstance().getMouseOverDetails(session))
                    .then((session.getOfflinePlayer().isOnline() ? " Online now" : " Last Seen: " + DateUtils.formatDateDiff(session.getLastLogin())) + " ago.")
                    .color((session.getOfflinePlayer().isOnline() ? ChatColor.GREEN : ChatColor.RED))
                    .send(sender.getSender());
        }

        DesireCore.getLangHandler().sendRenderMessageNoPrefix(sender, "alts.spacer");
    }

    private List<Session> getUUIDFromIP(String ip)
    {
        return SessionHandler.getInstance().createQuery().field("ip").equal(ip).asList();
    }
}

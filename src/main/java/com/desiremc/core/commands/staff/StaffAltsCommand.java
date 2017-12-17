package com.desiremc.core.commands.staff;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.LangHandler;
import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.fanciful.FancyMessage;
import com.desiremc.core.parsers.PlayerSessionParser;
import com.desiremc.core.punishment.PunishmentHandler;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;
import com.desiremc.core.utils.DateUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.List;

public class StaffAltsCommand extends ValidCommand
{
    private static final LangHandler LANG = DesireCore.getLangHandler();

    public StaffAltsCommand(String name, String... aliases)
    {
        super(name, "List all alts of a player.", Rank.HELPER, new String[] {"target"}, aliases);
        addParser(new PlayerSessionParser(), "target");

    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        Session target = (Session) args[0];

        List<Session> alts = getUUIDFromIP(target.getIp());

        if (alts.size() == 1)
        {
            LANG.sendRenderMessage(sender, "alts.none", "{player}", target.getName());
            return;
        }

        LANG.sendRenderMessage(sender, "alts.header", "{player}", target.getName());
        LANG.sendRenderMessageNoPrefix(sender, "alts.spacer");

        alts.removeIf(session -> session.getName().equalsIgnoreCase(target.getName()));

        for (Session session : alts)
        {
            new FancyMessage(session.getName())
                    .color(ChatColor.BLUE)
                    .tooltip(PunishmentHandler.getInstance().getMouseOverDetails(session))
                    .then((session.getOfflinePlayer().isOnline() ? " Online now" : " Last Seen: " + DateUtils.formatDateDiff(session.getLastLogin())) + " ago.")
                    .color((session.getOfflinePlayer().isOnline() ? ChatColor.GREEN : ChatColor.RED))
                    .send(sender);
        }

        LANG.sendRenderMessageNoPrefix(sender, "alts.spacer");
    }

    private List<Session> getUUIDFromIP(String ip)
    {
        return SessionHandler.getInstance().createQuery().field("ip").equal(ip).asList();
    }
}

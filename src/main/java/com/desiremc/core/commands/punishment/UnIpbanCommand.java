package com.desiremc.core.commands.punishment;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.LangHandler;
import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.parsers.PlayerSessionParser;
import com.desiremc.core.punishment.Punishment;
import com.desiremc.core.punishment.Punishment.Type;
import com.desiremc.core.punishment.PunishmentHandler;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.core.validators.PlayerIsIPBannedValidator;
import com.desiremc.core.validators.PlayerIsNotBlacklistedValidator;
import org.bukkit.command.CommandSender;

/**
 * Created by drkpr on 11/14/2017.
 */
public class UnIpbanCommand extends ValidCommand
{
    private static final LangHandler LANG = DesireCore.getLangHandler();

    public UnIpbanCommand()
    {
        super("unipban", "Unban a users ip from the server.", Rank.ADMIN, new String[] {"target"});
        addParser(new PlayerSessionParser(), "target");

        addValidator(new PlayerIsIPBannedValidator(), "target");
        addValidator(new PlayerIsNotBlacklistedValidator(), "target");
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        Session target = (Session) args[0];

        LANG.sendRenderMessage(sender, "ipban.unban_message", "{player}", target.getName());
        Punishment p = PunishmentHandler.getInstance().getPunishment(target.getUniqueId(), Type.IP_BAN);
        p.setRepealed(true);
        PunishmentHandler.getInstance().save(p);
        PunishmentHandler.getInstance().refreshPunishments(target);
    }
}

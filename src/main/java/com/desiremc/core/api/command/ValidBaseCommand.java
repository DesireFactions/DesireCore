package com.desiremc.core.api.command;

import java.util.Arrays;

import com.desiremc.core.api.LangHandler;
import org.bukkit.command.CommandSender;

import com.desiremc.core.DesireCore;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;

public abstract class ValidBaseCommand extends ValidCommand
{

    private static final LangHandler LANG = DesireCore.getLangHandler();

    /**
     * @param name the name of the command
     * @param description the description of the command
     * @param rank the rank required to use the command
     * @param aliases the aliases of the command
     */
    public ValidBaseCommand(String name, String description, Rank requiredRank, String... aliases)
    {
        super(name, description, requiredRank, new String[] {}, aliases);
    }

    public void run(CommandSender sender, String label, String[] args)
    {
        ValidCommand sub;
        if (args.length == 0 || (sub = getSubCommand(args[0])) == null)
        {
            help(sender, label);
        }
        else
        {
            Session s = SessionHandler.getSession(sender);
            if (s == null || s.getRank().getId() >= requiredRank.getId())
            {
                sub.run(sender, label + " " + args[0], Arrays.copyOfRange(args, 1, args.length));
            }
            else
            {
                DesireCore.getLangHandler().sendRenderMessage(sender, "no_permissions");
            }
        }
    }

    /**
     * Sends the help content to the player.
     * 
     * @param sender
     * @param label
     */
    public void help(CommandSender sender, String label)
    {
        LANG.sendString(sender, "list-header");
        Session s = SessionHandler.getSession(sender);

        for (ValidCommand command : subCommands)
        {
            if (command.getRequiredRank().getId() <= s.getRank().getId())
            {
                sender.sendMessage(" §b/" + label + " " + command.getName() + ": §7" + command.getDescription());
            }
        }
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
    }

}

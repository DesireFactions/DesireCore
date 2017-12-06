package com.desiremc.core.api.newcommands;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.desiremc.core.DesireCore;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;
import com.desiremc.core.utils.StringUtils;

public abstract class ValidBaseCommand extends ValidCommand
{

    protected List<ValidCommand> subCommands;

    protected ValidBaseCommand(String name, String description, String[] aliases)
    {
        super(name, description, aliases);
    }

    @Override
    protected void process(CommandSender sender, String[] label, String[] rawArguments)
    {
        ValidCommand sub;
        if (rawArguments.length == 0 || (sub = getSubCommand(rawArguments[0])) == null)
        {
            help(sender, label);
        }
        else
        {
            Session s = getSenderSession(sender);
            if (s == null || (s.getRank().getId() >= getRequiredRank().getId() && s.getRank().getId() >= sub.getRequiredRank().getId()))
            {
                sub.process(sender, StringUtils.add(label, rawArguments[0]), Arrays.copyOfRange(rawArguments, 1, rawArguments.length));
            }
            else
            {
                DesireCore.getLangHandler().sendRenderMessage(sender, "no_permissions");
            }
        }
    }

    @Override
    public List<String> processTabComplete(CommandSender sender, String[] rawArguments)
    {
        if (rawArguments.length == 1)
        {
            return getSubCommandNames(rawArguments[0]);
        }
        else
        {
            return getSubCommand(rawArguments[0]).processTabComplete(sender, Arrays.copyOfRange(rawArguments, 1, rawArguments.length));

        }
    }

    /**
     * Add a new sub command to this base command.
     * 
     * @param subCommand the sub command to add.
     */
    public void addSubCommand(ValidCommand subCommand)
    {
        subCommands.add(subCommand);
    }

    /**
     * Searches for a sub command by the given name. This can be either the command's name or one of it's aliases.
     * 
     * @param label the label sent by the player.
     * @return the sub command if one is found.
     */
    public ValidCommand getSubCommand(String label)
    {
        for (ValidCommand command : subCommands)
        {
            if (command.matches(label))
            {
                return command;
            }
        }
        return null;
    }

    /**
     * Get the name all sub commands whose name or one if it's aliases starts with the given string. The name for each
     * command will be whichever piece was provided, whether that be the alias or the name.
     * 
     * @param start the beginning of the label.
     * @return the command labels if any are found.
     */
    public List<String> getSubCommandNames(String start)
    {
        List<String> commandNames = new LinkedList<>();
        String match;
        for (ValidCommand sub : subCommands)
        {
            match = sub.getMatchingAlias(start);
            if (match != null)
            {
                commandNames.add(match);
            }
        }
        return commandNames;
    }

    /**
     * Get a view of all the sub commands. This is not able to be modified and doing so will throw an exception.
     * 
     * @return all the sub commands.
     */
    public List<ValidCommand> getSubCommands()
    {
        return Collections.unmodifiableList(subCommands);
    }

    /**
     * Sends the help content to the player.
     * 
     * @param sender
     * @param label
     */
    public void help(CommandSender sender, String label[])
    {
        DesireCore.getLangHandler().sendString(sender, "list-header");
        Session s = SessionHandler.getSession(sender);

        for (ValidCommand command : subCommands)
        {
            if (command.getRequiredRank().getId() <= s.getRank().getId())
            {
                sender.sendMessage(" §b/" + StringUtils.compile(label) + " " + command.getName() + ": §7" + command.getDescription());
            }
        }
    }

    @Override
    public void validRun(CommandSender sender, String[] label, List<CommandArgument<?>> arguments)
    {
    }

}

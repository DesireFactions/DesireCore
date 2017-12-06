package com.desiremc.core.api.newcommands;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.desiremc.core.DesireCore;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;

/**
 * @author Michael Ziluck
 * @param <T> the type the argument will be after being parsed.
 */
public class CommandArgument<T>
{

    protected ValidCommand command;

    protected T value;

    protected String name;

    protected boolean optional;

    protected boolean variableLength;

    protected int ordinal;

    protected List<Validator<T>> validators;

    protected Parser<T> parser;

    protected Rank requiredRank;

    protected String[] tabOptions;

    /**
     * Constructs a new argument. This will also initialize the validators list.
     */
    protected CommandArgument()
    {
        this.validators = new LinkedList<>();
    }

    /**
     * Process the given argument. This will parse it into it's appropriate form, as well as validate that it is in the
     * proper state. The value returned represents whether or not the process was successful. No error message is
     * required beyond what was already sent within the parsers and validators.
     * 
     * @param sender the sender of the command.
     * @param label the label of the command.
     * @param argument the raw string argument to be processed.
     * @return {@code true} if the process was successful. Otherwise, returns {@code false}.
     */
    protected boolean process(CommandSender sender, String[] label, String argument)
    {
        Session session = command.getSenderSession(sender);
        if (session != null && (hasRequiredRank() && session.getRank().getId() < getRequiredRank().getId()))
        {
            DesireCore.getLangHandler().sendRenderMessage(sender, "no_permissions");
            return false;
        }
        value = parser.parseArgument(sender, label, argument);

        if (value == null)
        {
            return false;
        }

        for (Validator<T> validator : validators)
        {
            if (!validator.validateArgument(sender, label, value))
            {
                return false;
            }
        }

        return true;
    }

    /**
     * Retrieves all potential options a player can have when trying to tab-complete this argument. This should always
     * be implemented. However, because that most likely will not happen, it defaults to the same behavior that Bukkit
     * uses which is to list off the names of all visible connected players.
     * 
     * @param sender the sender who is tab-completing.
     * @return all argument options.
     */
    protected List<String> getRecommendations(CommandSender sender, String lastWord)
    {
        List<String> recommendations = parser.getRecommendations(sender, lastWord);
        if (recommendations == null)
        {
            recommendations = CommandHandler.defaultTabComplete(sender, lastWord);
        }
        return recommendations;
    }

    /**
     * Gets the value that was just parsed and validated.
     * 
     * @return the value.
     */
    public T getValue()
    {
        if (value == null)
        {
            throw new IllegalStateException("Argument has not been processed.");
        }
        return value;
    }

    /**
     * Clears the most recent value processed by this argument.
     */
    public void clearValue()
    {
        value = null;
    }

    /**
     * @return {@code true} if the argument has a required rank. Otherwise returns {@code false}.
     */
    public boolean hasRequiredRank()
    {
        return requiredRank != null;
    }

    /**
     * The ability to use an argument might differ from the ability to use a command in general. This is the rank
     * required to use this argument within a command.
     * 
     * @return the rank required to use this argument.
     */
    public Rank getRequiredRank()
    {
        return requiredRank;
    }

    /**
     * Sets the required rank for this argument. This should only be set once, and should only be changed from the
     * CommandArgumentBuilder. Will throw an {@link IllegalStateException} if set a second time.
     * 
     * @param requiredRank the required rank.
     */
    public void setRequiredRank(Rank requiredRank)
    {
        if (this.requiredRank != null)
        {
            throw new IllegalArgumentException("Required rank can only be set once.");
        }
        this.requiredRank = requiredRank;
    }

    /**
     * @return the parser for this argument.
     */
    public Parser<T> getParser()
    {
        return parser;
    }

    /**
     * Sets the parser for this argument. This should only be set once, and should only be changed from the
     * CommandArgumentBuilder. Will throw an {@link IllegalStateException} if set a second time.
     * 
     * @param parser the argument's parser.
     */
    protected void setParser(Parser<T> parser)
    {
        if (this.parser != null)
        {
            throw new IllegalArgumentException("Parser can only be set once.");
        }
        this.parser = parser;
    }

    /**
     * Returns a view of the validators. This view can't be changed, and will throw exceptions if it is attempted.
     * 
     * @return the validators for this argument.
     */
    public List<Validator<T>> getValidators()
    {
        return Collections.unmodifiableList(validators);
    }

    /**
     * Adds a new validator to the argument.
     * 
     * @param validator the new validator.
     */
    public void addValidator(Validator<T> validator)
    {
        this.validators.add(validator);
    }

    /**
     * Gets where in the order of arguments this argument falls for a particular command. The default is -1, so if -1 is
     * returned it means that this argument has not yet been assigned to a command.
     * 
     * @return the argument ordinal.
     */
    public int getOrdinal()
    {
        return ordinal;
    }

    /**
     * Sets the order that this argument falls for a particular command. This method is protected to make sure that the
     * CommandHandler does not break when managing the argument system.
     * 
     * @param ordinal the new argument ordinal.
     */
    protected void setOrdinal(int ordinal)
    {
        this.ordinal = ordinal;
    }

    /**
     * @return whether or not this argument has a variable length.
     */
    public boolean hasVariableLength()
    {
        return variableLength;
    }

    /**
     * @param status whether or not the argument is of variable length.
     */
    public void setVariableLength(boolean status)
    {
        this.variableLength = status;
    }

    /**
     * @return whether this argument is optional or not.
     */
    public boolean isOptional()
    {
        return optional;
    }

    /**
     * @param state whether or not the argument is optional.
     */
    public void setOptional(boolean state)
    {
        this.optional = state;
    }

    /**
     * @return the name of the argument
     */
    public String getName()
    {
        return name;
    }

    /**
     * Sets the name for the argument. This should only be set once, and should only be changed from the
     * CommandArgumentBuilder. Will throw an {@link IllegalStateException} if set a second time.
     * 
     * @param name the name of the argument.
     */
    public void setName(String name)
    {
        if (this.name != null)
        {
            throw new IllegalStateException("Name can only be set once.");
        }
        this.name = name;
    }

    /**
     * @param command the command this argument belongs to.
     */
    protected void setCommand(ValidCommand command)
    {
        this.command = command;
    }

}

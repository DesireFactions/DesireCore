package com.desiremc.core.api;

import com.desiremc.core.session.Session;
import com.desiremc.core.utils.ChatUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;

/**
 * @author Michael Ziluck
 */
public class LangHandler extends FileHandler
{

    private String prefix;
    private boolean usePrefix;

    /**
     * Create a new {@link LangHandler} based on the {@link FileHandler}. Also loads the prefix.
     *
     * @param file
     */
    public LangHandler(File file, JavaPlugin plugin)
    {
        super(file, plugin);
        usePrefix = super.getBoolean("prefix.use");
        if (usePrefix)
        {
            prefix = super.getString("prefix.text");
        }

    }

    public String getPrefix()
    {
        return prefix;
    }

    /**
     * Gets a formatted string from the config file. Replaces any color place holders as well. If the string does not
     * exist in the config, returns null.
     *
     * @param string
     * @return the formatted string.
     */
    @Override
    public String getString(String string)
    {
        String str = super.getString(string);
        if (str == null)
        {
            str = "==ERROR==";
        }
        return (prefix != null && !str.startsWith("`") ? prefix + " " : "") + "§r" + (!str.startsWith("`") ? str : str.substring(1, str.length()));
    }

    /**
     * Gets a string without a prefix.
     * 
     * @param string the reference.
     * @return the formatted string.
     */
    public String getStringNoPrefix(String string)
    {
        return super.getString(string);
    }

    /**
     * Render a message using the format rendered in lang.yml
     *
     * @param string
     * @param args
     * @return
     */
    public String renderMessage(String string, Object... args)
    {
        return renderString(getString(string), args);
    }

    public String renderMessageNoPrefix(String string, Object... args)
    {
        return renderString(super.getString(string), args);
    }

    public void sendRenderMessageNoPrefix(CommandSender sender, String string, Object... args)
    {
        sender.sendMessage(renderString(super.getString(string), args));
    }

    public void sendRenderMessageCenteredeNoPrefix(CommandSender sender, String string, Object... args)
    {
        ChatUtils.sendCenteredMessage(sender, renderString(super.getString(string), args));
    }

    /**
     * Shorthand to send getString to {@link CommandSender}
     *
     * @param sender
     * @param string
     */
    public void sendString(CommandSender sender, String string)
    {
        sender.sendMessage(getString(string));
    }

    /**
     * Shorthand to render a command and send it to a {@link CommandSender}
     *
     * @param sender
     * @param string
     * @param args
     */
    public void sendRenderMessage(CommandSender sender, String string, Collection<Object> args)
    {
        sendRenderMessage(sender, string, args.toArray());
    }

    /**
     * Shorthand to render a command and send it to a {@link CommandSender}
     *
     * @param sender
     * @param string
     * @param args
     */
    public void sendRenderMessage(CommandSender sender, String string, Object... args)
    {
        sender.sendMessage(renderMessage(string, args));
    }

    public void sendCenteredRenderMessage(CommandSender sender, String string, Object... args)
    {
        ChatUtils.sendCenteredMessage(sender, renderMessage(string, args));
    }

    public void sendRenderMessage(CommandSender sender, String string, boolean center, Object... args)
    {
        if (center)
        {
        }
        else
        {
            sender.sendMessage(renderMessage(string, args));
        }
    }

    public void sendRenderMessage(Session s, String string, Object... args)
    {
        sendRenderMessage(s.getPlayer(), string, args);
    }

    public void sendRenderMessage(Session s, String string, boolean center, Object... args)
    {
        if (center)
        {
            ChatUtils.sendCenteredMessage(s.getPlayer(), renderMessage(string, args));
        }
        else
        {
            s.getPlayer().sendMessage(renderMessage(string, args));
        }
    }

    /**
     * Render a usage message using the format specified in lang.yml
     *
     * @param args
     * @return
     */
    public String usageMessage(String label, Object... args)
    {
        String argsString = "/" + label;

        for (Object arg : args)
        {
            argsString += " [" + arg + "]";
        }

        return renderMessage("usage-message", "{usage}", argsString);
    }

    /**
     * Shorthand to send a usage message to a {@link CommandSender}
     *
     * @param sender
     */
    public void sendUsageMessage(CommandSender sender, String label, Object... args)
    {
        sender.sendMessage(usageMessage(label, args));
    }

    /**
     * Render a string with the proper parameters.
     * 
     * @param string the rendered string.
     * @param args the placeholders and proper content.
     * @return the rendered string.
     */
    public String renderString(String string, Object... args)
    {
        if (args.length % 2 != 0)
        {
            throw new IllegalArgumentException("Message rendering requires arguments of an even number. " + Arrays.toString(args) + " given.");
        }

        for (int i = 0; i < args.length; i += 2)
        {
            string = string.replace(args[i].toString(), args[i + 1].toString());
        }

        return string;
    }

}
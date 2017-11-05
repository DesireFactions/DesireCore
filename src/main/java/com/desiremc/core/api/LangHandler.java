package com.desiremc.core.api;

import java.io.File;
import java.util.Arrays;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import com.desiremc.core.session.Session;
import com.desiremc.core.utils.ChatUtils;

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
     * Render a message using the format rendered in lang.yml
     *
     * @param string
     * @param args
     * @return
     */
    public String renderMessage(String string, String... args)
    {
        return renderString(getString(string), args);
    }

    public String renderMessageNoPrefix(String string, String... args)
    {
        return renderString(super.getString(string), args);
    }

    /**
     * Render a string with the proper parameters.
     * 
     * @param string the rendered string.
     * @param args the placeholders and proper content.
     * @return the rendered string.
     */
    public String renderString(String string, String... args)
    {
        if (args.length % 2 != 0)
        {
            throw new IllegalArgumentException("Message rendering requires arguments of an even number. " + Arrays.toString(args) + " given.");
        }

        for (int i = 0; i < args.length; i += 2)
        {
            string = string.replace(args[i], args[i + 1]);
        }

        return string;
    }

    /**
     * Shorthand to render a command and send it to a {@link CommandSender}
     *
     * @param sender
     * @param string
     * @param args
     */
    public void sendRenderMessage(CommandSender sender, String string, String... args)
    {
        sender.sendMessage(renderMessage(string, args));
    }

    public void sendRenderMessage(Session s, String string, String... args)
    {
        sendRenderMessage(s.getPlayer(), string, args);
    }

    public void sendRenderMessage(CommandSender sender, String string, boolean center, String... args)
    {
        if (center)
        {
            ChatUtils.sendCenteredMessage(sender, renderMessage(string, args));
        }
        else
        {
            sender.sendMessage(renderMessage(string, args));
        }
    }

    public void sendRenderMessage(Session s, String string, boolean center, String... args)
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
    public String usageMessage(String label, String... args)
    {
        String argsString = "/" + label;

        for (String arg : args)
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
    public void sendUsageMessage(CommandSender sender, String label, String... args)
    {
        sender.sendMessage(usageMessage(label, args));
    }

    public String getPrefix()
    {
        return prefix;
    }

}
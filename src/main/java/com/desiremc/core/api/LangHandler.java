package com.desiremc.core.api;

import com.desiremc.core.DesireCore;
import com.desiremc.core.session.Session;
import com.desiremc.core.utils.ChatUtils;
import com.desiremc.core.utils.CollectionUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * @author Michael Ziluck & Christian Tooley.
 */
public class LangHandler extends FileHandler
{

    private String prefix;

    /**
     * Create a new {@link LangHandler} based on the {@link FileHandler}. Also loads the prefix.
     *
     * @param file   file to retrieve the lang from
     * @param plugin main file of the plugin.
     */
    public LangHandler(File file, JavaPlugin plugin)
    {
        super(file, plugin);
        prefix = ChatColor.translateAlternateColorCodes('&', "&b&lDesire &8»");
    }

    /**
     * Gets the prefix from the config file.
     *
     * @return the prefix.
     */
    public String getPrefix()
    {
        return prefix;
    }

    /**
     * Gets a formatted string from the config file. Replaces any color place holders as well. If the string does not
     * exist in the config, returns null.
     *
     * @param string key to retrieve from the lang file.
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
     * Render a message using the format rendered in lang.yml
     *
     * @param string key to retrieve from the lang file.
     * @param args   arguments to replace.
     * @param prefix to use a prefix or not.
     * @return formatted message.
     */
    public String renderMessage(String string, boolean prefix, boolean center, Object... args)
    {
        String message;

        if (prefix)
        {
            message = renderString(getString(string), args);
        }
        else
        {
            message = renderString(getString(string), args).replace(getPrefix(), "");
        }

        if (center)
        {
            return ChatUtils.renderCenteredMessage(string);
        }
        else
        {
            return message;
        }
    }

    public void sendRenderList(CommandSender sender, String string, boolean prefix, boolean center, Object... args)
    {
        List<String> messages = DesireCore.getLangHandler().getStringList(string);

        for (String s : messages)
        {
            sender.sendMessage(renderMessage(s, prefix, center, args));
        }
    }

    public void sendRenderList(Session sender, String string, boolean prefix, boolean center, Object... args)
    {
        sendRenderList(sender.getSender(), string, prefix, center, args);
    }

    /**
     * Shorthand to render a command and send it to a {@link CommandSender}
     *
     * @param sender user to send the message to.
     * @param string the key to retrieve the message from the config file.
     * @param args   arguments to replace within the message.
     */
    public void sendRenderMessage(CommandSender sender, String string, boolean prefix, boolean center, Object... args)
    {
        String message;

        if (prefix)
        {
            message = renderMessage(string, true, center, args);
        }
        else
        {
            message = renderMessage(string, false, center, args);
        }

        if (center)
        {
            ChatUtils.sendCenteredMessage(sender, message);
        }
        else
        {
            sender.sendMessage(message);
        }
    }

    /**
     * Shorthand to render a command and send it to a {@link CommandSender}
     *
     * @param sender user to send the message to.
     * @param string the key to retrieve the message from the config file.
     * @param args   arguments to replace within the message.
     */
    public void sendRenderMessage(Session sender, String string, boolean prefix, boolean center, Object... args)
    {
        sendRenderMessage(sender.getSender(), string, prefix, center, args);
    }

    /**
     * Render a usage message using the format specified in lang.yml
     *
     * @param label the command label.
     * @param args  the arguments of the command.
     * @return the formatted usageMessage
     */
    private String usageMessage(String label, Object... args)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("/");
        sb.append(label);

        for (Object arg : args)
        {
            sb.append(" [");
            sb.append(arg);
            sb.append("]");
        }

        return renderMessage("usage-message", true, false, "{usage}", sb.toString());
    }

    /**
     * Shorthand to send a usage message to a {@link CommandSender}
     *
     * @param sender user to send the message to.
     * @param label  label of the command.
     * @param args   arguments of the command.
     */
    public void sendUsageMessage(CommandSender sender, String label, Object... args)
    {
        sender.sendMessage(usageMessage(label, args));
    }

    /**
     * Render a string with the proper parameters.
     *
     * @param string the rendered string.
     * @param args   the placeholders and proper content.
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
            string = string.replace(args[i].toString(), CollectionUtils.firstNonNull(args[i + 1], "").toString());
        }

        return string;
    }
}
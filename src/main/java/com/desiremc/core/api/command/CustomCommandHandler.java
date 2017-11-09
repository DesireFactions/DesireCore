package com.desiremc.core.api.command;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.desiremc.core.DesireCore;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;

/**
 * @author Michael Ziluck
 */
public class CustomCommandHandler implements CommandExecutor
{

    private static CustomCommandHandler instance;

    private static SimpleCommandMap commandMapInstance = getCommandMap();

    private static Map<String, Command> knownCommands = getKnownCommands();

    private LinkedList<ValidCommand> commands;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        ValidCommand command = getCustomCommand(label);
        if (command != null)
        {
            Session s = SessionHandler.getSession(sender);
            if (s != null && s.getRank().getId() >= command.getRequiredRank().getId())
            {
                command.run(sender, label, args);
            }
            else
            {
                DesireCore.getLangHandler().sendRenderMessage(sender, "no_permissions");
            }
        }
        else
        {
            return false;
        }

        return true;
    }

    /**
     * @param command
     */
    public void registerCommand(ValidCommand command)
    {
        registerCommand(command, DesireCore.getInstance());
    }

    public void registerCommand(ValidCommand command, JavaPlugin plugin)
    {
        if (commands == null)
        {
            commands = new LinkedList<>();
        }

        for (String str : checkString(knownCommands.keySet(), command))
        {
            knownCommands.remove(str);
        }

        PluginCommand bukkitCommand = createBukkitCommand(command.getName(), plugin);
        bukkitCommand.setAliases(Arrays.asList(command.getAliases()));
        bukkitCommand.setDescription(command.getDescription());
        commandMapInstance.register(plugin.getDescription().getName(), bukkitCommand);
        plugin.getCommand(command.getName()).setExecutor(this);

        commands.add(command);
    }

    private List<String> checkString(Collection<String> strings, ValidCommand command)
    {
        List<String> aliases = new ArrayList<>(Arrays.asList(command.getAliases()));
        aliases.add(command.getName());
        aliases.retainAll(strings);
        return aliases;
    }

    private PluginCommand createBukkitCommand(String name, JavaPlugin plugin)
    {
        PluginCommand command = null;
        try
        {
            Constructor<PluginCommand> c = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
            c.setAccessible(true);

            command = c.newInstance(name, plugin);
        }
        catch (SecurityException | IllegalArgumentException | IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException ex)
        {
            ex.printStackTrace();
        }

        return command;
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Command> getKnownCommands()
    {
        Map<String, Command> existingCommands = null;
        try
        {
            Field f = SimpleCommandMap.class.getDeclaredField("knownCommands");
            f.setAccessible(true);

            existingCommands = (Map<String, Command>) f.get(commandMapInstance);
        }
        catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex)
        {
            ex.printStackTrace();
        }

        return existingCommands;
    }

    private static SimpleCommandMap getCommandMap()
    {
        SimpleCommandMap commandMap = null;

        try
        {
            if (Bukkit.getPluginManager() instanceof SimplePluginManager)
            {
                Field f = SimplePluginManager.class.getDeclaredField("commandMap");
                f.setAccessible(true);

                commandMap = (SimpleCommandMap) f.get(Bukkit.getPluginManager());
            }
        }
        catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex)
        {
            ex.printStackTrace();
        }

        return commandMap;
    }

    private ValidCommand getCustomCommand(String cmd)
    {
        for (ValidCommand command : commands)
        {
            if (command.matches(cmd))
            {
                return command;
            }
        }
        return null;
    }

    public static void initialize()
    {
        instance = new CustomCommandHandler();
    }

    public static CustomCommandHandler getInstance()
    {
        return instance;
    }

}
package com.desiremc.core.commands.staff;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Player;

import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.core.staff.StaffHandler;

public class StaffReportsCommand extends ValidCommand
{

    private static HashMap<UUID, Integer> pages = new HashMap<>();

    public StaffReportsCommand(String name, String... aliases)
    {
        super(name, "Open reports GUI", Rank.HELPER, true, aliases);
    }

    @Override
    public void validRun(Session sender, String label[], List<CommandArgument<?>> args)
    {
        StaffHandler.getInstance().openReportsGUI(sender.getPlayer(), pages.getOrDefault(sender.getUniqueId(), 1));
    }

    public static void addPage(Player player)
    {
        UUID uuid = player.getUniqueId();

        pages.put(uuid, pages.getOrDefault(uuid, 1) + 1);
        StaffHandler.getInstance().openReportsGUI(player, pages.getOrDefault(player.getUniqueId(), 1));
    }

    public static void minusPage(Player player)
    {
        UUID uuid = player.getUniqueId();
        pages.put(uuid, pages.getOrDefault(uuid, 1) - 1);
        StaffHandler.getInstance().openReportsGUI(player, pages.getOrDefault(player.getUniqueId(), 1));
    }

    public static int getPage(Player player)
    {
        return pages.getOrDefault(player.getUniqueId(), 1);
    }
}

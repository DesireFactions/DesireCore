package com.desiremc.core.commands.staff;

import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.session.Rank;
import com.desiremc.core.staff.StaffHandler;
import com.desiremc.core.validators.PlayerValidator;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class StaffReportsCommand extends ValidCommand
{

    private static HashMap<UUID, Integer> pages = new HashMap<>();

    public StaffReportsCommand(String name, String... aliases)
    {
        super(name, "Open reports GUI", Rank.HELPER, new String[] {}, aliases);
        addValidator(new PlayerValidator());
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        Player p = (Player) sender;

        StaffHandler.getInstance().openReportsGUI(p, pages.getOrDefault(p.getUniqueId(), 1));
    }

    public static void addPage(Player p)
    {
        UUID uuid = p.getUniqueId();

        pages.put(uuid, pages.getOrDefault(uuid, 1) + 1);
        StaffHandler.getInstance().openReportsGUI(p, pages.getOrDefault(p.getUniqueId(), 1));
    }

    public static void minusPage(Player p)
    {
        UUID uuid = p.getUniqueId();
        pages.put(uuid, pages.getOrDefault(uuid, 1) - 1);
        StaffHandler.getInstance().openReportsGUI(p, pages.getOrDefault(p.getUniqueId(), 1));
    }

    public static int getPage(Player p)
    {
        return pages.getOrDefault(p.getUniqueId(), 1);
    }
}

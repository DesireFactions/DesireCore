package com.desiremc.core.commands;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.parsers.StringParser;
import com.desiremc.core.session.Rank;
import com.desiremc.core.validators.ItemInHandValidator;
import com.desiremc.core.validators.PlayerValidator;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class RenameCommand extends ValidCommand
{
    public RenameCommand()
    {
        super("rename", "Rename an item in your hand.", Rank.BRIGADIER, ARITY_REQUIRED_VARIADIC, new String[] {"name"});
        addParser(new StringParser(), "name");

        addValidator(new PlayerValidator());
        addValidator(new ItemInHandValidator());
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        Player player = (Player) sender;
        String name = (String) args[0];

        ItemStack item = player.getItemInHand();
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        item.setItemMeta(meta);

        player.updateInventory();

        DesireCore.getLangHandler().sendRenderMessage(sender, "rename", "{name}", ChatColor.translateAlternateColorCodes('&', name));
    }
}

package com.desiremc.core.commands;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.CommandArgumentBuilder;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.parsers.StringParser;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.core.validators.ItemInHandValidator;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class RenameCommand extends ValidCommand
{
    public RenameCommand()
    {
        super("rename", "Rename an item in your hand.", Rank.BRIGADIER, true);

        addArgument(CommandArgumentBuilder.createBuilder(String.class).setName("name").setParser(new StringParser()).addSenderValidator(new ItemInHandValidator()).build());
    }

    @Override
    public void validRun(Session sender, String[] label, List<CommandArgument<?>> args)
    {
        Player player = sender.getPlayer();
        String name = (String) args.get(0).getValue();

        ItemStack item = player.getItemInHand();
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        item.setItemMeta(meta);

        player.updateInventory();

        DesireCore.getLangHandler().sendRenderMessage(sender, "rename", true, false,
                "{name}", ChatColor.translateAlternateColorCodes('&', name));
    }
}

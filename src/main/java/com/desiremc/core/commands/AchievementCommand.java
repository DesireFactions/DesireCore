package com.desiremc.core.commands;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.LangHandler;
import com.desiremc.core.api.command.ValidCommand;
import com.desiremc.core.session.Achievement;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionHandler;
import com.desiremc.core.validators.PlayerValidator;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class AchievementCommand extends ValidCommand
{

    private static final LangHandler LANG = DesireCore.getLangHandler();

    private static HashMap<UUID, Integer> pages;

    public AchievementCommand()
    {
        super("achievements", "Open the Achievement GUI", Rank.GUEST, new String[] {});
        addValidator(new PlayerValidator());
    }

    @Override
    public void validRun(CommandSender sender, String label, Object... args)
    {
        Session player = SessionHandler.getSession(sender);
        openAchievementsGUI(player);
    }

    private void openAchievementsGUI(Session session)
    {
        Inventory inv = Bukkit.createInventory(null, 54, LANG.renderMessage("agui.inventory.title"));

        List<Achievement> achievements = Achievement.getAllAchievements();

        boolean next = true;
        int itemsPerPage = 45;
        int startingIndex = (pages.getOrDefault(session.getUniqueId(), 1) - 1) * itemsPerPage;
        int endingIndex = startingIndex + itemsPerPage;

        if (endingIndex > achievements.size())
        {
            endingIndex = achievements.size();
            next = false;
        }

        for (Achievement achievement : achievements.subList(startingIndex, endingIndex))
        {

            ItemStack item;

            if (session.hasAchievement(achievement))
            {
                item = new ItemStack(Material.WOOL, 1, (short) 13);
            }
            else
            {
                item = new ItemStack(Material.WOOL, 1, (short) 14);
            }
            ItemMeta skull = item.getItemMeta();
            skull.setDisplayName(achievement.getName());

            List<String> lore = new ArrayList<>();

            for (String loreString : LANG.getStringList("agui.inventory.item.lore"))
            {
                lore.add(LANG.renderString(loreString, "{desc}", achievement.getDescription()));
            }

            skull.setLore(lore);
            item.setItemMeta(skull);
            inv.addItem(item);
        }

        if (next)
        {
            ItemStack nextItem = new ItemStack(Material.matchMaterial(LANG.getString("agui.inventory.next.item")));
            ItemMeta nextMeta = nextItem.getItemMeta();

            nextMeta.setDisplayName(LANG.renderString("agui.inventory.next.name"));

            List<String> lore = new ArrayList<>();

            for (String loreString : LANG.getStringList("agui.inventory.next.lore"))
            {
                lore.add(LANG.renderString(loreString));
            }

            nextItem.setItemMeta(nextMeta);
            inv.setItem(53, nextItem);
        }

        if (pages.getOrDefault(session.getUniqueId(), 1) != 1)
        {
            ItemStack nextItem = new ItemStack(Material.matchMaterial(LANG.getString("agui.inventory.back.item")));
            ItemMeta nextMeta = nextItem.getItemMeta();

            nextMeta.setDisplayName(LANG.renderString("agui.inventory.back.name"));

            List<String> lore = new ArrayList<>();

            for (String loreString : LANG.getStringList("agui.inventory.back.lore"))
            {
                lore.add(LANG.renderString(loreString));
            }

            nextItem.setItemMeta(nextMeta);
            inv.setItem(45, nextItem);
        }

        session.getPlayer().openInventory(inv);
    }

    public static void addPage(UUID uuid)
    {
        pages.put(uuid, pages.get(uuid) + 1);
    }

    public static void minusPage(UUID uuid)
    {
        pages.put(uuid, pages.get(uuid) - 1);
    }
}

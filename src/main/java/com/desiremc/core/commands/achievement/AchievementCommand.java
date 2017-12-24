package com.desiremc.core.commands.achievement;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.newcommands.CommandArgument;
import com.desiremc.core.api.newcommands.ValidCommand;
import com.desiremc.core.session.Achievement;
import com.desiremc.core.session.Session;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class AchievementCommand extends ValidCommand
{

    private static HashMap<UUID, Integer> pages = new HashMap<>();

    public AchievementCommand()
    {
        super("achievements", "Open the Achievement GUI", true);
    }

    @Override
    public void validRun(Session sender, String label[], List<CommandArgument<?>> args)
    {
        openAchievementsGUI(sender);
    }

    private void openAchievementsGUI(Session session)
    {
        Inventory inv = Bukkit.createInventory(null, 54, DesireCore.getLangHandler().renderMessage("agui.inventory.title", false, false));

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
                item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 13);
            }
            else
            {
                item = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14);
            }
            ItemMeta skull = item.getItemMeta();
            skull.setDisplayName(DesireCore.getLangHandler().renderMessage("agui.inventory.item.name", false, false, "{name}", achievement.getName()));

            List<String> lore = new ArrayList<>();

            for (String loreString : DesireCore.getLangHandler().getStringList("agui.inventory.item.lore"))
            {
                lore.add(DesireCore.getLangHandler().renderString(loreString, "{desc}", achievement.getDescription(), "{tokens}", achievement.getReward() + ""));
            }

            skull.setLore(lore);
            item.setItemMeta(skull);
            inv.addItem(item);
        }

        if (next)
        {
            ItemStack nextItem = new ItemStack(Material.matchMaterial(DesireCore.getLangHandler().getString("agui.inventory.next.item")));
            ItemMeta nextMeta = nextItem.getItemMeta();

            nextMeta.setDisplayName(DesireCore.getLangHandler().renderString("agui.inventory.next.name"));

            List<String> lore = new ArrayList<>();

            for (String loreString : DesireCore.getLangHandler().getStringList("agui.inventory.next.lore"))
            {
                lore.add(DesireCore.getLangHandler().renderString(loreString));
            }

            nextItem.setItemMeta(nextMeta);
            inv.setItem(53, nextItem);
        }

        if (pages.getOrDefault(session.getUniqueId(), 1) != 1)
        {
            ItemStack nextItem = new ItemStack(Material.matchMaterial(DesireCore.getLangHandler().getString("agui.inventory.back.item")));
            ItemMeta nextMeta = nextItem.getItemMeta();

            nextMeta.setDisplayName(DesireCore.getLangHandler().renderString("agui.inventory.back.name"));

            List<String> lore = new ArrayList<>();

            for (String loreString : DesireCore.getLangHandler().getStringList("agui.inventory.back.lore"))
            {
                lore.add(DesireCore.getLangHandler().renderString(loreString));
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

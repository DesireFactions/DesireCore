package com.desiremc.core.session;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;

import com.desiremc.core.DesireCore;
import com.desiremc.core.utils.StringUtils;

public enum Rank
{

    GUEST(1, "Guest", "", ChatColor.WHITE, ChatColor.GRAY),
    COMMODORE(2, "Commodore", "§8[&aCommodore&§]", ChatColor.WHITE, ChatColor.GREEN),
    PREMIER(3, "Brigadier", "§8[§dPremier§8]", ChatColor.WHITE, ChatColor.LIGHT_PURPLE),
    GRANDMASTER(4, "Grandmaster", "§8[§eGM§8]", ChatColor.WHITE, ChatColor.YELLOW),
    BETA(5, "Beta", "§8[§bBeta§8]", ChatColor.WHITE, ChatColor.AQUA),
    YOUTUBER(6, "YouTuber", "§8[§cYou§fTube§8]", ChatColor.WHITE, ChatColor.RED),
    PARTNER(7, "Partner", "§8[§2Partner§8]", ChatColor.WHITE, ChatColor.DARK_GREEN),
    HELPER(8, "Helper", "§8[§dHelper§8]", ChatColor.WHITE, ChatColor.LIGHT_PURPLE, "helper", "help"),
    MODERATOR(9, "Moderator", "§8[§2Mod§8]", ChatColor.WHITE, ChatColor.DARK_GREEN, "mod"),
    SRMOD(10, "Senior_Mod", "§8[§6SrMod§8]", ChatColor.WHITE, ChatColor.GOLD, "sr", "srmod", "sr_mod"),
    ADMIN(11, "Admin", "§8[§cAdmin§8]", ChatColor.GREEN, ChatColor.RED),
    MANAGER(12, "Manager", "§8[§4Manager§8]", ChatColor.GREEN, ChatColor.DARK_RED),
    DEVELOPER(13, "Developer", "§8[§9Developer§8]", ChatColor.GREEN, ChatColor.BLUE, "dev"),
    OWNER(14, "Owner", "§8[§bOwner§8]", ChatColor.GREEN, ChatColor.AQUA);

    private final int id;
    private final String displayName;
    private final String prefix;
    private final ChatColor chatColor;
    private final ChatColor main;
    private final String[] aliases;

    Rank(int id, String displayName, String prefix, ChatColor chatColor, ChatColor main, String... aliases)
    {
        this.id = id;
        this.displayName = displayName;
        this.prefix = prefix;
        this.chatColor = chatColor;
        this.main = main;
        this.aliases = aliases;
    }

    public int getId()
    {
        return id;
    }

    public String getDisplayName()
    {
        return displayName;
    }

    public ChatColor getColor()
    {
        return chatColor;
    }

    public ChatColor getMain()
    {
        return main;
    }

    public String getPrefix()
    {
        return prefix + " ";
    }

    public boolean isStaff()
    {
        return getId() >= HELPER.getId();
    }

    public boolean isDonor()
    {
        return getId() < YOUTUBER.getId() && getId() > GUEST.getId();
    }

    public boolean isManager()
    {
        return getId() >= ADMIN.getId();
    }

    public long getDeathBanTime()
    {
        if (isStaff())
        {
            return 0;
        }
        Long lookup = DesireCore.getConfigHandler().getLong("deathban.times." + getDisplayName().toLowerCase());
        if (lookup == null || lookup == 0)
        {
            lookup = DesireCore.getConfigHandler().getLong("deathban.times.default");
        }
        return lookup * 60_000;
    }

    public static Rank getRank(String value)
    {
        for (Rank v : values())
        {
            if (v.name().equalsIgnoreCase(value) || StringUtils.contains(v.aliases, value))
            {
                return v;
            }
        }
        return null;
    }

    public static List<String> getRanks()
    {
        List<String> names = new ArrayList<>();

        for (Rank rank : values())
        {
            names.add(StringUtils.capitalize(rank.name().toLowerCase().replace("_", " ")));
        }

        return names;
    }

}

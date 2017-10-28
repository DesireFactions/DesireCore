package com.desiremc.core.session;

import org.bukkit.ChatColor;

import com.desiremc.core.DesireCore;
import com.desiremc.core.utils.StringUtils;

public enum Rank
{

    GUEST(1, "Guest", "§8§l[§7Guest§8§l]", "", ChatColor.GRAY, ChatColor.GRAY),
    BRIGADIER(2, "Brigadier", "§a⧫§7", "", ChatColor.GRAY, ChatColor.GREEN),
    COMMODORE(3, "Commodore", "§b⧫§7", "", ChatColor.GRAY, ChatColor.AQUA),
    GRANDMASTER(4, "Grandmaster", "§d⧫§7", "", ChatColor.GRAY, ChatColor.LIGHT_PURPLE),
    YOUTUBER(5, "YouTuber", "§6§[§eYT§6]§7", "", ChatColor.WHITE, ChatColor.GOLD),
    JRMOD(6, "Junior_Moderator", "§e§l[§bJr.Mod§e§l]§7", "", ChatColor.WHITE, ChatColor.LIGHT_PURPLE, "jr", "jrmod", "jr_mod"),
    MODERATOR(7, "Moderator", "§2§l[§aModerator§2§l]§7", "", ChatColor.WHITE, ChatColor.BLUE, "mod"),
    SRMOD(8, "Senior_Mod", "§e§l[§bSr.Mod§e§l]§7", "", ChatColor.WHITE, ChatColor.LIGHT_PURPLE, "sr", "srmod", "sr_mod"),
    ADMIN(9, "Admin", "§4§l[§cAdmin§4§l]§7", "", ChatColor.RED, ChatColor.RED),
    DEVELOPER(10, "Developer", "§5§l[§dDeveloper§5§l]§7", "", ChatColor.LIGHT_PURPLE, ChatColor.RED),
    OWNER(11, "Owner", "§9§l[§bOwner§9§l]§7", "", ChatColor.AQUA, ChatColor.RED);

    private final int id;
    private final String displayName;
    private final String prefix;
    private final String suffix;
    private final ChatColor color;
    private final ChatColor main;
    private final String[] aliases;

    Rank(int id, String displayName, String prefix, String suffix, ChatColor color, ChatColor main, String... aliases)
    {
        this.id = id;
        this.displayName = displayName;
        this.prefix = prefix;
        this.suffix = suffix;
        this.color = color;
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

    public String getSuffix()
    {
        return suffix;
    }

    public ChatColor getColor()
    {
        return color;
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
        return getId() >= JRMOD.getId();
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

}

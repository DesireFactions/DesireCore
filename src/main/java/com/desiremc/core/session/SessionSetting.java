package com.desiremc.core.session;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.desiremc.core.utils.StringUtils;

public enum SessionSetting
{

    MENTIONS("Mentions", true, true, Rank.GUEST),
    FINDORE("Find Ore Notifications", true, true, Rank.GUEST, "foundore", "fo", "xray"),
    DEATH("Death Messages", true, true, Rank.GUEST),
    ACHIEVEMENTS("Achievement Messages", true, true, Rank.GUEST),
    PLAYERS("Hide Players", false, true, Rank.GUEST),
    CLASSICTAB("Classic tablist", false, false, Rank.GUEST),
    MESSAGES("Pivate Messages", true, true, Rank.GUEST),
    COBBLE("Autosmelt Cobblestone.", true, true, Rank.GUEST);

    private String displayName;
    private Rank rank;
    private boolean defaultValue;
    private boolean enabled;
    private String[] aliases;

    private SessionSetting(String displayName, boolean defaultValue, boolean enabled, Rank rank, String... aliases)
    {
        this.displayName = displayName;
        this.defaultValue = defaultValue;
        this.rank = rank;
        this.enabled = enabled;
        this.aliases = aliases;
    }

    public String getDisplayName()
    {
        return displayName;
    }

    public Rank getRank()
    {
        return rank;
    }

    public boolean getDefaultValue()
    {
        return defaultValue;
    }

    public boolean isEnabled()
    {
        return enabled;
    }

    public String[] getAliases()
    {
        return aliases;
    }

    public static SessionSetting getValue(String value)
    {
        value = value.toLowerCase();
        for (SessionSetting setting : enabledValues())
        {
            if (setting.name().toLowerCase().equals(value) || StringUtils.containsAny(value, setting.getAliases()))
            {
                return setting;
            }
        }
        return null;
    }

    public static List<SessionSetting> enabledValues()
    {
        List<SessionSetting> enabled = new LinkedList<>(Arrays.asList(values()));
        enabled.removeIf(setting -> !setting.isEnabled());
        return enabled;
    }

}

package com.desiremc.core.session;

public enum SessionSetting
{

    MENTIONS("mentions", true, Rank.GUEST),
    FINDORE("find ore notifications", true, Rank.GUEST, "foundore", "fo", "xray"),
    DEATH("death messages", true, Rank.GUEST),
    ACHIEVEMENTS("achievment messages", true, Rank.GUEST),
    CLASSICTAB("classic tab list", false, Rank.GUEST);

    private String displayName;
    private Rank rank;
    private boolean defaultValue;
    private String[] aliases;

    private SessionSetting(String displayName, boolean defaultValue, Rank rank, String... aliases)
    {
        this.displayName = displayName;
        this.defaultValue = defaultValue;
        this.rank = rank;
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

    public String[] getAliases()
    {
        return aliases;
    }

}

package com.desiremc.core.session;

public enum SessionSetting
{

    MENTIONS("mentions", true, Rank.GUEST),
    FINDORE("find ore notifications", true, Rank.GUEST, "foundore", "fo", "xray"),
    DEATH("death messages", true, Rank.GUEST),
    ACHIEVEMENTS("achievment messages", true, Rank.GUEST);

    public String displayName;
    public Rank rank;
    public boolean defaultValue;
    public String[] aliases;

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

    public String[] getAliases()
    {
        return aliases;
    }

}

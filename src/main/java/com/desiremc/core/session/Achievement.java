package com.desiremc.core.session;

public enum Achievement
{

    FIRST_LOGIN("First Login", "Login to DesireHCF for the first time", 100),
    FIRST_FRIEND("First Friend", "Congratulations! You're making friends", 100),
    FIRST_ENDERCHEST_OPEN("First EnderChest Open", "Open your ender chest for the first time!", 100),
    FIRST_USE_CROWBAR ("First CrowBar Use", "Use a crowbar for the first time!", 100),
    FIRST_COMBAT("First Combat", "Enter Combat for the first time!", 250),
    FIRST_GAPPLE("First Gapple", "Use a Gapple for the first time!", 75);

    private String name;
    private String description;
    private int reward;

    Achievement(String name, String description, int reward)
    {
        this.name = name;
        this.description = description;
        this.reward = reward;
    }

    public String getTitle()
    {
        return name().toLowerCase();
    }

    public int getId()
    {
        return ordinal();
    }

    public String getName()
    {
        return name;
    }

    public String getDescription()
    {
        return description;
    }

    public int getReward()
    {
        return reward;
    }

    public static Achievement getAchievement(String id)
    {
        for (Achievement a : values())
        {
            if (a.getName().equals(id) || a.getTitle().equals(id))
            {
                return a;
            }
        }
        return null;
    }

}

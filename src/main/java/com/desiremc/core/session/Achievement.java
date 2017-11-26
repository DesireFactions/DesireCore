package com.desiremc.core.session;

import java.util.ArrayList;
import java.util.List;

public enum Achievement
{
    FIRST_KOTH("There are many koths, but this one is mine", "Captured 1st koth", 100),
    FIRST_KILL("Existance deleted", "Kill another player.", 25),
    FIRST_MAX_SWORD("Oooh shiny!", "Max out a diamond swords enchantment level.", 10),
    FIRST_GAPPLE("Hacks confirmed", "Eat your first gapple.", 10),
    FIRST_MINER("Diggy diggy hole!", "Equip the Miner class for the first time.", 10),
    FIRST_ROGUE("Now you see me, now you don't.", "Equip the Rogue class for the first time.", 10),
    FIRST_ARCHER("Boom, headshot!", "Equip the Archer class for the first time.", 10),
    FIRST_RAIDABLE("They're outta here!", "Made a faction go raidable", 100),
    FIRST_BARD("Support OP", "Equip the Bard class for the first time.", 10),
    FIRST_FACTION("Squad up!", "Join your first faction.", 5),
    FIRST_OWN_FACTION("Dominance established", "Create your own faction.", 10),
    FIRST_INVITE("Comrade!", "Invite your first faction member.", 5),
    FIRST_CLAIM("This is mine", "Lay your first claim.", 5),
    FIRST_OFFICER("O Captain! My Captain!", "Promote your first officer.", 5),
    FIRST_ALLY("Together, forever.", "Ally another Faction.", 5),
    FIRST_SEASON_WIN("Win the season", "Achieve rank #1 on the leaderboard at the end of the season", 0),
    FIRST_LOGIN("Here's Johnny!", "Login for the first time.", 5),
    FIRST_FRIEND("I dig you", "Make your first friend.", 10),
    FIRST_END_FARM("End Farming", "Killed an enderman and creeper", 5),
    FIRST_CROWBAR_USE("Crackin'", "Use your first Crowbar", 5),
    FIRST_BREW("Bubbles!", "Brew your first potion.", 10);

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

    public static List<Achievement> getAllAchievements()
    {
        List<Achievement> achievements = new ArrayList<>();

        for (Achievement a : values())
        {
            achievements.add(a);
        }

        return achievements;
    }

}

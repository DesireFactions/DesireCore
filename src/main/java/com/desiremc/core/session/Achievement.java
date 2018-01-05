package com.desiremc.core.session;

import java.util.ArrayList;
import java.util.List;

public enum Achievement
{
    FIRST_KOTH("There are many koths, but this one is mine", "Captured 1st koth", 100),
    FIRST_KILL("Existance deleted", "Kill another player.", 25),
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
    FIRST_ENDERMAN("Don't stare at me.", "Kill an enderman", 5),
    FIRST_CREEPER("Premature explosion", "Kill a creeper.", 5),
    FIRST_CROWBAR_USE("Crackin'", "Use your first Crowbar", 5),
    FIRST_POTION_USE("Bubbles!", "Use your first potion.", 10),

    PLAYTIME_24("You must like us or something", "Hit 24 hours of playtime", 20),
    DIAMONDS_1000("My shinys", "Mine 1000 diamonds", 45),
    KILLS_25("Exterminator", "Reach 25 kills", 50),
    FINAL_PUSH("Final Push", "Make another faction raidable, while raidable", 100),
    FIRST_SPAWNER("Fancy", "Place your first Spawner", 25),
    RAIDABLE_5("All your base are belong to us", "Make 5 factions raidable", 100),
    FIRST_CRATE("Upgrade time", "Open your first Crate", 25),
    FIRST_SHOP("Merchant", "Use the shop for the first time", 25),
    PLAYTIME_48("Determination", "Hit 48 hours of playtime", 50),
    HELLO_WORLD("Hello world!", "Speak in chat for the first time", 10),
    RAIDABLE_10("Population None", "Make 10 factions raidable", 250),
    FIRST_SETTING("Personalize", "Change your first setting", 5),
    NEW_LOOK("New look", "Rename your faction for the first time", 15),
    WEALTHY("Wealthy", "Reach 30k in your faction balance", 50),
    NEW_LEADER("Switching it up?", "Change your faction leader", 10),
    KILLS_100("Unstoppable force", "Reach 100 kills", 100),
    PLAYTIME_100("VIP", "Hit 100 hours of playtime", 100);


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

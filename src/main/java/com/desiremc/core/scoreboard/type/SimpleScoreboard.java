package com.desiremc.core.scoreboard.type;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Team;

import com.desiremc.core.DesireCore;
import com.desiremc.core.scoreboard.ScoreboardRegistry;
import com.desiremc.core.scoreboard.common.Strings;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

public class SimpleScoreboard implements Scoreboard
{

    private static final String TEAM_PREFIX = "Scoreboard_";
    private static int TEAM_COUNTER = 0;

    private final org.bukkit.scoreboard.Scoreboard scoreboard;
    private final Objective objective;

    protected Player holder;
    protected long updateInterval = 10L;

    private boolean activated;
    private ScoreboardHandler handler;
    private Map<FakePlayer, Integer> entryCache = new ConcurrentHashMap<>();
    private Map<UUID, FakePlayer> playerCache = new ConcurrentHashMap<>();
    private Table<Team, String, String> teamCache = HashBasedTable.create();
    private BukkitRunnable updateTask;

    public SimpleScoreboard(Player holder)
    {
        this.holder = holder;
        // Initiate the Bukkit scoreboard
        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        scoreboard.registerNewObjective("board", "dummy").setDisplaySlot(DisplaySlot.SIDEBAR);
        objective = scoreboard.getObjective(DisplaySlot.SIDEBAR);
    }

    @Override
    public void activate()
    {
        if (activated)
            return;
        if (handler == null)
            throw new IllegalArgumentException("Scoreboard handler not set");
        activated = true;
        // Set to the custom scoreboard
        holder.setScoreboard(scoreboard);
        // And start updating on a desired interval
        updateTask = new BukkitRunnable()
        {
            @Override
            public void run()
            {
                update();
            }
        };
        updateTask.runTaskTimer(DesireCore.getInstance(), 0, updateInterval);
    }

    @Override
    public void deactivate()
    {
        if (!activated)
            return;
        activated = false;
        // Set to the main scoreboard
        if (holder.isOnline())
        {
            synchronized (this)
            {
                holder.setScoreboard((Bukkit.getScoreboardManager().getMainScoreboard()));
            }
        }
        // Unregister teams that are created for this scoreboard
        for (Team team : teamCache.rowKeySet())
        {
            team.unregister();
        }
        // Stop updating
        updateTask.cancel();
    }

    @Override
    public boolean isActivated()
    {
        return activated;
    }

    @Override
    public ScoreboardHandler getHandler()
    {
        return handler;
    }

    @Override
    public Scoreboard setHandler(ScoreboardHandler handler)
    {
        this.handler = handler;
        return this;
    }

    @Override
    public long getUpdateInterval()
    {
        return updateInterval;
    }

    @Override
    public SimpleScoreboard setUpdateInterval(long updateInterval)
    {
        if (activated)
            throw new IllegalStateException("Scoreboard is already activated");
        this.updateInterval = updateInterval;
        return this;
    }

    @Override
    public Player getHolder()
    {
        return holder;
    }

    @SuppressWarnings("deprecation")
    private void update()
    {
        // make sure the person being sent the scoreboard is online
        if (!holder.isOnline())
        {
            // clear the scoreboard from the registry
            ScoreboardRegistry.getInstance().clearScoreboard(holder);
            // remove all teams and reset the holder's scoreboard to the main
            deactivate();
            return;
        }
        // Set the title
        String handlerTitle = handler.getTitle(holder);
        // If title is blank, set it to filler
        String finalTitle = Strings.format(handlerTitle != null ? handlerTitle : ChatColor.BOLD.toString());

        // update the title if it has changed
        if (!objective.getDisplayName().equals(finalTitle))
            objective.setDisplayName(Strings.format(finalTitle));

        // all the registered scoreboard entries
        List<Entry> passed = handler.getEntries(holder);

        // only continue if handler has entries
        if (passed == null)
        {
            return;
        }

        // everything to be displayed on the scoreboard
        Map<String, Integer> appeared = new HashMap<>();

        // what's been set on the scoreboard
        Map<FakePlayer, Integer> current = new HashMap<>();

        for (Entry entry : passed)
        {
            // get the entry values
            String key = entry.getName();
            Integer score = entry.getPosition();

            // scoreboards can't go over 48 characters, truncate
            if (key.length() > 32)
            {
                key = key.substring(0, 31);
            }

            // ensure there are no duplicates
            String appearance;
            if (key.length() > 16)
            {
                appearance = key.substring(16);
            }
            else
            {
                appearance = key;
            }
            if (!appeared.containsKey(appearance))
            {
                appeared.put(appearance, -1);
            }
            appeared.put(appearance, appeared.get(appearance) + 1);

            // Get fake player
            FakePlayer faker = getFakePlayer(key, entry.getUniqueId(), appeared.get(appearance));

            // Set score
            objective.getScore(faker).setScore(score);

            // Update references
            entryCache.put(faker, score);

            // add to entered entries
            current.put(faker, score);
        }
        // Remove duplicated or non-existent entries
        for (FakePlayer fakePlayer : entryCache.keySet())
        {
            if (!current.containsKey(fakePlayer))
            {
                entryCache.remove(fakePlayer);
                scoreboard.resetScores(fakePlayer.getName());
            }
        }
    }

    private FakePlayer getFakePlayer(String text, UUID uuid, int offset)
    {
        Team team = null;
        String name;
        String prefix;
        String suffix = "";

        if (text.equals(""))
        {
            prefix = " ";
            name = " ";
            suffix = " ";
        }
        else
        {
            prefix = text.substring(0, text.length() == 0 ? 0 : Math.min(16, text.length() - 1));
            if (prefix.endsWith("$"))
            {
                suffix += "§";
            }
            name = "";
            if (text.length() > 16)
            {
                suffix += text.substring(16);
            }
        }

        // If teams already exist, use them
        for (Team other : teamCache.rowKeySet())
        {
            if (other.getPrefix().equals(prefix) && other.getSuffix().equals(suffix))
            {
                team = other;
            }
        }

        // Otherwise create them
        if (team == null)
        {
            team = scoreboard.registerNewTeam(TEAM_PREFIX + TEAM_COUNTER++);
            team.setPrefix(prefix);
            team.setSuffix(suffix);
            teamCache.put(team, prefix, suffix);
        }

        FakePlayer faker;
        if (!playerCache.containsKey(uuid))
        {
            faker = new FakePlayer(uuid, name, team);
            playerCache.put(uuid, faker);
            if (faker.getTeam() != null)
            {
                faker.getTeam().addPlayer(faker);
            }
        }
        else
        {
            faker = playerCache.get(uuid);
            if (!faker.getName().equals(name))
            {
                scoreboard.resetScores(faker.getName());
                faker.setName(name);
            }
            if (team != null && faker.getTeam() != null)
            {
                faker.getTeam().removePlayer(faker);
            }
            faker.setTeam(team);
            if (faker.getTeam() != null)
            {
                faker.getTeam().addPlayer(faker);
            }
        }
        return faker;
    }

    public Objective getObjective()
    {
        return objective;
    }

    public org.bukkit.scoreboard.Scoreboard getScoreboard()
    {
        return scoreboard;
    }

    private static class FakePlayer implements OfflinePlayer
    {

        private UUID uuid;

        private String name;

        private Team team;

        FakePlayer(UUID uuid, String name, Team team)
        {
            this.name = name;
            this.team = team;
            this.uuid = uuid;
        }

        public Team getTeam()
        {
            return team;
        }

        public void setTeam(Team team)
        {
            this.team = team;
        }

        @Override
        public boolean isOnline()
        {
            return true;
        }

        @Override
        public String getName()
        {
            return name;
        }

        public void setName(String name)
        {
            this.name = name;
        }

        @Override
        public UUID getUniqueId()
        {
            return uuid;
        }

        @Override
        public boolean isBanned()
        {
            return false;
        }

        @Override
        public boolean isWhitelisted()
        {
            return false;
        }

        @Override
        public void setWhitelisted(boolean whitelisted)
        {
        }

        @Override
        public Player getPlayer()
        {
            return null;
        }

        @Override
        public long getFirstPlayed()
        {
            return 0;
        }

        @Override
        public long getLastPlayed()
        {
            return 0;
        }

        @Override
        public boolean hasPlayedBefore()
        {
            return false;
        }

        @Override
        public Location getBedSpawnLocation()
        {
            return null;
        }

        @Override
        public Map<String, Object> serialize()
        {
            return null;
        }

        @Override
        public boolean isOp()
        {
            return false;
        }

        @Override
        public void setOp(boolean op)
        {
        }

        @Override
        public String toString()
        {
            return "FakePlayer{" +
                    "name='" + name + '\'' +
                    ", team=" + team
                    + '}';
        }

        @Override
        public void setBanned(boolean arg0)
        {

        }

    }

}
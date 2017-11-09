package com.desiremc.core.scoreboard.type;

import java.util.LinkedList;
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
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Team;

import com.desiremc.core.DesireCore;
import com.desiremc.core.scoreboard.ScoreboardRegistry;
import com.desiremc.core.scoreboard.common.Strings;

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
    private Map<String, Team> teamCache = new ConcurrentHashMap<>();
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
        for (Team team : teamCache.values())
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

        // what's been set on the scoreboard
        List<UUID> current = new LinkedList<>();

        for (Entry entry : passed)
        {
            // get the entry values
            String prefix = entry.getPrefix();
            String value = entry.getValue();
            Integer val = entry.getPosition();

            // prefix can't go over 16 characters, truncate
            if (prefix.length() > 16)
            {
                prefix = prefix.substring(0, 15);
            }

            // Get fake player
            FakePlayer faker = getFakePlayer(prefix, value, entry.getUniqueId());

            // Set score
            Score score = objective.getScore(faker);
            score.setScore(val);

            // Update references
            entryCache.put(faker, val);

            // add to entered entries
            current.add(faker.getUniqueId());
        }
        // Remove duplicated or non-existent entries
        for (FakePlayer fakePlayer : entryCache.keySet())
        {
            if (!current.contains(fakePlayer.getUniqueId()))
            {
                entryCache.remove(fakePlayer);
                scoreboard.resetScores(fakePlayer.getName());
            }
        }
        blanks = 1;
    }

    private int blanks = 1;

    private FakePlayer getFakePlayer(String text, String value, UUID uuid)
    {
        Team team = null;
        String name;
        String suffix = "";

        if (text.equals(""))
        {
            for (int i = 0; i < blanks; i++)
            {
                text += " ";
            }
            blanks++;
            value = "";
        }
        name = text;
        suffix = value;

        FakePlayer faker = playerCache.get(uuid);

        if (faker == null)
        {
            faker = new FakePlayer(uuid, name, team);
            playerCache.put(uuid, faker);
            faker.setTeam(scoreboard.registerNewTeam(TEAM_PREFIX + TEAM_COUNTER++));
            faker.getTeam().addPlayer(faker);
        }

        team = faker.getTeam();
        if (!team.getSuffix().equals(suffix))
        {
            team.setSuffix(suffix);
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
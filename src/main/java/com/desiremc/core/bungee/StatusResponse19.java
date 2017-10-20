package com.desiremc.core.bungee;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.ChatColor;

public class StatusResponse19
        implements StatusResponse
{
    private Description description;
    private Players players;
    private Version version;
    private String favicon;

    public Integer getOnlinePlayers()
    {
        return Integer.valueOf(this.players.getOnline());
    }

    public Integer getMaxPlayers()
    {
        return Integer.valueOf(this.players.getMax());
    }

    public List<String> getPlayersName()
    {
        return this.players.getPlayersByName();
    }

    public List<UUID> getPlayersUUID()
    {
        return this.players.getPlayersByUUID();
    }

    public String getDescription()
    {
        return this.description.getText();
    }

    public Integer getProtocol()
    {
        return Integer.valueOf(this.version.getProtocol());
    }

    public String getVersion()
    {
        return this.version.getName();
    }

    public String getFavicon()
    {
        return this.favicon;
    }

    public List<Player> getPlayers()
    {
        return this.players.getPlayers();
    }

    public class Description
    {
        private ArrayList<StatusResponse19.TextPart> extra;
        private String text;

        public Description()
        {
        }

        public String getText()
        {
            if ((this.extra != null) && (!this.extra.isEmpty()))
            {
                StringBuilder localStringBuilder = new StringBuilder();
                for (StatusResponse19.TextPart localTextPart : this.extra)
                {
                    localStringBuilder.append(ChatColor.valueOf(localTextPart.getColor()));
                    if (localTextPart.isBold())
                    {
                        localStringBuilder.append(ChatColor.BOLD);
                    }
                    if (localTextPart.isItalic())
                    {
                        localStringBuilder.append(ChatColor.ITALIC);
                    }
                    if (localTextPart.isObfuscated())
                    {
                        localStringBuilder.append(ChatColor.MAGIC);
                    }
                    if (localTextPart.isStrikethrough())
                    {
                        localStringBuilder.append(ChatColor.STRIKETHROUGH);
                    }
                    if (localTextPart.isUnderline())
                    {
                        localStringBuilder.append(ChatColor.UNDERLINE);
                    }
                    localStringBuilder.append(localTextPart.getText());
                }
                return localStringBuilder.toString();
            }
            return this.text;
        }
    }

    public class TextPart
    {
        private String color;
        private String text;
        private boolean bold;
        private boolean italic;
        private boolean underline;
        private boolean strikethrough;
        private boolean obfuscated;

        public TextPart()
        {
        }

        public String getColor()
        {
            if (this.color != null)
            {
                return this.color.toUpperCase();
            }
            return "WHITE";
        }

        public String getText()
        {
            return this.text;
        }

        public boolean isBold()
        {
            return this.bold;
        }

        public boolean isItalic()
        {
            return this.italic;
        }

        public boolean isUnderline()
        {
            return this.underline;
        }

        public boolean isStrikethrough()
        {
            return this.strikethrough;
        }

        public boolean isObfuscated()
        {
            return this.obfuscated;
        }
    }

    public class Players
    {
        private int max;
        private int online;
        private List<StatusResponse19.Player> sample;

        public Players()
        {
        }

        public int getMax()
        {
            return this.max;
        }

        public int getOnline()
        {
            return this.online;
        }

        public List<StatusResponse19.Player> getPlayers()
        {
            return this.sample;
        }

        public List<String> getPlayersByName()
        {
            ArrayList<String> localArrayList = new ArrayList<>();
            if ((this.sample != null) && (!this.sample.isEmpty()))
            {
                for (StatusResponse19.Player localPlayer : this.sample)
                {
                    localArrayList.add(localPlayer.getName());
                }
            }
            return localArrayList;
        }

        public List<UUID> getPlayersByUUID()
        {
            ArrayList<UUID> localArrayList = new ArrayList<UUID>();
            if ((this.sample != null) && (!this.sample.isEmpty()))
            {
                for (StatusResponse19.Player localPlayer : this.sample)
                {
                    localArrayList.add(UUID.fromString(localPlayer.getUuid()));
                }
            }
            return localArrayList;
        }
    }

    public class Player
    {
        private String name;
        private String uuid;

        public Player()
        {
        }

        public String getName()
        {
            return this.name;
        }

        public String getUuid()
        {
            return this.uuid;
        }
    }

    public class Version
    {
        private String name;
        private int protocol;

        public Version()
        {
        }

        public String getName()
        {
            return this.name;
        }

        public int getProtocol()
        {
            return this.protocol;
        }
    }
}

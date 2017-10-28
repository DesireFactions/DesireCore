package com.desiremc.core.tablist;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.MinecraftServer;
import net.minecraft.server.v1_7_R4.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_7_R4.PlayerInteractManager;
import net.minecraft.util.com.mojang.authlib.GameProfile;

public class Entry
{

    private Tab playerTab;
    private int x;
    private int y;
    private String text;
    private EntityPlayer nms;
    private Team team;
    private boolean setup;

    public Entry(Tab playerTab, String text, int x, int y)
    {
        this.playerTab = playerTab;
        this.text = text;
        this.x = x;
        this.y = y;
        playerTab.getEntries().add(this);
    }

    private Entry setup()
    {
        this.setup = true;
        Player player = this.playerTab.getPlayer();
        CraftPlayer craftplayer = (CraftPlayer) player;

        this.nms = new EntityPlayer(MinecraftServer.getServer(), ((CraftWorld) player.getWorld()).getHandle(), new GameProfile(UUID.randomUUID(), ChatColor.translateAlternateColorCodes('&', this.text)), new PlayerInteractManager(((CraftWorld) player.getWorld()).getHandle()));
        PacketPlayOutPlayerInfo packet = PacketPlayOutPlayerInfo.updateDisplayName(this.nms);

        craftplayer.getHandle().playerConnection.sendPacket(packet);
        this.team = this.playerTab.getScoreboard().registerNewTeam(UUID.randomUUID().toString().substring(0, 16));
        this.team.addEntry(this.nms.getName());
        return this;
    }

    public Entry send()
    {
        if (!this.setup)
        {
            return this.setup();
        }
        else
        {
            this.text = ChatColor.translateAlternateColorCodes('&', this.text);
            if (this.text.length() > 16)
            {
                this.team.setPrefix(this.text.substring(0, 16));
                String suffix = ChatColor.getLastColors(this.team.getPrefix()) + this.text.substring(16, this.text.length());

                if (suffix.length() > 16)
                {
                    if (suffix.length() <= 16)
                    {
                        suffix = this.text.substring(16, this.text.length());
                        this.team.setSuffix(suffix.substring(0, suffix.length()));
                    }
                    else
                    {
                        this.team.setSuffix(suffix.substring(0, 16));
                    }
                }
                else
                {
                    this.team.setSuffix(suffix);
                }
            }
            else
            {
                this.team.setPrefix(this.text);
                this.team.setSuffix("");
            }

            return this;
        }
    }

    public Tab playerTab()
    {
        return this.playerTab;
    }

    public int x()
    {
        return this.x;
    }

    public int y()
    {
        return this.y;
    }

    public String text()
    {
        return this.text;
    }

    public EntityPlayer nms()
    {
        return this.nms;
    }

    public Team team()
    {
        return this.team;
    }

    public Entry playerTab(Tab playerTab)
    {
        this.playerTab = playerTab;
        return this;
    }

    public Entry x(int x)
    {
        this.x = x;
        return this;
    }

    public Entry y(int y)
    {
        this.y = y;
        return this;
    }

    public Entry text(String text)
    {
        this.text = text;
        return this;
    }

    public Entry nms(EntityPlayer nms)
    {
        this.nms = nms;
        return this;
    }

    public Entry team(Team team)
    {
        this.team = team;
        return this;
    }

    public Entry setup(boolean setup)
    {
        this.setup = setup;
        return this;
    }
}
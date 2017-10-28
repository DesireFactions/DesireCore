package com.desiremc.core.tablist;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import com.desiremc.core.utils.ReflectionUtils;
import com.desiremc.core.utils.ReflectionUtils.NMSFields;

import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.MinecraftServer;
import net.minecraft.server.v1_7_R4.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_7_R4.PlayerConnection;
import net.minecraft.server.v1_7_R4.PlayerInteractManager;
import net.minecraft.server.v1_7_R4.TileEntitySkull;
import net.minecraft.server.v1_7_R4.WorldServer;
import net.minecraft.util.com.mojang.authlib.GameProfile;

public class Entry
{

    private Tab playerTab;

    private int x;
    private int y;
    private String text;
    private String skin;

    private EntityPlayer nms;
    private UUID uuid;

    private Team team;
    private boolean setup;

    public Entry(Tab playerTab, String text, int x, int y)
    {
        this(playerTab, text, x, y, null);
    }

    public Entry(Tab playerTab, String text, int x, int y, String skin)
    {
        this.playerTab = playerTab;
        this.text = text;
        this.x = x;
        this.y = y;
        this.skin = skin;
        playerTab.getEntries().add(this);
    }

    private Entry setup()
    {
        this.setup = true;
        Player player = this.playerTab.getPlayer();
        CraftPlayer craftplayer = (CraftPlayer) player;

        GameProfile profile = new GameProfile((uuid = UUID.randomUUID()), ChatColor.translateAlternateColorCodes('&', this.text));
        if (skin != null)
        {
            GameProfile skinProfile = TileEntitySkull.skinCache.getUnchecked(skin.toLowerCase());
            ReflectionUtils.setValue(skinProfile, NMSFields.gameProfileId, uuid);
            ReflectionUtils.setValue(skinProfile, NMSFields.gameProfileId, text);
            profile = skinProfile;
        }
        this.nms = new EntityPlayer(MinecraftServer.getServer(), ((CraftWorld) player.getWorld()).getHandle(), profile, new PlayerInteractManager(((CraftWorld) player.getWorld()).getHandle()));
        PacketPlayOutPlayerInfo packet = PacketPlayOutPlayerInfo.updateDisplayName(this.nms);

        craftplayer.getHandle().playerConnection.sendPacket(packet);
        this.team = this.playerTab.getScoreboard().registerNewTeam(uuid.toString().substring(0, 16));
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
            // translate the text color
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

    public Tab getPlayerTab()
    {
        return this.playerTab;
    }

    public Entry setPlayerTab(Tab playerTab)
    {
        this.playerTab = playerTab;
        return this;
    }

    public String getText()
    {
        return this.text;
    }

    public Entry setText(String text)
    {
        this.text = text;
        return this;
    }

    public String getSkin()
    {
        return skin;
    }

    public Entry setSkin(String skin)
    {
        this.skin = skin;
        GameProfile profile = nms.getProfile();
        ReflectionUtils.setValue(profile, NMSFields.gameProfileId, uuid);
        ReflectionUtils.setValue(profile, NMSFields.gameProfileId, text);
        ((CraftPlayer) playerTab.getPlayer()).getHandle().playerConnection.sendPacket(PacketPlayOutPlayerInfo.updateDisplayName(nms));
        return this;
    }

    public UUID getUniqueId()
    {
        return uuid;
    }

    public Entry setUniqueId(UUID uuid)
    {
        PlayerConnection pc = ((CraftPlayer) playerTab.getPlayer()).getHandle().playerConnection;
        pc.sendPacket(PacketPlayOutPlayerInfo.removePlayer(nms));

        this.uuid = uuid;
        GameProfile profile = new GameProfile(uuid, text);
        if (skin != null)
        {
            GameProfile skinProfile = TileEntitySkull.skinCache.getUnchecked(skin.toLowerCase());
            ReflectionUtils.setValue(skinProfile, NMSFields.gameProfileId, uuid);
            ReflectionUtils.setValue(skinProfile, NMSFields.gameProfileId, text);
            profile = skinProfile;
        }
        this.nms = new EntityPlayer(MinecraftServer.getServer(), (WorldServer) nms.world, profile, new PlayerInteractManager(((CraftWorld) playerTab.getPlayer().getWorld()).getHandle()));

        PacketPlayOutPlayerInfo packet = PacketPlayOutPlayerInfo.updateDisplayName(this.nms);

        pc.sendPacket(packet);
        this.team = this.playerTab.getScoreboard().registerNewTeam(uuid.toString().substring(0, 16));
        this.team.addEntry(this.nms.getName());
        return this;
    }

    public Team getTeam()
    {
        return this.team;
    }

    public Entry setTeam(Team team)
    {
        this.team = team;
        return this;
    }

    public int getX()
    {
        return this.x;
    }

    public Entry setX(int x)
    {
        this.x = x;
        return this;
    }

    public int getY()
    {
        return this.y;
    }

    public Entry setY(int y)
    {
        this.y = y;
        return this;
    }

    public EntityPlayer getNms()
    {
        return this.nms;
    }

    public Entry setNms(EntityPlayer nms)
    {
        this.nms = nms;
        return this;
    }

    public Entry setup(boolean setup)
    {
        this.setup = setup;
        return this;
    }
}
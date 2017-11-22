package com.desiremc.core.tablistfour;

import java.io.IOException;

import net.minecraft.server.v1_7_R4.PacketDataSerializer;
import net.minecraft.server.v1_7_R4.PacketPlayOutListener;
import net.minecraft.server.v1_7_R4.PacketPlayOutPlayerInfo;

public class CustomPacketPlayOutPlayerInfo extends PacketPlayOutPlayerInfo
{
    private String a;
    private boolean b;
    private int c;

    public CustomPacketPlayOutPlayerInfo()
    {
    }

    public CustomPacketPlayOutPlayerInfo(String paramString, boolean paramBoolean, int paramInt)
    {
        this.a = paramString;
        this.b = paramBoolean;
        this.c = paramInt;
    }

    public void a(PacketDataSerializer paramPacketDataSerializer)
    {
        try
        {
            this.a = paramPacketDataSerializer.c(16);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        this.b = paramPacketDataSerializer.readBoolean();
        this.c = paramPacketDataSerializer.readShort();
    }

    public void b(PacketDataSerializer paramPacketDataSerializer)
    {
        try
        {
            paramPacketDataSerializer.a(this.a);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        paramPacketDataSerializer.writeBoolean(this.b);
        paramPacketDataSerializer.writeShort(this.c);
    }

    public void a(PacketPlayOutListener paramPacketPlayOutListener)
    {
        paramPacketPlayOutListener.a(this);
    }
}

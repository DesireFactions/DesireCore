package com.desiremc.core.bungee;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.Charset;

import net.minecraft.util.com.google.gson.Gson;
import net.minecraft.util.com.google.gson.JsonSyntaxException;

public class LoadData
{
    private static final Gson gson = new Gson();

    public int readVarInt(DataInputStream paramDataInputStream) throws IOException
    {
        int i = 0;
        int j = 0;
        int k;
        do
        {
            k = paramDataInputStream.readByte();
            i |= (k & 0x7F) << j++ * 7;
            if (j > 5)
            {
                throw new RuntimeException("VarInt too big");
            }
        } while ((k & 0x80) == 128);
        return i;
    }

    public void writeVarInt(DataOutputStream paramDataOutputStream, int paramInt) throws IOException
    {
        for (;;)
        {
            if ((paramInt & 0xFFFFFF80) == 0)
            {
                paramDataOutputStream.writeByte(paramInt);
                return;
            }
            paramDataOutputStream.writeByte(paramInt & 0x7F | 0x80);
            paramInt >>>= 7;
        }
    }

    public StatusResponse fetchData(InetSocketAddress paramInetSocketAddress, int paramInt) throws IOException
    {
        Socket localSocket = new Socket();

        localSocket.setSoTimeout(paramInt);
        localSocket.connect(paramInetSocketAddress, paramInt);

        OutputStream localOutputStream = localSocket.getOutputStream();
        DataOutputStream localDataOutputStream1 = new DataOutputStream(localOutputStream);

        InputStream localInputStream = localSocket.getInputStream();
        InputStreamReader localInputStreamReader = new InputStreamReader(localInputStream);

        ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream localDataOutputStream2 = new DataOutputStream(localByteArrayOutputStream);
        localDataOutputStream2.writeByte(0);
        writeVarInt(localDataOutputStream2, 4);
        writeVarInt(localDataOutputStream2, paramInetSocketAddress.getHostString().length());
        localDataOutputStream2.writeBytes(paramInetSocketAddress.getHostString());
        localDataOutputStream2.writeShort(paramInetSocketAddress.getPort());
        writeVarInt(localDataOutputStream2, 1);

        writeVarInt(localDataOutputStream1, localByteArrayOutputStream.size());
        localDataOutputStream1.write(localByteArrayOutputStream.toByteArray());

        localDataOutputStream1.writeByte(1);
        localDataOutputStream1.writeByte(0);

        DataInputStream localDataInputStream = new DataInputStream(localInputStream);
        readVarInt(localDataInputStream);
        int i = readVarInt(localDataInputStream);
        if (i == -1)
        {
            localSocket.close();
            throw new IOException("Premature end of stream.");
        }
        if (i != 0)
        {
            localSocket.close();
            throw new IOException("Invalid packetID");
        }
        int j = readVarInt(localDataInputStream);
        if (j == -1)
        {
            localSocket.close();
            throw new IOException("Premature end of stream.");
        }
        if (j == 0)
        {
            localSocket.close();
            throw new IOException("Invalid string length.");
        }
        byte[] arrayOfByte = new byte[j];
        localDataInputStream.readFully(arrayOfByte);

        String str = new String(arrayOfByte, Charset.forName("utf-8"));

        long l = System.currentTimeMillis();
        localDataOutputStream1.writeByte(9);
        localDataOutputStream1.writeByte(1);
        localDataOutputStream1.writeLong(l);

        readVarInt(localDataInputStream);
        i = readVarInt(localDataInputStream);
        if (i == -1)
        {
            localSocket.close();
            throw new IOException("Premature end of stream.");
        }
        if (i != 1)
        {
            localSocket.close();
            throw new IOException("Invalid packetID");
        }
        localDataOutputStream1.close();
        localOutputStream.close();
        localInputStreamReader.close();
        localInputStream.close();
        localSocket.close();
        try
        {
            return (StatusResponse) gson.fromJson(str, StatusResponse19.class);
        }
        catch (JsonSyntaxException localJsonSyntaxException1)
        {
            try
            {
                return (StatusResponse) gson.fromJson(str, StatusResponse17.class);
            }
            catch (JsonSyntaxException localJsonSyntaxException2)
            {
                return null;
            }
        }
    }
}
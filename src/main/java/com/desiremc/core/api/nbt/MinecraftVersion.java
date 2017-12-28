package com.desiremc.core.api.nbt;

public enum MinecraftVersion
{
    Unknown(0),
    MC1_7_R4(174),
    MC1_8_R3(183),
    MC1_9_R1(191),
    MC1_9_R2(192),
    MC1_10_R1(1101),
    MC1_11_R1(1111),
    MC1_12_R1(1121);

    MinecraftVersion(int id)
    {
        this.id = id;
    }

    private final int id;

    public int getId()
    {
        return id;
    }

    public static MinecraftVersion getVersion()
    {
        return MC1_7_R4;
    }

    private static Boolean cache = null;

    public static boolean hasGson()
    {
        if (cache != null)
            return cache;
        cache = false;
        try
        {
            System.out.println("Found Gson: " + Class.forName("com.google.gson.Gson"));
            cache = true;
            return cache;
        }
        catch (Exception ex)
        {
            return cache;
        }
    }

}
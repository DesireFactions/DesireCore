package com.desiremc.core.session;

import org.mongodb.morphia.annotations.Embedded;

@Embedded
public class OreData
{

    private int worldNumber;
    private int emerald;
    private int diamond;
    private int gold;
    private int lapis;
    private int redstone;
    private int iron;
    private int coal;

    public OreData()
    {

    }

    /**
     * @return the worldNumber
     */
    public int getWorldNumber()
    {
        return worldNumber;
    }

    /**
     * @param amount the amount to add to worldNumber
     */
    public void addWorldNumber(int amount)
    {
        worldNumber += amount;
    }

    /**
     * @param worldNumber the worldNumber to set
     */
    public void setWorldNumber(int worldNumber)
    {
        this.worldNumber = worldNumber;
    }

    /**
     * @return the emerald
     */
    public int getEmerald()
    {
        return emerald;
    }

    /**
     * @param amount the amount to add to emerald
     */
    public void addEmerald(int amount)
    {
        emerald += amount;
    }

    /**
     * @param emerald the emerald to set
     */
    public void setEmerald(int emerald)
    {
        this.emerald = emerald;
    }

    /**
     * @return the diamond
     */
    public int getDiamond()
    {
        return diamond;
    }

    /**
     * @param amount the amount to add to diamond
     */
    public void addDiamond(int amount)
    {
        diamond += amount;
    }

    /**
     * @param diamond the diamond to set
     */
    public void setDiamond(int diamond)
    {
        this.diamond = diamond;
    }

    /**
     * @return the gold
     */
    public int getGold()
    {
        return gold;
    }

    /**
     * @param amount the amount to add to gold
     */
    public void addGold(int amount)
    {
        gold += amount;
    }

    /**
     * @param gold the gold to set
     */
    public void setGold(int gold)
    {
        this.gold = gold;
    }

    /**
     * @return the lapis
     */
    public int getLapis()
    {
        return lapis;
    }

    /**
     * @param amount the amount to add to lapis
     */
    public void addLapis(int amount)
    {
        lapis += amount;
    }

    /**
     * @param lapis the lapis to set
     */
    public void setLapis(int lapis)
    {
        this.lapis = lapis;
    }

    /**
     * @return the redstone
     */
    public int getRedstone()
    {
        return redstone;
    }

    /**
     * @param amount the amount to add to redstone
     */
    public void addRedstone(int amount)
    {
        redstone += amount;
    }

    /**
     * @param redstone the redstone to set
     */
    public void setRedstone(int redstone)
    {
        this.redstone = redstone;
    }

    /**
     * @return the iron
     */
    public int getIron()
    {
        return iron;
    }

    /**
     * @param amount the amount to add to iron
     */
    public void addIron(int amount)
    {
        iron += amount;
    }

    /**
     * @param iron the iron to set
     */
    public void setIron(int iron)
    {
        this.iron = iron;
    }

    /**
     * @return the coal
     */
    public int getCoal()
    {
        return coal;
    }

    /**
     * @param amount the amount to add to coal
     */
    public void addCoal(int amount)
    {
        coal += amount;
    }

    /**
     * @param coal the coal to set
     */
    public void setCoal(int coal)
    {
        this.coal = coal;
    }

}

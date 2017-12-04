package com.desiremc.core.api;

import com.desiremc.core.utils.StringUtils;

public enum WeatherType
{
    SUN,
    RAIN;
    
    public static WeatherType getByName(String name)
    {
        name = name.toLowerCase();
        if (StringUtils.containsAny(name, "sun", "sunny", "clear"))
        {
            return SUN;
        }
        else if (StringUtils.containsAny(name, "rain", "storm", "rainy", "raining", "stormy", "storming"))
        {
            return RAIN;
        }
        else
        {
            return null;
        }
    }
}

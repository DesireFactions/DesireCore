package com.desiremc.core.api;

import org.bukkit.Location;
import org.mongodb.morphia.converters.TypeConverter;
import org.mongodb.morphia.mapping.MappedField;

import com.desiremc.core.utils.Utils;

public class LocationTypeConverter extends TypeConverter
{

    public LocationTypeConverter()
    {
        super(Location.class);
    }

    @Override
    public Object decode(Class<?> arg0, Object data, MappedField arg2)
    {
        return Utils.toLocation((String) data);
    }

    @Override
    public Object encode(Object value, MappedField optionalExtraInfo)
    {
        if (!(value instanceof Location))
        {
            return null;
        }
        Location loc = (Location) value;
        return Utils.toString(loc);
    }

}

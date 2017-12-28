package com.desiremc.core.parsers;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.newcommands.Parser;
import com.desiremc.core.session.Session;

public class MaterialDataParser implements Parser<MaterialData>
{

    @Override
    public MaterialData parseArgument(Session sender, String[] label, String rawArgument)
    {
        ItemStack is = DesireCore.getItemHandler().get(rawArgument);

        if (is == null)
        {
            sender.sendMessage(DesireCore.getLangHandler().getString("invalid_item"));
            return null;
        }

        return is.getData();
    }

    @Override
    public List<String> getRecommendations(Session sender, String lastWord)
    {
        lastWord = lastWord.toLowerCase();
        List<String> materials = new LinkedList<>();
        for (Material material : Material.values())
        {
            if (material.name().toLowerCase().startsWith(lastWord))
            {
                materials.add(material.name());
            }
        }
        return materials;
    }

}

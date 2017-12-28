package com.desiremc.core.parsers;

import java.util.LinkedList;
import java.util.List;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.newcommands.Parser;
import com.desiremc.core.session.Session;
import com.desiremc.core.session.SessionSetting;

public class SessionSettingParser implements Parser<SessionSetting>
{

    @Override
    public SessionSetting parseArgument(Session sender, String[] label, String rawArgument)
    {
        SessionSetting setting = SessionSetting.getValue(rawArgument);
        if (setting == null)
        {
            DesireCore.getLangHandler().sendRenderMessage(sender, "settings.not_found", true, false);
            return null;
        }
        return setting;
    }

    @Override
    public List<String> getRecommendations(Session sender, String lastWord)
    {
        List<String> values = new LinkedList<>();
        for (SessionSetting setting : SessionSetting.enabledValues())
        {
            if (setting.getRank().getId() <= sender.getRank().getId())
            {
                values.add(setting.getDisplayName());
            }
        }
        Parser.pruneSuggestions(values, lastWord);
        return values;
    }

}

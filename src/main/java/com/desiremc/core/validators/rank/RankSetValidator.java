package com.desiremc.core.validators.rank;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.newcommands.Validator;
import com.desiremc.core.session.Rank;
import com.desiremc.core.session.Session;

public class RankSetValidator implements Validator<Rank>
{

    @Override
    public boolean validateArgument(Session sender, String[] label, Rank arg)
    {
        if (sender.getRank().getId() >= arg.getId())
        {
            return true;
        }
        else
        {
            DesireCore.getLangHandler().sendRenderMessage(sender, "rank_too_high");
            return false;
        }
    }

}

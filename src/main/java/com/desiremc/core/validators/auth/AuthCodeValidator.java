package com.desiremc.core.validators.auth;

import com.desiremc.core.DesireCore;
import com.desiremc.core.api.newcommands.Validator;
import com.desiremc.core.session.Session;
import com.warrenstrange.googleauth.GoogleAuthenticator;

public class AuthCodeValidator implements Validator<Integer>
{

    private GoogleAuthenticator auth = new GoogleAuthenticator();

    @Override
    public boolean validateArgument(Session sender, String[] label, Integer arg)
    {
        Integer authCode = (Integer) arg;
        String key = sender.getAuthkey();

        boolean validAuth = auth.authorize(key, authCode);

        if (!validAuth)
        {
            DesireCore.getLangHandler().sendRenderMessage(sender, "auth.invalid-code");
        }

        return validAuth;
    }

}

package com.desiremc.core.api.newcommands;

import java.util.Iterator;
import java.util.List;

import com.desiremc.core.session.Session;
import com.desiremc.core.utils.CollectionUtils;

public interface Parser<T>
{

    /**
     * Parses the argument from the raw string into the appropriate type. If the argument can't be parsed
     * 
     * @param sender the sender of the command.
     * @param label the label of the command
     * @param argument the argument to be parsed.
     * @return the successfully parsed argument.
     */
    public T parseArgument(Session sender, String[] label, String rawArgument);

    /**
     * Get tab complete recommendations for an argument with this given parser. If the default is wanted, it exists in
     * {@link CommandHandler#defaultTabComplete(Session, String)}.
     * 
     * @param sender the sender of the tab complete.
     * @param str the content of the item so far.
     * @return the recommendations.
     */
    public List<String> getRecommendations(Session sender, String lastWord);

    public static void pruneSuggestions(List<String> values, String lastWord)
    {
        if (values == null || values.size() == 0 || CollectionUtils.isImmutable(values))
        {
            return;
        }
        lastWord = lastWord.toLowerCase();
        Iterator<String> it = values.iterator();
        while (it.hasNext())
        {
            if (!it.next().toLowerCase().startsWith(lastWord))
            {
                it.remove();
            }
        }
    }

}

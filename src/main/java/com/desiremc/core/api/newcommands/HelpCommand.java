package com.desiremc.core.api.newcommands;

import java.util.List;

import com.desiremc.core.DesireCore;
import com.desiremc.core.parsers.PositiveIntegerParser;
import com.desiremc.core.session.Session;
import com.desiremc.core.utils.StringUtils;

public class HelpCommand extends ValidCommand
{

    private ValidBaseCommand baseCommand;

    HelpCommand(ValidBaseCommand baseCommand)
    {
        super("help", "View all sub commands.", new String[] { "?" });

        this.baseCommand = baseCommand;

        addArgument(CommandArgumentBuilder.createBuilder(Integer.class)
                .setName("page")
                .setParser(new PositiveIntegerParser())
                .addValidator(new HelpPageValidator(baseCommand))
                .setOptional()
                .build());
    }

    @Override
    public void validRun(Session sender, String[] label, List<CommandArgument<?>> arguments)
    {
        List<ValidCommand> usable = baseCommand.getUsableCommands(sender);

        if (usable.size() == 0)
        {
            DesireCore.getLangHandler().sendRenderMessage(sender, "no_subs", true, false);
            return;
        }

        int page = arguments.get(0).hasValue() ? (Integer) arguments.get(0).getValue() : 1;
        int pages = (baseCommand.getUsableCommands(sender).size() + 5) / 6;

        DesireCore.getLangHandler().sendRenderMessage(sender, "list-header", false, false);
        DesireCore.getLangHandler().sendRenderMessage(sender, "help_pages", false, false,
                "{command}", StringUtils.compile(label),
                "{current}", page,
                "{max}", pages);
        for (int i = 0 + (page * 6); i < Math.min(usable.size(), 6 + (page * 6)); i++)
        {
            sender.sendMessage(" §b/" + StringUtils.compile(label) + " " + usable.get(i).getName() + ": §7" + usable.get(i).getDescription());
        }
    }

}

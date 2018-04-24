package fr.hadriel.util.command;

import java.util.HashMap;
import java.util.Map;

public abstract class CommandRegistry<Context> implements ICommand<Context> {

    protected final Map<String, ICommand<Context>> commands = new HashMap<>();

    public void register(String name, ICommand<Context> lambda) {
        if (lambda != null)
            commands.put(name, lambda);
        else
            commands.remove(name);
    }

    public void execute(String input) {
        Context context = parse(input);
        execute(context);
    }

    //This should parse input into a Context and a CommandName.
    public abstract Context parse(String input);
    public abstract void execute(Context context);
}
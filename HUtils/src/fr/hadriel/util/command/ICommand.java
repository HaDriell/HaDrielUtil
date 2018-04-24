package fr.hadriel.util.command;

@FunctionalInterface
public interface ICommand<Context> {
    public void execute(Context context);
}

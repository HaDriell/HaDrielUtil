package fr.hadriel.util.command;


import java.util.Arrays;

public class CommandRegistryExample {

    //Simple Context Implementation
    private static class MyContext {
        public final String command;
        public final String[] arguments;

        public MyContext(String command, String[] arguments) {
            this.command = command;
            this.arguments = arguments;
        }
    }

    //Simple Registry Implementation
    private static class MyRegistry extends CommandRegistry<MyContext> {

        public MyContext parse(String input) {
            String[] args = input.split("\\s+"); // split by whitespace (not the best yet easy to implement)
            String command = args[0]; // first argument in line
            String[] arguments = Arrays.copyOfRange(args, 1, args.length); // removes args[0] which we'll store in command
            return new MyContext(command, arguments);
        }

        public void execute(MyContext myContext) {
            ICommand<MyContext> command = commands.get(myContext.command);
            if (command != null) command.execute(myContext);
        }
    }

    public static void main(String... args) {
        MyRegistry registry = new MyRegistry();
        registry.register("echo", ctx -> {
            System.out.println("Echoing " + String.join(" ", ctx.arguments));
        });

        registry.register("quit", ctx -> {
            System.exit(0);
        });

        registry.execute("echo this is a command");
        registry.execute("quit");
    }
}

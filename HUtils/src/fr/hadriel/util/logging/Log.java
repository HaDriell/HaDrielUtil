package fr.hadriel.util.logging;

import java.util.logging.*;

public class Log {
    public static Logger getLogger(Class clazz) {
        return getLogger(clazz.getSimpleName());
    }
    public static Logger getLogger(String name) {
        Logger logger = Logger.getLogger(name);
        logger.setUseParentHandlers(false);
        logger.setLevel(Level.CONFIG);

        ConsoleHandler handler = new ConsoleHandler();
        handler.setFormatter(new DefaultFormatter());

        logger.addHandler(handler);
        return logger;
    }
}
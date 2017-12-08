package fr.hadriel.application;

/**
 *
 * @author glathuiliere
 */
public final class Launcher {
    private Launcher() {}

    public static void launch(Class<? extends IHDUApplication> applicationClass) {
        launch(applicationClass, new String[]{});
    }

    public static void launch(Class<? extends IHDUApplication> applicationClass, String[] args) {
        try {
            launch(applicationClass.newInstance(), args);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void launch(IHDUApplication application, String[] args) {
        application.run(args);
    }
}
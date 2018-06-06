package fr.hadriel.application;

import fr.hadriel.asset.AssetManager;
import fr.hadriel.asset.graphics.WindowHint;
import fr.hadriel.g2d.Renderer;
import fr.hadriel.util.Timer;
import fr.hadriel.util.logging.Log;

import java.util.logging.Logger;

public abstract class Application {
    protected static final Logger logger = Log.getLogger(Application.class);

    private static final int LAUNCHING   = 0b001;
    private static final int UPDATING    = 0b010;
    private static final int TERMINATING = 0b100;

    private long applicationState;

    protected final AssetManager manager;

    protected Application() {
        this.applicationState = LAUNCHING;
        this.manager = new AssetManager();
    }

    public void close() {
        if(applicationState == UPDATING)
            applicationState = TERMINATING;
    }

    public boolean isLaunching() {
        return applicationState == LAUNCHING;
    }

    public boolean isUpdating() {
        return applicationState == UPDATING;
    }

    public boolean isTerminating() {
        return applicationState == TERMINATING;
    }

    private void _init(WindowHint hint, String[] args) {
        logger.fine("Initializing Application Graphics");
        //Graphics INIT
        Graphic2D.create(hint);

        logger.fine("Initializing Application Audio");
        //Audio INIT
        Audio2D.initialize();
        applicationState = UPDATING;

        logger.fine("Starting Application");
        // End-User INIT
        start(args);
        Graphic2D.show();
    }

    private void _update(float delta) {
        if(Graphic2D.shouldClose()) {
            close();
            return;
        }
        Graphic2D.makeContextCurrent();
        Renderer renderer = Graphic2D.getRenderer();
        update(renderer, delta);
        Graphic2D.update();
    }

    private void _terminate() {
        logger.fine("Terminating Application");
        terminate();

        logger.fine("Uninitializng Audio");
        Audio2D.terminate();

        logger.fine("Uninitializng Graohics");
        Graphic2D.terminate();
    }

    protected abstract void start(String[] args);
    protected abstract void update(Renderer renderer, float delta);
    protected abstract void terminate();


    // Launchers

    public static void launch(Application application) {
        launch(application, new WindowHint());
    }

    public static void launch(Application application, WindowHint hint) {
        launch(application, hint, new String[] {});
    }

    public static void launch(Application application, WindowHint hint, String[] args) {
        application._init(hint, args);
        Timer timer = new Timer();
        while (!application.isTerminating()) {
            float delta = timer.elapsed();
            timer.reset();
            application._update(delta);
        }
        application._terminate();
    }
}
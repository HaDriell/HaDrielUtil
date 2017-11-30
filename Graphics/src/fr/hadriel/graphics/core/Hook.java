package fr.hadriel.graphics.core;

/**
 * Created by glathuiliere on 20/07/2017.
 */
public abstract class Hook {

    private boolean initialized;

    public void initialize() {
        if(!initialized) {
            onInit();
            initialized = true;
        }
    }

    public void terminate() {
        if(initialized) {
            onTerminate();
            initialized = false;
        }
    }


    /**
     * Condition that allows the GraphicsThread to stop itself. Doesn't unregister the Hook !
     * @return
     */
    public  abstract boolean isIdle();

    /**
     * Called once when the Hook runs it's first Frame
     */
    protected abstract void onInit();

    /**
     * Called every Frame
     */
    public abstract void update();

    /**
     * Called when the GraphicsThread terminates or when the Hook is removed from the GrpahicsThread
     */
    protected abstract void onTerminate();
}
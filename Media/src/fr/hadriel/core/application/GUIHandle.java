package fr.hadriel.core.application;

/**
 * Executed on the GraphicsThread (Main thread)
 * @author glathuiliere
 */
public abstract class GUIHandle implements GUITask {

    public static final byte INIT = 0;
    public static final byte EXECUTE = 1;
    public static final byte DESTROY = 2;
    public static final byte GARBAGE = 3;

    private byte state = INIT;

    public boolean execute(float dt) {
        switch (state) {
                //First time running, onInit() is ran
            case INIT:
                onInit();
                state = EXECUTE;
                break;

                //Main running state
            case EXECUTE:
                if(onUpdate(dt))
                    state = DESTROY;
                break;

                //Handle is destroying
            case DESTROY:
                onDestroy();
                state = GARBAGE;
                break;

                //Destroyed (or invalid state) Handle, should remove from GUITasks
            default:
            case GARBAGE:
                return true;
        }
        return false;
    }

    public void destroy() {
        state = DESTROY;
    }

    /**
     * Called  once before the Handle is executed
     */
    protected abstract void onInit();

    /**
     * Called every frame after INIT is done
     * @param dt the elapsed time since the last frame
     * @return true if the Handle destroys itself
     */
    protected abstract boolean onUpdate(float dt);

    /**
     * Called once before the Handle is trashed
     */
    protected abstract void onDestroy();
}

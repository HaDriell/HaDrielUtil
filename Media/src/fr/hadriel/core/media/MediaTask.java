package fr.hadriel.core.media;

/**
 *
 * @author glathuiliere
 */
public abstract class MediaTask {

    public static final byte STARTING = 0;
    public static final byte EXECUTING = 1;
    public static final byte TERMINATING = 2;

    private byte state = STARTING;

    public boolean execute(float dt) {
        if(state == TERMINATING)
            return true;

        if(state == STARTING) {
            onBegin();
            state = EXECUTING;
        }

        if(state == EXECUTING) {
            if(onExecute(dt)) {
                onEnd();
                state = TERMINATING;
            }
        }
        return false;
    }

    /**
     * called before the action's first update
     */
    public abstract void onBegin();

    /**
     * called in a loop fashion by the Medias.Worker
     * @return true if the action is done executing
     */
    public abstract boolean onExecute(float dt);

    /**
     * called after the action's execution
     */
    public abstract void onEnd();
}
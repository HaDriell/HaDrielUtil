package fr.hadriel.core.media;

/**
 *
 * @author glathuiliere
 */
public abstract class MediaTask {

    public static final byte STARTING       = 0b001;
    public static final byte EXECUTING      = 0b010;
    public static final byte TERMINATING    = 0b100;

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

    public boolean isExecuting() {
        return state == EXECUTING;
    }

    public boolean isTerminating() {
        return state == TERMINATING;
    }

    public boolean isStarting() {
        return state == STARTING;
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
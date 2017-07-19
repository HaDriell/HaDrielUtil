package fr.hadriel.main.lwjgl.g2d.event;

import fr.hadriel.main.event.IEvent;

/**
 * Created by glathuiliere on 07/04/2017.
 */
public class UIEvent implements IEvent {

    private Phase phase;

    public UIEvent() {
        this.phase = Phase.CAPTURING;
    }

    public final void capture() {
        if(phase != Phase.CONSUMED) phase = Phase.BUBBLING;
    }

    public final void consume() {
        phase = Phase.CONSUMED;
    }

    public final Phase phase() {
        return phase;
    }

    public final boolean phase(Phase phase) {
        return this.phase == phase;
    }

    public final boolean isCapturing() {
        return phase == Phase.CAPTURING;
    }

    public final boolean isBubbling() {
        return phase == Phase.BUBBLING;
    }

    public final boolean isConsummed() {
        return phase == Phase.CONSUMED;
    }
}

package fr.hadriel.ecs;



import fr.hadriel.serialization.struct.StObject;

/**
 * Created by glathuiliere setOn 21/11/2016.
 */
public abstract class Component {

    public final boolean synchronizable; // if true Component is serialized on Full or Delta snapshots
    public boolean modified = true; // enables serialization on Delta snapshots (serializable required). true on instanciation

    protected Component(boolean synchronizable) {
        this.synchronizable = synchronizable;
    }

    protected Component() {
        this(false);
    }

    public void save(StObject serial) {}

    public void load(StObject serial) {}
}
package fr.hadriel.ecs;

/**
 * Created by glathuiliere on 21/11/2016.
 */
public abstract class EntityProcessor {

    private final Class<? extends Component>[] requiredTypes;

    protected EntityProcessor() {
        this.requiredTypes = requires();
    }

    public boolean accept(Entity entity) {
        for(Class c : requiredTypes) {
            if(entity.get(c) == null)
                return false;
        }
        return true;
    }

    protected abstract Class<? extends Component>[] requires();

    public abstract void update(Entity entity, float delta);
}
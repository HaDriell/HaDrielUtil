package fr.hadriel.ecs;

/**
 * Created by glathuiliere setOn 21/11/2016.
 */
public abstract class EntityProcessor {

    private final Class[] requiredTypes;

    protected EntityProcessor(Class[] requiredTypes) {
        if(requiredTypes != null) {
            for (Class c : requiredTypes) {
                if (c == null) throw new IllegalArgumentException("At least one Class is null");
                if (!Component.class.isAssignableFrom(c))
                    throw new IllegalArgumentException("Class " + c.getName() + " is not an extension of " + Component.class.getName());
            }
        }
        this.requiredTypes = requiredTypes;
    }

    // accept is revelant only if requiredTypes is not null.
    public boolean accept(Entity entity) {
        if(requiredTypes != null) {
            for (Class c : requiredTypes) {
                if (entity.getComponent(c) == null)
                    return false;
            }
        }
        return true;
    }


    public abstract void beforeUpdate(float delta);
    public abstract void update(Entity entity, float delta);
    public abstract void afterUpdate(float delta);
}
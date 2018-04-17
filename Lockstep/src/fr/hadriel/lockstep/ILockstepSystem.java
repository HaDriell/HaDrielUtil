package fr.hadriel.lockstep;

public abstract strictfp interface ILockstepSystem {

    /**
     * Called when the LockstepSystem is added to the LockstepEngine
     */
    public void load();

    /**
     * Called when the LockstepSystem is removed from the LockstepEngine
     */
    public void unload();

    /**
     * Called when an entity is created in the LockstepEngine
     * @param entity the entity reference
     */
    public void entityCreated(Entity entity);

    /**
     * Called when an entity signature is modified
     * @param entity
     */
    public void entityModified(Entity entity);

    /**
     * Called when an entity is destroyed
     * @param entity
     */
    public void entityDestroyed(Entity entity);


    /**
     * Called every time the LockstepEngine step a new step.
     * @param deltaTime deltaTime for the current step
     */
    public void step(float deltaTime);
}
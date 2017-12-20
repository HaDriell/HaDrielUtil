package fr.hadriel.ecs;

public strictfp interface IEntityProcessor {

    public void onEntityRegistered(long id);

    public void begin();

    public void process(long id, IEntityManager manager, float deltaTime);

    public void end();

    public void onEntityUnregistered(long id);
}
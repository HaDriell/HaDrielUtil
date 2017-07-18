package fr.hadriel.main.ecs;

/**
 * Created by glathuiliere on 30/03/2017.
 */
public interface EntityDataManager {

    public void createEntity(long id);

    public void destroyEntity(long id);

    public Object getComponent(long id, String name);

    public <T> T getComponent(long id, String name, Class<T> type);

    public void setComponent(long id, String name, Object component);
}
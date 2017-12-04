package fr.hadriel.ecs;

import java.util.Map;

public interface IEntityManager {

    public long create();

    public void destroy(long id);

    public boolean exists(long id);

    public <T> boolean hasComponent(long id, Class<T> type);

    public <T> void setComponent(long id, T component);

    public <T> boolean removeComponent(long id, Class<T> type);

    public <T> T getComponent(long id, Class<T> type);

    public Map<Class, Object> getComponents(long id);
}
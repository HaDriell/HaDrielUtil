package fr.hadriel.ecs;

import fr.hadriel.ecs.filter.EntityMapper;
import fr.hadriel.ecs.filter.IFilter;

/**
 * Created by glathuiliere on 30/03/2017.
 */
public abstract class EntitySystem {

    private EntityMapper mapper;

    public EntitySystem(IFilter... filters) {
        this.mapper = new EntityMapper(filters);
    }

    //package-private
    final void map(long id, EntityData data) {
        mapper.map(id, data);
    }

    //package-private
    final void unmap(long id) {
        mapper.unmap(id);
    }

    public final void update(ECSEngine engine, float delta) {
        begin();
        mapper.getEntityStream().forEach((id) -> update(engine, id, delta));
        end();
    }

    protected abstract void begin();
    protected abstract void update(EntityDataManager manager, long entityID, float delta);
    protected abstract void end();
}
package fr.hadriel;

import fr.hadriel.lockstep.Entity;
import fr.hadriel.lockstep.EntityMapper;
import fr.hadriel.lockstep.EntityProfile;
import fr.hadriel.lockstep.IComponent;

public class TestMapper {

    public static final class ComponentA implements IComponent { }
    public static final class ComponentB implements IComponent { }
    public static final class ComponentC implements IComponent { }

    public static void main(String[] args) {
        EntityMapper mapper1 = new EntityMapper(EntityProfile.include(ComponentA.class));
        EntityMapper mapper2 = new EntityMapper(EntityProfile.include(ComponentB.class));
        EntityMapper mapper3 = new EntityMapper(EntityProfile.include(ComponentC.class));

        Entity entity = new Entity(0, new ComponentA(), new ComponentB());

        mapper1.map(entity);
        mapper2.map(entity);
        mapper3.map(entity);

        if(mapper1.entities().count() == 0)
            throw new RuntimeException("Should contain Entity");
        if(mapper2.entities().count() == 0)
            throw new RuntimeException("Should contain Entity");
        if(mapper3.entities().count() == 1)
            throw new RuntimeException("Should not contain Entity");

        entity.removeComponent(ComponentA.class);
        entity.setComponent(new ComponentC());

        mapper1.map(entity);
        mapper2.map(entity);
        mapper3.map(entity);

        if(mapper1.entities().count() == 1)
            throw new RuntimeException("Should not contain Entity");
        if(mapper2.entities().count() == 0)
            throw new RuntimeException("Should contain Entity");
        if(mapper3.entities().count() == 0)
            throw new RuntimeException("Should contain Entity");
    }
}

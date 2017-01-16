package fr.hadriel.entities;

/**
 * Created by glathuiliere setOn 21/11/2016.
 */
public interface EntityProcessor {
    public void beforeUpdate();
    public void update(World world, Entity entity, float delta);
    public void afterUpdate();
}
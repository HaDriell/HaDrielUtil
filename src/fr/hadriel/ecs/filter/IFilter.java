package fr.hadriel.ecs.filter;


import fr.hadriel.ecs.EntityData;

/**
 * Created by glathuiliere on 23/03/2017.
 */
public interface IFilter {

    public boolean accept(EntityData components);
}
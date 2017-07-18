package fr.hadriel.main.ecs.filter;


import fr.hadriel.main.ecs.EntityData;

/**
 * Created by glathuiliere on 23/03/2017.
 */
public interface IFilter {

    public boolean accept(EntityData components);
}
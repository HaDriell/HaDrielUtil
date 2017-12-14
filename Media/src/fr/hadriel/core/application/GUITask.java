package fr.hadriel.core.application;

/**
 * Created by glathuiliere on 20/07/2017.
 */
@FunctionalInterface
public interface GUITask {

    /**
     * executed every frame
     * @param dt delta time since the last frame
     * @return true if the GUITask should be removed
     */
    public boolean execute(float dt);
}
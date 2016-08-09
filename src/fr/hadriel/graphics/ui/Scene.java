package fr.hadriel.graphics.ui;

import fr.hadriel.event.IEvent;
import fr.hadriel.event.IEventListener;
import fr.hadriel.graphics.Transform;

/**
 * Created by glathuiliere on 09/08/2016.
 */
public class Scene implements IEventListener {

    private Transform transform;

    public Transform getTransform() {
        return transform;
    }

    public void setTransform(Transform transform) {
        this.transform = transform == null ? new Transform() : transform;
    }

    public void onEvent(IEvent event) {

    }
}
package fr.hadriel.hgl.core.events;

import fr.hadriel.hgl.core.HGLContext;

/**
 * Created by glathuiliere on 28/11/2016.
 */
public class HGLContextDestroyed extends HGLEvent {

    public final HGLContext context;

    public HGLContextDestroyed(HGLContext context) {
        this.context = context;
    }
}
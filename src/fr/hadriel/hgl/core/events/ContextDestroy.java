package fr.hadriel.hgl.core.events;

import fr.hadriel.hgl.core.HGLContext;

/**
 * Created by glathuiliere on 28/11/2016.
 */
public class ContextDestroy extends HGLEvent {

    public final HGLContext context;

    public ContextDestroy(HGLContext context) {
        this.context = context;
    }
}
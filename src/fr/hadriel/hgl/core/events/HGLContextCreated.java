package fr.hadriel.hgl.core.events;

import fr.hadriel.hgl.core.HGLContext;
import fr.hadriel.hgl.WindowConfig;

/**
 * Created by glathuiliere on 28/11/2016.
 */
public class HGLContextCreated extends HGLEvent {

    public final HGLContext context;
    public final WindowConfig configuration;

    public HGLContextCreated(HGLContext context, WindowConfig configuration) {
        this.context = context;
        this.configuration = configuration;
    }
}
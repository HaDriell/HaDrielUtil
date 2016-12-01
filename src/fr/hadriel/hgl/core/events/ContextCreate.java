package fr.hadriel.hgl.core.events;

import fr.hadriel.hgl.core.HGLContext;
import fr.hadriel.hgl.core.configuration.WindowConfig;

/**
 * Created by glathuiliere on 28/11/2016.
 */
public class ContextCreate extends HGLEvent {

    public final HGLContext context;
    public final WindowConfig configuration;

    public ContextCreate(HGLContext context, WindowConfig configuration) {
        this.context = context;
        this.configuration = configuration;
    }
}
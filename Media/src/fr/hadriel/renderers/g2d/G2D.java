package fr.hadriel.renderers.g2d;

import fr.hadriel.opengl.BlendEquation;
import fr.hadriel.opengl.BlendFactor;
import fr.hadriel.opengl.FaceCulling;
import fr.hadriel.opengl.OpenGLConfiguration;
import org.pmw.tinylog.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.lwjgl.opengl.GL11.*;

public final class G2D {
    private static final OpenGLConfiguration configuration = new OpenGLConfiguration();
    private static final Map<Class, IRenderer> renderers = new HashMap<>();

    public void clear() {
        glClearColor(0, 0, 0, 1);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
    }

    public static <Mesh> void define(Class<Mesh> type, IRenderer<Mesh> renderer) {
        if (null != renderers.putIfAbsent(Objects.requireNonNull(type), Objects.requireNonNull(renderer)))
            Logger.warn("Unable to define {} to render (one already exist)", renderer);
    }

    @SuppressWarnings("unchecked")
    public static void draw(Object mesh) {
        IRenderer renderer = renderers.get(mesh.getClass());
        if (renderer != null) renderer.submit(mesh);
    }

    public static void setViewport(int x, int y, int width, int height) {
        configuration.setViewport(x, y, width, height);
    }

    public static void setBlending(boolean enable) {
        configuration.setBlending(enable);
    }

    public static void setBlendEquation(BlendEquation equation) {
        configuration.setBlendEquation(equation);
    }

    public static void setBlendFunction(BlendFactor source, BlendFactor destination) {
        configuration.setBlendFunction(source, destination);
    }

    public static void setFaceCulling(FaceCulling faceCulling) {
        configuration.setFaceCulling(Objects.requireNonNull(faceCulling));
    }
}
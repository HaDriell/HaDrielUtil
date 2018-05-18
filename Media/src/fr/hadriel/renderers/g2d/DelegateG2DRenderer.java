package fr.hadriel.renderers.g2d;

import fr.hadriel.math.Matrix3;
import fr.hadriel.opengl.BlendEquation;
import fr.hadriel.opengl.BlendFactor;

import java.util.HashMap;
import java.util.Map;

public class DelegateG2DRenderer implements G2DRenderer<Object> {

    private final Map<Class, G2DRenderer> renderers;

    public DelegateG2DRenderer() {
        this.renderers = new HashMap<>();
    }

    public <Primitive> void on(Class<Primitive> type, G2DRenderer<Primitive> renderer) {
        if (renderer == null)
            renderers.remove(type);
        else
            renderers.put(type, renderer);
    }

    public void begin() {
        renderers.values().forEach(G2DRenderer::begin);
    }

    @SuppressWarnings("unchecked")
    public void draw(Matrix3 transform, float depth, Object primitive) {
        G2DRenderer renderer = renderers.get(primitive.getClass());
        if (renderer != null)
            renderer.draw(transform, depth, primitive);
    }

    public void end() {
        renderers.values().forEach(G2DRenderer::end);
    }

    public void setDepthTesting(boolean enable) {
        renderers.values().forEach(renderer -> renderer.setDepthTesting(enable));
    }

    public void setViewport(int x, int y, int width, int height) {
        renderers.values().forEach(renderer -> renderer.setViewport(x, y, width, height));
    }

    public void setProjection(float left, float right, float top, float bottom, float near, float far) {
        renderers.values().forEach(renderer -> setProjection(left, right, top, bottom, near, far));
    }

    public void setBlending(boolean enable) {
        renderers.values().forEach(renderer -> renderer.setBlending(enable));
    }

    public void setBlendFunction(BlendFactor source, BlendFactor destination) {
        renderers.values().forEach(renderer -> renderer.setBlendFunction(source, destination));
    }

    public void setBlendEquation(BlendEquation equation) {
        renderers.values().forEach(renderer -> renderer.setBlendEquation(equation));
    }
}
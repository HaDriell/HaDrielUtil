package fr.hadriel.renderers.g2d;

import fr.hadriel.math.Matrix3;
import fr.hadriel.opengl.BlendEquation;
import fr.hadriel.opengl.BlendFactor;

public interface G2DRenderer<Primitive> {
    public void begin();
    public void draw(Matrix3 transform, float depth, Primitive primitive);
    public void end();

    public void setDepthTesting(boolean enable);

    public void setViewport(int x, int y, int width, int height);
    public void setProjection(float left, float right, float top, float bottom, float near, float far);

    public void setBlending(boolean enable);
    public void setBlendFunction(BlendFactor source, BlendFactor destination);
    public void setBlendEquation(BlendEquation equation);
}
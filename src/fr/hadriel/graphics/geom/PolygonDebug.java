package fr.hadriel.graphics.geom;

import fr.hadriel.graphics.HLGraphics;
import fr.hadriel.graphics.HLRenderable;
import fr.hadriel.math.Matrix3f;
import fr.hadriel.math.Vec2;
import fr.hadriel.math.geom.Polygon;

import java.awt.Color;

/**
 * Created by glathuiliere on 05/09/2016.
 */
public class PolygonDebug implements HLRenderable {

    public Polygon polygon;

    public final Vec2 scale = new Vec2(1, 1);
    public final Vec2 position = new Vec2(0, 0);
    public float rotation = 0f;

    public Color drawColor;
    public Color fillColor;

    public PolygonDebug(Polygon polygon, Color drawColor, Color fillColor) {
        this.drawColor = drawColor;
        this.fillColor = fillColor;
        this.polygon = polygon;
    }

    public Matrix3f getMatrix() {
        return Matrix3f.Transform(scale.x, scale.y, rotation, position.x, position.y);
    }

    public void render(HLGraphics g) {
        Polygon polygon = this.polygon;
        if(polygon == null) {
            return;
        }
        //Generate a transformed Polygon instance
        polygon = polygon.copy().transform(getMatrix());

        Vec2[] vertices = polygon.getVertices();
        int count = vertices.length;
        int[] x = new int[count];
        int[] y = new int[count];
        for(int i = 0; i < count; i++) {
            x[i] = (int) vertices[i].x;
            y[i] = (int) vertices[i].y;
        }
        if(fillColor != null) g.fillPolygon(x, y, count, fillColor);
        if(drawColor != null) g.drawPolygon(x, y, count, drawColor);
    }
}
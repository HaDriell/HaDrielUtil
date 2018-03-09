package fr.hadriel;

import fr.hadriel.math.Mathf;
import fr.hadriel.math.Matrix3f;
import fr.hadriel.math.Vec2;
import fr.hadriel.math.geometry.Bayazit;
import fr.hadriel.math.geometry.Convex;
import fr.hadriel.math.geometry.Polygon;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class TestBayazit {

    public static final int RENDER_DEFINITION = 256;

    private static final Vec2[] cross = {
            new Vec2(5, 0),
            new Vec2(35, 30),
            new Vec2(30, 35),
            new Vec2(0, 5),
            new Vec2(-30, 35),
            new Vec2(-35, 30),
            new Vec2(-5, 0),
            new Vec2(-35, -30),
            new Vec2(-30, -35),
            new Vec2(0, -5),
            new Vec2(30, -35),
            new Vec2(35, -30)
    };

    public static void main(String[] args) {
        List<Convex> polygons = Bayazit.decompose(cross);
        Polygon[] ps = new Polygon[polygons.size() + 1];
        polygons.toArray(ps);
        ps[ps.length - 1] = new Polygon(cross);

        printToImage("polygon_decomp", ps);
        for (int i = 0; i < polygons.size(); i++) {
            Polygon polygon = polygons.get(i);
            System.out.println("Polygon " + i);
            for(Vec2 vertex : polygon.vertices)
                System.out.println("\t - " + vertex);
            printToImage("polygon_" + i, polygon);
        }
    }

    private static void printToImage(String filename, Polygon... polygons) {
        float min_x_value = Float.MAX_VALUE;
        float min_y_value = Float.MAX_VALUE;
        float max_x_value = Float.MIN_VALUE;
        float max_y_value = Float.MIN_VALUE;

        for(Polygon polygon : polygons) {
            for (Vec2 v : polygon.vertices) {
                if (Mathf.abs(v.x) < min_x_value) min_x_value = Mathf.abs(v.x);
                if (Mathf.abs(v.y) < min_y_value) min_y_value = Mathf.abs(v.x);
                if (Mathf.abs(v.x) > max_x_value) max_x_value = Mathf.abs(v.x);
                if (Mathf.abs(v.y) > max_y_value) max_y_value = Mathf.abs(v.y);
            }
        }

        float rectWidth = max_x_value - min_x_value;
        float rectHeight = max_y_value - min_y_value;
        float scale = RENDER_DEFINITION / Mathf.max(rectWidth, rectHeight);
        scale /= 2.2;

        Matrix3f transform = new Matrix3f();
        transform.multiply(Matrix3f.Translation(RENDER_DEFINITION / 2, RENDER_DEFINITION / 2));
        transform.multiply(Matrix3f.Scale(scale, scale));

        BufferedImage render = new BufferedImage(RENDER_DEFINITION, RENDER_DEFINITION, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = render.createGraphics();
        g.setColor(Color.black);
        g.fillRect(0, 0, RENDER_DEFINITION, RENDER_DEFINITION);
        g.setColor(Color.white);

        for(Polygon polygon : polygons) {
            for (int i = 0; i < polygon.vertices.length; i++) {
                Vec2 v0 = polygon.vertices[i];
                Vec2 v1 = polygon.vertices[i + 1 == polygon.vertices.length ? 0 : i + 1];
                v0 = transform.multiply(v0);
                v1 = transform.multiply(v1);
                int x0 = (int) (v0.x);
                int y0 = (int) (v0.y);
                int x1 = (int) (v1.x);
                int y1 = (int) (v1.y);
                g.drawLine(x0, y0, x1, y1);
            }
        }
        g.dispose();

        try {
            ImageIO.write(render, "PNG", new File(filename + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

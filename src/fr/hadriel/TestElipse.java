package fr.hadriel;

import fr.hadriel.math.Mathf;
import fr.hadriel.math.Vec2;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by HaDriel setOn 23/10/2016.
 */
public class TestElipse {
    public static void main(String[] args) throws IOException {
        BufferedImage img = new BufferedImage(300, 300, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setColor(Color.black);
        g.fillRect(0, 0, img.getWidth(), img.getHeight());
        g.setColor(Color.white);
        g.translate(140, 145);
        drawGalaxy(g, 2, 40, 35, 150, Color.cyan.getRGB(), Color.cyan.brighter().getRGB());
//        drawGalaxy(g, 3, 60, 20, 150, Color.red.getRGB(), Color.red.darker().getRGB());
        g.dispose();
        ImageIO.write(img, "PNG", new File("view.png"));
    }

    public static void drawGalaxy(Graphics2D g, int branchCount, int branchAreaCount, int areaStarCount, float torsionDispersion, int minColor, int maxColor) {
        float unitBranchAngle = Mathf.toRadians(360F / branchCount);
        AffineTransform origin = g.getTransform();
        for(int i = 0; i < branchCount; i++) {
            g.setTransform(origin);
            g.rotate(unitBranchAngle * i);
            for(int s = 0; s < branchAreaCount; s++) {
                Vec2 area = getPointOnSpiral(s, branchAreaCount, 0, torsionDispersion);
                drawStarsInArea(g, areaStarCount, area, 0.1f * area.copy().sub(150, 150).len(), minColor, maxColor);
            }
        }
    }

    public static void drawStarsInArea(Graphics2D g, int starCount, Vec2 locationCenter, float maxRange, int minColor, int maxColor) {
        for(int i = 0; i < starCount; i++) {
            float dist = Mathf.random(0, maxRange);
            float radians = Mathf.random(0, 2 * Mathf.PI);
            drawStar(g, locationCenter.copy().add(dist * Mathf.cos(radians), dist * Mathf.sin(radians)), (int) Mathf.random(2, 5), minColor, maxColor);
        }
    }

    public static void drawStar(Graphics2D g, Vec2 v, int size, int minColor, int maxColor) {
        int mix = (int) Mathf.lerp(minColor, maxColor, Mathf.random());
        g.setColor(new Color(mix));
        g.fillOval((int) v.x, (int) v.y, size, size);
    }

    public static Vec2 getPointOnSpiral(int index, int pointPerRevolution, int xOffset, float deltaRadius) {
        float unitRotation = 360F / pointPerRevolution;
        float unitTranslation = deltaRadius / pointPerRevolution;
        Vec2 v = new Vec2(xOffset, 0);
        v.add(unitTranslation * index, 0);
        v.rotate(unitRotation * index);
        return v;
    }
}
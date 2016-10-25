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
 * Created by HaDriel on 23/10/2016.
 */
public class TestElipse {
    public static void main(String[] args) throws IOException {
        BufferedImage img = new BufferedImage(300, 300, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setColor(Color.black);
        g.fillRect(0, 0, img.getWidth(), img.getHeight());
        g.setColor(Color.white);
        g.translate(140, 145);
        drawGalaxy(g, 3, 180, 150);
        g.dispose();
        ImageIO.write(img, "PNG", new File("view.png"));
    }

    public static void drawGalaxy(Graphics2D g, int branchCount, int branchStarCount, float torsionDispersion) {
        float unitBranchAngle = Mathf.toRadians(360F / branchCount);
        AffineTransform origin = g.getTransform();
        for(int i = 0; i < branchCount; i++) {
            g.setTransform(origin);
            g.rotate(unitBranchAngle * i);
            for(int s = 0; s < branchStarCount; s++) {
                Vec2 v = getPointOnSpiral(s, branchStarCount, 0, torsionDispersion);
                drawVector(g, v, 3, Color.cyan);
            }
        }
    }

    public static void drawVector(Graphics2D g, Vec2 v, int size, Color c) {
        g.setColor(c);
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
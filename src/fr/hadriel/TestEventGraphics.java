package fr.hadriel;


import fr.hadriel.events.*;
import fr.hadriel.graphics.HLGraphics;
import fr.hadriel.graphics.Window;
import fr.hadriel.graphics.ui.Group;
import fr.hadriel.graphics.ui.Widget;
import fr.hadriel.math.Matrix3f;
import fr.hadriel.math.Vec2;

import java.awt.Color;


/**
 * Created by glathuiliere on 08/08/2016.
 */
public class TestEventGraphics {

    public static class EchoWidget extends Widget {

        boolean hitted = false;

        public void onRender(HLGraphics graphics) {
            graphics.fillRect(0, 0, (int) size.x, (int) size.y, hitted ? Color.red : Color.blue);
        }

        public void onUpdate(float delta) {
            System.out.println("update");
        }

        public boolean onKeyPressed(KeyPressedEvent event) {
            System.out.println("keyPressed");
            return false;
        }

        public boolean onKeyReleased(KeyReleasedEvent event) {
            System.out.println("keyReleased");
            return false;
        }

        public boolean onMouseMoved(MouseMovedEvent event) {
            System.out.println("mouseMoved");
            return false;
        }

        public boolean onMousePressed(MousePressedEvent event) {
            System.out.println("mousePressed");
            return false;
        }

        public boolean onMouseReleased(MouseReleasedEvent event) {
            System.out.println("mouseReleased");
            return false;
        }

        public boolean hit(Vec2 v) {
            hitted = super.hit(v);
            if(hitted) {
                System.out.println("hit!");
            }
            return hitted;
        }
    }

    public static void main(String[] args) {
        Group g = new Group();
        Widget w = new EchoWidget();
        w.size.set(100, 100);
        g.setTransform(Matrix3f.Translation(100, 100));
        g.add(w);

        Window window = new Window();
        window.getRoot().add(g);
        window.start();

        Matrix3f tr = new Matrix3f();
        while(!Thread.interrupted()) {
            tr.rotate(1f);
//            w.setTransform(tr);
            try {
                Thread.sleep(16);
            } catch (InterruptedException ingore) {}
        }
    }
}
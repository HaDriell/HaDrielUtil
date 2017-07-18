package fr.hadriel.test.networkedECS;

import fr.hadriel.main.math.Vec2;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by glathuiliere on 10/04/2017.
 */
public class PlayerController implements KeyListener {

    public static final float SPEED = 20;

    public final long id;
    private boolean up, down, left, right;

    public PlayerController(long id) {
        this.id = id;
    }

    public Vec2 getSpeed() {
        float x = 0;
        float y = 0;

        if(up) y -= SPEED;
        if(down) y += SPEED;
        if(right) x += SPEED;
        if(left) x -= SPEED;

        return new Vec2(x, y);
    }

    private void setKey(int key, boolean value) {
        switch (key) {
            case KeyEvent.VK_UP: up = value; break;
            case KeyEvent.VK_DOWN: down = value; break;
            case KeyEvent.VK_LEFT: left = value; break;
            case KeyEvent.VK_RIGHT: right = value; break;
        }
    }

    public void keyPressed(KeyEvent e) {
        setKey(e.getKeyCode(), true);
    }

    public void keyReleased(KeyEvent e) {
        setKey(e.getKeyCode(), false);
    }

    public void keyTyped(KeyEvent e) {}
}
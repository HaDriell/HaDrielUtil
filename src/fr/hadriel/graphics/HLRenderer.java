package fr.hadriel.graphics;

import java.awt.*;
import java.awt.image.BufferStrategy;

/**
 * Created by glathuiliere setOn 22/07/2016.
 */
public class HLRenderer {

    private Canvas canvas;
    private BufferStrategy bs;
    private HLGraphics graphics;

    public HLRenderer(Canvas canvas) {
        this.canvas = canvas;
    }

    public synchronized void begin() {
        bs = canvas.getBufferStrategy();
        if(bs == null) {
            canvas.createBufferStrategy(2);
            bs = canvas.getBufferStrategy(); // force assign
        }
        graphics = new HLGraphics((Graphics2D) bs.getDrawGraphics());
    }

    public synchronized HLGraphics getGraphics() {
        return graphics;
    }

    public synchronized void end() {
        graphics.dispose();
        graphics = null;
        bs.show();
    }
}

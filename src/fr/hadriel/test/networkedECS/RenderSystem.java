package fr.hadriel.test.networkedECS;

import fr.hadriel.main.ecs.EntityDataManager;
import fr.hadriel.main.ecs.EntitySystem;
import fr.hadriel.main.ecs.filter.Filters;
import fr.hadriel.main.math.Vec2;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by glathuiliere on 10/04/2017.
 */
public class RenderSystem extends EntitySystem {

    private Canvas canvas;
    private Lock renderLock;

    private BufferStrategy bs;
    private Graphics2D g;

    public RenderSystem(Canvas canvas) {
        super(Filters.Require("position", Vec2.class));
        this.canvas = canvas;
        this.renderLock = new ReentrantLock();
    }

    @Override
    protected void begin() {
        renderLock.lock();
        if(canvas != null) {
            bs = canvas.getBufferStrategy();
            if(bs == null) {
                canvas.createBufferStrategy(3);
                bs = canvas.getBufferStrategy();
            }
            g = (Graphics2D) bs.getDrawGraphics();
            g.setColor(Color.black);
            g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        }
    }

    @Override
    protected void update(EntityDataManager manager, long id, float delta) {
        if(canvas == null) return;
        Vec2 position = manager.getComponent(id, "position", Vec2.class);

        g.setColor(Color.white); // always white shapes
        g.translate(position.x, position.y);
        g.fillOval(-5, -5, 10, 10);
        g.translate(-position.x, -position.y);
    }

    @Override
    protected void end() {
        g.dispose();
        bs.show();
        bs = null;
        renderLock.unlock();
    }
}
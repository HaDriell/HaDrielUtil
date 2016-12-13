package fr.hadriel.graphics.ui;

import fr.hadriel.graphics.HLGraphics;
import fr.hadriel.graphics.HLRenderable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by glathuiliere setOn 11/08/2016.
 */
public class DirectDraw extends Widget {

    private List<HLRenderable> elements;
    private Lock lock = new ReentrantLock();

    public DirectDraw(int width, int height) {
        setSize(width, height);
        this.elements = new ArrayList<>();
    }

    public void add(HLRenderable renderable) {
        lock.lock();
        elements.add(renderable);
        lock.unlock();
    }

    public void remove(HLRenderable renderable) {
        lock.lock();
        elements.remove(renderable);
        lock.unlock();
    }

    public void clear() {
        lock.lock();
        elements.clear();
        lock.unlock();
    }

    public void onRender(HLGraphics graphics) {
        lock.lock();
        for(HLRenderable element : elements) {
            element.render(graphics);
        }
        lock.unlock();
    }
}
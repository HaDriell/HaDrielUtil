package fr.hadriel.graphics;


import fr.hadriel.events.*;
import fr.hadriel.graphics.ui.Group;
import fr.hadriel.threading.TickedLoop;

import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by glathuiliere setOn 22/07/2016.
 */
public class Window extends TickedLoop {

    private JFrame frame;
    private Canvas canvas;

    private List<Group> roots;
    private Lock lock = new ReentrantLock();
    private HLRenderer renderer;

    public Window(WindowConfiguration configuration) {
        this.frame = null;
        this.roots = new ArrayList<>();
        this.canvas = new Canvas();
        this.renderer = new HLRenderer(canvas);
        initCanvas();
        configure(configuration);
    }

    public Window() {
        this(new WindowConfiguration());
    }

    private void initCanvas() {
        canvas.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                lock.lock();
                for(int i = roots.size() - 1; i >= 0; i--)
                    roots.get(i).onEvent(new MousePressedEvent(e.getX(), e.getY(), e.getButton()));
                lock.unlock();
            }

            public void mouseReleased(MouseEvent e) {
                lock.lock();
                for(int i = roots.size() - 1; i >= 0; i--)
                    roots.get(i).onEvent(new MouseReleasedEvent(e.getX(), e.getY(), e.getButton()));
                lock.unlock();
            }
        });
        canvas.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                lock.lock();
                for(int i = roots.size() - 1; i >= 0; i--)
                    roots.get(i).onEvent(new MouseMovedEvent(e.getX(), e.getY(), true));
                lock.unlock();
            }

            public void mouseMoved(MouseEvent e) {
                lock.lock();
                for(int i = roots.size() - 1; i >= 0; i--)
                    roots.get(i).onEvent(new MouseMovedEvent(e.getX(), e.getY(), false));
                lock.unlock();
            }
        });
        canvas.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                lock.lock();
                for (int i = roots.size() - 1; i >= 0; i--)
                    roots.get(i).onEvent(new KeyPressedEvent(e.getKeyCode()));
                lock.unlock();
            }

            public void keyReleased(KeyEvent e) {
                lock.lock();
                for (int i = roots.size() - 1; i >= 0; i--)
                    roots.get(i).onEvent(new KeyReleasedEvent(e.getKeyCode()));
                lock.unlock();
            }
        });
    }

    public void configure(WindowConfiguration configuration) {
        if(frame != null) {
            frame.dispose();
        }
        DisplayMode vidmode = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode();
        //override configuration setOn fullscreen
        if(configuration.fullscreen) {
            configuration.decorated = false;
            configuration.width = vidmode.getWidth();
            configuration.height = vidmode.getHeight();
        }

        frame = new JFrame();
        frame.setUndecorated(!configuration.decorated);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
        frame.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                canvas.setSize(frame.getSize());
            }
        });

        frame.setLayout(null);
        canvas.setBounds(0, 0, configuration.width, configuration.height);
        frame.setBounds(0, 0, configuration.width, configuration.height);
        frame.getContentPane().add(canvas);
//        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setTitle(configuration.title);
        frame.setVisible(true);
        frame.requestFocus();
    }

    public void dispose() {
        if(frame != null)
            frame.dispose();
        frame = null;
        try {
            stop();
        } catch (InterruptedException ignore) {}
    }

    public void addRoot(Group group) {
        lock.lock();
        roots.add(group);
        lock.unlock();
    }

    public void removeRoot(Group group) {
        lock.lock();
        roots.remove(group);
        lock.unlock();
    }

    public void clearRoots() {
        lock.lock();
        roots.clear();
        lock.unlock();
    }

    public Dimension getSize() {
        return frame.getSize();
    }

    public boolean isDisposed() {
        return frame == null;
    }

    protected void onStart() {
        setTickRate(60);
    }

    protected void onTick() {
        renderer.begin();
        HLGraphics g = renderer.getGraphics();
        g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight(), Color.black);
        lock.lock();
        for(Group root : roots) {
            g.draw(root);
        }
        lock.unlock();
        g.dispose();
        renderer.end();
    }

    protected void onStop() {}

}
package fr.hadriel.graphics;


import fr.hadriel.events.*;
import fr.hadriel.graphics.ui.Group;
import fr.hadriel.threading.TickedLoop;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by glathuiliere on 22/07/2016.
 */
public class Window extends TickedLoop {

    private JFrame frame;
    private Canvas canvas;
    private Group root;

    private HLRenderer renderer;

    public Window(WindowConfiguration configuration) {
        this.frame = null;
        this.root = new Group();
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
                root.onEvent(new MousePressedEvent(e.getX(), e.getY(), e.getButton()));
            }

            public void mouseReleased(MouseEvent e) {
                root.onEvent(new MouseReleasedEvent(e.getX(), e.getY(), e.getButton()));
            }
        });
        canvas.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                root.onEvent(new MouseMovedEvent(e.getX(), e.getY(), true));
            }

            public void mouseMoved(MouseEvent e) {
                root.onEvent(new MouseMovedEvent(e.getX(), e.getY(), false));
            }
        });
        canvas.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                root.onEvent(new KeyPressedEvent(e.getKeyCode()));
            }

            public void keyReleased(KeyEvent e) {
                root.onEvent(new KeyReleasedEvent(e.getKeyCode()));
            }
        });
    }

    public void configure(WindowConfiguration configuration) {
        if(frame != null) {
            frame.dispose();
        }
        DisplayMode vidmode = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode();
        //override configuration on fullscreen
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
        frame.dispose();
        frame = null;
        interrupt();
    }

    public Group getRoot() {
        return root;
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

    private int age;

    protected void onTick() {
        renderer.begin();
        HLGraphics g = renderer.getGraphics();
        age = (age + 1) % 255;
        g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight(), new Color(age, age, age));
        g.draw(root);
        g.dispose();
        renderer.end();
    }

    protected void onStop() {}

}
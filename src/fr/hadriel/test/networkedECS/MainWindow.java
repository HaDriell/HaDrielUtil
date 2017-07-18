package fr.hadriel.test.networkedECS;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;

/**
 * Created by glathuiliere on 10/04/2017.
 */
public class MainWindow extends JFrame {

    private Canvas canvas;

    public MainWindow(int width, int height) {
        this("MainWindow", width, height);
    }

    public MainWindow(String title, int width, int height) {
        canvas = new Canvas();
        canvas.setSize(width, height);
        getContentPane().add(canvas);
        pack();
        canvas.requestFocus();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle(title);
        setVisible(true);
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public void bind(KeyListener listener) {
        canvas.addKeyListener(listener);
    }
}
package fr.hadriel.graphics.ui;

import fr.hadriel.graphics.Texture;

import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.image.BufferedImage;

/**
 * Created by glathuiliere on 11/08/2016.
 */
public final class UIDefaults {
    private UIDefaults(){}

    //Button defaults
    public static final Texture DEFAULT_IDLE_TEXTURE;
    public static final Texture DEFAULT_HOVERED_TEXTURE;
    public static final Texture DEFAULT_PRESSED_TEXTURE;

    static {
        BufferedImage idle = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
        BufferedImage hovered = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
        BufferedImage pressed = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
        Graphics g;
        g = idle.createGraphics();
        g.setColor(new Color(128, 128, 128));
        g.fillRect(0, 0, 64, 64);
        g.dispose();

        g = hovered.createGraphics();
        g.setColor(new Color(172, 172, 172));
        g.fillRect(0, 0, 64, 64);
        g.dispose();

        g = pressed.createGraphics();
        g.setColor(new Color(72, 72, 72));
        g.fillRect(0, 0, 64, 64);
        g.dispose();
        DEFAULT_IDLE_TEXTURE = new Texture(idle);
        DEFAULT_HOVERED_TEXTURE = new Texture(hovered);
        DEFAULT_PRESSED_TEXTURE = new Texture(pressed);

    }


    //Text defaults
    public static final FontRenderContext HQ_FRC = new FontRenderContext(null, true, true);
    public static final Font DEFAULT_FONT = new JLabel().getFont(); // bullshit work but functionnal
    public static final float DEFAULT_FONTSIZE = 20f;
    public static final Color DEFAULT_COLOR = Color.white;
}

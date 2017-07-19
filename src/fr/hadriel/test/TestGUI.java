package fr.hadriel.test;

import fr.hadriel.main.event.DelegateEventListener;
import fr.hadriel.main.lwjgl.g2d.BatchGraphics;
import fr.hadriel.main.lwjgl.g2d.G2DWindow;
import fr.hadriel.main.lwjgl.g2d.event.MouseMovedEvent;
import fr.hadriel.main.lwjgl.g2d.event.MousePressedEvent;
import fr.hadriel.main.lwjgl.g2d.ui.Group;
import fr.hadriel.main.lwjgl.g2d.ui.UIContext;
import fr.hadriel.main.lwjgl.g2d.ui.Widget;

/**
 * Created by glathuiliere on 18/07/2017.
 */
public class TestGUI {

    private static class Rect extends Widget {

        public Rect(float width, float height) {
            super(width, height);
        }

        protected void onRender(BatchGraphics g, float width, float height, UIContext context) {
            if(context.isFocused(this))
                g.setColor(0xFF0000FF);//Focused Color
            else if(context.isHovered(this))
                g.setColor(0x888888FF);//Hovered Color
            else
                g.setColor(0x444444FF);//Default Color
            g.fillRect(0, 0, width, height);
        }
    }

    public static void main(String[] args) {
        G2DWindow window = new G2DWindow();

        Group mouseClickInterceptor = new Group();

        Group root = new Group();
        root.translate(200, 100);
        mouseClickInterceptor.add(root);

        Rect a = new Rect(200, 200);
        Rect b = new Rect(100, 100);
        Rect c = new Rect(50, 50);

        mouseClickInterceptor.addEventListener(new DelegateEventListener(MousePressedEvent.class, event -> {
            event.consume();
            return event;
        }), true);

        a.addEventListener(new DelegateEventListener(MouseMovedEvent.class, event -> {
            return event;
        }));

        c.addEventListener(new DelegateEventListener(MousePressedEvent.class, event -> {
            b.setEnbaled(false);
            event.consume();
            return event;
        }));


        root.add(a);
        root.add(b);
        root.add(c);

        window.getScene().add(mouseClickInterceptor);
    }
}

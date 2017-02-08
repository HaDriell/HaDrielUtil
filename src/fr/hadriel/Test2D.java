package fr.hadriel;

import fr.hadriel.lwjgl.g2d.G2DWindow;
import fr.hadriel.lwjgl.g2d.ui.Group;
import fr.hadriel.lwjgl.g2d.ui.Rectangle;

/**
 * Created by glathuiliere on 31/01/2017.
 */
public class Test2D {

    public static void main(String[] args) {
        Rectangle a = new Rectangle(50, 50);

        Rectangle b = new Rectangle(50, 50);
        b.translate(55, 0);

        Group group = new Group();
        group.translate(100, 0);
        group.rotate(-45);
        group.add(a);
        group.add(b);

        G2DWindow window = new G2DWindow();
        window.getRoot().add(group);
    }
}

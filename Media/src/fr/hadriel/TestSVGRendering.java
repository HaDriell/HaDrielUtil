package fr.hadriel;

import fr.hadriel.application.Launcher;
import fr.hadriel.core.application.GUIApplication;
import fr.hadriel.graphics.Graphics;
import fr.hadriel.math.geometry.Triangle;
import fr.hadriel.math.svg.Path;
import fr.hadriel.math.svg.SVG;
import fr.hadriel.gui.Window;
import fr.hadriel.math.Matrix3f;
import fr.hadriel.math.Vec2;
import fr.hadriel.math.geometry.Polygon;
import fr.hadriel.util.Timer;

import java.util.List;

/**
 * TODO comment
 *
 * @author glathuiliere
 */
public class TestSVGRendering {

    private static final String PATH = ""
            + "M 5 5 "
            + "L 5 10 "
            + "L -5 10"
            + "L -5 5"
            + "L -10 5"
            + "L -10 -5"
            + "L -5 -5"
            + "L -5 -10"
            + "L 5 -10"
            + "L 5 -5"
            + "L 10 -5"
            + "L 10 5"
            ;

    private static Vec2 cursor = Vec2.ZERO;

    public static class App extends GUIApplication {
        protected void start(Window window) {
            Vec2 size = window.getSize();
            Timer t = new Timer();
            t.reset();
            Path path = SVG.parse(PATH);
            System.out.println(String.format("%f seconds to parse SVG", t.elapsed()));
            t.reset();

            Polygon polygonPath = path.toPolygon();
            System.out.println(String.format("%f seconds to get a Polygon from SVG", t.elapsed()));
            t.reset();

            List<Triangle> triangles = polygonPath.triangulate();
            System.out.println(String.format("%f seconds to Triangulate Polygon", t.elapsed()));

            GUIApplication.runLater(() -> {
                System.out.println("running");
                final Graphics g = new Graphics(size.x, size.y);
                window.bindCursorPos((w, xpos, ypos) -> cursor = new Vec2(xpos, ypos));
                window.bindWindowRender(w -> {
                    g.begin();
                    g.settings().color(0,0,0,1);
                    g.fillRect(0, 0, size.x, size.y);
                    g.push(Matrix3f.Translation(cursor.x + 50, cursor.y));
                    g.push(Matrix3f.Scale(2, 2));

                    //Draw Triangulation
                    g.settings().color(1,0, 0, 0.8f);
                    triangles.forEach(g::fill);
                    g.settings().color(0.8f, 0.2f, 0.2f, 1);
                    triangles.forEach(g::draw);



                    //Draw Edges
                    g.settings().color(1,0,0,1);
                    g.draw(polygonPath);


                    g.pop();
                    g.end();
                });
                window.show();
            });
        }
    }

    public static void main(String[] args) {
        Launcher.launch(new App());
    }
}

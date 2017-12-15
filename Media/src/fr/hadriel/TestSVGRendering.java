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

import java.util.List;

/**
 * TODO comment
 *
 * @author glathuiliere
 */
public class TestSVGRendering {

    private static final String PATH_1 = ""
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

    private static final String PATH_2 = "M33 0H15A15 15 0 0 0 0 15v8A15 15 0 0 0 15 38h3v9a1 1 0 0 0 1.71.71L29.41 38H33A15 15 0 0 0 48 23V15A15 15 0 0 0 33 0ZM46 23A13 13 0 0 1 33 36H29a1 1 0 0 0-.71.29L20 44.59V37a1 1 0 0 0-1-1H15A13 13 0 0 1 2 23V15A13 13 0 0 1 15 2H33A13 13 0 0 1 46 15Z";

    private static Vec2 cursor = Vec2.ZERO;

    public static class App extends GUIApplication {
        protected void start(Window window) {
            Vec2 size = window.getSize();
            Path path = SVG.parse(PATH_2);
            Polygon polygonPath = path.toPolygon();
            List<Triangle> triangles = polygonPath.triangulate();

            GUIApplication.runLater(() -> {
                final Graphics g = new Graphics(size.x, size.y);
                window.bindCursorPos((w, xpos, ypos) -> cursor = new Vec2(xpos, ypos));
                window.bindWindowRender(w -> {
                    g.begin();
                    g.settings().color(0,0,0,1);
                    g.fillRect(0, 0, size.x, size.y);
                    g.push(Matrix3f.Translation(cursor.x, cursor.y));
                    g.push(Matrix3f.Scale(2, 2));

                    //Draw Triangulation
                    g.settings().color(0.8f, 0.2f, 0.2f, 1);
                    triangles.forEach(g::draw);


                    //Draw Edges
                    g.settings().color(1,0,0,1);
                    g.draw(polygonPath);


                    g.pop();
                    g.end();
                });
            });


            window.show();
        }
    }

    public static void main(String[] args) {
        Launcher.launch(new App());
    }
}

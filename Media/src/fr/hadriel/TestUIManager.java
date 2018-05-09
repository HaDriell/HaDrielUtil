package fr.hadriel;

import fr.hadriel.application.Application;
import fr.hadriel.asset.graphics.Graphic2D;
import fr.hadriel.asset.graphics.font.Font;
import fr.hadriel.asset.graphics.ui.components.Label;
import fr.hadriel.asset.graphics.ui.UIManager;
import fr.hadriel.asset.graphics.ui.containers.VBox;
import fr.hadriel.math.Mathf;
import fr.hadriel.math.Vec2;
import fr.hadriel.math.Vec4;
import fr.hadriel.renderers.BatchGraphics;
import fr.hadriel.renderers.RenderUtil;
import fr.hadriel.util.Timer;
import org.lwjgl.glfw.GLFW;

public class TestUIManager extends Application {
    private static final String[] LOREM_IPSUM = {
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Praesent pellentesque turpis ut consequat venenatis. Nulla",
            "vitae libero nec elit convallis imperdiet at et elit. Morbi in scelerisque orci. In ut leo ante. Cras lacus dolor,",
            "congue et ornare et, sollicitudin eu justo. Pellentesque placerat elementum orci, ac laoreet quam tristique eu.",
            "Nam in suscipit nisi. Nullam efficitur, turpis eget sollicitudin commodo, libero nulla condimentum velit, sit amet",
            "gravida nulla augue eu elit. Fusce est velit, varius quis quam ut, ultricies commodo diam. Nunc in feugiat massa.",
            "Proin metus purus, cursus a sapien eget, vulputate iaculis ipsum."
    };

    private UIManager ui;
    private BatchGraphics graphics;
    private Font diablo2Font;
    private Font verdana;

    private Label first;
    private Label second;

    private Timer timer;

    @Override
    protected void start(String[] args) {
        timer = new Timer();
        diablo2Font = manager.load("Media/res/Diablo2.fnt", Font.class);
        verdana = manager.load("Media/res/Verdana.fnt", Font.class);
        graphics = new BatchGraphics();
        ui = new UIManager();

        first = new Label("you are broken", verdana, 12f, new Vec4(1, 0, 0, 1));
        second = new Label("Diablo", diablo2Font, 12f, new Vec4(1, 0.2, 0.2, 1));
        second.setPosition(0, 20);

        ui.add(new VBox(first, second));

        VBox lorem_ipsum = new VBox();
        for(String line : LOREM_IPSUM) {
            lorem_ipsum.add(new Label(line, verdana, 12f, new Vec4(0.5, 0.5, 0.5, 1)));
        }
        ui.add(lorem_ipsum);

        lorem_ipsum.setPosition(0, 100);

        Graphic2D.addEventListener(ui);
    }

    @Override
    protected void update(float delta) {
        RenderUtil.Clear();
        //Prepare renderer
        Vec2 size = Graphic2D.getWindowSize();
        graphics.setProjection(0, size.x, 0, size.y);

        //Prepare Font settings
        Vec2 mouse = Graphic2D.getMouse();
        if (Graphic2D.isKeyPressed(GLFW.GLFW_KEY_S)) {
            first.setFontSize(mouse.x);
            second.setFontSize(mouse.y);
        }

        if (Graphic2D.isKeyPressed(GLFW.GLFW_KEY_F)) {
            float buffer = Mathf.rlerp(mouse.x, 0, size.x);
            float gamma = Mathf.rlerp(mouse.y, 0, size.y);
            graphics.setSDFSettings(buffer, gamma);
            first.setText(String.format("%s (Buffer = %.3f)", verdana.info().face, buffer));
            second.setText(String.format("%s (Gamma = %.3f)", diablo2Font.info().face, gamma));
        }

        graphics.begin();
        ui.render(graphics);
        graphics.end();
    }

    @Override
    protected void terminate() {

    }

    public static void main(String... args) {
        launch(new TestUIManager());
    }
}

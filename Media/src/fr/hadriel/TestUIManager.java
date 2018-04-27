package fr.hadriel;

import fr.hadriel.application.Application;
import fr.hadriel.application.event.MouseMovedEvent;
import fr.hadriel.asset.graphics.Graphic2D;
import fr.hadriel.asset.graphics.font.Font;
import fr.hadriel.asset.graphics.gui.Container;
import fr.hadriel.asset.graphics.gui.Label;
import fr.hadriel.asset.graphics.gui.UIManager;
import fr.hadriel.event.MultiEventListener;
import fr.hadriel.math.Mathf;
import fr.hadriel.math.Matrix3;
import fr.hadriel.math.Vec2;
import fr.hadriel.math.Vec4;
import fr.hadriel.renderers.BatchGraphics;
import fr.hadriel.renderers.RenderUtil;
import org.lwjgl.glfw.GLFW;

public class TestUIManager extends Application {

    private UIManager ui;
    private BatchGraphics graphics;
    private Font diabloFont;
    private Font diablo2Font;
    private Font arial;

    private Label first;
    private Label second;

    @Override
    protected void start(String[] args) {
        diabloFont = manager.load("Media/res/Diablo.fnt", Font.class);
        diablo2Font = manager.load("Media/res/Diablo2.fnt", Font.class);
        arial = manager.load("Media/res/Arial.fnt", Font.class);
        graphics = new BatchGraphics();
        ui = new UIManager();

        Container root = new Container();

        first = new Label();
        first.setTransform(Matrix3.Identity);
        first.color = new Vec4(1, 0, 0, 1);
        first.size = 12f;
        first.text = "Diablo";
        first.font = diabloFont;
        root.getChildren().add(first);

        second = new Label();
        second.setTransform(Matrix3.Translation(0, 20));
        second.color = new Vec4(1, 0.2f, 0.2f, 1);
        second.size = 12f;
        second.text = "Diablo";
        second.font = arial;
        root.getChildren().add(second);
        ui.setRoot(root);

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
        float buffer = Mathf.rlerp(mouse.x, 0, size.x);
        float gamma = Mathf.rlerp(mouse.y, 0, size.y);
        if (Graphic2D.isMouseButtonDown(0)) {
            graphics.setSDFSettings(buffer, gamma);
            first.text = String.format("Diablo (Buffer = %.3f)", buffer);
            second.text = String.format("Diablo (Gamma = %.3f)", gamma);
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

package fr.hadriel;

import fr.hadriel.application.Application;
import fr.hadriel.application.event.MouseMovedEvent;
import fr.hadriel.application.event.MousePressedEvent;
import fr.hadriel.application.event.MouseReleasedEvent;
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

public class TestUIManager extends Application {

    private UIManager ui;
    private BatchGraphics graphics;
    private Font diabloFont;
    private Font arialFont;

    @Override
    protected void start(String[] args) {
        diabloFont = manager.load("Media/res/Diablo.fnt", Font.class);
        arialFont = manager.load("Media/res/Arial.fnt", Font.class);
        graphics = new BatchGraphics();
        ui = new UIManager();

        Container root = new Container();

        Label arial = new Label();
        arial.setTransform(Matrix3.Identity);
        arial.color = new Vec4(1, 0, 0, 1);
        arial.size = 12f;
        arial.text = "Label";
        arial.font = arialFont;
        root.getChildren().add(arial);

        Label diablo = new Label();
        diablo.setTransform(Matrix3.Translation(0, 12));
        diablo.color = new Vec4(1, 1, 1, 1);
        diablo.size = 12f;
        diablo.text = "Diablo";
        diablo.font = diabloFont;
        root.getChildren().add(diablo);

        MultiEventListener listener = new MultiEventListener();

        listener.on(MouseMovedEvent.class, event -> {
            float sharpness = Mathf.rlerp(event.x, 0, 800);
            diablo.text = "Sharpness " + sharpness;
            graphics.setFontSharpness(sharpness);
            return event;
        });

        Graphic2D.addEventListener(listener);
        Graphic2D.addEventListener(ui);
        ui.setRoot(root);
    }

    @Override
    protected void update(float delta) {
        RenderUtil.Clear();
        Vec2 size = Graphic2D.getWindowSize();
        graphics.setProjection(0, size.x, 0, size.y);
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

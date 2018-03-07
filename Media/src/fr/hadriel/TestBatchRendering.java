package fr.hadriel;

import fr.hadriel.application.Application;
import fr.hadriel.renderers.BatchRenderer2D;

public class TestBatchRendering extends Application {

    BatchRenderer2D renderer;

    protected void start(String[] args) {
        //TODO : grant a Display-like class that takes glfw as a backend
    }

    protected void update(float delta) { }

    protected void terminate() { }

    public static void main(String[] args) {
        launch(new TestBatchRendering());
    }
}
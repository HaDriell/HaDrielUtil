package fr.hadriel.renderers.g2d;

public interface IRenderer<Mesh> {
    public void prepare();

    public void submit(Mesh mesh);

    public void present();
}
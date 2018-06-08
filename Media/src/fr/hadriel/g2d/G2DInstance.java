package fr.hadriel.g2d;

import fr.hadriel.math.Matrix3;

public class G2DInstance {

    private final G2DScene scene;

    private Matrix3 transform;
    private G2DMaterial material;
    private G2DMesh mesh;

    public G2DInstance(G2DScene scene, Matrix3 transform, String mesh, String material) {
        this.scene = scene;
        this.transform = transform;
        this.material = scene.getMaterial(material);
        this.mesh = scene.getMesh(mesh);
    }


    public void setTransform(Matrix3 transform) {
        this.transform = transform != null ? transform : Matrix3.Identity;
    }
}
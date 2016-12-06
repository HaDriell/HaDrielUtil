package fr.hadriel.hgl;

import fr.hadriel.math.Matrix4f;
import fr.hadriel.math.Vec3;

/**
 * Created by HaDriel on 30/11/2016.
 */
public class Camera {

    private Matrix4f projectionMatrix;

    private Vec3 position;
    private Vec3 rotation;

    public Camera(Matrix4f projectionMatrix) {
        this.projectionMatrix = projectionMatrix;
        this.position = new Vec3();
        this.rotation = new Vec3();
    }

    public void translate(float x, float y, float z) {
        position.add(x, y, z);
    }

    public void translate(Vec3 translation) {
        position.add(translation);
    }

    public void rotate(float x, float y, float z) {
        rotation.add(x, y, z);
    }

    public void rotate(Vec3 rotation) {
        this.rotation.add(rotation);
    }

    public void setPosition(Vec3 position) {
        this.position = position;
    }

    public Vec3 getPosition() {
        return position;
    }

    public void setRotation(Vec3 rotation) {
        this.rotation = rotation;
    }

    public Vec3 getRotation() {
        return rotation;
    }

    public Matrix4f getViewMatrix() {
        return new Matrix4f();
    }

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    public void setProjectionMatrix(Matrix4f projectionMatrix) {
        this.projectionMatrix = projectionMatrix;
    }

    public void update() {}
}
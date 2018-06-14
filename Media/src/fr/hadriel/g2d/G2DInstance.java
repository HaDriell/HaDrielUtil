package fr.hadriel.g2d;


import fr.hadriel.math.Matrix3;

import java.util.ArrayList;
import java.util.List;

public final class G2DInstance {

    private Matrix3 transform;
    private int meshID;

    private final List<Integer> passes;
    private boolean dirty;

    private boolean visible;

    public G2DInstance(Matrix3 transform, int meshID) {
        this.transform = transform;
        this.meshID = meshID;
        this.passes = new ArrayList<>();
    }

    //package-private
    void clean() {
        dirty = false;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isVisible() {
        return visible;
    }

    public boolean isDirty() {
        return dirty;
    }

    public void setTransform(Matrix3 transform) {
        this.transform = transform;
    }

    public Matrix3 getTransform() {
        return transform == null ? Matrix3.Identity : transform;
    }


    public void setMeshID(String name) {
        setMesh(name.hashCode());
    }

    public void setMesh(int meshID) {
        this.meshID = meshID;
    }

    public int getMeshID() {
        return meshID;
    }

    public void addShaderPass(String name) {
        addShaderPass(name.hashCode());
    }

    public void addShaderPass(int id) {
        if (!passes.contains(id)) {
            passes.add(id);
            dirty = true;
        }
    }

    public boolean hasShaderPass(int id) {
        return passes.contains(id);
    }

    public void removeShaderPass(String name) {
        removeShaderPass(name.hashCode());
    }

    public void removeShaderPass(int id) {
        if (passes.contains(id)) {
            passes.remove(id);
            dirty = true;
        }
    }
}
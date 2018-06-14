package fr.hadriel.g2d;


import fr.hadriel.opengl.shader.Shader;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class G2DShaderPass implements Iterable<G2DInstance> {
    private int shaderID;
    private final List<G2DInstance> instances = new ArrayList<>();

    public int getShaderID() {
        return shaderID;
    }

    public void setShaderID(int shaderID) {
        this.shaderID = shaderID;
    }

    public boolean contains(G2DInstance instance) {
        return instances.contains(instance);
    }

    public void add(G2DInstance instance) {
        instances.add(instance);
    }

    public void remove(G2DInstance instance) {
        instances.remove(instance);
    }

    public Iterator<G2DInstance> iterator() {
        return instances.iterator();
    }

    protected abstract void setUniforms(Shader shader);
}
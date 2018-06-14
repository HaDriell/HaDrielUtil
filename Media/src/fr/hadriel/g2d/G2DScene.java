package fr.hadriel.g2d;

import fr.hadriel.opengl.shader.Shader;
import fr.hadriel.opengl.texture.Texture2D;

import java.util.*;

public final class G2DScene implements Iterable<G2DShaderPass> {

    //Instances global list
    private final List<G2DInstance> instances;

    //Mesh infos
    private final Map<Integer, G2DMesh> meshes;

    //Shader infos
    private final Map<Integer, Shader> shaders;

    //ShaderPass infos
    private final Map<Integer, G2DShaderPass> passes;

    //Textures infos
    private final Map<Integer, Texture2D> textures;

    public G2DScene() {
        this.instances = new ArrayList<>();
        this.meshes = new HashMap<>();
        this.shaders = new HashMap<>();
        this.passes = new HashMap<>();
        this.textures = new HashMap<>();
    }

    public void addTexture(String name, Texture2D texture) {
        addTexture(name.hashCode(), texture);
    }

    public void addTexture(int id, Texture2D texture) {
        textures.put(id, Objects.requireNonNull(texture));
    }

    public Texture2D getTexture(String name) {
        return getTexture(name.hashCode());
    }

    public Texture2D getTexture(int id) {
        return textures.get(id);
    }

    public void removeTexture(String name) {
        removeTexture(name.hashCode());
    }

    public void removeTexture(int id) {
        textures.remove(id);
    }

    public void addMesh(String name, G2DMesh mesh) {
        addMesh(name.hashCode(), mesh);
    }

    public void addMesh(int id, G2DMesh mesh) {
        meshes.put(id, Objects.requireNonNull(mesh));
    }

    public G2DMesh getMesh(String name) {
        return getMesh(name.hashCode());
    }

    public G2DMesh getMesh(int id) {
        return meshes.get(id);
    }

    public void removeMesh(String name) {
        removeMesh(name.hashCode());
    }

    public void removeMesh(int id) {
        meshes.remove(id);
    }

    public void addShader(String name, Shader shader) {
        addShader(name.hashCode(), shader);
    }

    public void addShader(int id, Shader shader) {
        shaders.put(id, Objects.requireNonNull(shader));
    }

    public Shader getShader(String name) {
        return getShader(name.hashCode());
    }

    public Shader getShader(int id) {
        return shaders.get(id);
    }

    public void removeShader(String name) {
        removeShader(name.hashCode());
    }

    public void removeShader(int id) {
        shaders.remove(id);
    }

    public void addShaderPass(String name, G2DShaderPass shaderPass) {
        addShaderPass(name.hashCode(), shaderPass);
    }

    public void addShaderPass(int id, G2DShaderPass shaderPass) {
        passes.put(id, Objects.requireNonNull(shaderPass));
    }

    public G2DShaderPass getShaderPass(String name) {
        return getShaderPass(name.hashCode());
    }

    public G2DShaderPass getShaderPass(int id) {
        return passes.get(id);
    }

    public void removeShaderPass(String name) {
        removeShaderPass(name.hashCode());
    }

    public void removeShaderPass(int id) {
        passes.remove(id);
    }

    public void addInstance(G2DInstance instance) {
        instances.add(instance);
        __mapInstance(instance);
    }

    public void removeInstance(G2DInstance instance) {
        instances.remove(instance);
        __unmapInstance(instance);
    }

    private void __mapInstance(G2DInstance instance) {
        passes.forEach((id, pass) -> {
            boolean ok = instance.hasShaderPass(id);
            boolean mapped = pass.contains(instance);

            if (ok && !mapped) pass.add(instance);
            if (!ok && mapped) pass.remove(instance);
        });
    }

    private void __unmapInstance(G2DInstance instance) {
        passes.forEach((id, pass) -> pass.remove(instance));
    }

    public void update() {
        instances.forEach(instance -> {
            if (instance.isDirty()) {
                __mapInstance(instance);
                instance.clean();
            }
        });
    }

    public Iterator<G2DShaderPass> iterator() {
        update(); // automatically called each frame
        return passes.values().iterator();
    }
}
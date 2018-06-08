package fr.hadriel.g2d;

import fr.hadriel.math.Matrix3;
import fr.hadriel.util.logging.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public final class G2DScene {
    private static final Logger logger = Log.getLogger(G2DScene.class);

    private Map<Integer, G2DInstance> instances;
    private Map<Integer, G2DMesh> meshes;
    private Map<Integer, G2DMaterial> materials;

    public G2DScene() {
        this.meshes = new HashMap<>();
        this.materials = new HashMap<>();
        this.instances = new HashMap<>();
    }

    // MATERIAL

    public G2DMaterial getMaterial(String name) {
        return getMaterial(name.hashCode());
    }

    public G2DMaterial getMaterial(int id) {
        return materials.get(id);
    }

    public void addMaterial(String name, G2DMaterial material) {
        int id = name.hashCode();
        if (materials.containsKey(id)) {
            logger.warning("Material " + name + " was replaced !");
        }
        materials.put(id, material);
    }

    public void removeMaterial(String name) {
        materials.remove(name.hashCode());
    }

    // MESH

    public G2DMesh getMesh(String name) {
        return getMesh(name.hashCode());
    }

    public G2DMesh getMesh(int id) {
        return meshes.get(id);
    }

    public void addMesh(String name, G2DMesh mesh) {
        int id = name.hashCode();
        if (meshes.containsKey(id)) {
            logger.warning("Mesh " + name + " was overriden !");
        }
        meshes.put(id, mesh);
    }

    public void removeMesh(String name) {
        meshes.remove(name.hashCode());
    }

    public G2DInstance newInstance(String name, Matrix3 transform, String mesh, String material) {
        if (instances.containsKey(name)) {
            logger.warning("Instance " + name + " was overriden !");
        }
        G2DInstance instance = new G2DInstance(this, transform, mesh, material);
        instances.put(name, instance);
        return instance;
    }
}
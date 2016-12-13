package fr.hadriel.hgl.resources;

import fr.hadriel.hgl.opengl.Shader;
import fr.hadriel.math.Vec3;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by HaDriel on 07/12/2016.
 * Custom Material setup for Phong Shader
 */
public class Material {

    public final Vec3 ambientReflection;
    public final Vec3 diffuseReflection;
    public final Vec3 specularReflection;
    public float shininess;

    public Material() {
        ambientReflection = new Vec3(1, 1, 1);
        specularReflection = new Vec3(1, 1, 1);
        diffuseReflection = new Vec3(1, 1, 1);
        shininess = 1f;
    }

    public Material(String filename) throws IOException {
        this();
        load(filename);
    }

    public void load(String filename) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
        String line;
        while ((line = in.readLine()) != null) {
            if(line.startsWith("#")) continue;
            String[] words = line.trim().split("\\s+");
            switch (words[0]) {
                case "ambient":
                    ambientReflection.set(Float.parseFloat(words[1]), Float.parseFloat(words[2]), Float.parseFloat(words[3]));
                    break;
                case "diffuse":
                    diffuseReflection.set(Float.parseFloat(words[1]), Float.parseFloat(words[2]), Float.parseFloat(words[3]));
                    break;
                case "specular":
                    specularReflection.set(Float.parseFloat(words[1]), Float.parseFloat(words[2]), Float.parseFloat(words[3]));
                    break;

                case "shininess":
                    shininess = Float.parseFloat(words[1]);
                    break;
            }
        }
        in.close();
    }

    public void setUniforms(Shader shader) {
        shader.setUniform3f("ambientReflection", ambientReflection);
        shader.setUniform3f("diffuseReflection", diffuseReflection);
        shader.setUniform3f("specularReflection", specularReflection);
        shader.setUniform1f("shininess", shininess);
    }
}
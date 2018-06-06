package fr.hadriel.asset.graphics.shader;

import fr.hadriel.asset.Asset;
import fr.hadriel.asset.AssetManager;
import fr.hadriel.opengl.shader.Shader;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.file.Path;

public class ShaderProgram extends Asset {

    private Shader shader;

    protected void onLoad(Path path, ByteBuffer fileContent) {
        byte[] data = new byte[fileContent.remaining()];
        fileContent.get(data, 0, data.length);

        try (InputStream stream = new ByteArrayInputStream(data)) {
            shader = Shader.GLSL(stream);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read the FontFile file", e);
        }
    }

    protected void onUnload() {
        shader.destroy();
    }
}
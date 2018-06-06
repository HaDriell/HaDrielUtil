package fr.hadriel.asset.graphics.animation;

import fr.hadriel.asset.Asset;
import fr.hadriel.io.ImageFile;
import fr.hadriel.opengl.texture.Texture2D;
import fr.hadriel.opengl.texture.TextureFormat;
import fr.hadriel.opengl.texture.TextureRegion;
import fr.hadriel.util.LineParser;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.file.Path;

public class Animation extends Asset {

    private static final String PREFIX_INFO  = "info";
    private static final String PREFIX_FRAME = "frame";
    private static final String PREFIX_FRAMES = "frames";

    private Texture2D texture;
    private TextureRegion[] frames;

    protected void onLoad(Path path, ByteBuffer fileContent) {
        Texture2D texture = null;
        TextureRegion[] frames = null;

        byte[] data = new byte[fileContent.remaining()];
        fileContent.get(data, 0, data.length);

        try (BufferedReader in = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(data)))) {
            LineParser parser = new LineParser();
            String line;
            while ((line = in.readLine()) != null) {
                parser.parse(line);
                switch (parser.getPrefix()) {
                    case PREFIX_INFO:
                        String file = parser.getString("texture");
                        ImageFile image = new ImageFile(path.resolveSibling(file).toString());
                        texture = new Texture2D();
                        texture.bind();
                        texture.setData(image.width, image.height, image.pixels, TextureFormat.RGBA8);
                        break;

                    case PREFIX_FRAMES:
                        frames = new TextureRegion[parser.getInt("count")];
                        break;

                    case PREFIX_FRAME:
                        TextureRegion region = texture.region(
                                parser.getInt("x"),
                                parser.getInt("y"),
                                parser.getInt("width"),
                                parser.getInt("hegiht"));
                        frames[parser.getInt("id")] = region;
                        break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read the Animation file", e);
        }
        this.texture = texture;
        this.frames = frames;
    }

    public TextureRegion frame(int id) {
        return frames[id];
    }

    protected void onUnload() {
        texture.destroy();
    }
}
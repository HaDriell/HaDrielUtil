package fr.hadriel.asset.texture;

import fr.hadriel.asset.Asset;
import fr.hadriel.asset.AssetManager;

import java.io.IOException;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL30.*;

public class Texture extends Asset {

    private final String filename;
    private final TextureHint textureHint;

    private int texture;
    private int width;
    private int height;

    public Texture(String filename) {
        this(filename, new TextureHint());
    }

    public Texture(String filename, TextureHint textureHint) {
        this.filename = filename;
        this.textureHint = textureHint;
    }

    public int id() {
        return texture;
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    protected void onLoad(AssetManager manager) {
        try {
            Image image = new Image(filename);
            this.width = image.width;
            this.height = image.height;
            this.texture = glGenTextures();
            glBindTexture(GL_TEXTURE_2D, texture);

            //Texture2D minifying / magnifying options
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, textureHint.GL_MIN_FILTER);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, textureHint.GL_MAG_FILTER);

            //Texture2D wrap options
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, textureHint.GL_TEXTURE_WRAP_S);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, textureHint.GL_TEXTURE_WRAP_T);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_R, textureHint.GL_TEXTURE_WRAP_R);
            //Texture2D io setup
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, image.width, image.height, 0, GL_RGBA, GL_UNSIGNED_BYTE, image.pixels);

            //Generate mipmaps only when not using LINEAR / NEAREST min filters
            if(textureHint.GL_MIN_FILTER != GL_LINEAR && textureHint.GL_MIN_FILTER != GL_NEAREST) {
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_BASE_LEVEL, 0);
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAX_LEVEL, Math.max(1, textureHint.GL_TEXTURE_MIPMAP_COUNT));
                glGenerateMipmap(GL_TEXTURE_2D);
            }
            glBindTexture(GL_TEXTURE_2D, 0);
        } catch (IOException e) {
            throw new RuntimeException("Unable to load Texture2D :", e);
        }
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, texture);
    }

    public void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    protected void onUnload(AssetManager manager) {
        glDeleteTextures(texture);
    }
}

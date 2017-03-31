package fr.hadriel.lwjgl.opengl;



import fr.hadriel.lwjgl.data.ImageData;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL30.*;

/**
 * Created by glathuiliere on 29/11/2016.
 */
public class Texture {

    public final int handle;
    public final int width;
    public final int height;

    public Texture(String filename) throws IOException {
        this(filename, new TextureHint());
    }

    public Texture(String filename, TextureHint hint) throws IOException {
        this(new ImageData(filename), hint);
    }

    public Texture(ImageData image) {
        this(image, new TextureHint());
    }

    public Texture(ImageData image, TextureHint hint) {
        this.width = image.width;
        this.height = image.height;
        this.handle = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, handle);

        //Texture minifying / magnifying options
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, hint.GL_MIN_FILTER);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, hint.GL_MAG_FILTER);

        //Texture wrap options
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, hint.GL_TEXTURE_WRAP_S);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, hint.GL_TEXTURE_WRAP_T);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_R, hint.GL_TEXTURE_WRAP_R);

        //Texture data setup
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, image.width, image.height, 0, GL_RGBA, GL_UNSIGNED_BYTE, image.pixels);

        //Generate mipmaps only when not using LINEAR / NEAREST min filters
        if(hint.GL_MIN_FILTER != GL_NEAREST && hint.GL_MIN_FILTER != GL_LINEAR) {
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_BASE_LEVEL, 0);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAX_LEVEL, Math.max(1, hint.GL_TEXTURE_MIPMAP_COUNT));
            glGenerateMipmap(GL_TEXTURE_2D);
        }
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public TextureRegion getRegion(float x, float y, float width, float height) {
        return new TextureRegion(this, x, y, width, height);
    }

    public TextureRegion getRegion() {
        return new TextureRegion(this, 0, 0, width, height);
    }

    public void setData(ImageData image) {
        ByteBuffer data = BufferUtils.createByteBuffer(image.pixels.length * 4);
        for(int p : image.pixels) data.putInt(p);
        data.flip();
        glTexSubImage2D(GL_TEXTURE_2D, 0, 0, 0, image.width, image.height, GL_RGBA, GL_UNSIGNED_BYTE, data);
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, handle);
    }

    public void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public void destroy() {
        glDeleteTextures(handle);
    }
}
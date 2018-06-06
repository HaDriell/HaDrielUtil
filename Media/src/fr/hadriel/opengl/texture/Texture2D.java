package fr.hadriel.opengl.texture;



import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;

/**
 * Created by glathuiliere on 29/11/2016.
 */
public class Texture2D {

    public final int handle;

    private int width;
    private int height;
    private TextureFormat format;
    private TextureFilter minFilter;
    private TextureFilter magFilter;
    private TextureWrapper xWrapper;
    private TextureWrapper yWrapper;

    public Texture2D() {
        this.handle = glGenTextures();
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public void setWrapping(TextureWrapper xWrapper, TextureWrapper yWrapper) {
        if (this.xWrapper != xWrapper || this.yWrapper != yWrapper) {
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, xWrapper.value);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, yWrapper.value);
            this.xWrapper = xWrapper;
            this.yWrapper = yWrapper;
        }
    }

    public void setFilter(TextureFilter minFilter, TextureFilter magFilter) {
        if (this.minFilter != minFilter || this.magFilter != magFilter) {
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, minFilter.value);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, magFilter.value);
            this.minFilter = minFilter;
            this.magFilter = magFilter;
        }
    }

    public void setData(int width, int height) {
        setData(width, height, TextureFormat.RGBA8);
    }

    public void setData(int width, int height, TextureFormat format) {
        glTexImage2D(GL_TEXTURE_2D, 0, format.internalFormat, width, height, 0, format.format, GL_UNSIGNED_BYTE, 0);
        glGenerateMipmap(GL_TEXTURE_2D); // always Generate mipmaps
        this.width = width;
        this.height = height;
        this.format = format;
    }

    public void setData(int width, int height, int[] pixels) {
        setData(width, height, pixels, TextureFormat.RGBA8);
    }

    public void setData(int width, int height, int[] pixels, TextureFormat format) {
        glTexImage2D(GL_TEXTURE_2D, 0, format.internalFormat, width, height, 0, format.format, GL_UNSIGNED_BYTE, pixels);
        glGenerateMipmap(GL_TEXTURE_2D); // always Generate mipmaps
        this.width = width;
        this.height = height;
        this.format = format;
    }

    public void write(int x, int y, int width, int  height, int[] pixels) {
        glTexSubImage2D(GL_TEXTURE_2D, 0, x, y, width, height, GL_RGBA, GL_UNSIGNED_BYTE, pixels);
    }

    public TextureRegion region(int x, int y, int width, int height) {
        return new TextureRegion(this, x, y, width, height);
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

    public boolean equals(Object obj) {
        return obj instanceof Texture2D && ((Texture2D) obj).handle == handle;
    }
}
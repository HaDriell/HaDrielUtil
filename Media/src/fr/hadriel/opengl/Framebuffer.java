package fr.hadriel.opengl;


import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;

public class Framebuffer {

    private int framebuffer;
    private Texture2D texture;

    public Framebuffer(int width, int height) {
        this.framebuffer = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, framebuffer);
        resize(width, height);
    }

    public void destroy() {
        glDeleteFramebuffers(framebuffer);
        texture.destroy();
    }

    public int width() {
        return texture.width;
    }

    public int height() {
        return texture.height;
    }

    public void resize(int width, int height) {
        if(texture != null) texture.destroy();
        texture = new Texture2D(width, height);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, texture.handle, 0);
    }

    public void bind() {
        glBindFramebuffer(GL_FRAMEBUFFER, framebuffer);
        texture.bind();
    }

    public void unbind() {
        texture.unbind();
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }
}
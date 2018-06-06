package fr.hadriel.opengl;

import fr.hadriel.opengl.texture.Texture2D;
import fr.hadriel.util.logging.Log;

import java.util.logging.Logger;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL32.*;

public class Framebuffer {
    private static final Logger logger = Log.getLogger(Framebuffer.class);

    private int framebuffer;
    private Texture2D colorTexture;
    private Texture2D depthTexture;

    public Framebuffer() {
        this.framebuffer = glGenFramebuffers();
    }

    public void destroy() {
        glDeleteFramebuffers(framebuffer);
    }

    public void bind() {
        glBindFramebuffer(GL_FRAMEBUFFER, framebuffer);
    }

    public void unbind() {
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    public Texture2D getColorTexture() {
        return colorTexture;
    }

    public Texture2D getDepthTexture() {
        return depthTexture;
    }

    public void setColorTexture(Texture2D texture) {
        glFramebufferTexture(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, texture.handle, 0);
        this.colorTexture = texture;
        glDrawBuffer(GL_COLOR_ATTACHMENT0);
        glReadBuffer(GL_COLOR_ATTACHMENT0);
    }

    public void setDepthTexture(Texture2D texture) {
        glFramebufferTexture(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, texture.handle, 0);
        this.depthTexture = texture;
    }

    public void clear() {
        clear(0, 0, 0, 1);
    }

    public void clear(float r, float g, float b, float a) {
        glClearColor(r, g, b, a);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void validate() {
        int error = glCheckFramebufferStatus(GL_FRAMEBUFFER);

        if (error != GL_FRAMEBUFFER_COMPLETE) {
            switch (error) {
                case GL_FRAMEBUFFER_UNDEFINED:
                    logger.severe("Target is the default framebuffer, but the default framebuffer does not exist"); break;
                case GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT:
                    logger.severe("Any of the framebuffer attachment points are framebuffer incomplete"); break;
                case GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT:
                    logger.severe("The framebuffer does not have any texture attached to it"); break;
                case GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER:
                    logger.severe("The internalFormat of GL_FRAMEBUFFER_ATTACHMENT_OBJECT_TYPE is GL_NONE for any color attachment point(s) named by GL_DRAW_BUFFERi"); break;
                case GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER:
                    logger.severe("GL_READ_BUFFER is not GL_NONE and the internalFormat of GL_FRAMEBUFFER_ATTACHMENT_OBJECT_TYPE is GL_NONE for the color attachment point named by GL_READ_BUFFER"); break;
                case GL_FRAMEBUFFER_UNSUPPORTED:
                    logger.severe("The combination of internal formats of the attached textures violates an implementation-dependent set of restrictions."); break;
                case GL_FRAMEBUFFER_INCOMPLETE_MULTISAMPLE:
                    logger.severe("The internalFormat of GL_RENDERBUFFER_SAMPLES is not the same for all attached renderbuffers"); break;
                default:
                    logger.severe("There is a problem with the framebuffer");

            }
        }
    }
}
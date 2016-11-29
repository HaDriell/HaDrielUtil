package fr.hadriel;


import fr.hadriel.hgl.core.HGLContext;
import fr.hadriel.hgl.graphics.Texture;
import fr.hadriel.hgl.stb.Image;
import org.lwjgl.opengl.GL11;

import java.io.IOException;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by glathuiliere on 28/11/2016.
 */
public class TestHGL {
    public static void main(String[] args) throws IOException {
        HGLContext context = new HGLContext() {

            Texture texture;

            public void onInit() {
                glClearColor(0, 0, 0, 1);
                glMatrixMode(GL_PROJECTION);
                glLoadIdentity();
                glOrtho(0, 800, 450, 0, -1, 1);
                glEnable(GL_BLEND);
                glEnable(GL_TEXTURE_2D);
                glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
                texture = new Texture(Image.load("Teron Fielsang.png"));
            }

            public void onDestroy() {
                texture.dispose();
            }

            public void onRender() {
                glClear(GL_COLOR_BUFFER_BIT);
                texture.bind();
                glBegin(GL_QUADS);
                glTexCoord2f(0, 0); glVertex2f(0, 0);
                glTexCoord2f(0, 1); glVertex2f(0, 32);
                glTexCoord2f(1, 1); glVertex2f(32, 32);
                glTexCoord2f(1, 0); glVertex2f(32, 0);
                glEnd();
            }

            public void onKey(int key, int scancode, int action, int mods) {
            }

            public void onMouse(int button, int action, int mods) {

            }

            public void onScroll(double xOffset, double yOffset) {

            }

            public void onCursorPos(double xpos, double ypox) {

            }

            public void onCursorEnter(boolean inside) {

            }

            public void onFrameBuffer(int width, int height) {

            }

            public void onWindowPos(int xpos, int ypos) {

            }

            public void onWindowSize(int width, int height) {

            }

            public void onWindowFocus(boolean focussed) {

            }

            public void onWindowRefresh() {

            }

            public void onWindowClosed() {

            }

            public void onWindowIconified(boolean iconified) {

            }
        };
    }
}

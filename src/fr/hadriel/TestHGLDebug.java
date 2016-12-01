package fr.hadriel;


import fr.hadriel.hgl.core.*;
import fr.hadriel.hgl.graphics.Shader;
import fr.hadriel.hgl.graphics.Texture;
import fr.hadriel.hgl.stb.Image;
import fr.hadriel.math.Matrix4f;
import fr.hadriel.time.Timer;
import org.lwjgl.BufferUtils;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.glfw.GLFW.*;

/**
 * Created by glathuiliere on 28/11/2016.
 */
public class TestHGLDebug {

    public static void main(String[] args) throws IOException {
        new HGLContext() {

            Timer timer = new Timer();
            int fps;

            Texture texture;
            Shader shader;
            Camera camera;

            int ibo, vbo, vao;

            public void onInit() {
                texture = new Texture(Image.load("Teron Fielsang.png"));
                camera = new Camera(Matrix4f.Orthographic(0, 800, 0, 450, -1, 1));
                try {
                    shader = new Shader(new File("simple.vert"), new File("simple.frag"));
                } catch (Exception error) {
                    error.printStackTrace();
                }

                int MAX_COUNT = 6000;
                int STRIDE = (3 * 4) + (4 * 4); // position + color

                ByteBuffer vertices = BufferUtils.createByteBuffer(MAX_COUNT * 4 * STRIDE);
                for(int x = 0; x < 80; x += 1) {
                    for(int y = 0; y < 45; y += 1) {
                        vertices.putFloat(  x  ).putFloat(  y  ).putFloat(0);
                        vertices.putFloat(1).putFloat(0).putFloat(0).putFloat(1);

                        vertices.putFloat(  x+1).putFloat(  y  ).putFloat(0);
                        vertices.putFloat(1).putFloat(0).putFloat(0).putFloat(1);

                        vertices.putFloat(  x+1).putFloat(  y+1).putFloat(0);
                        vertices.putFloat(1).putFloat(0).putFloat(0).putFloat(1);

                        vertices.putFloat(  x  ).putFloat(  y+1).putFloat(0);
                        vertices.putFloat(1).putFloat(0).putFloat(0).putFloat(1);
                    }
                }
                vertices.clear();

                ShortBuffer indices = BufferUtils.createShortBuffer(MAX_COUNT * 6);
                short offset = 0;
                int count = indices.remaining();
                for(int i = 0; i < count; i += 6) {
                    indices.put((short) (offset + 0));
                    indices.put((short) (offset + 1));
                    indices.put((short) (offset + 2));
                    indices.put((short) (offset + 2));
                    indices.put((short) (offset + 3));
                    indices.put((short) (offset + 0));
                    offset += 4;
                }
                indices.flip();

                vao = glGenVertexArrays();
                glBindVertexArray(vao);

                vbo = glGenBuffers();
                glBindBuffer(GL_ARRAY_BUFFER, vbo);
                glBufferData(GL_ARRAY_BUFFER, vertices, GL_DYNAMIC_DRAW);

                ibo = glGenBuffers();
                glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
                glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

                glEnableVertexAttribArray(0);
                glVertexAttribPointer(0, 3, GL_FLOAT, false, STRIDE, 0);

                glEnableVertexAttribArray(1);
                glVertexAttribPointer(10, 4, GL_FLOAT, false, STRIDE, 3 * 4);
                glBindVertexArray(0);
            }

            public void onRender() {
                glClearColor(0, 1, 0, 1);
                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
                shader.bind();
                shader.setUniformMat4f("pr_matrix", camera.getProjectionMatrix());

                glBindVertexArray(vao);
                glDrawElements(GL_TRIANGLES, 6 * 45 * 80, GL_UNSIGNED_SHORT, 0);
                glBindVertexArray(0);
                shader.unbind();

                //FPS count
                fps++;
                if(timer.elapsed() > 1) {
                    timer.set(timer.elapsed() - 1);
//                    System.out.println("FPS:" + fps);
//                    System.out.println(camera.getProjectionMatrix());
                    fps = 0;
                }
            }

            public void onDestroy() {}

            public void onKey(int key, int scancode, int action, int mods) {
                if(action != GLFW_RELEASE) {
                    switch (key) {
                        case GLFW_KEY_UP:
                            camera.getProjectionMatrix().translate(0, 1, 0);
                            break;
                        case GLFW_KEY_DOWN:
                            camera.getProjectionMatrix().translate(0, -1, 0);
                            break;
                        case GLFW_KEY_RIGHT:
                            camera.getProjectionMatrix().translate(1, 0, 0);
                            break;
                        case GLFW_KEY_LEFT:
                            camera.getProjectionMatrix().translate(-1, 0, 0);
                            break;
                    }
                }
            }
            public void onMouse(int button, int action, int mods) {}
            public void onScroll(double xOffset, double yOffset) {}
            public void onCursorPos(double xpos, double ypox) {}
            public void onCursorEnter(boolean inside) {}
            public void onFrameBuffer(int width, int height) {}
            public void onWindowPos(int xpos, int ypos) {}

            public void onWindowSize(int width, int height) {
                camera.getProjectionMatrix().setToOrthographic(0, width, 0, height, -1, 1);
            }

            public void onWindowFocus(boolean focussed) {}
            public void onWindowRefresh() {}
            public void onWindowClosed() {}
            public void onWindowIconified(boolean iconified) {}
        };
    }
}

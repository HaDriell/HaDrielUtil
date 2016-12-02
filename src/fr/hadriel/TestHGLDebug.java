package fr.hadriel;


import fr.hadriel.hgl.core.*;
import fr.hadriel.hgl.core.buffers.IndexGenerator;
import fr.hadriel.hgl.graphics.Shader;
import fr.hadriel.math.Matrix4f;
import fr.hadriel.time.Timer;
import org.lwjgl.BufferUtils;

import java.io.File;
import java.io.IOException;
import java.nio.FloatBuffer;
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

            Shader shader;
            Camera camera;

            int ibo, vboPosition, vboColor, vao;

            public void onInit() {
                camera = new Camera(Matrix4f.Orthographic(0, 800, 0, 450, -1, 1));
                try { shader = new Shader(new File("simple.vert"), new File("simple.frag")); } catch (Exception ignore) {
                    ignore.printStackTrace();
                }

                FloatBuffer positions = BufferUtils.createFloatBuffer(Short.MAX_VALUE * 3);
                FloatBuffer colors = BufferUtils.createFloatBuffer(Short.MAX_VALUE * 4);

                positions.put(  0f).put(  0f).put(  0f);
                positions.put(  0f).put(450f).put(  0f);
                positions.put(800f).put(450f).put(  0f);
                positions.put(800f).put(  0f).put(  0f);
                positions.flip();

                colors.put(1).put(0).put(0).put(1);
                colors.put(0).put(1).put(0).put(1);
                colors.put(0).put(0).put(1).put(1);
                colors.put(1).put(0).put(0).put(1);
                colors.flip();

                //Indices
                ShortBuffer indices = IndexGenerator.QUADS.getIndexBuffer(6);
                vao = glGenVertexArrays();
                glBindVertexArray(vao);

                ibo = glGenBuffers();
                glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
                glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

                vboPosition = glGenBuffers();
                glBindBuffer(GL_ARRAY_BUFFER, vboPosition);
                glBufferData(GL_ARRAY_BUFFER, positions, GL_DYNAMIC_DRAW);
                glEnableVertexAttribArray(0);
                glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

                vboColor = glGenBuffers();
                glBindBuffer(GL_ARRAY_BUFFER, vboColor);
                glBufferData(GL_ARRAY_BUFFER, colors, GL_DYNAMIC_DRAW);
                glEnableVertexAttribArray(1);
                glVertexAttribPointer(1, 4, GL_FLOAT, false, 0, 0);

                glBindVertexArray(0);
            }

            public void onRender() {
                glClearColor(0, 0, 0, 1);
                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
                shader.bind();
                shader.setUniformMat4f("pr_matrix", camera.getProjectionMatrix());

                glBindVertexArray(vao);
                glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_SHORT, 0);
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

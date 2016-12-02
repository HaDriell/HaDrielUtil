package fr.hadriel;


import fr.hadriel.hgl.core.*;
import fr.hadriel.hgl.core.buffers.IndexBuffer;
import fr.hadriel.hgl.core.buffers.IndexGenerator;
import fr.hadriel.hgl.core.buffers.VertexArray;
import fr.hadriel.hgl.graphics.Shader;
import fr.hadriel.math.Matrix4f;
import fr.hadriel.time.Timer;
import org.lwjgl.BufferUtils;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by glathuiliere on 28/11/2016.
 */
public class TestHGL {

    public static final short MAX_SPRITES = Short.MAX_VALUE / 6;
    public static final short MAX_ELEMENTS = (short) (MAX_SPRITES * 6);

    public static void main(String[] args) throws IOException {
        new HGLContext() {

            Timer timer = new Timer();
            int fps;

            Shader shader;

            Camera camera;
            VertexArray vao;
            ByteBuffer colors;
            FloatBuffer positions;

            public void onInit() {
                camera = new Camera(Matrix4f.Orthographic(0, 800, 0, 450, -1, 1));
                try { shader = new Shader(new File("simple.vert"), new File("simple.frag")); } catch (IOException ignore) {}

                vao = new VertexArray();
                vao.bind();
                vao.setIndexation(new IndexBuffer(IndexGenerator.QUADS.getIndexBuffer(Short.MAX_VALUE)));
                vao.enableVertexLayout(0, GLType.FLOAT, 3); // position
                vao.enableVertexLayout(1, GLType.BYTE, 4, true); // color
                vao.unbind();

                positions = BufferUtils.createFloatBuffer(3 * MAX_ELEMENTS);
                colors = BufferUtils.createByteBuffer(4 * MAX_ELEMENTS);
            }

            public void onRender() {
                // Fill Data Buffers
                colors.clear();
                positions.clear();
                //poisitions  X      Y      Z
                positions.put(0).put(0).put(0);
                positions.put(0).put(450).put(0);
                positions.put(800).put(450).put(0);
                positions.put(800).put(0).put(0);

                //colors RGBA
                colors.put((byte)0xFF).put((byte)0x0).put((byte)0x0).put((byte)0xFF);
                colors.put((byte)0xFF).put((byte)0x0).put((byte)0x0).put((byte)0xFF);
                colors.put((byte)0xFF).put((byte)0x0).put((byte)0x0).put((byte)0xFF);
                colors.put((byte)0xFF).put((byte)0x0).put((byte)0x0).put((byte)0xFF);
                positions.flip();
                colors.flip();

                //Write buffers
                vao.setLayoutSubData(0, 0, positions);
                vao.setLayoutSubData(1, 0, colors);
                //
                glClearColor(0, 0, 0, 1);
                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
                shader.bind();
                shader.setUniformMat4f("pr_matrix", camera.getProjectionMatrix());

                vao.bind();
                vao.draw(GL_TRIANGLES, 6);
                vao.unbind();

                shader.unbind();

                //FPS count
                fps++;
                if(timer.elapsed() > 1) {
                    timer.set(timer.elapsed() - 1);
                    System.out.println("FPS:" + fps);
                    fps = 0;
                }
            }

            public void onDestroy() {}
            public void onKey(int key, int scancode, int action, int mods) {}
            public void onMouse(int button, int action, int mods) {}
            public void onScroll(double xOffset, double yOffset) {}
            public void onCursorPos(double xpos, double ypox) {}
            public void onCursorEnter(boolean inside) {}
            public void onFrameBuffer(int width, int height) {}
            public void onWindowPos(int xpos, int ypos) {}
            public void onWindowSize(int width, int height) {}
            public void onWindowFocus(boolean focussed) {}
            public void onWindowRefresh() {}
            public void onWindowClosed() {}
            public void onWindowIconified(boolean iconified) {}
        };
    }
}

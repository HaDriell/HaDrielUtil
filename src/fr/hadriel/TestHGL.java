package fr.hadriel;


import fr.hadriel.hgl.core.*;
import fr.hadriel.hgl.core.BufferLayout;
import fr.hadriel.hgl.core.buffers.VertexArray;
import fr.hadriel.hgl.core.buffers.IndexBuffer;
import fr.hadriel.hgl.core.buffers.VertexBuffer;
import fr.hadriel.hgl.graphics.Shader;
import fr.hadriel.hgl.graphics.Texture;
import fr.hadriel.hgl.stb.Image;
import fr.hadriel.math.Matrix4f;
import fr.hadriel.time.Timer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by glathuiliere on 28/11/2016.
 */
public class TestHGL {

    public static void main(String[] args) throws IOException {
        new HGLContext() {

            Timer timer = new Timer();
            int fps;
            Texture texture;
            Shader shader;
            Camera camera;

            VertexArray vao;
            IndexBuffer ibo;
            VertexBuffer vbo;

            BufferLayout layout;

            public void onInit() {
                glEnable(GL_BLEND);
                glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

                texture = new Texture(Image.load("Teron Fielsang.png"));
                camera = new Camera(Matrix4f.Orthographic(0, 800, 450, 0, -1, 1000));
                try {
                    shader = new Shader(new File("simple.vert"), new File("simple.frag"));
                    shader = new Shader(new File("basic.vert"), new File("basic.frag"));
                } catch (Exception error) {
                    error.printStackTrace();
                }

                layout = new BufferLayout();
                layout.pushVec4("position", false);
//                layout.pushVec2("uv", false);
//                layout.pushVec2("mask_uv", false);
//                layout.pushFloat("tid", 1, false);
//                layout.pushFloat("mid", 1, false);
//                layout.pushUnsignedInt("color", 1, false);
                int maxSprites = 6000;
                int spriteSize = layout.getStride() * 4;
                int vertexBufferSize = spriteSize * maxSprites;
                int indexBufferSize = maxSprites * 6;

                ByteBuffer vertices = BufferUtils.createByteBuffer(vertexBufferSize);
                vertices.putFloat(0).putFloat(0).putFloat(0).putFloat(0); //position
//                vertices.putFloat(0).putFloat(0); //uv
//                vertices.putFloat(0).putFloat(0); //mask_uv
//                vertices.putFloat(0); //tid (texture)
//                vertices.putFloat(0); //mid (mask)
//                vertices.putInt(0xFFFFFFFF); //color

                vertices.putFloat(0).putFloat(100).putFloat(0).putFloat(0); //position
//                vertices.putFloat(0).putFloat(1); //uv
//                vertices.putFloat(0).putFloat(0); //mask_uv
//                vertices.putFloat(0); //tid (texture)
//                vertices.putFloat(0); //mid (mask)
//                vertices.putInt(0xFFFFFFFF); //color

                vertices.putFloat(100).putFloat(100).putFloat(0).putFloat(0); //position
//                vertices.putFloat(1).putFloat(1); //uv
//                vertices.putFloat(0).putFloat(0); //mask_uv
//                vertices.putFloat(0); //tid (texture)
//                vertices.putFloat(0); //mid (mask)
//                vertices.putInt(0xFFFFFFFF); //color

                vertices.putFloat(100).putFloat(0).putFloat(0).putFloat(0); //position
//                vertices.putFloat(1).putFloat(0); //uv
//                vertices.putFloat(0).putFloat(0); //mask_uv
//                vertices.putFloat(0); //tid (texture)
//                vertices.putFloat(0); //mid (mask)
//                vertices.putInt(0xFFFFFFFF); //color
                vertices.flip();

                ShortBuffer indices = BufferUtils.createShortBuffer(indexBufferSize);
                int offset = 0;
                for(int i = 0; i < indexBufferSize; i += 6) {
                    indices.put((short) (offset + 0));
                    indices.put((short) (offset + 1));
                    indices.put((short) (offset + 2));
                    indices.put((short) (offset + 2));
                    indices.put((short) (offset + 3));
                    indices.put((short) (offset + 0));
                    offset += 4;
                }
                indices.flip();

                vao = new VertexArray();
                vao.bind();

                ibo = new IndexBuffer(GL_UNSIGNED_SHORT, indexBufferSize);
                ibo.bind();
                GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indices, ibo.getUsage());

                vbo = new VertexBuffer(vertexBufferSize);
                vbo.bind();
                GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertices, vbo.getUsage());

                layout.bind(0);

                ibo.unbind();
                vbo.unbind();
                vao.unbind();
            }

            public void onRender() {
                glClearColor(0, 0, 0, 1);
                glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
                shader.bind();
                shader.setUniformMat4f("pr_matrix", camera.getProjectionMatrix());
                GL13.glActiveTexture(GL13.GL_TEXTURE0);
                texture.bind();
                vao.bind();
                vao.draw(4);
                vao.unbind();
                texture.unbind();
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

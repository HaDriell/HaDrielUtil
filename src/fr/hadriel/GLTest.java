package fr.hadriel;

import fr.hadriel.hgl.glfw.GLFWWindow;
import fr.hadriel.hgl.opengl.*;
import fr.hadriel.hgl.resources.Material;
import fr.hadriel.hgl.resources.Object3D;
import fr.hadriel.math.Matrix4f;
import fr.hadriel.math.Vec3;
import fr.hadriel.time.Timer;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import java.io.File;

/**
 * Created by HaDriel on 04/12/2016.
 */
public class GLTest {

    public static void main(String[] args) {
        new GLFWWindow() {
            private Texture2D texture;

            private MultiBufferVertexArray vao;

            private Object3D obj;
            private Material material;
            private Shader shader;
            private Timer timer;

            private Matrix4f projection = Matrix4f.Perspective(120, 8 / 4.5f, 0, 1000);
            private Matrix4f model = new Matrix4f().translate(-1, -1, 0);
            private Matrix4f view = new Matrix4f().translate(0, 0, -5);

            public void onInit() {
                timer = new Timer();
                try {
                    obj = new Object3D("cube.obj");
                    material = new Material("white.mtl");
                    shader = new Shader(new File("phong.vert"), new File("phong.frag"));
                    texture = new Texture2D("Teron Fielsang.png");
//                    texture = new Texture2D("illuminati.png");

                } catch (Exception ignore) {
                    ignore.printStackTrace();
                }

                vao = new MultiBufferVertexArray(obj.getVertexCount(),
                        new AttribPointer(GLType.FLOAT, 3), //position
                        new AttribPointer(GLType.FLOAT, 3), //normal
                        new AttribPointer(GLType.FLOAT, 2)  //uv
                );
            }

            public void onRender(long window) {
                obj.draw(vao, 0, 1, 2);
                GL11.glEnable(GL11.GL_DEPTH_TEST);
                GL11.glClearColor(0, 0, 0, 1);
                GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
                model.rotate(timer.elapsed() * 50, new Vec3(0, 1, 0));
                timer.reset();

                shader.bind();
                shader.setUniformMat4f("projection", projection);
                shader.setUniformMat4f("model", model);
                shader.setUniformMat4f("view", view);

                shader.setUniform3f("cameraPosition", new Vec3());
                shader.setUniform3f("lightPosition", new Vec3(0, 3, 5));
                material.setUniforms(shader);
                shader.setUniform3f("ambientLight", new Vec3(1, 1, 1));
                shader.setUniform3f("diffuseLight", new Vec3(1, 1, 1));
                shader.setUniform3f("specularLight", new Vec3(1, 1, 1));

                texture.bind();
                vao.bind();
                vao.draw(GL11.GL_TRIANGLES);
                vao.unbind();
                texture.unbind();

                shader.unbind();
            }

            public void onKey(long window, int key, int scancode, int action, int mods) {
                if(key == GLFW.GLFW_KEY_W) view.translate(0, 0, .1f);
                if(key ==  GLFW.GLFW_KEY_S) view.translate(0, 0, -.1f);

                if(key ==  GLFW.GLFW_KEY_D) view.translate(.1f, 0, 0);
                if(key ==  GLFW.GLFW_KEY_A) view.translate(-.1f, 0, 0);

                if(key ==  GLFW.GLFW_KEY_SPACE) view.translate(0, .1f, 0);
                if(key ==  GLFW.GLFW_KEY_C) view.translate(0, -.1f, 0);
            }

            public void onDestroy() {}
            public void onMouseButton(long window, int button, int action, int mods) {}
            public void onMousePos(long window, double xpos, double ypos) {}
            public void onCursorEnter(long window, boolean entered) {}
            public void onWindowFocus(long window, boolean focus) {}
            public void onWindowClose(long window) {}
            public void onWindowSize(long window, int width, int height) {}
        };
    }
}
package fr.hadriel;

import fr.hadriel.hgl.glfw.GLFWWindow;
import fr.hadriel.hgl.opengl.*;
import fr.hadriel.math.Matrix4f;
import fr.hadriel.math.Vec2;
import fr.hadriel.math.Vec3;
import fr.hadriel.math.Vec4;
import fr.hadriel.time.Timer;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import java.io.File;

/**
 * Created by HaDriel on 04/12/2016.
 */
public class GLTest {


    public static final Vec3
            X = new Vec3(1, 0, 0),
            NX = new Vec3(-1, 0, 0),
            Y = new Vec3(0, 1, 0),
            NY = new Vec3(0, -1, 0),
            Z = new Vec3(0, 0, 1),
            NZ = new Vec3(0, 0, -1);

    public static final Vec4
            WHITE = new Vec4(1, 1, 1, 1),
            BLACK = new Vec4(0, 0, 0, 1),
            BLUE = new Vec4(0, 0, 1, 1),
            RED = new Vec4(1, 0, 0, 1),
            GREEN = new Vec4(0, 1, 0, 1);

    public static final Vec2
            TOP = new Vec2(0.5f, 0.1f),
            BOTL = new Vec2(0, 1),
            BOTR = new Vec2(1, 1);

    public static Vec3[] POSITIONS = new Vec3[] {
            Y, Z, X,
            Y, X, NZ,
            Y, NZ, NX,
            Y, NX, Z
    };

    public static Vec4[] COLORS = new Vec4[] {
            RED, RED, RED,
            GREEN, GREEN, GREEN,
            BLUE, BLUE, BLUE,
            WHITE, WHITE, WHITE
    };

    public static Vec2[] UVS = new Vec2[] {
            TOP, BOTL, BOTR,
            TOP, BOTL, BOTR,
            TOP, BOTL, BOTR,
            TOP, BOTL, BOTR,
    };




    /******************************************************************************************************************/
    // Main
    /******************************************************************************************************************/
    public static void main(String[] args) {
        new GLFWWindow() {
            private Texture2D texture;

            private SingleBufferVertexArray vao3;
            private MultiBufferVertexArray vao2;
            private OldVertexArray vao;

            private Shader shader;
            private Timer timer;
            private Matrix4f pr_matrix = Matrix4f.Perspective(120, 8 / 4.5f, 0, 1000);
            private Matrix4f ml_matrix = new Matrix4f();
            private Matrix4f vw_matrix = new Matrix4f().translate(0, 0, -5);

            public void onInit() {
                GL11.glEnable(GL11.GL_DEPTH_TEST);
                timer = new Timer();
                try {
                    shader = new Shader(new File("simple.vert"), new File("simple.frag"));
                    texture = new Texture2D("illuminati.png");
                } catch (Exception ignore) {
                    ignore.printStackTrace();
                }

                vao = new OldVertexArray(POSITIONS.length,
                        new AttribPointer(GLType.FLOAT, 3),
                        new AttribPointer(GLType.FLOAT, 4),
                        new AttribPointer(GLType.FLOAT, 2)
                );

                vao2 = new MultiBufferVertexArray(POSITIONS.length,
                        new AttribPointer(GLType.FLOAT, 3),
                        new AttribPointer(GLType.FLOAT, 4),
                        new AttribPointer(GLType.FLOAT, 2)
                );

                vao3 = new SingleBufferVertexArray(POSITIONS.length,
                        new AttribPointer(GLType.FLOAT, 3),
                        new AttribPointer(GLType.FLOAT, 4),
                        new AttribPointer(GLType.FLOAT, 2)
                );
                setupVAOS();
            }

            private void setupVAOS() {
                VertexBuffer positions, colors, uvs;

                //Setup Positions
                positions = vao.getBuffer(0);
                positions.bind();
                positions.map();
                for(Vec3 p : POSITIONS) positions.write(p);
                positions.unmap();
                positions.unbind();

                //Setup Colors
                colors = vao.getBuffer(1);
                colors.bind();
                colors.map();
                for(Vec4 c : COLORS) colors.write(c);
                colors.unmap();
                colors.unbind();

                //Setup UVs
                uvs = vao.getBuffer(2);
                uvs.bind();
                uvs.map();
                for(Vec2 v : UVS) uvs.write(v);
                uvs.unmap();
                uvs.unbind();

                /**************************************************/

                //Setup Positions
                positions = vao2.getBuffer(0);
                positions.bind();
                positions.map();
                for(Vec3 p : POSITIONS) positions.write(p);
                positions.unmap();
                positions.unbind();

                //Setup Colors
                colors = vao2.getBuffer(1);
                colors.bind();
                colors.map();
                for(Vec4 c : COLORS) colors.write(c);
                colors.unmap();
                colors.unbind();

                //Setup UVs
                uvs = vao2.getBuffer(2);
                uvs.bind();
                uvs.map();
                for(Vec2 v : UVS) uvs.write(v);
                uvs.unmap();
                uvs.unbind();

                /**************************************************/

                VertexBuffer vbo = vao3.getBuffer(0);
                //Setup Positions
                vbo.bind();
                vbo.map();
                for(int i = 0; i < POSITIONS.length; i++) {
                    vbo.write(POSITIONS[i]);
                    vbo.write(COLORS[i]);
                    vbo.write(UVS[i]);
                }
                vbo.unmap();
                vbo.unbind();
            }

            public void onRender(long window) {
                GL11.glClearColor(0, 0, 0, 1);
                GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

                ml_matrix.rotate(timer.elapsed() * 50, new Vec3(0, 1, 0));
                timer.reset();

                shader.bind();
                texture.bind();
                shader.setUniformMat4f("pr_matrix", pr_matrix);
                shader.setUniformMat4f("vw_matrix", vw_matrix);
                shader.setUniformMat4f("ml_matrix", ml_matrix);

//                vao.bind();
//                vao.draw(GL11.GL_TRIANGLES); // OK (will be deleted)
//                vao.unbind();

//                vao2.bind();
//                vao2.draw(GL11.GL_TRIANGLES); // OK
//                vao2.unbind();

                vao3.bind();
                vao3.draw(GL11.GL_TRIANGLES);
                vao3.unbind();

                texture.unbind();
                shader.unbind();
            }

            public void onKey(long window, int key, int scancode, int action, int mods) {
                if(key == GLFW.GLFW_KEY_W) vw_matrix.translate(0, 0, .1f);
                if(key ==  GLFW.GLFW_KEY_S) vw_matrix.translate(0, 0, -.1f);

                if(key ==  GLFW.GLFW_KEY_D) vw_matrix.translate(.1f, 0, 0);
                if(key ==  GLFW.GLFW_KEY_A) vw_matrix.translate(-.1f, 0, 0);

                if(key ==  GLFW.GLFW_KEY_SPACE) vw_matrix.translate(0, .1f, 0);
                if(key ==  GLFW.GLFW_KEY_C) vw_matrix.translate(0, -.1f, 0);
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
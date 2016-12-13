package fr.hadriel.hgl.resources;

import fr.hadriel.hgl.opengl.Texture2D;
import fr.hadriel.hgl.opengl.VertexArray;
import fr.hadriel.hgl.opengl.VertexBuffer;
import fr.hadriel.math.Vec2;
import fr.hadriel.math.Vec3;
import fr.hadriel.math.Vec4;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by HaDriel on 07/12/2016.
 */
public class Object3D {

    public static final class Face {
        public final int componentCount;
        public final int[] v;
        public final int[] vt;
        public final int[] vn;

        public Face(int componentCount) {
            this.componentCount = componentCount;
            this.v = new int[componentCount];
            this.vt = new int[componentCount];
            this.vn = new int[componentCount];
        }

        public String toString() {
            StringBuilder s = new StringBuilder();
            s.append("Face ");
            for(int i = 0; i < componentCount; i++) {
                s.append(String.format("%d/%d/%d", v[i], vt[i], vn[i])).append(" ");
            }
            return s.toString();
        }
    }

    private List<Vec3> positions;
    private List<Vec3> normals;
    private List<Vec4> colors;
    private List<Vec2> uvs;
    private List<Texture2D> textures;
    private List<Face> faces;

    public Object3D() {
        positions = new ArrayList<>();
        normals = new ArrayList<>();
        colors = new ArrayList<>();
        uvs = new ArrayList<>();
        textures = new ArrayList<>();
        faces = new ArrayList<>();
    }

    public Object3D(String filename) throws IOException {
        this();
        load(filename, this);
    }

    public int getVertexCount() {
        int count = 0;
        for(Face f : faces) count += f.componentCount;
        System.out.println(count + " vertices");
        return count;
    }

    public List<Vec3> getNormals() {
        return normals;
    }

    public List<Face> getFaces() {
        return faces;
    }

    public List<Texture2D> getTextures() {
        return textures;
    }

    public List<Vec2> getUvs() {
        return uvs;
    }

    public List<Vec3> getPositions() {
        return positions;
    }

    public List<Vec4> getColors() {
        return colors;
    }

    public static void load(String filename, Object3D o) throws IOException {
        o.positions.clear();
        o.normals.clear();
        o.colors.clear();
        o.uvs.clear();
        o.textures.clear();
        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
        String line;
        while ((line = in.readLine()) != null) {
            if(line.startsWith("#")) continue;
            String[] words = line.trim().split("\\s+");
            switch (words[0]) {
                case "v":
                    Vec3 position = new Vec3(Float.parseFloat(words[1]), Float.parseFloat(words[2]), Float.parseFloat(words[3]));
                    o.positions.add(position);
                    break;
                case "vn":
                    Vec3 normal = new Vec3(Float.parseFloat(words[1]), Float.parseFloat(words[2]), Float.parseFloat(words[3]));
                    o.normals.add(normal);
                    break;
                case "vt":
                    Vec2 uv = new Vec2(Float.parseFloat(words[1]), Float.parseFloat(words[2]));
                    o.uvs.add(uv);
                    break;
                case "f":
                    Face f = new Face(words.length - 1);
                    for(int w = 1; w < words.length; w++) {
                        String[] ids = words[w].split("/");
                        f.v[w-1] = Integer.parseInt(ids[0]) - 1;
                        f.vt[w-1] = Integer.parseInt(ids[1]) - 1;
                        f.vn[w-1] = Integer.parseInt(ids[2]) - 1;
                    }
                    o.faces.add(f);
                    break;
            }
        }
        in.close();
    }

    public void draw(VertexArray vao, int positionBufferIndex, int normalBufferIndex, int uvBufferIndex) {
        VertexBuffer vbo;

        //Positions
        vbo = vao.getBuffer(positionBufferIndex);
        vbo.bind().map();
        for(Face f : faces) {
            vbo.write(positions.get(f.v[0]));
            vbo.write(positions.get(f.v[1]));
            vbo.write(positions.get(f.v[2]));
        }
        vbo.unmap().unbind();

        //Normals
        vbo = vao.getBuffer(normalBufferIndex);
        vbo.bind().map();
        for(Face f : faces) {
            vbo.write(normals.get(f.vn[0]));
            vbo.write(normals.get(f.vn[1]));
            vbo.write(normals.get(f.vn[2]));
        }
        vbo.unmap().unbind();

        //UVs
        vbo = vao.getBuffer(uvBufferIndex);
        vbo.bind().map();
        for(Face f : faces) {
            vbo.write(uvs.get(f.vt[0]));
            vbo.write(uvs.get(f.vt[1]));
            vbo.write(uvs.get(f.vt[2]));
        }
        vbo.unmap().unbind();
    }
}
package fr.hadriel.math;

import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by glathuiliere on 28/11/2016.
 */
public class Matrix4f {

    //[row + col * 4]
    public final float[] elements;

    public Matrix4f(float diagonal) {
        elements = new float[4 * 4];
        for (int i = 0; i < 4; i++)
            elements[i + i * 4] = diagonal;
    }

    public Matrix4f() {
        this(1);
    }

    public Matrix4f(float[] flatmatrix) {
        this(0);
        set(flatmatrix);
    }

    public Matrix4f(Matrix4f m) {
        this(m.elements);
    }

    public Vec3 getPosition() {
        return new Vec3(
                elements[0 + 3 * 4],
                elements[1 + 3 * 4],
                elements[2 + 3 * 4]);
    }

    public Matrix4f setPosition(float x, float y, float z) {
        elements[0 + 3 * 4] = x;
        elements[1 + 3 * 4] = y;
        elements[2 + 3 * 4] = z;
        return this;
    }

    public Matrix4f multiply(Matrix4f rightMatrix) {
        float[] data = new float[16];
        for(int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                float sum = 0;
                for(int e = 0; e < 4; e++) {
                    sum += elements[e + row * 4] * rightMatrix.elements[col + e * 4];
                }
                data[col + row * 4] = sum;
            }
        }
        return set(data);
    }

    public Vec3 multiply(Vec3 v) { return multiply(v.x, v.y, v.z); }
    public Vec3 multiply(float x, float y, float z) {
        float nx = elements[0 + 0 * 4] * x + elements[0 + 1 * 4] * y + elements[0 + 2 * 4] * z + elements[0 + 3 * 4];
        float ny = elements[1 + 0 * 4] * x + elements[1 + 1 * 4] * y + elements[1 + 2 * 4] * z + elements[1 + 3 * 4];
        float nz = elements[2 + 0 * 4] * x + elements[2 + 1 * 4] * y + elements[2 + 2 * 4] * z + elements[2 + 3 * 4];
        return new Vec3(nx, ny, nz);
    }

    public Vec4 multiply(Vec4 v) { return multiply(v.x, v.y, v.z, v.w); }
    public Vec4 multiply(float x, float y, float z, float w) {
        float nx = elements[0 + 0 * 4] * x + elements[0 + 1 * 4] * y + elements[0 + 2 * 4] * z + elements[0 + 3 * 4] * w;
        float ny = elements[1 + 0 * 4] * x + elements[1 + 1 * 4] * y + elements[1 + 2 * 4] * z + elements[1 + 3 * 4] * w;
        float nz = elements[2 + 0 * 4] * x + elements[2 + 1 * 4] * y + elements[2 + 2 * 4] * z + elements[2 + 3 * 4] * w;
        float nw = elements[3 + 0 * 4] * x + elements[3 + 1 * 4] * y + elements[3 + 2 * 4] * z + elements[3 + 3 * 4] * w;
        return new Vec4(nx, ny, nz, nw);
    }

    public Matrix4f translate(float x, float y, float z) { return translate(new Vec3(x, y, z)); }
    public Matrix4f translate(Vec3 v) {
        multiply(Matrix4f.Translation(v));
        return this;
    }

    public Matrix4f rotate(float angle, Vec3 axis) {
        multiply(Matrix4f.Rotation(angle, axis));
        return this;
    }

    public Matrix4f rotate(Vec4 quaternion) {
        multiply(Matrix4f.Rotation(quaternion));
        return this;
    }

    public Matrix4f scale(Vec3 scale) {
        multiply(Matrix4f.Scale(scale));
        return this;
    }

    public Matrix4f invert() {
        float[] data = new float[4 * 4];
        data[0] = elements[5] * elements[10] * elements[15] -
                elements[5] * elements[11] * elements[14] -
                elements[9] * elements[6] * elements[15] +
                elements[9] * elements[7] * elements[14] +
                elements[13] * elements[6] * elements[11] -
                elements[13] * elements[7] * elements[10];

        data[4] = -elements[4] * elements[10] * elements[15] +
                elements[4] * elements[11] * elements[14] +
                elements[8] * elements[6] * elements[15] -
                elements[8] * elements[7] * elements[14] -
                elements[12] * elements[6] * elements[11] +
                elements[12] * elements[7] * elements[10];

        data[8] = elements[4] * elements[9] * elements[15] -
                elements[4] * elements[11] * elements[13] -
                elements[8] * elements[5] * elements[15] +
                elements[8] * elements[7] * elements[13] +
                elements[12] * elements[5] * elements[11] -
                elements[12] * elements[7] * elements[9];

        data[12] = -elements[4] * elements[9] * elements[14] +
                elements[4] * elements[10] * elements[13] +
                elements[8] * elements[5] * elements[14] -
                elements[8] * elements[6] * elements[13] -
                elements[12] * elements[5] * elements[10] +
                elements[12] * elements[6] * elements[9];

        data[1] = -elements[1] * elements[10] * elements[15] +
                elements[1] * elements[11] * elements[14] +
                elements[9] * elements[2] * elements[15] -
                elements[9] * elements[3] * elements[14] -
                elements[13] * elements[2] * elements[11] +
                elements[13] * elements[3] * elements[10];

        data[5] = elements[0] * elements[10] * elements[15] -
                elements[0] * elements[11] * elements[14] -
                elements[8] * elements[2] * elements[15] +
                elements[8] * elements[3] * elements[14] +
                elements[12] * elements[2] * elements[11] -
                elements[12] * elements[3] * elements[10];

        data[9] = -elements[0] * elements[9] * elements[15] +
                elements[0] * elements[11] * elements[13] +
                elements[8] * elements[1] * elements[15] -
                elements[8] * elements[3] * elements[13] -
                elements[12] * elements[1] * elements[11] +
                elements[12] * elements[3] * elements[9];

        data[13] = elements[0] * elements[9] * elements[14] -
                elements[0] * elements[10] * elements[13] -
                elements[8] * elements[1] * elements[14] +
                elements[8] * elements[2] * elements[13] +
                elements[12] * elements[1] * elements[10] -
                elements[12] * elements[2] * elements[9];

        data[2] = elements[1] * elements[6] * elements[15] -
                elements[1] * elements[7] * elements[14] -
                elements[5] * elements[2] * elements[15] +
                elements[5] * elements[3] * elements[14] +
                elements[13] * elements[2] * elements[7] -
                elements[13] * elements[3] * elements[6];

        data[6] = -elements[0] * elements[6] * elements[15] +
                elements[0] * elements[7] * elements[14] +
                elements[4] * elements[2] * elements[15] -
                elements[4] * elements[3] * elements[14] -
                elements[12] * elements[2] * elements[7] +
                elements[12] * elements[3] * elements[6];

        data[10] = elements[0] * elements[5] * elements[15] -
                elements[0] * elements[7] * elements[13] -
                elements[4] * elements[1] * elements[15] +
                elements[4] * elements[3] * elements[13] +
                elements[12] * elements[1] * elements[7] -
                elements[12] * elements[3] * elements[5];

        data[14] = -elements[0] * elements[5] * elements[14] +
                elements[0] * elements[6] * elements[13] +
                elements[4] * elements[1] * elements[14] -
                elements[4] * elements[2] * elements[13] -
                elements[12] * elements[1] * elements[6] +
                elements[12] * elements[2] * elements[5];

        data[3] = -elements[1] * elements[6] * elements[11] +
                elements[1] * elements[7] * elements[10] +
                elements[5] * elements[2] * elements[11] -
                elements[5] * elements[3] * elements[10] -
                elements[9] * elements[2] * elements[7] +
                elements[9] * elements[3] * elements[6];

        data[7] = elements[0] * elements[6] * elements[11] -
                elements[0] * elements[7] * elements[10] -
                elements[4] * elements[2] * elements[11] +
                elements[4] * elements[3] * elements[10] +
                elements[8] * elements[2] * elements[7] -
                elements[8] * elements[3] * elements[6];

        data[11] = -elements[0] * elements[5] * elements[11] +
                elements[0] * elements[7] * elements[9] +
                elements[4] * elements[1] * elements[11] -
                elements[4] * elements[3] * elements[9] -
                elements[8] * elements[1] * elements[7] +
                elements[8] * elements[3] * elements[5];

        data[15] = elements[0] * elements[5] * elements[10] -
                elements[0] * elements[6] * elements[9] -
                elements[4] * elements[1] * elements[10] +
                elements[4] * elements[2] * elements[9] +
                elements[8] * elements[1] * elements[6] -
                elements[8] * elements[2] * elements[5];

        float det = elements[0] * data[0] + elements[1] * data[4] + elements[2] * data[8] + elements[3] * data[12];
        det = 1 / det;

        //More Efficient than
        for(int i = 0; i < data.length; i++)
            elements[i] = data[i] * det;
        return this;
    }

    public Matrix4f set(float[] flatMatrix) {
        if(flatMatrix.length != 16) throw new IllegalArgumentException("FlatMatrix length != 16");
        System.arraycopy(flatMatrix, 0, elements, 0, 16);
        return this;
    }

    public Matrix4f setIdentity() {
        for(int i = 0; i < 4 * 4; i++) {
            elements[i] = 0;
        }
        elements[0 + 0 * 4] = 1;
        elements[1 + 1 * 4] = 1;
        elements[2 + 2 * 4] = 1;
        elements[3 + 3 * 4] = 1;
        return this;
    }

    public Matrix4f setOrthographic(float left, float right, float bottom, float top, float near, float far) {
        setIdentity();
        elements[0 + 0 * 4] = 2f / (right - left);
        elements[1 + 1 * 4] = 2f / (top - bottom);
        elements[2 + 2 * 4] = 2f / (near - far);

        elements[3 + 0 * 4] = (left + right) / (left - right);
        elements[3 + 1 * 4] = (bottom + top) / (bottom - top);
        elements[3 + 2 * 4] = (far + near) / (far - near);
        return this;
    }

    public Matrix4f setPerspective(float fov, float aspectRatio, float near, float far) {
        setIdentity();
        float q = 1 / (float) Math.tan(Math.toRadians(0.5 * fov));
        float a = q / aspectRatio;
        float b = (near + far) / (near - far);
        float c = (2 * near * far) / (near - far);
        elements[0 + 0 * 4] = a;
        elements[1 + 1 * 4] = q;
        elements[2 + 2 * 4] = b;
        elements[2 + 3 * 4] = -1;
        elements[3 + 2 * 4] = c;
        return this;
    }

    public Matrix4f setLookAt(Vec3 camera, Vec3 object, Vec3 up) {
        setIdentity();
        Vec3 f = object.copy().sub(camera).normalize();
        Vec3 s = f.cross(up.copy().normalize());
        Vec3 u = s.cross(f);

        elements[0 + 0 * 4] = s.x;
        elements[0 + 1 * 4] = s.y;
        elements[0 + 2 * 4] = s.z;

        elements[1 + 0 * 4] = u.x;
        elements[1 + 1 * 4] = u.y;
        elements[1 + 2 * 4] = u.z;

        elements[2 + 0 * 4] = -f.x;
        elements[2 + 1 * 4] = -f.y;
        elements[2 + 2 * 4] = -f.z;
        return translate(-camera.x, -camera.y, -camera.z);
    }

    public Matrix4f setTranslation(Vec3 translation) {
        setIdentity();
        return translate(translation);
    }

    public Matrix4f setRotation(float angle, Vec3 axis) {
        setIdentity();
        float r = (float) Math.toRadians(angle);
        float c = (float) Math.cos(r);
        float s = (float) Math.sin(r);
        float omc = 1 - c;

        float x = axis.x;
        float y = axis.y;
        float z = axis.z;

        elements[0 + 0 * 4] = x * omc + c;
        elements[0 + 1 * 4] = y * x * omc + z * s;
        elements[0 + 2 * 4] = x * z * omc - y * s;

        elements[1 + 0 * 4] = x * y * omc - z * s;
        elements[1 + 1 * 4] = y * omc + c;
        elements[1 + 2 * 4] = y * z * omc + x * s;

        elements[2 + 0 * 4] = x * z * omc + y * s;
        elements[2 + 1 * 4] = y * z * omc - x * s;
        elements[2 + 2 * 4] = z * omc + c;
        return this;
    }

    public Matrix4f setRotation(Vec4 quaternion) {
        setIdentity();
        float qx, qy, qz, qw, qx2, qy2, qz2, qxqx2, qyqy2, qzqz2, qxqy2, qyqz2, qzqw2, qxqz2, qyqw2, qxqw2;
        qx = quaternion.x;
        qy = quaternion.y;
        qz = quaternion.z;
        qw = quaternion.w;
        qx2 = (qx + qx);
        qy2 = (qy + qy);
        qz2 = (qz + qz);
        qxqx2 = (qx * qx2);
        qxqy2 = (qx * qy2);
        qxqz2 = (qx * qz2);
        qxqw2 = (qw * qx2);
        qyqy2 = (qy * qy2);
        qyqz2 = (qy * qz2);
        qyqw2 = (qw * qy2);
        qzqz2 = (qz * qz2);
        qzqw2 = (qw * qz2);

        //[row + col * 4]
        //row 0
        elements[0 + 0 * 4] = ((1.0f - qyqy2) - qzqz2);
        elements[0 + 1 * 4] = (qxqy2 - qzqw2);
        elements[0 + 2 * 4] = (qxqz2 + qyqw2);
        elements[0 + 3 * 4] = 0f;
        //row 1
        elements[1 + 0 * 4] = (qxqy2 + qzqw2);
        elements[1 + 1 * 4] = ((1.0f - qxqx2) - qzqz2);
        elements[1 + 2 * 4] = (qyqz2 - qxqw2);
        elements[1 + 3 * 4] = 0f;
        //row 2
        elements[2 + 0 * 4] = (qxqz2 - qyqw2);
        elements[2 + 1 * 4] = (qyqz2 + qxqw2);
        elements[2 + 2 * 4] = ((1.0f - qxqx2) - qyqy2);
        elements[2 + 3 * 4] = 0f;
        return this;
    }

    public Matrix4f setScale(Vec3 scale) {
        setIdentity();
        elements[0 + 0 * 4] = scale.x;
        elements[1 + 1 * 4] = scale.y;
        elements[2 + 2 * 4] = scale.z;
        return this;
    }

    public Matrix4f transpose() {
        float[] data = new float[16];

        //TODO : test
        for(int x = 0; x < 4; x++)
            for(int y = 0; y < 4; y++)
                data[x + y * 4] = elements[y + x * 4];

        return set(data);
    }

    public Matrix4f copy() {
        return new Matrix4f(this);
    }

    public String toString() {
        return "Matrix4\n" +
                String.format("[%.2f, %.2f, %.2f, %.2f]\n", elements[0 + 0 * 4], elements[0 + 1 * 4], elements[0 + 2 * 4], elements[0 + 3 * 4]) +
                String.format("[%.2f, %.2f, %.2f, %.2f]\n", elements[1 + 0 * 4], elements[1 + 1 * 4], elements[1 + 2 * 4], elements[1 + 3 * 4]) +
                String.format("[%.2f, %.2f, %.2f, %.2f]\n", elements[2 + 0 * 4], elements[2 + 1 * 4], elements[2 + 2 * 4], elements[2 + 3 * 4]) +
                String.format("[%.2f, %.2f, %.2f, %.2f]", elements[3 + 0 * 4], elements[3 + 1 * 4], elements[3 + 2 * 4], elements[3 + 3 * 4]);
    }

    public FloatBuffer toFloatBuffer() {
        FloatBuffer floatBuffer = BufferUtils.createFloatBuffer(16);
        floatBuffer.put(elements).flip();
        return floatBuffer;
    }

    public static Matrix4f Orthographic(float left, float right, float bottom, float top, float near,float far) {
        return new Matrix4f().setOrthographic(left, right, bottom, top, near, far);
    }

    public static Matrix4f Perspective(float fov, float aspectRatio, float near,float far) {
        return new Matrix4f().setPerspective(fov, aspectRatio, near, far);
    }

    public static Matrix4f Rotation(float angle, Vec3 axis) {
        return new Matrix4f().setRotation(angle, axis);
    }

    public static Matrix4f Rotation(Vec4 quaternion) {
        return new Matrix4f().setRotation(quaternion);
    }

    public static Matrix4f Translation(Vec3 translation) {
        return new Matrix4f().setTranslation(translation);
    }

    public static Matrix4f Scale(Vec3 scale) {
        return new Matrix4f().setScale(scale);
    }
}
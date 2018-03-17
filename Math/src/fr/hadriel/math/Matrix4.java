package fr.hadriel.math;


/**
 * Created by glathuiliere on 13/07/2016.
 *
 * inspiration from
 * https://github.com/gamedev-js/vmath/blob/master/lib/mat4.js
 */
public strictfp class Matrix4 {
    private static final float[] IDENTITY = new float[] {
            1, 0, 0, 0,
            0, 1, 0, 0,
            0, 0, 1, 0,
            0, 0, 0, 1
    };

    public static final Matrix4 Identity = new Matrix4();

    /*
    * Matrix Order [M_COL_ROW] :
    *
    * [M00 .... M30]
    * [M01 .... M31]
    * [M02 .... M32]
    * [M03 .... M33]
    * */

    //First Row
    public static final byte M00 = 0;
    public static final byte M01 = 1;
    public static final byte M02 = 2;
    public static final byte M03 = 3;

    //Second Row
    public static final byte M10 = 4;
    public static final byte M11 = 5;
    public static final byte M12 = 6;
    public static final byte M13 = 7;

    //Third Row
    public static final byte M20 = 8;
    public static final byte M21 = 9;
    public static final byte M22 = 10;
    public static final byte M23 = 11;

    //Third Row
    public static final byte M30 = 12;
    public static final byte M31 = 13;
    public static final byte M32 = 14;
    public static final byte M33 = 15;

    public final float m00, m01, m02, m03;
    public final float m10, m11, m12, m13;
    public final float m20, m21, m22, m23;
    public final float m30, m31, m32, m33;

    public Matrix4(
            float m00, float m01, float m02, float m03,
            float m10, float m11, float m12, float m13,
            float m20, float m21, float m22, float m23,
            float m30, float m31, float m32, float m33) {
        this.m00 = m00; this.m01 = m01; this.m02 = m02; this.m03 = m03;
        this.m10 = m10; this.m11 = m11; this.m12 = m12; this.m13 = m13;
        this.m20 = m20; this.m21 = m21; this.m22 = m22; this.m23 = m23;
        this.m30 = m30; this.m31 = m31; this.m32 = m32; this.m33 = m33;
    }

    public Matrix4(float[] matrix) {
        this(
                matrix[M00], matrix[M01], matrix[M02], matrix[M03],
                matrix[M10], matrix[M11], matrix[M12], matrix[M13],
                matrix[M20], matrix[M21], matrix[M22], matrix[M23],
                matrix[M30], matrix[M31], matrix[M32], matrix[M33]);
    }
    public Matrix4(Matrix4 m) {
        this(
                m.m00, m.m01, m.m02, m.m03,
                m.m10, m.m11, m.m12, m.m13,
                m.m20, m.m21, m.m22, m.m23,
                m.m30, m.m31, m.m32, m.m33);
    }

    public Matrix4() {
        this(IDENTITY);
    }

    public float determinant() {
        float b00 = m00 * m11 - m01 * m10;
        float b01 = m00 * m12 - m02 * m10;
        float b02 = m00 * m13 - m03 * m10;
        float b03 = m01 * m12 - m02 * m11;
        float b04 = m01 * m13 - m03 * m11;
        float b05 = m02 * m13 - m03 * m12;
        float b06 = m20 * m31 - m21 * m30;
        float b07 = m20 * m32 - m22 * m30;
        float b08 = m20 * m33 - m23 * m30;
        float b09 = m21 * m32 - m22 * m31;
        float b10 = m21 * m33 - m23 * m31;
        float b11 = m22 * m33 - m23 * m32;
        return b00 * b11 - b01 * b10 + b02 * b09 + b03 * b08 - b04 * b07 + b05 * b06;
    }

    public Matrix4 invert() {
        float b00 = m00 * m11 - m01 * m10;
        float b01 = m00 * m12 - m02 * m10;
        float b02 = m00 * m13 - m03 * m10;
        float b03 = m01 * m12 - m02 * m11;
        float b04 = m01 * m13 - m03 * m11;
        float b05 = m02 * m13 - m03 * m12;
        float b06 = m20 * m31 - m21 * m30;
        float b07 = m20 * m32 - m22 * m30;
        float b08 = m20 * m33 - m23 * m30;
        float b09 = m21 * m32 - m22 * m31;
        float b10 = m21 * m33 - m23 * m31;
        float b11 = m22 * m33 - m23 * m32;

        float det = b00 * b11 - b01 * b10 + b02 * b09 + b03 * b08 - b04 * b07 + b05 * b06;

        if(det == 0) return null; // singular matrix
        det = 1f / det;

        float[] elements = new float[16];
        elements[M00] = (m11 * b11 - m12 * b10 + m13 * b09) * det;
        elements[M01] = (m02 * b10 - m01 * b11 - m03 * b09) * det;
        elements[M02] = (m31 * b05 - m32 * b04 + m33 * b03) * det;
        elements[M03] = (m22 * b04 - m21 * b05 - m23 * b03) * det;
        elements[M10] = (m12 * b08 - m10 * b11 - m13 * b07) * det;
        elements[M11] = (m00 * b11 - m02 * b08 + m03 * b07) * det;
        elements[M12] = (m32 * b02 - m30 * b05 - m33 * b01) * det;
        elements[M13] = (m20 * b05 - m22 * b02 + m23 * b01) * det;
        elements[M20] = (m10 * b10 - m11 * b08 + m13 * b06) * det;
        elements[M21] = (m01 * b08 - m00 * b10 - m03 * b06) * det;
        elements[M22] = (m30 * b04 - m31 * b02 + m33 * b00) * det;
        elements[M23] = (m21 * b02 - m20 * b04 - m23 * b00) * det;
        elements[M30] = (m11 * b07 - m10 * b09 - m12 * b06) * det;
        elements[M31] = (m00 * b09 - m01 * b07 + m02 * b06) * det;
        elements[M32] = (m31 * b01 - m30 * b03 - m32 * b00) * det;
        elements[M33] = (m20 * b03 - m21 * b01 + m22 * b00) * det;
        return new Matrix4(elements);
    }

    public static Matrix4 Orthographic(float left, float right, float top, float bottom, float near, float far) {
        float[] elements = new float[16];
        elements[M00] = 2f / (right - left);
        elements[M11] = 2f / (top - bottom);
        elements[M22] = -2f / (far - near);
        elements[M33] = 1f;

        elements[M30] = -(right + left) / (right - left);
        elements[M31] = -(top + bottom) / (top - bottom);
        elements[M32] = -(far + near) / (far - near);
        return new Matrix4(elements);
    }

    public Matrix4 Perspective(float fov, float aspectRatio, float near, float far) {
        float[] elements = new float[16];
        elements[M00] = 1f;
        elements[M11] = 1f;
        elements[M22] = 1f;
        elements[M33] = 1f;

        float q = 1f / (float) Math.tan(Math.toRadians(0.5f * fov));
        float a = q / aspectRatio;

        float b = (near + far) / (near - far);
        float c = (2f * near * far) / (near - far);
        elements[M00] = a;
        elements[M11] = q;
        elements[M22] = b;
        elements[M23] = -1f;
        elements[M32] = c;
        return new Matrix4(elements);
    }

    public Matrix4 LookAt(Vec3 position, Vec3 target, Vec3 up) {
        float[] elements = new float[16];
        elements[M00] = 1f;
        elements[M11] = 1f;
        elements[M22] = 1f;
        elements[M33] = 1f;

        Vec3 f = position.sub(target).normalize();
        Vec3 s = f.cross(up.normalize());
        Vec3 u = s.cross(f);
        elements[M00] = s.x;
        elements[M01] = s.y;
        elements[M02] = s.z;

        elements[M10] = u.x;
        elements[M11] = u.y;
        elements[M12] = u.z;

        elements[M20] = -f.x;
        elements[M21] = -f.y;
        elements[M22] = -f.z;

        return new Matrix4(elements).multiply(Translation(-position.x, -position.y, -position.z));
    }

    public static Matrix4 Scale(float sx, float sy, float sz) {
        float[] elements = new float[16];
        elements[M00] = 1f;
        elements[M11] = 1f;
        elements[M22] = 1f;
        elements[M33] = 1f;

        elements[M00] *= sx;
        elements[M11] *= sy;
        elements[M22] *= sz;
        return new Matrix4(elements);
    }

    public static Matrix4 Rotation(float angle, Vec3 axis) {
        float[] elements = new float[16];
        float r = Mathf.toRadians(angle);
        float c = Mathf.cos(r);
        float s = Mathf.sin(r);
        float t = 1 - c;
        float x = axis.x;
        float y = axis.y;
        float z = axis.z;

        elements[M00] = x * x * t + c;
        elements[M01] = y * x * t + z * s;
        elements[M02] = z * x * t - y * s;
//        elements[M03] = 0;
        elements[M10] = x * y * t - z * s;
        elements[M11] = y * y * t + c;
        elements[M12] = z * y * t + x * s;
//        elements[M13] = 0;
        elements[M20] = x * z * t + y * s;
        elements[M21] = y * z * t - x * s;
        elements[M22] = z * z * t + c;
//        elements[M23] = 0;
//        elements[M30] = 0;
//        elements[M31] = 0;
//        elements[M32] = 0;
        elements[M33] = 1;
        return new Matrix4(elements);
    }

    public static Matrix4 RotationX(float angle) {
        float r = Mathf.toRadians(angle);
        float c = Mathf.cos(r);
        float s = Mathf.sin(r);
        float[] elements = new float[16];
        elements[M00] = 1;
//        elements[M01] = 0;
//        elements[M02] = 0;
//        elements[M03] = 0;
//        elements[M10] = 0;
        elements[M11] = c;
        elements[M12] = s;
//        elements[M13] = 0;
//        elements[M20] = 0;
        elements[M21] = -s;
        elements[M22] = c;
//        elements[M23] = 0;
//        elements[M30] = 0;
//        elements[M31] = 0;
//        elements[M32] = 0;
        elements[M33] = 1;
        return new Matrix4(elements);
    }

    public static Matrix4 RotationY(float angle) {
        float r = Mathf.toRadians(angle);
        float c = Mathf.cos(r);
        float s = Mathf.sin(r);
        float[] elements = new float[16];
        elements[M00] = c;
//        elements[M01] = 0;
        elements[M02] = -s;
//        elements[M03] = 0;
//        elements[M10] = 0;
        elements[M11] = 1;
//        elements[M12] = 0;
//        elements[M13] = 0;
        elements[M20] = s;
//        elements[M21] = 0;
        elements[M22] = c;
//        elements[M23] = 0;
//        elements[M30] = 0;
//        elements[M31] = 0;
//        elements[M32] = 0;
        elements[M33] = 1;
        return new Matrix4(elements);
    }

    public static Matrix4 RotationZ(float angle) {
        float r = Mathf.toRadians(angle);
        float c = Mathf.cos(r);
        float s = Mathf.sin(r);
        float[] elements = new float[16];
        elements[M00] = c;
        elements[M01] = s;
//        elements[M02] = 0;
//        elements[M03] = 0;
        elements[M10] = -s;
        elements[M11] = c;
//        elements[M12] = 0;
//        elements[M13] = 0;
//        elements[M20] = 0;
//        elements[M21] = 0;
        elements[M22] = 1;
//        elements[M23] = 0;
//        elements[M30] = 0;
//        elements[M31] = 0;
//        elements[M32] = 0;
        elements[M33] = 1;
        return new Matrix4(elements);
    }

    public static Matrix4 Translation(float x, float y, float z) {
        float[] elements = new float[16];
        elements[M00] = 1f;
        elements[M11] = 1f;
        elements[M22] = 1f;
        elements[M33] = 1f;

        elements[M30] = x;
        elements[M31] = y;
        elements[M32] = z;
        return new Matrix4(elements);
    }

    public float[] elements() {
        return new float[] {
                m00, m01, m02, m03,
                m10, m11, m12, m13,
                m20, m21, m22, m23,
                m30, m31, m32, m33
        };
    }

    public Matrix4 multiply(Matrix4 m) {
        float[] elements = new float[16];

        //ROW 0
        elements[M00] = m.m00 * m00 + m.m01 * m10 + m.m02 * m20 + m.m03 * m30;
        elements[M01] = m.m00 * m01 + m.m01 * m11 + m.m02 * m21 + m.m03 * m31;
        elements[M02] = m.m00 * m02 + m.m01 * m12 + m.m02 * m22 + m.m03 * m32;
        elements[M03] = m.m00 * m03 + m.m01 * m13 + m.m02 * m23 + m.m03 * m33;

        //ROW 1
        elements[M10] = m.m10 * m00 + m.m11 * m10 + m.m12 * m20 + m.m13 * m30;
        elements[M11] = m.m10 * m01 + m.m11 * m11 + m.m12 * m21 + m.m13 * m31;
        elements[M12] = m.m10 * m02 + m.m11 * m12 + m.m12 * m22 + m.m13 * m32;
        elements[M13] = m.m10 * m03 + m.m11 * m13 + m.m12 * m23 + m.m13 * m33;

        //ROW 2
        elements[M20] = m.m20 * m00 + m.m21 * m10 + m.m22 * m20 + m.m23 * m30;
        elements[M21] = m.m20 * m01 + m.m21 * m11 + m.m22 * m21 + m.m23 * m31;
        elements[M22] = m.m20 * m02 + m.m21 * m12 + m.m22 * m22 + m.m23 * m32;
        elements[M23] = m.m20 * m03 + m.m21 * m13 + m.m22 * m23 + m.m23 * m33;

        //ROW 3
        elements[M30] = m.m30 * m00 + m.m31 * m10 + m.m32 * m20 + m.m33 * m30;
        elements[M31] = m.m30 * m01 + m.m31 * m11 + m.m32 * m21 + m.m33 * m31;
        elements[M32] = m.m30 * m02 + m.m31 * m12 + m.m32 * m22 + m.m33 * m32;
        elements[M33] = m.m30 * m03 + m.m31 * m13 + m.m32 * m23 + m.m33 * m33;

        return new Matrix4(elements);
    }

    // assumed Vec4(x, y, 0, 0)
    public Vec2 multiply(Vec2 v) {
        float x = m00 * v.x + m10 * v.y;
        float y = m01 * v.x + m11 * v.y;
        return new Vec2(x, y);
    }

    // assumed Vec4(x, y, z, 0)
    public Vec3 multiply(Vec3 v) {
        float x = m00 * v.x + m10 * v.y + m20 * v.z;
        float y = m01 * v.x + m11 * v.y + m21 * v.z;
        float z = m02 * v.x + m12 * v.y + m22 * v.z;
        return new Vec3(x, y, z);
    }

    public Vec4 multiply(Vec4 v) {
        float x = m00 * v.x + m10 * v.y + m20 * v.z + m30 * v.w;
        float y = m01 * v.x + m11 * v.y + m21 * v.z + m31 * v.w;
        float z = m02 * v.x + m12 * v.y + m22 * v.z + m32 * v.w;
        float w = m03 * v.x + m13 * v.y + m23 * v.z + m33 * v.w;
        return new Vec4(x, y, z, w);
    }

    public String toString() {
        return
                String.format("[%.2f %.2f %.2f %.2f]", m00, m10, m20, m30) + '\n' +
                String.format("[%.2f %.2f %.2f %.2f]", m01, m11, m21, m31) + '\n' +
                String.format("[%.2f %.2f %.2f %.2f]", m02, m12, m22, m32) + '\n' +
                String.format("[%.2f %.2f %.2f %.2f]", m03, m13, m23, m33) + '\n';
    }
}
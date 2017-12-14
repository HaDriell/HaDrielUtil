package fr.hadriel.math;

/**
 * Created by glathuiliere on 13/07/2016.
 */
public class Matrix3f {

    //First Row
    public static final byte M00 = 0;
    public static final byte M01 = 1;
    public static final byte M02 = 2;

    //Second Row
    public static final byte M10 = 3;
    public static final byte M11 = 4;
    public static final byte M12 = 5;

    //Third Row
    public static final byte M20 = 6;
    public static final byte M21 = 7;
    public static final byte M22 = 8;


    private static final float[] IDENTITY = new float[] {
            1, 0, 0,
            0, 1, 0,
            0, 0, 1
    };

    public final float[] elements = new float[3 * 3];

    public Matrix3f(float[] flatMatrix) {
        set(flatMatrix);
    }

    public Matrix3f(float m00, float m01, float m02, float m10, float m11, float m12, float m20, float m21, float m22) {
        this(new float[] {
                m00, m01, m02,
                m10, m11, m12,
                m20, m21, m22
        });
    }

    public Matrix3f(Matrix3f m) {
        this(m.elements);
    }

    public Matrix3f() {
        this(IDENTITY);
    }

    public Matrix3f setToTransform(float scaleX, float scaleY, float rotation, float positionX, float positionY) {
        float cos = Mathf.cos(Mathf.toRadians(rotation));
        float sin = Mathf.sin(Mathf.toRadians(rotation));
        elements[M00] = cos * scaleX;
        elements[M01] = sin;
        elements[M02] = positionX;
        elements[M10] = -sin;
        elements[M11] = cos * scaleY;
        elements[M12] = positionY;
        elements[M20] = 0;
        elements[M21] = 0;
        elements[M22] = 1;
        return this;
    }

    public Matrix3f set(float[] flatMatrix) {
        if(flatMatrix.length != 9)
            throw new IllegalArgumentException("Invalid flat Matrix sizeProperty");
        System.arraycopy(flatMatrix, 0, elements, 0, flatMatrix.length);
        return this;
    }

    public Matrix3f set(Matrix3f matrix) {
        return set(matrix.elements);
    }

    public Matrix3f setToIdentity() {
        set(IDENTITY);
        return this;
    }

    public Matrix3f translate(float x, float y) {
        elements[M02] += x;
        elements[M12] += y;
        return this;
    }

    public Matrix3f scale(float x, float y) {
        elements[M00] *= x;
        elements[M11] *= y;
        return this;
    }

    public Matrix3f rotate(float angle) {
        return this.multiply(Matrix3f.Rotation(angle));
    }

    public Matrix3f setToScale(float sx, float sy) {
        elements[M00] = sx;
        elements[M01] = 0;
        elements[M02] = 0;

        elements[M10] = 0;
        elements[M11] = sy;
        elements[M12] = 0;

        elements[M20] = 0;
        elements[M21] = 0;
        elements[M22] = 1;
        return this;
    }

    /**
     * Around Z axis
     * @param angle in degree
     * @return this matrix setWallpaper to Rotation
     */
    public Matrix3f setToRotation(float angle) {
        float r = Mathf.toRadians(angle);
        float cos = Mathf.cos(r);
        float sin = Mathf.sin(r);
        elements[M00] = cos;
        elements[M01] = sin;
        elements[M02] = 0;

        elements[M10] = -sin;
        elements[M11] = cos;
        elements[M12] = 0;

        elements[M20] = 0;
        elements[M21] = 0;
        elements[M22] = 1;
        return this;
    }

    public Matrix3f setToTranslation(float x, float y) {
        elements[M00] = 1;
        elements[M01] = 0;
        elements[M02] = x;
        elements[M10] = 0;
        elements[M11] = 1;
        elements[M12] = y;
        elements[M20] = 0;
        elements[M21] = 0;
        elements[M22] = 1;
        return this;
    }

    public Matrix3f multiply(Matrix3f matrix) {
        float[] data = new float[9];
        for(int row = 0; row < 3; row++) {
            for(int col = 0; col < 3; col++) {
                float sum = 0;
                for(int e = 0; e < 3; e++) {
                    sum += elements[e + row * 3] * matrix.elements[col + e * 3];
                }
                data[col + row * 3] = sum;
            }
        }
        set(data);
        return this;
    }


    public Vec2 multiply(Vec2 v) { return multiply(v.x, v.y); }
    public Vec3 multiply(Vec3 v) { return multiply(v.x, v.y, v.z); }
    public Vec2 multiplyInverse(Vec2 v) { return multiplyInverse(v.x, v.y); }

    public Vec2 multiply(float x, float y) {
        float dx = x * elements[M00] + y * elements[M01] + elements[M02];
        float dy = x * elements[M10] + y * elements[M11] + elements[M12];
        return new Vec2(dx, dy);
    }

    public Vec3 multiply(float x, float y, float z) {
        float dx = elements[M00] * x + elements[M01] * y + elements[M02] * z;
        float dy = elements[M10] * x + elements[M11] * y + elements[M12] * z;
        float dz = elements[M20] * x + elements[M21] * y + elements[M22] * z;
        return new Vec3(dx, dy, dz);
    }

    //Credits to Thomas Lathuilière for his awesome work around inversion optimizations
    public Vec2 multiplyInverse(float x, float y) {
        x -= elements[M02];
        y -= elements[M12];
        float det22_inv = 1f / (elements[M00] * elements[M11] - elements[M01] * elements[M10]);
        float m00 =  det22_inv * elements[M11];
        float m01 =  det22_inv * -elements[M01];
        float m10 =  det22_inv * -elements[M10];
        float m11 =  det22_inv * elements[M00];
        float dx = x * m00 + y * m01;
        float dy = x * m10 + y * m11;
        return new Vec2(dx, dy);
    }

    public Vec2 getTranslation() {
        return new Vec2(elements[M02], elements[M12]);
    }

    public float getRotation() {
        Vec2 scale = getScale();
        float cos = elements[M00] / scale.x;
        float sin = -elements[M10] / scale.x;
        return Mathf.toDegrees(Mathf.atan2(cos, sin));
    }

    public Vec2 getScale() {
        float sx = new Vec2(elements[M00], elements[M10]).len();
        float sy = new Vec2(elements[M01], elements[M11]).len();
        return new Vec2(sx, sy);
    }

    public float det() {
        return elements[M00] * elements[M11] * elements[M22] +
                elements[M01] * elements[M12] * elements[M20] +
                elements[M02] * elements[M10] * elements[M21] -
                elements[M00] * elements[M12] * elements[M21] -
                elements[M01] * elements[M10] * elements[M22] -
                elements[M02] * elements[M11] * elements[M20];
    }

    public Matrix3f copy() {
        return new Matrix3f(this);
    }

    public Matrix3f invert() {
        float det = det();
        if(det == 0)
            throw new RuntimeException("Can't Invert Matrix");

        float invDet = 1f / det;
        float[] tmp = new float[9];

        // --- //
        tmp[M00] = elements[M11] * elements[M22] - elements[M12] * elements[M21];
        tmp[M01] = elements[M02] * elements[M21] - elements[M01] * elements[M22];
        tmp[M02] = elements[M01] * elements[M12] - elements[M02] * elements[M11];
        // --- //
        tmp[M10] = elements[M12] * elements[M20] - elements[M10] * elements[M22];
        tmp[M11] = elements[M00] * elements[M22] - elements[M02] * elements[M20];
        tmp[M12] = elements[M02] * elements[M10] - elements[M00] * elements[M12];
        // --- //
        tmp[M20] = elements[M10] * elements[M21] - elements[M11] * elements[M20];
        tmp[M21] = elements[M01] * elements[M20] - elements[M00] * elements[M21];
        tmp[M22] = elements[M00] * elements[M11] - elements[M01] * elements[M10];

        for(int i = 0; i < tmp.length; i++)
            tmp[i] *= invDet;

        set(tmp);
        return this;
    }

    public static Matrix3f Identity() {
        return new Matrix3f();
    }

    public static Matrix3f Scale(float sx, float sy) {
        return new Matrix3f().setToScale(sx, sy);
    }

    public static Matrix3f Rotation(float angle) {
        return new Matrix3f().setToRotation(angle);
    }

    public static Matrix3f Translation(float x, float y) {
        return new Matrix3f().setToTranslation(x, y);
    }

    public static Matrix3f Transform(float scaleX, float scaleY, float rotation, float positionX, float positionY) {
        return new Matrix3f().setToTransform(scaleX, scaleY, rotation, positionX, positionY);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[ ");
        for(int row = 0; row < 3; row++) {
            if (row > 0) {
                sb.append(" ");
            }
            sb.append("[");
            for (int col = 0; col < 3; col++) {
                if(col > 0) {
                    sb.append(", ");
                }
                sb.append(String.format("%.2f", elements[col + row * 3]));
            }
            sb.append("]");
        }
        sb.append(" ]");
        return sb.toString();
    }
}
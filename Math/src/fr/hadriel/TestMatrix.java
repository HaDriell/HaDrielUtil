package fr.hadriel;

import fr.hadriel.math.Matrix3;
import fr.hadriel.math.Matrix3f;
import fr.hadriel.math.Vec2;

public class TestMatrix {
    public static void main(String[] args) {
        test_matrix3f();
        test_matrix();
    }

    private static void test_matrix3f() {
        Vec2 v = new Vec2(5, 3);
        Vec2 dv = null;
        Matrix3f m;
        System.out.println("Starting test");
        for(float angle = 0; angle < 360; angle += 30) {
            m = new Matrix3f();
            m = m.multiply(Matrix3f.Translation(100, 30));
            m = m.multiply(Matrix3f.Rotation(angle));
            m = m.multiply(Matrix3f.Scale(1, 10));
            dv = m.multiply(v);
            dv = m.multiplyInverse(dv);

            if(v.equals(dv))
                continue;

            System.out.println(String.format("angle: %3.1f    origin:%s    (m-1 * (m * origin)): %s", angle, v,dv));
        }
        System.out.println("Ending test");
    }

    private static void test_matrix() {
        Vec2 v = new Vec2(5, 3);
        Vec2 dv =null;
        Matrix3 matrix;
        System.out.println("Starting test");
        for (float angle = 0; angle < 360; angle +=30) {
            matrix = Matrix3.Identity;
            matrix = matrix.multiply(Matrix3.Translation(100, 30));
            matrix = matrix.multiply(Matrix3.Rotation(angle));
            matrix = matrix.multiply(Matrix3.Scale(1, 10));
            dv = matrix.multiply(v);
            dv = matrix.multiplyInverse(dv);

            if(v.equals(dv))
                continue;

            System.out.println(String.format("angle: %3.1f    origin:%s    (m-1 * (m * origin)): %s", angle, v,dv));
        }
        System.out.println("Ending test");
    }
}
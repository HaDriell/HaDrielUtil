package fr.hadriel;

import fr.hadriel.math.Matrix3f;
import fr.hadriel.math.Vec2;

/**
 * TODO comment
 *
 * @author glathuiliere
 */
public class TestMatrix {
    public static void main(String[] args) {
        test_multiplication_inversion();
    }

    private static void test_multiplication_inversion() {
        Vec2 v = new Vec2(5, 3);
        Vec2 dv = null;
        Matrix3f m = new Matrix3f();
        for(float angle = 0; angle < 360; angle += 30) {
            m.setToIdentity();
            m = m.multiply(Matrix3f.Translation(100, 30));
            m = m.multiply(Matrix3f.Rotation(angle));
            m = m.multiply(Matrix3f.Scale(1, 10));
            dv = m.multiply(v);
            dv = m.multiplyInverse(dv);

            if(v.equals(dv))
                continue;

            System.out.println(String.format("angle: %3.1f    origin:%s    (m-1 * (m * origin)): %s", angle, v,dv));
        }
    }
}
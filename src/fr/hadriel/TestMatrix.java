package fr.hadriel;

import fr.hadriel.math.Matrix3f;

/**
 * Created by glathuiliere on 08/02/2017.
 */
public class TestMatrix {

    public static void main(String[] args) {
        Matrix3f matrix = Matrix3f.Translation(100, 100);
        matrix.scale(5, 5);
        matrix.rotate(45);
        matrix.translate(100, 100);
        System.out.println(matrix.copy().invert().multiply(matrix));
    }
}
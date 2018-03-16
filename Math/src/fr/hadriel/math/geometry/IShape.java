package fr.hadriel.math.geometry;

import fr.hadriel.math.Matrix3;
import fr.hadriel.math.Vec2;

import java.util.List;

public interface IShape {

    /**
     * Provides the normals of the Edges of that shape
     * @param matrix the transformation matrix
     * @return the array of normals transformed
     */
    public Vec2[] getNormals(Matrix3 matrix);

    /**
     * Provides the array of Vertices of that Shape
     * @param matrix the transformation matrix
     * @return the array of Vertices transformed
     */
    public Vec2[] getVertices(Matrix3 matrix);

    /**
     * Computes the convex decomposition of the shape
     * @return the list of convex Shapes of the decomposition
     */
    public List<IShape> getConvexDecomposition();

    /**
     * Computes the smallest circle surrounding the Shape
     * @return the smallest Circle surrounding the Shape
     */
    public Circle getBoundingCircle();

    public Interval getProjection(Vec2 axis);
}
package fr.hadriel.g2d.commandbuffer;

import fr.hadriel.math.Matrix3;
import fr.hadriel.math.Vec2;
import fr.hadriel.math.Vec4;

//package-private
public final class Command {
    public final Matrix3 transform;
    public final Vec2 position;
    public final Vec2 size;
    public final Vec4 color;
    public final Vec2[] uv;

    public Command(Matrix3 transform, float x, float y, float with, float height) {
        this(transform, new Vec2(x, y), new Vec2(with, height));
    }

    public Command(Matrix3 transform, Vec2 position, Vec2 size) {
        this(transform, position, size, Vec4.XYZW);
    }

    public Command(Matrix3 transform, float x, float y, float with, float height, Vec4 color) {
        this(transform, new Vec2(x, y), new Vec2(with, height), color);
    }

    public Command(Matrix3 transform, Vec2 position, Vec2 size, Vec4 color) {
        this.transform = transform;
        this.position = position;
        this.size = size;
        this.color = color;
        this.uv = new Vec2[] {
                Vec2.ZERO,
                Vec2.X,
                Vec2.XY,
                Vec2.Y
        };
    }
}
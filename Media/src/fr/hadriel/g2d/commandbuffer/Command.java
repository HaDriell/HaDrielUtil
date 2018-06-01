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

    public final int drawOrder;

    public Command(Matrix3 transform, Vec2 position, Vec2 size, Vec4 color, int drawOrder) {
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
        this.drawOrder = drawOrder;
    }
}
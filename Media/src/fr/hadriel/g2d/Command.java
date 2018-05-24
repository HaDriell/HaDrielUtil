package fr.hadriel.g2d;

import fr.hadriel.math.Matrix3;

public class Command {
    public final Matrix3 transform;
    public final Sprite sprite;

    public Command(Matrix3 transform, Sprite sprite) {
        this.transform = transform;
        this.sprite = sprite;
    }
}
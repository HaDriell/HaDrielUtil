package fr.hadriel.asset.graphics.ui;

public enum Align {
    TOP_LEFT,
    TOP_CENTER,
    TOP_RIGHT,

    MID_LEFT,
    MID_CENTER,
    MID_RIGHT,

    BOT_LEFT,
    BOT_CENTER,
    BOT_RIGHT;

    public boolean left() {
        return this == TOP_LEFT || this == MID_LEFT || this == BOT_LEFT;
    }

    public boolean center() {
        return this == TOP_CENTER || this == MID_CENTER || this == BOT_CENTER;
    }

    public boolean right() {
        return this == TOP_RIGHT || this == MID_RIGHT || this == BOT_RIGHT;
    }

    public boolean top() {
        return this == TOP_LEFT || this == TOP_CENTER || this == TOP_RIGHT;
    }

    public boolean mid() {
        return this == MID_LEFT || this == MID_CENTER || this == MID_RIGHT;
    }

    public boolean bot() {
        return this == BOT_LEFT || this == BOT_CENTER || this == MID_RIGHT;
    }
}

package collision;

public enum CollisionSide {

    LEFT,
    RIGHT,
    TOP,
    BOTTOM,

    BOTTOM_LEFT,
    BOTTOM_RIGHT,
    TOP_LEFT,
    TOP_RIGHT;

    public boolean isBottom() {
        return this == CollisionSide.BOTTOM || this == CollisionSide.BOTTOM_LEFT || this == CollisionSide.BOTTOM_RIGHT;
    }

    public boolean isTop() {
        return this == CollisionSide.TOP || this == CollisionSide.TOP_LEFT || this == CollisionSide.TOP_RIGHT;
    }
}

import java.util.Arrays;

public enum CollisionPlace {

    LEFT(1),
    RIGHT(2),
    TOP(4),
    BOTTOM(8),

    TOP_LEFT(5),
    BOTTOM_LEFT(9),
    TOP_RIGHT(6),
    BOTTOM_RIGHT(10);

    private int number;

    CollisionPlace(int number) {
        this.number = number;
    }

    public static CollisionPlace getCollisionPlaceByNumber(int number) {
        return Arrays.stream(CollisionPlace.values()).filter(collisionPlace1 -> collisionPlace1.number == number).findFirst().orElse(TOP);
    }
}

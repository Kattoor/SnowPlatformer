import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Snowball implements Updatable, Drawable {

    private int x;
    private int y;
    private int horizontalVelocity;
    private int verticalVelocity = -20;
    private List<Collidable> collisions;
    private int size = 15;
    private Direction direction;
    private final int speed = 35;

    public Snowball(int x, int y, Direction direction, List<Collidable> collisions) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.horizontalVelocity = speed * (direction == Direction.LEFT ? -1 : 1);
        this.collisions = collisions;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(new Color(255, 250, 250));
        g.fillOval(x, y, size, size);
        g.setColor(Color.BLACK);
        g.drawOval(x, y, size, size);
    }

    @Override
    public void update() {
        if (horizontalVelocity > 0) {
            horizontalVelocity -= .1;
        } else if (horizontalVelocity < 0) {
            horizontalVelocity += .1;
        }

        for (Collidable collision : collisions) {
            CollisionHit collisionHit = collision.collidesWith(x + horizontalVelocity, y + verticalVelocity, size, size);
            if (collisionHit != null) {

                SquareObject collisionObject = (SquareObject) collision;

                CollisionPlace collisionPlace = collisionHit.getCollisionPlace();
                if (collisionPlace == CollisionPlace.BOTTOM || collisionPlace == CollisionPlace.BOTTOM_LEFT || collisionPlace == CollisionPlace.BOTTOM_RIGHT) {
                    verticalVelocity = 10;
                    y = collisionObject.getBoundsY2();
                    horizontalVelocity /= 1.5;
                } else if (collisionPlace == CollisionPlace.TOP || collisionPlace == CollisionPlace.TOP_LEFT || collisionPlace == CollisionPlace.TOP_RIGHT) {
                    y = collisionObject.getBoundsY1() - size;
                    horizontalVelocity /= 2;
                }
                break;
            }
        }

        x += horizontalVelocity;

        float acceleration = 9.8f / 5;
        verticalVelocity += acceleration;

        List<Collidable> actualCollisions = new ArrayList<>();
        if (verticalVelocity >= 0) {
            for (Collidable collision : collisions) {
                CollisionHit collisionHit = collision.collidesWith(x, y + verticalVelocity, size, size);
                if (collisionHit != null) {
                    actualCollisions.add(collision);
                    verticalVelocity = 0;
                }
            }
        }

        if (actualCollisions.size() == 0) {
            y += verticalVelocity;
        } else {
            SquareObject highest = actualCollisions.stream()
                    .map(collision -> (SquareObject) collision)
                    .reduce((current, acc) -> current.getBoundsY1() < acc.getBoundsY1() ? current : acc)
                    .get();
            y = highest.getBoundsY1() - size;
        }
    }
}

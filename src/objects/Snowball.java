package objects;

import collision.Collidable;
import collision.CollisionCheckType;
import collision.CollisionHit;
import collision.CollisionSide;
import collision.collidables.SnowballCollidable;
import drawables.SnowballDrawable;
import engine.Drawable;
import engine.Updatable;
import player.Direction;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Snowball extends GameObject implements Updatable, Drawable, Collidable {

    private SnowballDrawable snowballDrawable;
    private SnowballCollidable snowballCollidable;

    private int horizontalVelocity;
    private int verticalVelocity = -20;
    private List<Collidable> collisions;
    private final int speed = 35;

    public Snowball(int x, int y, Direction direction, List<Collidable> collisions) {
        super(x, y, 15, 15);

        snowballDrawable = new SnowballDrawable();
        snowballCollidable = new SnowballCollidable();

        this.horizontalVelocity = speed * (direction == Direction.LEFT ? -1 : 1);
        this.collisions = collisions;
    }

    @Override
    public void draw(Graphics g) {
        snowballDrawable.draw(x, y, width, height, g);
    }

    @Override
    public void update() {
        if (horizontalVelocity > 0) {
            horizontalVelocity -= .1;
        } else if (horizontalVelocity < 0) {
            horizontalVelocity += .1;
        }

        for (Collidable collision : collisions) {
            CollisionHit collisionHit = collision.collidesWith(x + horizontalVelocity, y + verticalVelocity, width, height, CollisionCheckType.BOTH);
            if (collisionHit != null) {

                FloatingBar collisionObject = (FloatingBar) collision;

                CollisionSide collisionSide = collisionHit.getCollisionSide();
                if (collisionSide.isBottom()) {
                    verticalVelocity = 10;
                    y = collisionObject.getY() + collisionObject.getHeight();
                    horizontalVelocity /= 1.5;
                } else if (collisionSide.isTop()) {
                    y = collisionObject.getY() - height;
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
                CollisionHit collisionHit = collision.collidesWith(x, y + verticalVelocity, width, height, CollisionCheckType.BOTH);
                if (collisionHit != null) {
                    actualCollisions.add(collision);
                    verticalVelocity = 0;
                }
            }
        }

        if (actualCollisions.size() == 0) {
            y += verticalVelocity;
        } else {
            FloatingBar highest = actualCollisions.stream()
                    .map(collision -> (FloatingBar) collision)
                    .reduce((current, acc) -> current.getY() < acc.getY() ? current : acc)
                    .get();
            y = highest.getY() - height;
        }
    }

    @Override
    public CollisionHit collidesWith(int left, int top, int width, int height, CollisionCheckType collisionCheckType) {
        return snowballCollidable.collidesWith(left, top, width, height, collisionCheckType);
    }
}

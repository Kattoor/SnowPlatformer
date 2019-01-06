package collision.collidables;

import collision.CollisionCheckType;
import collision.CollisionHit;
import collision.CollisionSide;
import objects.GameObject;

public class BarCollidable {

    private int boundsX1;
    private int boundsX2;
    private int boundsY1;
    private int boundsY2;

    private GameObject gameObject;

    public BarCollidable(int x, int y, int width, int height, GameObject gameObject) {
        this.boundsX1 = x;
        this.boundsX2 = x + width;
        this.boundsY1 = y;
        this.boundsY2 = y + height;

        this.gameObject = gameObject;
    }

    public CollisionHit collidesWith(int left, int top, int width, int height, CollisionCheckType collisionCheckType) {
        //todo: too high velocity -> snowball goes through bar. Possible solution: collision-check in (bar_height / 2) intervals instead of velocity-based intervals?
        boolean collides = left + width >= boundsX1 && left <= boundsX2 && top + height >= boundsY1 && top <= boundsY2;

        if (!collides) {
            return null;
        }

        CollisionSide verticalCollision = (collisionCheckType == CollisionCheckType.VERTICAL || collisionCheckType == CollisionCheckType.BOTH)
                ? /*verticalVelocity >= 0 ? CollisionSide.TOP : CollisionSide.BOTTOM*/ checkVerticalCollision(top, height)
                : null;

        CollisionSide horizontalCollision = (collisionCheckType == CollisionCheckType.HORIZONTAL || collisionCheckType == CollisionCheckType.BOTH)
                ? /*horizontalVelocity >= 0 ? CollisionSide.LEFT : CollisionSide.RIGHT*/ checkHorizontalCollision(left, width)
                : null;

        return (verticalCollision != null && horizontalCollision != null)
                ? merge(horizontalCollision, verticalCollision)
                : flatten(horizontalCollision, verticalCollision);
    }


    private CollisionSide checkHorizontalCollision(int left, int width) {
        if (left + width >= boundsX1 && left + width <= boundsX1 + 25) {
            return CollisionSide.LEFT;
        } else if (left >= boundsX2 - 25 && left <= boundsX2) {
            return CollisionSide.RIGHT;
        } else {
            return null;
        }
    }

    private CollisionSide checkVerticalCollision(int top, int height) {
        if (top + height >= boundsY1 && top + height <= boundsY1 + 25) {
            return CollisionSide.TOP;
        } else if (top >= boundsY2 - 25 && top <= boundsY2) {
            return CollisionSide.BOTTOM;
        } else {
            return null;
        }
    }

    private CollisionHit merge(CollisionSide horizontal, CollisionSide vertical) {
        return new CollisionHit(CollisionSide.valueOf(vertical.name() + "_" + horizontal.name()), gameObject);
    }

    private CollisionHit flatten(CollisionSide horizontal, CollisionSide vertical) {
        if (horizontal != null && vertical == null) {
            return new CollisionHit(horizontal, gameObject);
        } else if (horizontal == null && vertical != null) {
            return new CollisionHit(vertical, gameObject);
        } else {
            return null;
        }
    }
}

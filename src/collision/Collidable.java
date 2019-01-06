package collision;

public interface Collidable {

    CollisionHit collidesWith(int left, int top, int width, int height, CollisionCheckType collisionCheckType);
}

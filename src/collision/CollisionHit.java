package collision;

import objects.GameObject;

public class CollisionHit {

    private CollisionSide collisionSide;
    private GameObject gameObject;

    public CollisionHit(CollisionSide collisionSide, GameObject gameObject) {
        this.collisionSide = collisionSide;
        this.gameObject = gameObject;
    }

    public CollisionSide getCollisionSide() {
        return collisionSide;
    }

    public GameObject getGameObject() {
        return gameObject;
    }
}

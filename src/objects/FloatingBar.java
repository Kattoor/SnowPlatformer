package objects;

import collision.collidables.BarCollidable;
import collision.Collidable;
import collision.CollisionCheckType;
import collision.CollisionHit;
import drawables.BarDrawable;
import engine.Drawable;

import java.awt.*;
import java.io.IOException;

public class FloatingBar extends GameObject implements Drawable, Collidable {

    private BarCollidable barCollidable;
    private BarDrawable barDrawable;

    public FloatingBar(int x, int y, int width, int height, int imageIndex) throws IOException {
        super(x, y, width, height);

        barCollidable = new BarCollidable(x, y, width, height, this);
        barDrawable = new BarDrawable(imageIndex);
    }

    @Override
    public void draw(Graphics g) {
        barDrawable.draw(x, y, width, height, g);
    }

    @Override
    public CollisionHit collidesWith(int left, int top, int width, int height, CollisionCheckType collisionCheckType) {
        return barCollidable.collidesWith(left, top, width, height, collisionCheckType);
    }
}

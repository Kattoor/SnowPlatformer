import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SquareObject implements Drawable, Collidable {

    private int boundsX1;
    private int boundsX2;
    private int boundsY1;
    private int boundsY2;
    private int imageIndex;

    private Image[] images = new Image[]{
            ImageIO.read(Files.newInputStream(Paths.get("bars", "snowLeft.png"))),
            ImageIO.read(Files.newInputStream(Paths.get("bars", "snowMid.png"))),
            ImageIO.read(Files.newInputStream(Paths.get("bars", "snowRight.png")))
    };

    SquareObject(int boundsX1, int boundsX2, int boundsY1, int boundsY2, int imageIndex) throws IOException {
        this.boundsX1 = boundsX1;
        this.boundsX2 = boundsX2;
        this.boundsY1 = boundsY1;
        this.boundsY2 = boundsY2;
        this.imageIndex = imageIndex;
    }

    int getBoundsY1() {
        return boundsY1;
    }

    int getBoundsY2() {
        return boundsY2;
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(images[imageIndex], boundsX1, boundsY1, null);
    }

    @Override
    public CollisionHit collidesWith(int left, int top, int width, int height) {
        boolean collides = left + width >= boundsX1 && left <= boundsX2 && top + height >= boundsY1 && top <= boundsY2;

        if (collides) {
            boolean hitsLeft = left + width >= boundsX1 && left + width <= boundsX1 + 25;
            boolean hitsRight = left >= boundsX2 - 25 && left <= boundsX2;
            boolean hitsTop = top + height >= boundsY1 && top + height <= boundsY1 + 25;
            boolean hitsBottom = top >= boundsY2 - 50 && top <= boundsY2;

            int number = (hitsLeft ? 0b0001 : hitsRight ? 0b0010 : 0) | (hitsTop ? 0b0100 : hitsBottom ? 0b1000 : 0);

            CollisionPlace collisionPlace = CollisionPlace.getCollisionPlaceByNumber(number);
            return new CollisionHit(collisionPlace);
        } else {
            return null;
        }
    }

    public int getBoundsX1() {
        return boundsX1;
    }

    public int getBoundsX2() {
        return boundsX2;
    }
}

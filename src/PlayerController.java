import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class PlayerController implements Updatable, Drawable {

    private final List<Collidable> collisions;
    private Direction direction = Direction.RIGHT;
    private int horizontalVelocity = 0;
    private double verticalVelocity = 0;
    private final int speed = 5;
    private final int amountOfFrames = 5;
    private int currentFrameIndex;
    private int x;
    private int y = 100;
    private boolean goingLeft = false;
    private boolean goingRight = false;
    private int height = 78;
    private int width = 42;
    private List<Snowball> snowballs = new CopyOnWriteArrayList<>();
    private boolean mousePressed;


    private KeyAdapter keyListener;
    private MouseAdapter mouseAdapter;

    private int jumpCount = 0;

    private Image[] images = new Image[]{
            ImageIO.read(Files.newInputStream(Paths.get("snowman", "f1.png"))),
            ImageIO.read(Files.newInputStream(Paths.get("snowman", "f2.png"))),
            ImageIO.read(Files.newInputStream(Paths.get("snowman", "f3.png"))),
            ImageIO.read(Files.newInputStream(Paths.get("snowman", "f4.png"))),
            ImageIO.read(Files.newInputStream(Paths.get("snowman", "f5.png")))
    };

    PlayerController(List<Collidable> collisions) throws IOException {
        this.collisions = collisions;
        keyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 32 || e.getKeyCode() == 87) {
                    if (jumpCount < 2) {
                        verticalVelocity = -25;
                        jumpCount++;
                    }
                }

                if (e.getKeyCode() == 37 || e.getKeyCode() == 65) {
                    goingLeft = true;
                    direction = Direction.LEFT;
                } else if (e.getKeyCode() == 39 || e.getKeyCode() == 68) {
                    goingRight = true;
                    direction = Direction.RIGHT;
                }

                if (e.getKeyCode() == 69) {
                    throwSnowball();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == 37 || e.getKeyCode() == 65) {
                    goingLeft = false;
                    if (goingRight) {
                        direction = Direction.RIGHT;
                    }
                } else if (e.getKeyCode() == 39 || e.getKeyCode() == 68) {
                    goingRight = false;
                    if (goingLeft) {
                        direction = Direction.LEFT;
                    }
                }
            }
        };
        mouseAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mousePressed = true;
                System.out.println(e.getX() + ", " + e.getY());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                System.out.println("released");
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                System.out.println("dragged");
            }
        };
    }

    @Override
    public void update() {
        if (goingLeft || goingRight) {
            updateFrameAnimation();
        }

        if (goingLeft) {
            boolean collides = false;
            for (Collidable collision : collisions) {
                CollisionHit collisionHit = collision.collidesWith(x - speed, y , width, height - 1);
                if (collisionHit != null) {
                    collides = true;
                    break;
                }
            }

            if (!collides) {
                horizontalVelocity = -speed;
            } else {
                horizontalVelocity = 0;
            }
        } else if (goingRight) {
            boolean collides = false;
            for (Collidable collision : collisions) {
                CollisionHit collisionHit = collision.collidesWith(x + speed, y, width, height - 1);
                if (collisionHit != null) {
                    collides = true;
                    break;
                }
            }

            if (!collides) {
                horizontalVelocity = speed;
            } else {
                horizontalVelocity = 0;
            }
        } else {
            horizontalVelocity = 0;
        }

        x += horizontalVelocity;

        float acceleration = 9.8f / 5;
        verticalVelocity += acceleration;

        List<Collidable> actualCollisions = new ArrayList<>();

            for (Collidable collision : collisions) {
                CollisionHit collisionHit = collision.collidesWith(x, (int) (y + verticalVelocity), width, height);
                if (collisionHit != null) {
                    CollisionPlace collisionPlace = collisionHit.getCollisionPlace();
                    if (collisionPlace == CollisionPlace.BOTTOM || collisionPlace == CollisionPlace.BOTTOM_LEFT || collisionPlace == CollisionPlace.BOTTOM_RIGHT) {
                        actualCollisions.add(collision);
                        verticalVelocity = 10;

                        y = ((SquareObject) collision).getBoundsY2();
                    } else if (collisionPlace == CollisionPlace.TOP || collisionPlace == CollisionPlace.TOP_LEFT || collisionPlace == CollisionPlace.TOP_RIGHT) {
                        jumpCount = 0;
                        actualCollisions.add(collision);
                        verticalVelocity = 0;

                        y = ((SquareObject) collision).getBoundsY1() - height;
                    }
                }
            }


        if (actualCollisions.size() == 0) {
            y += verticalVelocity;
        }

        snowballs.forEach(Snowball::update);
    }

    @Override
    public void draw(Graphics g) {
        if (direction == Direction.LEFT) {
            g.drawImage(images[currentFrameIndex / animationStretch], x + width, y, -width, height, null);
        } else if (direction == Direction.RIGHT) {
            g.drawImage(images[currentFrameIndex / animationStretch], x, y, null);
        }

        snowballs.forEach(snowball -> snowball.draw(g));
    }

    private boolean frameIndexIncrementing = false;
    private int animationStretch = 2;

    private void updateFrameAnimation() {
        if (frameIndexIncrementing) {
            if (++currentFrameIndex >= amountOfFrames * animationStretch) {
                frameIndexIncrementing = false;
                currentFrameIndex = (amountOfFrames - 2) * animationStretch;
            }
        } else {
            if (--currentFrameIndex <= 0) {
                frameIndexIncrementing = true;
                currentFrameIndex = 0;
            }
        }
    }

    private void throwSnowball() {
        Snowball snowball = new Snowball(x, y, direction, collisions);
        snowballs.add(snowball);
    }

    KeyListener getKeyListener() {
        return keyListener;
    }

    MouseAdapter getMouseAdapter() {
        return mouseAdapter;
    }

}

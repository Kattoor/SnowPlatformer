package player;

import collision.Collidable;
import collision.CollisionCheckType;
import collision.CollisionHit;
import collision.CollisionSide;
import engine.Drawable;
import engine.Updatable;
import objects.GameObject;
import objects.Snowball;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class PlayerController implements Updatable, Drawable {

    private final List<Collidable> collidables;
    private Direction direction = Direction.RIGHT;
    private int verticalVelocity = 0;
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
    private final float verticalAcceleration = 9.8f / 5;


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

    public PlayerController(List<Collidable> collidables) throws IOException {
        this.collidables = collidables;
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

    private void updateHorizontalMovement() {
        if (goingLeft || goingRight) {

            int velocity = goingLeft ? -speed : speed;
            int nextX = x + velocity;

            updateFrameAnimation();

            //boolean collides = collidables.stream().anyMatch(collidable -> collidable.collidesWith(nextX, y, width, height - 1, CollisionCheckType.HORIZONTAL, velocity, 0) != null);
            boolean collided = false;
            for (Collidable collidable : collidables) {
                CollisionHit collisionHit = collidable.collidesWith(nextX, y, width, height - 1, CollisionCheckType.HORIZONTAL);

                if (collisionHit != null) {
                    CollisionSide collisionSide = collisionHit.getCollisionSide();
                    GameObject collisionObject = collisionHit.getGameObject();

                    collided = true;
                    if (collisionSide == CollisionSide.LEFT ) {
                        x = collisionObject.getX() - width;
                    } else if (collisionSide == CollisionSide.RIGHT) {
                        x = collisionObject.getX() + collisionObject.getWidth();
                    }

                    break;
                }
            }

            if (!collided) {
                x = nextX;
            }
        }
    }

    private void updateVerticalMovement() {
        verticalVelocity += verticalAcceleration;

        int nextY = y + verticalVelocity;

        boolean collided = false;

        for (Collidable collidable : collidables) {
            CollisionHit collisionHit = collidable.collidesWith(x, nextY, width, height, CollisionCheckType.VERTICAL);
            if (collisionHit != null) {
                collided = true;

                CollisionSide collisionSide = collisionHit.getCollisionSide();
                GameObject collisionObject = collisionHit.getGameObject();

                if (collisionSide.isBottom()) {
                    verticalVelocity = 10;
                    y = collisionObject.getY() + collisionObject.getHeight() + 1;
                } else if (collisionSide.isTop()) {
                    jumpCount = 0;
                    verticalVelocity = 0;
                    y = collisionObject.getY() - height;
                }

                break;
            }
        }

        if (!collided) {
            y = nextY;
        }

        /*

        Optional<CollisionHit> collision = collidables.stream()
                .map(collidable -> collidable.collidesWith(x, nextY, width, height, CollisionCheckType.BOTH))
                .filter(Objects::nonNull)
                .findFirst();

        collision.ifPresent(collisionHit -> {
            CollisionSide collisionSide = collisionHit.getCollisionSide();
            HasPositionAndSize collisionObject = collisionHit.getObject();

            if (collisionSide.isBottom()) {
                verticalVelocity = -verticalVelocity;
                y = collisionObject.getY() + collisionObject.getHeight();
            } else if (collisionSide.isTop()) {
                jumpCount = 0;
                verticalVelocity = 0;
                y = collisionObject.getY() - height;
            }
        });

        if (!collision.isPresent()) {
            y = nextY;
        }*/
    }

    @Override
    public void update() {
        updateHorizontalMovement();
        updateVerticalMovement();

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
        Snowball snowball = new Snowball(x, y, direction, collidables);
        snowballs.add(snowball);
    }

    public KeyListener getKeyListener() {
        return keyListener;
    }

    public MouseAdapter getMouseAdapter() {
        return mouseAdapter;
    }

}

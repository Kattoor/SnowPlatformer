package engine;

import background.Background;
import collision.Collidable;
import objects.FloatingBar;
import player.PlayerController;
import view.GameView;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Game {

    private GameView gameView;
    private boolean gameRunning = true;

    private List<Updatable> updatables;
    private List<Drawable> drawables;

    public static void main(String[] args) throws IOException {
        new Game();
    }

    private void initialize() throws IOException {
        Background background = new Background();
        gameView = new GameView();

        FloatingBar[] floorSquares = new FloatingBar[(int) Math.ceil(GameView.width / 70.0)];

        for (int i = 0; i < floorSquares.length; i++) {
            floorSquares[i] = new FloatingBar(i * 70, GameView.height - 70, 70, 70, 1);
        }

        FloatingBar[] floatingSquares = new FloatingBar[] {
                new FloatingBar(0, 250, 70, 70, 0),
                new FloatingBar(70, 250, 70, 70, 2),

                new FloatingBar(280, 400, 70, 70, 0),
                new FloatingBar(350, 400, 70, 70, 1),
                new FloatingBar(420, 400, 70, 70, 1),
                new FloatingBar(490, 400, 70, 70, 2),

                new FloatingBar(700, 910, 70, 70, 0),
                new FloatingBar(770, 910, 70, 70, 1),
                new FloatingBar(840, 910, 70, 70, 2)
        };

        List<Collidable> collisions = new ArrayList<>();
        collisions.addAll(Arrays.asList(floorSquares));
        collisions.addAll(Arrays.asList(floatingSquares));

        PlayerController playerController = new PlayerController(collisions);
        gameView.setListeners(playerController.getKeyListener(), playerController.getMouseAdapter());

        updatables = new ArrayList<>();
        drawables = new ArrayList<>();

        updatables.add(playerController);

        drawables.add(background);
        drawables.add(playerController);
        drawables.addAll(Arrays.asList(floorSquares));
        drawables.addAll(Arrays.asList(floatingSquares));
    }

    private Game() throws IOException {
        initialize();

        new Thread(() -> {
            while (gameRunning) {
                try {
                    long startTime = System.currentTimeMillis();

                    updatables.forEach(Updatable::update);

                    BufferedImage image = new BufferedImage(GameView.width, GameView.height, BufferedImage.TYPE_INT_ARGB);
                    drawables.forEach(drawable -> drawable.draw(image.getGraphics()));
                    gameView.draw(image);

                    long deltaTime = System.currentTimeMillis() - startTime;

                    long millisToSleep = (1000 / 30) - deltaTime;
                    if (millisToSleep > 0) {
                        try {
                            Thread.sleep(millisToSleep);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        throw new RuntimeException(String.format("Game loop iteration took %sms!", deltaTime));
                    }
                } catch (RuntimeException e) {
                    System.out.println(e.getMessage());
                }
            }
        }).start();
    }
}

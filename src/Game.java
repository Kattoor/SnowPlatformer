import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Game {

    private GameView gameView;
    private PlayerController playerController;

    public static void main(String[] args) throws IOException {
        new Game();
    }

    private Game() throws IOException {
        Background background = new Background();
        gameView = new GameView();

        SquareObject[] floorSquares = new SquareObject[(int) Math.ceil(GameView.width / 70.0)];

        for (int i = 0; i < floorSquares.length; i++) {
            floorSquares[i] = new SquareObject(i * 70, (i + 1) * 70, GameView.height - 70, GameView.height, 1);
        }

        SquareObject[] floatingSquares = new SquareObject[] {
                new SquareObject(100, 170, 500, 570, 0),
                new SquareObject(170, 240, 500, 570, 1),
                new SquareObject(240, 310, 500, 570, 1),
                new SquareObject(310, 380, 500, 570, 1),
                new SquareObject(380, 450, 500, 570, 2),

                new SquareObject(700, 770, 800, 870, 0),
                new SquareObject(770, 840, 800, 870, 1),
                new SquareObject(840, 910, 800, 870, 2)
        };

        List<Collidable> collisions = new ArrayList<>();
        collisions.addAll(Arrays.asList(floorSquares));
        collisions.addAll(Arrays.asList(floatingSquares));

        playerController = new PlayerController(collisions);
        gameView.setListeners(playerController.getKeyListener(), playerController.getMouseAdapter());

        new Thread(() -> {
            while (true) {
                try {
                    long startTime = System.currentTimeMillis();

                    playerController.update();

                    BufferedImage image = new BufferedImage(GameView.width, GameView.height, BufferedImage.TYPE_INT_ARGB);

                    background.draw(image.getGraphics());

                    for (SquareObject floorSquare : floorSquares) {
                        floorSquare.draw(image.getGraphics());
                    }

                    for (SquareObject floatingSquare : floatingSquares) {
                        floatingSquare.draw(image.getGraphics());
                    }

                    playerController.draw(image.getGraphics());

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

package drawables;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class BarDrawable {

    private Image[] images;
    private int imageIndex;

    public BarDrawable(int imageIndex) {
        this.imageIndex = imageIndex;
        try {
            this.images = new Image[] {
                    ImageIO.read(Files.newInputStream(Paths.get("bars", "snowLeft.png"))),
                    ImageIO.read(Files.newInputStream(Paths.get("bars", "snowMid.png"))),
                    ImageIO.read(Files.newInputStream(Paths.get("bars", "snowRight.png")))
            };
        } catch (IOException e) {
            System.out.println("Error: couldn't load bar sprites");
        }
    }

    public void draw(int x, int y, int width, int height, Graphics g) {
        g.drawImage(images[imageIndex], x, y, null);
        g.setColor(Color.RED);
        g.drawRect(x, y, width, height);
    }
}

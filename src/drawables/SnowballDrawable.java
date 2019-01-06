package drawables;

import java.awt.*;

public class SnowballDrawable {

    public void draw(int x, int y, int width, int height, Graphics g) {
        g.setColor(new Color(255, 250, 250));
        g.fillOval(x, y, width, height);
        g.setColor(Color.BLACK);
        g.drawOval(x, y, width, height);
    }
}

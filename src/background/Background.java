package background;

import engine.Drawable;
import view.GameView;

import java.awt.*;

public class Background implements Drawable {

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.CYAN);
        g.fillRect(0, 0, GameView.width, GameView.height);
    }
}

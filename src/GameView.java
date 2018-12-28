import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.io.IOException;

class GameView extends JFrame {

    static int width;
    static int height;

    private JPanel panel;

    GameView() throws IOException {
        JPanel panel = new JPanel();

        this.panel = panel;

        add(panel);

        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);

        GameView.width = getWidth();
        GameView.height = getHeight();
        System.out.println(GameView.width + ", "+ GameView.height);
    }

    void draw(Image image) {
        panel.getGraphics().drawImage(image, 0, 0, this);
    }

    void setListeners(KeyListener keyListener, MouseAdapter mouseAdapter) {
        addKeyListener(keyListener);
        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);
    }
}

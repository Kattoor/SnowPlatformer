package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.io.IOException;

public class GameView extends JFrame {

    public static int width;
    public static int height;

    private JPanel panel;

    public GameView() throws IOException {
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
    }

    public void draw(Image image) {
        panel.getGraphics().drawImage(image, 0, 0, this);
    }

    public void setListeners(KeyListener keyListener, MouseAdapter mouseAdapter) {
        addKeyListener(keyListener);
        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);
    }
}

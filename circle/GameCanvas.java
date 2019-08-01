import javax.swing.*;
import java.awt.*;

public class GameCanvas extends JPanel {

    MainCircles gameWindow;
    private long lastFrameTime;

    GameCanvas(MainCircles gameWindow) {
        this.gameWindow = gameWindow;
        setBackground(Color.BLUE);
        lastFrameTime = System.nanoTime();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        long currentTime = System.nanoTime();
        float delta = (currentTime - lastFrameTime) * 0.000000001f;
//System.out.println(delta);
        lastFrameTime = currentTime;
        try {
            Thread.sleep(17);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        gameWindow.onDrawFrame(this, g, delta);
        repaint();
    }

    public int getLeft() { return 0; }
    public int getRight() { return getWidth() - 1; }
    public int getTop() { return 0; }
    public int getBottom() { return getHeight() - 1; }
}

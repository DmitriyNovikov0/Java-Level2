import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/*
	Полностью разобраться с кодом
	Прочитать методичку к следующему уроку
	Написать класс Бэкграунд, изменяющий цвет канвы в зависимости от времени
	 * Реализовать добавление новых кружков по клику используя ТОЛЬКО массивы
	 ** Реализовать по клику другой кнопки удаление кружков (никаких эррейЛист)
* */

/*
не делается pull request
 */


public class MainCircles extends JFrame {

    private static final int POS_X = 600;
    private static final int POS_Y = 200;
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;
//максимальное количество шариков
    private static final int MAX_SPRITE = 20;


    private Sprite[] sprites = new Sprite[MAX_SPRITE];
    private int spritesCount = 1;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainCircles();
            }
        });
    }

    MainCircles() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(POS_X, POS_Y, WINDOW_WIDTH, WINDOW_HEIGHT);
        setTitle("Circles");
        GameCanvas gameCanvas = new GameCanvas(this);
        initApplication();
        add(gameCanvas, BorderLayout.CENTER);
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                if (me.getButton() == MouseEvent.BUTTON1) {
                    if(spritesCount < MAX_SPRITE) {
                        sprites[spritesCount] = new Ball();
                        spritesCount++;
                    }
                    else{
                        JOptionPane.showMessageDialog(null,"Достигнуто максимальное количество шариков на экране");
                    }
                }
                else if (me.getButton() == MouseEvent.BUTTON3) {
                    if(spritesCount > 1) {
                        spritesCount--;
                        sprites[spritesCount] = null;
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(null,"Достигнуто минимальное количество шариков на экране");
                    }
                }
            }
        });
        setVisible(true);
    }


    void initApplication() {
 //       for (int i = 0; i < sprites.length; i++) {
            sprites[0] = new Ball();
//        }
    }

    void onDrawFrame(GameCanvas canvas, Graphics g, float deltaTime) {
//делаю через анонимный класс, по другому у меня не получается, я плохо разбираюсь в многопоточных приложениях
// а отладка многопоточных приложений для меня темный лес.
        Background bg = new Background(){

            public void setBakground(GameCanvas canvas) {
                i++;
                if(i > 100){
                    int a = (int) (Math.random() * 200);
                    int b = (int) (System.currentTimeMillis() % 250);
                    int c = (int) (Math.random() * 200);
                    canvas.setBackground(new Color(a, b, c));
                    i = 0;
                }
            }
        };

        bg.setBakground(canvas);
        update(canvas, deltaTime);
        render(canvas, g);
    }

    private void update(GameCanvas canvas, float deltaTime){
        for (int i = 0; i < spritesCount; i++) {
            sprites[i].update(canvas, deltaTime);
        }
    }

    private void render(GameCanvas canvas, Graphics g) {
        for (int i = 0; i < spritesCount; i++) {
            sprites[i].render(canvas, g);
        }
    }
}

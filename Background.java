import java.awt.*;

public class Background {
   private int i = 0;

    public void setBakground(GameCanvas canvas){
        i++;
        if (i > 100) {
            int a = (int)(Math.random() * 200);
            canvas.setBackground(new Color(a, a, 0));
            i = 0;
        }
    }

}

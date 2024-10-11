package UI;

import main.GamePanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;

public class Bar {
    private int current;
    private int max;
    private int barWidth;
    private int barHeight;
    GamePanel gp;
    BufferedImage bgImage;
    BufferedImage healthImage;
    int x, y;
    public Bar(int max,int x, int y,  int barWidth, int barHeight, GamePanel gp, Color color) {
        this.max = max;
        this.current = max; // Lúc đầu máu đầy
        this.barWidth = barWidth;
        this.barHeight = barHeight;
        this.x = x;
        this.y = y;
        gp.setPreferredSize(new Dimension(barWidth, barHeight));
        healthImage = new BufferedImage(barWidth, barHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = healthImage.createGraphics();
        g2d.setColor(color);
        g2d.fillRect(0, 0, barWidth, barHeight);
        g2d.dispose();
        bgImage = new BufferedImage(barWidth, barHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D gd = bgImage.createGraphics();
        gd.setColor(new Color(100,100,100));
        gd.fillRect(0, 0, barWidth, barHeight);
        gd.dispose();
    }
    public void update(int current){
        this.current = current;
        if(this.current < 0) this.current = 0;
        if(this.current >= max) this.current = max;
    }
    public void draw(Graphics2D g2d){

        // Vẽ hình nền của thanh máu
        g2d.drawImage(bgImage, x, y, barWidth, barHeight, null);

        // Vẽ phần máu còn lại
        float healthPercent = (float) current / max;
        int filledWidth = (int) (barWidth * healthPercent);
        g2d.drawImage(healthImage, x, y, filledWidth, barHeight, null);

        // Vẽ đường viền của thanh máu
        g2d.drawRect(x, y, barWidth - 1, barHeight - 1);
    }
}

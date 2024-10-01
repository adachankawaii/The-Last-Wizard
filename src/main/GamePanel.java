package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;
import entity.Player;
import tile.TileManager;

public class GamePanel extends JPanel implements Runnable{
    final int originalTitleSize = 16;
    final int scale = 3;
    public final int titleSize = originalTitleSize * scale;
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = maxScreenCol * titleSize;
    public final int screenHeight = maxScreenRow * titleSize;

    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;   
    public final int worldWidth = maxWorldCol * titleSize;
    public final int worldHeight = maxWorldRow * titleSize;    

    Thread gameThread;
    KeyHandler key = new KeyHandler();
    public Player player = new Player(this, key);
    TileManager tileMng = new TileManager(this);
    public CollisionCheck collision = new CollisionCheck(this);
    
    int FPS = 60;

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(key);
        this.setFocusable(true);
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double drawInterval = 1000000000/FPS;
        double delta = 0;
        long currentTime;
        long lastTime = System.nanoTime();

        while(gameThread != null) {
            currentTime = System.nanoTime();
            delta += ((currentTime - lastTime) / drawInterval);
            lastTime = currentTime;
            if(delta >= 1) {
                update();
                repaint();
                delta--;
            }
        }
    }

    public void update() {
        player.update();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
        tileMng.draw(g2d);
        player.draw(g2d);
        g2d.dispose();
    }
}

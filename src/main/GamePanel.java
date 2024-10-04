package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;
import entity.Player;
import object.SuperObject;
import tile.TileManager;

public class GamePanel extends JPanel implements Runnable{

    // Constants for tile sizes and world dimensions
    final int originalTitleSize = 16;  // Original size of a tile
    final int scale = 3;               // Scale factor
    public final int titleSize = originalTitleSize * scale;  // Final tile size after scaling

    // Screen size and world size parameters
    public final int maxScreenCol = 25;  // Number of columns on the screen
    public final int maxScreenRow = 15;  // Number of rows on the screen
    public final int screenWidth = maxScreenCol * titleSize;  // Total screen width in pixels
    public final int screenHeight = maxScreenRow * titleSize;  // Total screen height in pixels

    // World settings
    public final int maxWorldCol = 50;  // Max world columns
    public final int maxWorldRow = 50;  // Max world rows
    public final int worldWidth = maxWorldCol * titleSize;    // World width in pixels
    public final int worldHeight = maxWorldRow * titleSize;   // World height in pixels

    // Game objects and entities
    Thread gameThread;
    KeyHandler key = new KeyHandler();
    public Player player = new Player(this, key);
    public TileManager tileMng = new TileManager(this);
    public CollisionCheck collision = new CollisionCheck(this);
    public SuperObject[] obj = new SuperObject[10];
    public AssetSetter aSetter = new AssetSetter(this);

    int FPS = 60; // Frames per second

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);  // Enable double buffering for smoother rendering
        this.addKeyListener(key);
        this.setFocusable(true); // Allow focus on the panel to capture user input
    }

   // Method to start the game loop thread
    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void setupGame() {
        aSetter.setObject();
    }

    @Override
    public void run() {

        // Game loop logic (fixed time-step for consistent FPS)
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

    // Method to update the game state (e.g., player position, collision detection)
    public void update() {
        player.update();
        for (int i = 0; i < obj.length; i++) {
            if (obj[i] != null) {
                obj[i].update();
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g); // Call parent class's method to ensure proper rendering
        Graphics2D g2d = (Graphics2D)g;
        tileMng.draw(g2d);

        for (int i = 0; i < obj.length; i++) {
            if (obj[i] != null) {
                obj[i].draw(g2d, this);
            }
        }

        // Draw tiles and player
        player.draw(g2d);
        g2d.dispose(); // Dispose graphics context to free resources
    }
}
package main;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

import UI.Bar;
import collision.CollisionCheck;
import entity.Entity;
import entity.bullet.ThrowingObj;
import entity.player.Player;
import tile.TileManager;
import entity.effect.Effect;
import entity.bullet.NormalBullet;
public class GamePanel extends JPanel implements Runnable{
    // SCREEN SETTING
    // Cài đặt tile size
    public boolean gameOver = false;
    public boolean running = true;
    public final int originalTitleSize = 16;  // Size gốc của 1 tile
    public final int scale = 3;               // Chỉ số scale
    public final int tileSize = originalTitleSize * scale;  // Size 1 tile sau khi scale

    // Cài đặt size màn hình
    public final int maxScreenCol = 20;  // Số cột hiện ở màn hình (Width)
    public final int maxScreenRow = 15;  // Số hàng hiện ở màn hình (Height)
    public final int screenWidth = maxScreenCol * tileSize;  // Width tính theo pixel
    public final int screenHeight = maxScreenRow * tileSize;  // Height tính theo pixel

    // Cài đặt Map
    public final int maxWorldCol = 50;  // Max world columns
    public final int maxWorldRow = 50;  // Max world rows
    public final int worldWidth = maxWorldCol * tileSize;
    public final int worldHeight = maxWorldRow * tileSize;

    // Cài đặt FPS
    public final int FPS = 60;

    // CONSTRUCTOR
    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight)); // Set size màn hình
        this.setDoubleBuffered(true); // Chất lượng render tốt hơn
        this.addKeyListener(keyH); // Thêm vào để game detect key input
        this.addMouseListener(mouseH); // Detect Mouse input
        this.setFocusable(true); // Tập trung vào nhận diện và xử lí key input
    }

    // TẠO THREAD
    Thread gameThread;
    public void startGameThread() { // Chạy luồng mới cho logic game
        gameThread = new Thread(this);
        gameThread.start(); // Chạy run()
    }

    // TẠO KEY HANDLER
    public KeyHandler keyH = new KeyHandler();
    // Tạo PLAYER
    public Player player = new Player(this, keyH);
    // TẠO TILE MANAGER
    public TileManager tileMng = new TileManager(this);
    // TẠO COLLISION
    public CollisionCheck cCheck = new CollisionCheck(this);
    // TẠO ARRAY LƯU OBJ
    public ArrayList<Entity> obj = new ArrayList<>();
    // TẠO SET OBJECT
    AssetSetter aSetter = new AssetSetter(this);
    // MOUSE
    MouseHandler mouseH = new MouseHandler(this);
    public int mouseX = 0, mouseY = 0;
    int reloadTime = 0;
    Bar HPbar = new Bar(10,15, 15, 200, 20, this, new Color(255,0,0));
    Bar EnergyBar = new Bar(200,15, 15+20, 150, 10, this, new Color(0,200,255));
    // Setup các sự vật trong game
    public void setupGame() {
        // Đọc đường dẫn tới file thông tin và nhập
        String url = "res/maps/set_obj.txt";
        aSetter.setObject(url);
    }

    // CHẠY GAME
    @Override
    public void run() {

        // SETTING FPS BẰNG DELTA TIME
        double drawInterval = 1000000000.0 / FPS; // Khoảng thời gian giữa mỗi khung hình (nanosecond)
        // Ví dụ, với 60 FPS, mỗi khung hình cần có thời gian là 16.67 ms
        double delta = 0;
        long lastTime = System.nanoTime(); // Thời điểm bắt đầu của vòng lặp
        long currentTime; // Thời điểm hiện tại trong vòng lặp
        // long timer = 0;
        // int count = 0;

        while (gameThread != null) {
            currentTime = System.nanoTime(); // Theo dõi thời gian đã trôi qua
            // và xác định khi nào nên cập nhật logic và vẽ lại trò chơi
            delta += (currentTime - lastTime) / drawInterval; // Tính khoảng thời gian trôi qua
            // timer += (currentTime - lastTime);
            lastTime = currentTime;

            if (delta >= 1) { // Đủ thời gian đã trôi qua để vẽ một khung hình mới
                // 1. Cập nhật logic trò chơi
                update();
                // 2. Vẽ lại màn hình
                repaint();
                delta--; // Giảm delta để tiếp tục điều chỉnh tốc độ
                // count++;
            }
            // if (timer >= 1000000000) {
            //     System.out.println("FPS: " + count);
            //     count = 0;
            //     timer = 0;
            // }
        }
    }
    public void resetGame() {
        // Đặt lại tất cả các thành phần của trò chơi về trạng thái ban đầu
        player.setDefaultValue(
                tileSize * 23,
                tileSize * 21,
                5,
                "down");
        obj.clear(); // Xóa tất cả các object
        gameOver = false;
        running = true;
        player.HP = 10;
        player.Energy = 200;
        reloadTime = 0;
        player.items.clear();
        player.itemsCount.clear();
        setupGame(); // Gọi lại setup ban đầu của game
        repaint();
    }
    // NƠI CHỨA UPDATE NÈ
    public void update() {
        if (gameOver) {
            if(keyH.RPressed){
                System.out.println("isReset");
                keyH.RPressed = false;
                resetGame();

            }
            return;
        }
        player.update();
        for (int i = 0; i < obj.size(); i++) {
            if (obj.get(i) != null) {
                obj.get(i).update();
            }
        }
        reloadTime--;
        HPbar.update(player.HP);
        EnergyBar.update(player.Energy);
        // Kiểm tra điều kiện game over (ví dụ: HP <= 0)
        if (player.HP <= 0) {
            gameOver = true;
        }
    }
    public void onClick(int mouseInfo){
        if(reloadTime <= 0){
            if(mouseInfo == 1 && player.Energy >= 10){
                NormalBullet b = new NormalBullet(null,"bullet",20,20, 8, 8, player.worldX, player.worldY,50,this ,0, 7, 1, 1, mouseX, mouseY);
                obj.add(b);
                Effect c = new Effect ("/effect/Blue Effect.png", 0, 0, player.worldX, player.worldY, 15, this, 4, 1.5,1.5, mouseX, mouseY);
                obj.add(c);
                player.Energy -= 10;
            }
            else if(mouseInfo == 2 && !player.items.isEmpty()){
                player.items.get(player.pointer).effect();
                player.itemsCount.set(player.pointer, player.itemsCount.get(player.pointer) - 1);
                if(player.itemsCount.get(player.pointer) <= 0){
                    player.itemsCount.remove(player.pointer);
                    player.items.remove(player.pointer);
                }
            }
            reloadTime = 20;
        }
    }

    // NƠI CHỨA VẼ NÈ
    public void draw(Graphics2D g2) {
        // Vẽ Map
        tileMng.draw(g2);
        // Vẽ Obj
        for (int i = 0; i < obj.size(); i++) {
            if (obj.get(i) != null) {
                obj.get(i).draw(g2, this);
            }
        }

        // Vẽ nhân vật
        player.draw(g2);
        HPbar.draw(g2);
        EnergyBar.draw(g2);
    }
    // VẼ OBJ Ở ĐÂY
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        if (gameOver) {
            // Vẽ màn hình game over
            g2.setColor(Color.RED);
            g2.setFont(new Font("Arial", Font.BOLD, 50));
            g2.drawString("GAME OVER", screenWidth / 2 - 150, screenHeight / 2);
            g2.setFont(new Font("Arial", Font.PLAIN, 30));
            g2.drawString("Press R to Restart", screenWidth / 2 - 130, screenHeight / 2 + 50);
        } else {
            // Vẽ game
            draw(g2);
        }
        g2.dispose();
    }
}
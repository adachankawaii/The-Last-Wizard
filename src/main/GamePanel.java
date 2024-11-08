package main;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import javax.swing.*;
import UI.Bar;
import collision.CollisionCheck;
import entity.Entity;
import entity.Items.CommonItem;
import entity.Items.HPBottle;
import entity.Items.ObjectMap1;
import entity.Items.ThrowingBottle;
import entity.bullet.ThrowingObj;
import entity.enemy.Slime;
import entity.enemy.Soldier;
import entity.npc.GuildMaster;
import entity.npc.NPC;
import entity.npc.Portal;
import entity.npc.ShopKeeper;
import entity.player.Player;
import entity.player.Quest;
import tile.TileManager;
import entity.effect.Effect;
import entity.bullet.NormalBullet;

public class GamePanel extends JPanel implements Runnable{
    // SCREEN SETTING
    // Cài đặt tile size
    public boolean gameOver = false;
    public boolean running = true;
    public final int originalTitleSize = 16;  // Size gốc của 1 tile
    public final int scale = 2;               // Chỉ số scale
    public final int tileSize = originalTitleSize * scale;  // Size 1 tile sau khi scale

    // Cài đặt size màn hình
    public final int maxScreenCol =20*3/2;  // Số cột hiện ở màn hình (Width)
    public final int maxScreenRow = 15*3/2;  // Số hàng hiện ở màn hình (Height)
    public final int screenWidth = maxScreenCol * tileSize;  // Width tính theo pixel
    public final int screenHeight = maxScreenRow * tileSize;  // Height tính theo pixel

    // Cài đặt Map
    public final int maxWorldCol = 100;  // Max world columns
    public final int maxWorldRow = 100;  // Max world rows
    public final int worldWidth = maxWorldCol * tileSize;
    public final int worldHeight = maxWorldRow * tileSize;
    public boolean startMenu = true;  // Kiểm tra trạng thái của Start Menu
    public float fadeAlpha = 0f;   // Độ trong suốt cho hiệu ứng mờ dần
    public boolean fadingIn = false; // Kiểm tra nếu đang chuyển cảnh từ trắng sang game

    // Cài đặt FPS
    public final int FPS = 60;
    public int map = 1;

    // CONSTRUCTOR
    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight)); // Set size màn hình
        this.setDoubleBuffered(true); // Chất lượng render tốt hơn
        this.addKeyListener(keyH); // Thêm vào để game detect key input
        this.addMouseListener(mouseH); // Detect Mouse input
        this.setFocusable(true); // Tập trung vào nhận diện và xử lí key input
        soundManager = new Sound();
        soundManager.setVolumeAll(-20.0f);
        soundManager.setVolume("background", -30.0f);
        soundManager.loop("background");
    }

    // TẠO SOUNDMNG
    public Sound soundManager;

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
    public ArrayList<ObjectMap1> objMap1 = new ArrayList<>();

    // TẠO SET OBJECT
    AssetSetter aSetter = new AssetSetter(this);
    ObjectSetter oSetter = new ObjectSetter(this);

    // MOUSE
    MouseHandler mouseH = new MouseHandler(this);
    public int mouseX = 0, mouseY = 0;
    int reloadTime = 0;
    Bar HPbar = new Bar(10,15, 15, 200, 20, this, new Color(255,0,0));
    Bar EnergyBar = new Bar(200,15, 15+20, 150, 10, this, new Color(0,200,255));
    // Setup các sự vật trong game
    public void setupGame() {
        // Đọc đường dẫn tới file thông tin và nhập
        String url = "res/maps/set_obj" + map +".txt";
        String url1 = "res/maps/map" + map +"obj.txt";
        aSetter.setObject(url);
        oSetter.setObject(url1);
        tileMng = new TileManager(this);
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
                tileSize * 42,
                tileSize * 50,
                5,
                "down");
        obj.clear(); // Xóa tất cả các object
        objMap1.clear();
        gameOver = false;
        running = true;
        reloadTime = 0;
        player.kills= 0;
        soundManager.setVolumeAll(-20.0f);
        soundManager.setVolume("background", -30.0f);
        soundManager.play("background");
        soundManager.loop("background");
        tileMng = new TileManager(this);
        loadGame();
        player.HP = 10;
        player.Energy = 200;
        setupGame(); // Gọi lại setup ban đầu của game
        repaint();
    }
    public void nextMap(){
        fadingIn = true;
        player.setDefaultValue(
                tileSize * 42,
                tileSize * 50,
                5,
                "down");
        obj.clear(); // Xóa tất cả các object
        objMap1.clear();
        gameOver = false;
        running = true;
        player.Energy = 200;
        reloadTime = 0;
        player.kills= 0;
        soundManager.setVolumeAll(-20.0f);
        soundManager.setVolume("background", -30.0f);
        soundManager.play("background");
        soundManager.loop("background");
        tileMng = new TileManager(this);
        setupGame(); // Gọi lại setup ban đầu của game
        repaint();
        saveGame();
    }
    // NƠI CHỨA UPDATE NÈ
    public boolean pauseMenu = false;
    public void update() {
        if (gameOver) {
            if(keyH.RPressed){
                System.out.println("isReset");
                keyH.RPressed = false;
                resetGame();
            }
            return;
        }
        if (keyH.escPressed) {
            pauseMenu = !pauseMenu; // Chuyển đổi trạng thái pause khi nhấn ESC
            keyH.escPressed = false; // Đặt lại trạng thái phím ESC
        }

        if (pauseMenu) {
            return; // Nếu đang pause hoặc game over, không cập nhật game
        }
        if (keyH.volumeUpPressed) {
            soundManager.increaseVolume("background"); // Tăng âm lượng của nhạc nền
            keyH.volumeUpPressed = false;
        }

        if (keyH.volumeDownPressed) {
            soundManager.decreaseVolume("background"); // Giảm âm lượng của nhạc nền
            keyH.volumeDownPressed = false;
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
            // Dừng nhạc nền khi người chơi chết
            soundManager.play("player_die"); // Phát âm thanh khi người chơi chết
            gameOver = true;
        }
    }
    public void onClick(int mouseInfo){
        if (startMenu) { // Kiểm tra nếu đang ở trạng thái Start Menu
            if (mouseInfo == 1 && keyH.i != -1) { // Nếu nhấn chuột trái
                if(keyH.i % 3 == 0){
                    clearGameData();
                }
                else if(keyH.i % 3 == 1){
                    loadGame();
                }
                else if(keyH.i % 3 == 2){
                    System.exit(0);
                }
                startMenu = false; // Tắt Start Menu
                fadingIn = true; // Bắt đầu hiệu ứng chuyển cảnh
                setupGame();
            }
        }
        else if(player.combat && !gameOver){
            if(reloadTime <= 0){
                if(mouseInfo == 1 && player.Energy >= 10){
                    NormalBullet b = new NormalBullet(null,"bullet",12,12, 8, 8, player.worldX, player.worldY,20,this ,0, 12, 1, 1, mouseX, mouseY);
                    obj.add(b);
                    Effect c = new Effect ("/effect/Blue Effect.png", 0, 0, player.worldX, player.worldY, 15, this, 4, 1.5,1.5, mouseX, mouseY);
                    obj.add(c);
                    player.Energy -= 10;
                }
                else if(mouseInfo == 2 && !player.items.isEmpty()){
                    player.items.get(player.pointer).effect();
                    player.itemsCount.set(player.pointer, player.itemsCount.get(player.pointer) );
                    if(player.itemsCount.get(player.pointer) <= 0){
                        player.itemsCount.remove(player.pointer);
                        player.items.remove(player.pointer);
                        if(player.pointer >= player.items.size()){
                            player.pointer = 0;
                        }
                    }
                }
                reloadTime = 20;
            }
        }

    }
    public void saveGame() {
        // Thu thập các giá trị cần lưu từ game
        int currentHP = player.HP;
        int currentMap = map;
        ArrayList<String> currentItems = new ArrayList<>();
        ArrayList<Integer> currentItemsCount = player.itemsCount;
        for(Entity item : player.items){
            currentItems.add(item.objName);
        }
        int currentMoney = player.money;
        // Tạo đối tượng GameSaveData với trạng thái hiện tại của game
        GameSaveData saveData = new GameSaveData(currentHP, currentMap, currentItems, currentItemsCount,currentMoney);

        // Lưu đối tượng GameSaveData vào file
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("savegame.dat"))) {
            oos.writeObject(saveData);
            System.out.println("Game đã được lưu thành công!");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Lưu game thất bại.");
        }
    }
    public void loadGame() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("savegame.dat"))) {
            GameSaveData saveData = (GameSaveData) ois.readObject();
            System.out.println("Game đã được tải thành công!");

            // Thiết lập trạng thái game từ saveData
            player.HP = (saveData.HP);
            map = saveData.map;
            for(String item : saveData.items){
                player.items.add(createObject(item));
            }
            player.itemsCount = (saveData.itemsCount);
            player.money = saveData.money;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Tải game thất bại.");
        }
    }
    public void clearGameData() {
        File saveFile = new File("savegame.dat");

        if (saveFile.exists()) {
            if (saveFile.delete()) {
                System.out.println("File lưu game đã được xóa thành công!");
            } else {
                System.out.println("Không thể xóa file lưu game.");
            }
        } else {
            System.out.println("Không có file lưu game để xóa.");
        }
    }
    // NƠI CHỨA VẼ NÈ
    public void draw(Graphics2D g2) {
        // Vẽ Map
        tileMng.draw(g2);

        // Vẽ các đối tượng trong objMap1, chỉ vẽ khi chúng nằm trong phạm vi màn hình
        for (int i = 0; i < objMap1.size(); i++) {
            ObjectMap1 object = objMap1.get(i);
            if (object != null) {
                object.draw(g2, this);
            }
        }

        // Vẽ các đối tượng trong obj, chỉ vẽ khi chúng nằm trong phạm vi màn hình
        for (int i = 0; i < obj.size(); i++) {
            Entity entity = obj.get(i);
            if (entity != null && isObjectInScreen(entity)) {
                entity.draw(g2, this);
            }
        }

        // Vẽ nhân vật (Player)
        player.draw(g2);

        // Vẽ các đối tượng ở layer >= 1 trong objMap1, chỉ vẽ khi chúng nằm trong phạm vi màn hình
        for (int i = 0; i < objMap1.size(); i++) {
            ObjectMap1 object = objMap1.get(i);
            if (object != null && object.layer >= 1) {
                object.draw(g2, this);
            }
        }

        // Vẽ các đối tượng ở layer >= 1 trong obj, chỉ vẽ khi chúng nằm trong phạm vi màn hình
        for (int i = 0; i < obj.size(); i++) {
            Entity entity = obj.get(i);
            if (entity != null && entity.layer >= 1 && isObjectInScreen(entity)) {
                entity.draw(g2, this);
            }
        }

        // Vẽ các đối tượng UI
        for (int i = 0; i < obj.size(); i++) {
            Entity entity = obj.get(i);
            if (entity != null) {
                entity.drawUI(g2, this);
            }
        }

        // Vẽ UI cho nhân vật
        player.drawUI(g2, this);

        // Vẽ thanh HP và Energy
        HPbar.draw(g2);
        EnergyBar.draw(g2);
    }

    // Kiểm tra xem đối tượng có nằm trong phạm vi màn hình không
    private boolean isObjectInScreen(Entity entity){
        if(Math.abs(entity.worldX - player.worldX) <= 15*tileSize && Math.abs(entity.worldY - player.worldY) <= 12*tileSize){
            return true;
        }
        return false;
    }
    public void drawStartMenu(Graphics2D g2) {
        // Vẽ background trắng
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, screenWidth, screenHeight);

        // Vẽ tên game
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.BOLD, 50));
        g2.drawString("The Last Wizard", screenWidth / 2 - 200, screenHeight / 2 - 150);

        // Cấu hình các lựa chọn menu
        String[] choices = {"New Game", "Load Game", "Quit"};
        int choiceBoxX = screenWidth / 2 - 200; // Vị trí X của hộp lựa chọn
        int choiceBoxY = screenHeight / 2 - 20; // Vị trí Y ban đầu của hộp lựa chọn
        int choiceBoxWidth = 400; // Chiều rộng hộp lựa chọn
        int choiceBoxHeight = 50; // Chiều cao mỗi hộp lựa chọn
        int selectedChoice = keyH.i % 3; // Chọn mặc định (có thể điều chỉnh sau theo yêu cầu)
        g2.setFont(new Font("Arial", Font.PLAIN, 24));
        // Vẽ các lựa chọn
        for (int i = 0; i < choices.length; i++) {
            // Vẽ hộp lựa chọn
            g2.setColor(new Color(0, 0, 0, 180));
            g2.fillRoundRect(choiceBoxX, choiceBoxY + i * choiceBoxHeight, choiceBoxWidth, 40, 15, 15);
            g2.setColor(Color.WHITE);
            g2.drawRoundRect(choiceBoxX, choiceBoxY + i * choiceBoxHeight, choiceBoxWidth, 40, 15, 15);
            g2.drawString("Key " + (i + 1) + ": " + choices[i], choiceBoxX + 10, choiceBoxY + i * choiceBoxHeight + 28);

            // Đánh dấu lựa chọn hiện tại
            if (i == selectedChoice) {
                g2.setColor(Color.WHITE);
                g2.drawRoundRect(choiceBoxX - 5, choiceBoxY + i * choiceBoxHeight, choiceBoxWidth + 10, 40, 15, 15);
                g2.setColor(new Color(100, 100, 100, 100));
                g2.fillRoundRect(choiceBoxX - 5, choiceBoxY + i * choiceBoxHeight, choiceBoxWidth + 10, 40, 15, 15);
            }
        }
    }

    // VẼ OBJ Ở ĐÂY
    private boolean introPhase = true;  // Đang ở giai đoạn giới thiệu
    private float introAlpha = 0f;      // Độ trong suốt cho phần giới thiệu
    private boolean introFadingIn = true; // Kiểm tra nếu đang mờ dần để hiện lên
    private boolean showMadeBy = true;   // Kiểm tra xem có hiển thị "made by" không
    // Các phần khác của GamePanel...

    // Hàm vẽ phần giới thiệu (Intro)
    private void drawIntro(Graphics2D g2) {
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, screenWidth, screenHeight);

        g2.setFont(new Font("Arial", Font.BOLD, 30));
        g2.setColor(new Color(0, 0, 0, introAlpha));

        if (showMadeBy) {
            int textWidth = g2.getFontMetrics(new Font("Arial", Font.BOLD, 30)).stringWidth("from DM THA with luv <3");
            g2.drawString("from DM THA with luv <3", screenWidth / 2 - textWidth/2, screenHeight / 2);
        }

        if (introFadingIn) {
            introAlpha += 0.05f; // Hiệu ứng mờ dần hiện lên
            if (introAlpha >= 1.0f) {
                introAlpha = 1.0f;
                introFadingIn = false; // Đổi sang mờ dần đi
            }
        } else {
            introAlpha -= 0.02f; // Hiệu ứng mờ dần biến mất
            if (introAlpha <= 0) {
                introAlpha = 0;
                introFadingIn = true; // Đổi sang mờ dần hiện lên lần nữa
                if (showMadeBy) {
                    showMadeBy = false;    // Kết thúc "Made by", chuyển sang "ChatthisT"
                    introPhase = false;    // Kết thúc phần giới thiệu
                    startMenu = true;      // Chuyển sang Start Menu
                }
            }
        }
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        if (introPhase) {
            drawIntro(g2); // Vẽ phần giới thiệu
        } else if (startMenu) {
            drawStartMenu(g2); // Vẽ màn hình Start Menu
        } else {
            // Các phần còn lại của game
            if (fadingIn) {
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, fadeAlpha));
                draw(g2); // Vẽ màn hình game
                fadeAlpha += 0.02f; // Giảm dần độ trong suốt
                if (fadeAlpha >= 1.0) {
                    fadeAlpha = 1;
                    fadingIn = false; // Kết thúc hiệu ứng chuyển cảnh
                }
            } else if (gameOver) {
                // Hiển thị màn hình Game Over
                g2.setColor(Color.RED);
                g2.setFont(new Font("Arial", Font.BOLD, 50));
                g2.drawString("GAME OVER", screenWidth / 2 - 150, screenHeight / 2);
                g2.setFont(new Font("Arial", Font.PLAIN, 30));
                g2.drawString("Press R to Restart", screenWidth / 2 - 130, screenHeight / 2 + 50);
            } else if (pauseMenu) {
                draw(g2); // Vẽ nội dung game phía sau khung pause

                // Vẽ menu pause
                int pauseWidth = screenWidth / 3;
                int pauseHeight = screenHeight / 3;
                int pauseX = (screenWidth - pauseWidth) / 2;
                int pauseY = (screenHeight - pauseHeight) / 2;

                g2.setColor(new Color(0, 0, 0, 150)); // Nền mờ
                g2.fillRect(pauseX, pauseY, pauseWidth, pauseHeight);

                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Arial", Font.BOLD, 20));
                g2.drawString("PAUSED", pauseX + pauseWidth / 2 - 40, pauseY + pauseHeight / 2);
            } else {
                // Vẽ game
                draw(g2);
            }
        }
        g2.dispose();
    }
    public Entity createObject(String objectType) {
        switch (objectType) {
            case "Slime":
                return new Slime(this);
            case "NPC":
                return new NPC(this);
            case "Portal":
                return new Portal(this);
            case "ThrowingBottle":
                return new ThrowingBottle(this);
            case "HPBottle":
                return new HPBottle("HPBottle",this);
            case "InvisiblePotion":
                return new HPBottle("InvisiblePotion",this);
            case "Key":
                return new CommonItem("Key", this);
            case "ShopKeeper":
                return new ShopKeeper(this);
            case "Soldier":
                return new Soldier(this);
            case "GuildMaster":
                return new GuildMaster(this);
            default:
                System.out.println("Unknown object type: " + objectType);
                return null;
        }
    }
}
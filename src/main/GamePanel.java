package main;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Vector;
import javax.imageio.ImageIO;
import javax.swing.*;
import UI.Bar;
import collision.CollisionCheck;
import entity.Entity;
import entity.Items.CommonItem;
import entity.Items.HPBottle;
import entity.Items.ObjectMap1;
import entity.Items.ThrowingBottle;
import entity.enemy.Slime;
import entity.enemy.Soldier;
import entity.npc.*;
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
    public double loadingTime = 0;

    // Cài đặt FPS
    public final int FPS = 60;
    public int map = 1;

    // CONSTRUCTOR
    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight)); // Set size màn hình
        this.setDoubleBuffered(true); // Chất lượng render tốt hơn
        this.addKeyListener(keyH); // Thêm vào để game detect key input
        this.addMouseListener(mouseH); // Detect Mouse input
        this.addMouseMotionListener(mouseH);
        this.setFocusable(true); // Tập trung vào nhận diện và xử lí key input
        soundManager = new Sound();
        /*soundManager.setVolumeAll(-20.0f);
        soundManager.setVolume("background", -30.0f);
        soundManager.loop("background");*/
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
    public MouseHandler mouseH = new MouseHandler(this);
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
        if(map == 1){
            player.locX = 18;
            player.locY = 81;
        }
        else if(map == 2){
            player.locX = 51;
            player.locY = 48;
        }
        else if(map == 3){
            player.locX = 17;
            player.locY = 67;
        }
        else if(map == 4){
            player.locX = 30;
            player.locY = 44;
        }
        // Đặt lại tất cả các thành phần của trò chơi về trạng thái ban đầu
        player.setDefaultValue(
            tileSize * player.locX,
            tileSize * player.locY,
            5,
            "down");
        obj.clear(); // Xóa tất cả các object
        objMap1.clear();
        gameOver = false;
        running = true;
        reloadTime = 0;
        player.kills= 0;
        player.money = 5;
        soundManager.setVolumeAll(-20.0f);
        soundManager.setVolume("background", -30.0f);
        soundManager.play("background");
        soundManager.loop("background");
        tileMng = new TileManager(this);
        loadGame();
        player.HP = 10;
        player.Energy = 200;
         // Gọi lại setup ban đầu của game
        repaint();
    }
    public void nextMap(){
        if(map == 1){
            player.locX = 18;
            player.locY = 81;
        }
        else if(map == 2){
            player.locX = 51;
            player.locY = 48;
        }
        else if(map == 3){
            player.locX = 17;
            player.locY = 67;
        }
        else if(map == 4){
            player.locX = 30;
            player.locY = 44;
        }
        fadingIn = true;
        player.setDefaultValue(
            tileSize * player.locX,
            tileSize * player.locY,
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
        tileMng = new TileManager(this);// Gọi lại setup ban đầu của game
        loadingTime = 100;
        repaint();
        saveGame();
    }
    // NƠI CHỨA UPDATE NÈ
    public boolean pauseMenu = false;
    private Rectangle restartButton;
    private Rectangle menuButton;

    // Khởi tạo vị trí và kích thước nút

    public void update() {
        if (gameOver) {
            if(keyH.RPressed){
                System.out.println("isReset");
                resetGame();
                loadingTime = 100;
                keyH.RPressed = false;
                player.dead = false;
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
        loadingTime--;
        reloadTime--;
        HPbar.update(player.HP);
        EnergyBar.update(player.Energy);
        if(map == 1){
            Rectangle[] zone = new Rectangle[]{new Rectangle(41 * tileSize, 70 * tileSize, 30 * tileSize, 10 * tileSize), new Rectangle(10*tileSize, 10*tileSize, 28*tileSize, 40*tileSize)};
// Kiểm tra nếu player đang trong vùng
            boolean flag = false;

            for(int i = 0;i<zone.length;i++){

                if (zone[i].contains(new Point(player.worldX, player.worldY))) {
                    for (Entity entity : obj){
                        if(entity.isEnemy && zone[i].contains(entity.worldX, entity.worldY)) {
                            flag = true;
                            break;
                        }
                    }
                    // Player đã vào vùng, bật trạng thái `on` cho các CombatWall
                    if (flag) {
                        for (Entity object : obj) {
                            // Lọc các CombatWall
                            if (Objects.equals(object.objName, "CombatWall")) {
                                object.on = true;
                            }
                            // Lọc các enemy trong vùng zone
                        }
                        for (Entity entity : obj) {
                            if (entity.isEnemy && zone[i].contains(entity.worldX, entity.worldY)) entity.awake = true;
                        }
                    }
                    if (!flag) {
                        for (Entity object : obj) {
                            // Lọc các CombatWall
                            if (Objects.equals(object.objName, "CombatWall")) {
                                // System.out.println(1);
                                object.on = false;
                            }
                            // Lọc các enemy trong vùng zone
                        }
                    }
                }
            }
        }
        // Kiểm tra điều kiện game over (ví dụ: HP <= 0)
        if (player.dead) {
            // Dừng nhạc nền khi người chơi chết
            soundManager.play("player_die"); // Phát âm thanh khi người chơi chết
            gameOver = true;
        }
    }
    int selectedChoice = -1;
    private boolean newGameSlideShow = false;
    private int slideIndex = 0;
    private long slideTimer = 0; // Thời gian hiện tại của slide
    private final long SLIDE_DURATION = 3000; // Thời gian mỗi slide (ms)
    private ArrayList<BufferedImage> slideImages = new ArrayList<>();
    Font bigFont = FontLoader.loadFont("/UI/SVN-Determination Sans.otf",50);
    Font smallFont = FontLoader.loadFont("/UI/SVN-Determination Sans.otf",30);

    private void loadSlideImages() {
        try {
            for (int i = 1; i <= 5; i++) { // Giả sử bạn có 5 cặp ảnh
                slideImages.add(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/UI/Cutscene/ND" + i + "_notext.png"))));
                slideImages.add(ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/UI/Cutscene/ND" + i + "_text.png"))));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    boolean clickSlide = false;
    public void onClick(int mouseInfo){
        if (startMenu) { // Kiểm tra nếu đang ở trạng thái Start Menu
            if (mouseInfo == 1 && selectedChoice != -1) { // Nếu nhấn chuột trái
                if(selectedChoice % 3 == 0){
                    clearGameData();
                    newGameSlideShow = true;
                    loadSlideImages();
                    slideIndex = 0;
                    slideTimer = System.currentTimeMillis();
                    soundManager.play("opening");
                    soundManager.loop("opening");
                }
                else if(selectedChoice % 3 == 1){
                    loadGame();
                    loadingTime = 100;
                }
                else if(selectedChoice % 3 == 2){
                    System.exit(0);
                }
                startMenu = false; // Tắt Start Menu
                // Bắt đầu hiệu ứng chuyển cảnh

            }
        }
        else if(newGameSlideShow){
            clickSlide = true;
        }
        else if(pauseMenu){
            int screenMouseX = mouseX - (player.worldX - player.screenX);
            int screenMouseY = mouseY - (player.worldY - player.screenY);
            if (restartButton.contains(screenMouseX, screenMouseY)) {
                resetGame();  // Gọi hàm resetGame
                loadingTime = 100;
                pauseMenu = false;
            } else if (menuButton.contains(screenMouseX, screenMouseY)) {
                pauseMenu = false;
                startMenu = true;  // Quay lại màn hình startMenu
            }
        }
        else if(player.combat && !gameOver){
            if(reloadTime <= 0){
                if(mouseInfo == 1 && player.Energy >= 10){
                    NormalBullet b = new NormalBullet("/bullet/bullet.png","bullet",12,12, 8, 8, player.worldX, player.worldY,20,this ,0, 12, 1, 1, mouseX, mouseY);
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
        for(Entity item : player.items){
            currentItems.add(item.objName);
        }
        ArrayList<Integer> currentItemsCount = new ArrayList<>(player.itemsCount);
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
            System.out.println("Map: " + saveData.map);
            if(saveData.map == 1){
                player.locX = 18;
                player.locY = 81;
            }
            else if(saveData.map == 2){
                player.locX = 51;
                player.locY = 48;
            }
            else if(map == 3){
                player.locX = 17;
                player.locY = 67;
            }
            else if(map == 4){
                player.locX = 30;
                player.locY = 44;
            }
            player.setDefaultValue(tileSize*player.locX, tileSize*player.locY, 5, "down");
            // Thiết lập trạng thái game từ saveData
            player.HP = (saveData.HP);
            map = saveData.map;
            player.items.clear();
            player.itemsCount.clear();
            player.quests.clear();
            player.alpha = 1;
            for(String item : saveData.items){
                player.items.add(createObject(item));
            }
            player.itemsCount = new ArrayList<>(saveData.itemsCount);
            player.money = saveData.money;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Tải game thất bại.");
        }
    }
    public void clearGameData() {
        int currentHP = 10;
        int currentMap = 1;
        ArrayList<String> currentItems = new ArrayList<>();
        ArrayList<Integer> currentItemsCount =new ArrayList<>();
        int currentMoney = 0;
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
    // NƠI CHỨA VẼ NÈ
    public void draw(Graphics2D g2) {
        // Vẽ Map
        tileMng.draw(g2);

        // Vẽ các đối tượng trong objMap1, chỉ vẽ khi chúng nằm trong phạm vi màn hình
        for (int i = 0; i < objMap1.size(); i++) {
            ObjectMap1 object = objMap1.get(i);
            if (object != null && object.layer < 1) {
                object.draw(g2, this);
            }
        }

        // Vẽ các đối tượng trong obj, chỉ vẽ khi chúng nằm trong phạm vi màn hình
        for (int i = 0; i < obj.size(); i++) {
            Entity entity = obj.get(i);
            if (entity != null && entity.layer < 1 && isObjectInScreen(entity)) {
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
            if ((entity != null && entity.layer >= 1 && isObjectInScreen(entity)) || Objects.equals(Objects.requireNonNull(entity).objName, "Golem")) {
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
        if(Objects.equals(entity.objName, "CombatWall")) return true;
        if(Math.abs(entity.worldX - player.worldX) <= 15*tileSize && Math.abs(entity.worldY - player.worldY) <= 12*tileSize){
            return true;
        }
        return false;
    }
    public void drawStartMenu(Graphics2D g2) {
        // Vẽ background trắng
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, screenWidth, screenHeight);

        // Tính toán lại tọa độ chuột trên màn hình
        int screenMouseX = mouseX - (player.worldX - player.screenX);
        int screenMouseY = mouseY - (player.worldY - player.screenY);

        // Vẽ tên game
        g2.setColor(Color.BLACK);
        g2.setFont(bigFont);
        g2.drawString("The Last Wizard", screenWidth / 2 - 200, screenHeight / 2 - 150);

        // Cấu hình các lựa chọn menu
        String[] choices = {"New Game", "Load Game", "Quit"};
        int choiceBoxX = screenWidth / 2 - 200; // Vị trí X của hộp lựa chọn
        int choiceBoxY = screenHeight / 2 - 20; // Vị trí Y ban đầu của hộp lựa chọn
        int choiceBoxWidth = 400; // Chiều rộng hộp lựa chọn
        int choiceBoxHeight = 50; // Chiều cao mỗi hộp lựa chọn
        g2.setFont(smallFont);

        // Vẽ các lựa chọn và kiểm tra vị trí chuột
          // Đặt mặc định là không chọn lựa chọn nào
        for (int i = 0; i < choices.length; i++) {
            Rectangle r = new Rectangle(choiceBoxX, choiceBoxY + i * choiceBoxHeight, choiceBoxWidth, 40);

            // Vẽ hộp lựa chọn
            g2.setColor(new Color(0, 0, 0, 180));
            g2.fillRoundRect(choiceBoxX, choiceBoxY + i * choiceBoxHeight, choiceBoxWidth, 40, 15, 15);
            g2.setColor(Color.WHITE);
            g2.drawRoundRect(choiceBoxX, choiceBoxY + i * choiceBoxHeight, choiceBoxWidth, 40, 15, 15);
            g2.drawString("Key " + (i + 1) + ": " + choices[i], choiceBoxX + 10, choiceBoxY + i * choiceBoxHeight + 28);

            // Kiểm tra nếu chuột đang ở trong vị trí của lựa chọn này
            if (r.contains(screenMouseX, screenMouseY)) {
                selectedChoice = i;  // Gán lựa chọn dựa trên vị trí của chuột
            }

            // Đánh dấu lựa chọn hiện tại
            if (i == selectedChoice) {
                g2.setColor(Color.WHITE);
                g2.drawRoundRect(choiceBoxX - 5, choiceBoxY + i * choiceBoxHeight , choiceBoxWidth + 10, 40, 15, 15);
                g2.setColor(new Color(100, 100, 100, 100));
                g2.fillRoundRect(choiceBoxX - 5, choiceBoxY + i * choiceBoxHeight , choiceBoxWidth + 10, 40, 15, 15);
            }
        }
    }

    private void drawLoadingScreen(Graphics g) {
        BufferedImage img = null;
        // Thiết lập nền màn hình loading
        try(InputStream is = getClass().getResourceAsStream("/UI/LoadingScene/MAP_" + map + ".png")){
            img = ImageIO.read(is);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        g.drawImage(img,0,0,getWidth(),getHeight(),null);
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

        g2.setFont(smallFont);
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

    public void drawPauseMenu(Graphics g) {
        // Màu nền và các thiết lập cơ bản khác của menu pause
        int buttonWidth = 100;
        int buttonHeight = 50;
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        restartButton = new Rectangle(centerX - buttonWidth / 2, centerY - 100, buttonWidth, buttonHeight);
        menuButton = new Rectangle(centerX - buttonWidth / 2, centerY, buttonWidth, buttonHeight);
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, getWidth(), getHeight());
        int screenMouseX = mouseX - (player.worldX - player.screenX);
        int screenMouseY = mouseY - (player.worldY - player.screenY);
        // Vẽ nút Restart
        g.setColor(Color.WHITE);
        if (restartButton.contains(screenMouseX, screenMouseY)) {
            g.setColor(Color.YELLOW); // Đổi màu khi hover
        }
        g.fillRect(restartButton.x, restartButton.y, restartButton.width, restartButton.height);
        g.setColor(Color.BLACK);
        g.setFont(FontLoader.loadFont("/UI/SVN-Determination Sans.otf", 20));
        g.drawString("Restart", restartButton.x + 20, restartButton.y + 30);

        // Vẽ nút Menu
        g.setColor(Color.WHITE);
        if (menuButton.contains(screenMouseX, screenMouseY)) {
            g.setColor(Color.YELLOW);
        }
        g.fillRect(menuButton.x, menuButton.y, menuButton.width, menuButton.height);
        g.setColor(Color.BLACK);
        g.drawString("Menu", menuButton.x + 25, menuButton.y + 30);
    }
    private float slideAlpha = 0f; // Độ trong suốt

    // Thêm biến frameCounter
    private int frameCounter = 0;

    private void drawSlideShow(Graphics2D g2) {
        if (slideIndex < slideImages.size()) {
            BufferedImage currentSlide = slideImages.get(slideIndex);
            BufferedImage nextSlide = (slideIndex + 1 < slideImages.size()) ? slideImages.get(slideIndex + 1) : null;

            // Tăng độ trong suốt dần cho slide hiện tại
            slideAlpha = Math.min(slideAlpha + 0.02f, 1.0f);

            // Vẽ slide hiện tại
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
            g2.drawImage(currentSlide, 0, 0, screenWidth, screenHeight, null);

            // Vẽ slide tiếp theo (nếu có)
            if (nextSlide != null) {
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, slideAlpha));
                g2.drawImage(nextSlide, 0, 0, screenWidth, screenHeight, null);
            }

            // Chuyển sang slide tiếp theo nếu người dùng click hoặc alpha đạt mức tối đa
            if (clickSlide && slideAlpha >= 1.0f) {
                slideIndex++;
                slideAlpha = 0f; // Reset độ trong suốt chỉ khi chuyển hoàn toàn
                clickSlide = false;
            }
        } else {
            // Xử lý khi đến slide cuối cùng
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
            g2.drawImage(slideImages.get(slideIndex - 1), 0, 0, screenWidth, screenHeight, null);

            // Đếm số frame sau slide cuối
            frameCounter++;
            if (frameCounter >= 50) { // Kết thúc slideshow sau 50 frame (~50ms x số FPS)
                newGameSlideShow = false;
                startMenu = false;
                loadingTime = 100;
                soundManager.stop("opening");
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
        } else if (newGameSlideShow) {
            drawSlideShow(g2); // Hàm vẽ slideshow
        } else {
            // Các phần còn lại của game
            if(loadingTime >= 0){
                player.combat = false;
                drawLoadingScreen(g2);
                if(loadingTime <= 0) {
                    resetGame();
                    fadingIn = true;
                    setupGame();
                    loadingTime = -10;
                    player.combat = true;
                }
            }
            else if (fadingIn) {
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, fadeAlpha));
                draw(g2); // Vẽ màn hình game
                fadeAlpha += 0.02f; // Giảm dần độ trong suốt
                if (fadeAlpha >= 1.0) {
                    fadeAlpha = 0;
                    fadingIn = false; // Kết thúc hiệu ứng chuyển cảnh
                }
            } else if (gameOver) {
                // Hiển thị màn hình Game Over
                soundManager.muteAll();
                g2.setColor(Color.RED);
                g2.setFont(bigFont);
                g2.drawString("GAME OVER", screenWidth / 2 - 150, screenHeight / 2);
                g2.setFont(smallFont);
                g2.drawString("Press R to Restart", screenWidth / 2 - 130, screenHeight / 2 + 50);
            } else if (pauseMenu) {
                draw(g2); // Vẽ nội dung game phía sau khung pause
                drawPauseMenu(g2);
                // Vẽ menu pause

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
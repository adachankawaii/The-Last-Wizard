package entity.player;

import entity.Items.CommonItem;
// import entity.Items.Crow;
import entity.Items.HPBottle;
import entity.Items.ThrowingBottle;
import entity.enemy.*;
import entity.npc.GuildMaster;
import entity.npc.NPC;
import entity.npc.Portal;
import entity.npc.ShopKeeper;
import main.FontLoader;
import main.KeyHandler;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;
// import java.util.Vector;

import entity.Entity;
import main.GamePanel;

import javax.imageio.ImageIO;

public class Player extends Entity{
    public int HP = 10;
    public int Energy = 200;
    int timer = 0;
    int itemTimer = 50;
    int invisibleTimer = 150;
    public int money = 2;
    int delay = 8;
    public ArrayList<Entity> items = new ArrayList<>(8);
    public ArrayList<Integer> itemsCount = new ArrayList<>(8);
    public int pointer = 0;
    public boolean dead = false;
    public boolean combat = false;
    // --------------------------Tham số cho nhiệm vụ--------------------------------------------
    public int kills = 0;
    public String interact = null;
    public boolean completed = false;
    // --------------------------Hết tham số cho nhiệm vụ-----------------------------------------
    BufferedImage coin;
    public int locX = 0, locY = 0;
    Font smallFont = FontLoader.loadFont("/UI/SVN-Determination Sans.otf", 15);
    Font bigFont = FontLoader.loadFont("/UI/SVN-Determination Sans.otf", 20);
    public boolean call = false;
    public boolean lineOn = true;

    public Player(GamePanel gp, KeyHandler keyH) {
        if(gp.map == 1){
            locX = 18;
            locY = 81;
        }
        else if(gp.map == 2){
            locX = 51;
            locY = 48;
        }
        else if(gp.map == 3){
            locX = 17;
            locY = 67;
        }
        else if(gp.map == 4){
            locX = 30;
            locY = 44;
        }
        this.gp = gp;
        this.keyH = keyH;
        layer = 1;
        // Khởi tạo vị trí nhân vật
        screenX = gp.screenWidth/2 - gp.tileSize/2;
        screenY = gp.screenHeight/2 - gp.tileSize/2;
        isTrigger = true;
        // Khởi tạo giá trị ban đầu, gọi các hàm khởi tạo trong Entity
        
        setDefaultValue(
        gp.tileSize * locX,
        gp.tileSize * locY,
        5,
        "down");

        rectGet(5, 3, 20, 30);
        getPlayerImage(); // Lấy hình ảnh Player

        try(InputStream is = getClass().getResourceAsStream("/effect/coin.png")){
            coin = ImageIO.read(is);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

        // IMPORT NHIỀU ẢNH 1 LOẠI CHUYỂN ĐỘNG

    public void getPlayerImage() {
        // Lấy từng chuyển động của nhân vật. Mỗi chuyển động lên/xuống/trái/phải tương ứng với 4 ảnh
        // Quy ước ở đây: [0]: lên - [1]: xuống - [2]: trái - [3]: phải
        importAndSliceVertical("/player/move/B_witch_idle.png",6, 0,0);
        importAndSliceVertical("/player/move/B_witch_run.png",8, 0,0);
        importAndSliceVertical("/player/move/B_witch_death.png",9, 0,0);

    }
    private BufferedImage loadBorder(){
        BufferedImage bufferedImage = null;
        try (InputStream is = getClass().getResourceAsStream("/UI/Border.png")) {
            bufferedImage = ImageIO.read(is);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bufferedImage;
    }
    public void drawItems(Graphics2D g2) {

        int itemX = 10; // Xác định vị trí X (cạnh trái màn hình)
        int itemY = 50; // Xác định vị trí Y ban đầu
        int itemSize = 32; // Kích thước của mỗi item (hoặc bạn có thể lấy kích thước thực của hình ảnh)
        g2.drawImage(loadBorder(),itemX - 5, itemY - 5, itemSize, itemSize+10, null);
        g2.drawImage(coin, itemX, itemY, itemSize- 10, itemSize- 5, null);

        // Vẽ số lượng item
        g2.setFont(smallFont);
        g2.setColor(Color.WHITE);
        g2.drawString(money + "", itemX, itemY + itemSize);
        itemY += 10 + itemSize;
        // Lặp qua các item trong danh sách
        for (int i = 0; i < itemsCount.size(); i++) {
            Entity item = items.get(i); // Lấy item hiện tại

            // Nếu item này là item mà pointer đang chỉ đến, vẽ khung đỏ

            g2.drawImage(loadBorder(),itemX - 5, itemY - 5, itemSize+10, itemSize+10, null);
            // Vẽ hình ảnh của item (giả sử item có phương thức drawItem để vẽ hình ảnh của nó)
            g2.drawImage(item.animations.get(0).get(0), itemX, itemY, itemSize, itemSize, null);

            // Vẽ số lượng item
            g2.setFont(smallFont);
            g2.setColor(Color.WHITE);
            g2.drawString(itemsCount.get(i).toString(), itemX, itemY + itemSize);
            if(i == pointer) {
                g2.setColor(Color.RED);
                g2.drawRect(itemX - 2, itemY - 2, itemSize + 4, itemSize + 4); // Vẽ khung viền màu đỏ lớn hơn hình item một chút
            }
            // Di chuyển Y xuống cho item tiếp theo
            itemY += itemSize + 10; // Thêm khoảng cách giữa các item
        }
    }
    int delayE = 0;
    boolean isDead = false;
    public void update() {
        for (int i = itemsCount.size() - 1; i >= 0; i--) {
            if (itemsCount.get(i) <= 0) {
                items.remove(i);
                itemsCount.remove(i);
                if (pointer >= items.size() && !items.isEmpty()) {
                    pointer = 0;
                }
            }
        }

        if (combat && !isDead) {
            System.out.println(worldX/gp.tileSize + " " + worldY/gp.tileSize);
            int objIndex = gp.cCheck.checkObject(this, true);
            // Điều kiện khi viên đạn hoặc slime chạm vào người chơi
            if (isTriggerOn && (gp.obj.get(objIndex).objName.equals("Slime_attack") || gp.obj.get(objIndex).objName.equals("enemyBullet")) && timer <= 0) {
                alpha = 1;
                invisibleTimer = 150;
                timer = 20;
                HP--;
                gp.soundManager.play("hurt");
                if(HP > 0) isHurt = true;
                else{
                    isDead = true;
                }
            }

            // Giới hạn HP và Energy
            if (HP >= 10) HP = 10;
            if (Energy >= 200) Energy = 200;
            //else if (Energy < 200) Energy = 200;

            // Kiểm tra các phím di chuyển
            if ((keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed)) {
                delay = 8;
                aniCount = 1;

                // Di chuyển chéo
                if (keyH.upPressed && keyH.leftPressed) {
                    direction = "up-left";
                } else if (keyH.upPressed && keyH.rightPressed) {
                    direction = "up-right";
                } else if (keyH.downPressed && keyH.leftPressed) {
                    direction = "down-left";
                } else if (keyH.downPressed && keyH.rightPressed) {
                    direction = "down-right";
                } else { // Di chuyển đơn
                    if (keyH.upPressed) {
                        direction = "up";
                    } else if (keyH.downPressed) {
                        direction = "down";
                    } else if (keyH.leftPressed) {
                        direction = "left";
                    } else if (keyH.rightPressed) {
                        direction = "right";
                    }
                }

                // Kiểm tra va chạm bản đồ
                gp.cCheck.checkMapObject(this);
                if (!collisionOn) {
                    switch (direction) {
                        case "up" -> worldY -= speed;
                        case "down" -> worldY += speed;
                        case "left" -> worldX -= speed;
                        case "right" -> worldX += speed;
                        case "up-left" -> {
                            worldY -= (int) (speed / Math.sqrt(2));
                            worldX -= (int) (speed / Math.sqrt(2));
                        }
                        case "up-right" -> {
                            worldY -= (int) (speed / Math.sqrt(2));
                            worldX += (int) (speed / Math.sqrt(2));
                        }
                        case "down-left" -> {
                            worldY += (int) (speed / Math.sqrt(2));
                            worldX -= (int) (speed / Math.sqrt(2));
                        }
                        case "down-right" -> {
                            worldY += (int) (speed / Math.sqrt(2));
                            worldX += (int) (speed / Math.sqrt(2));
                        }
                    }
                }
            } else if (!isHurt) {
                aniCount = 0;
                delay = 8;// Đặt về animation idle nếu không di chuyển và không bị thương
            }

            // Quản lý khung hình của animation
            spriteCounter++;
            if (spriteCounter > delay) {
                spriteNum++;
                if (spriteNum >= animations.get(aniCount).size()) spriteNum = 0;
                spriteCounter = 0;
                Energy++;
            }
            if(timer <= 10){
                isHurt = false;
            }
            // Giảm invisible timer khi alpha < 0.9
            if (alpha < 0.9) {
                invisibleTimer--;
            }

            // Khi invisible timer hết, kết thúc trạng thái "hurt"
            if (invisibleTimer <= 0) {
                alpha = 1;
                invisibleTimer = 150;
            }

            collisionOn = false;
            isTriggerOn = false;

            if (objIndex != 999 && gp.obj.get(objIndex) != null) {
                close = (gp.obj.get(objIndex).isItem);
                if (!close && gp.obj.get(objIndex).objName.equals("Coin")) {
                    money++;
                    gp.obj.remove(gp.obj.get(objIndex));
                }
                if (keyH.EPressed && close && delayE <= 0) {
                    pickUpObj(objIndex);
                    close = false;
                    delayE = 20;
                }
            } else {
                close = false;
            }

            updateQuests();

            if (gp.keyH.i != -1 && !items.isEmpty() && (gp.keyH.i < items.size())) {
                if (pointer != gp.keyH.i) itemTimer = 50;
                pointer = gp.keyH.i;
            }
            delayE --;
            timer--;
            itemTimer--;
        }
        else if(isDead){
            aniCount = 2;
            spriteCounter++;
            if (spriteCounter > delay) {
                if (spriteNum >= animations.get(aniCount).size() - 1) {
                    dead = true;
                    spriteNum = animations.get(aniCount).size() -1;
                    isDead = false;
                }
                spriteNum++;
                spriteCounter = 0;
            }
        }
        else {
            spriteCounter++;
            if (spriteCounter > delay) {
                spriteNum++;
                if (spriteNum >= animations.get(aniCount).size()) spriteNum = 0;
                spriteCounter = 0;
            }
        }
    }

    public void drawTorchEffect(Graphics2D g2, int playerX, int playerY) {
        // Lưu trạng thái ban đầu của Graphics2D
        Composite originalComposite = g2.getComposite();

        // Kích thước và bán kính của vòng sáng quanh người chơi
        int radius = 300;  // Bán kính của vùng sáng
        float[] dist = {0.0f, 0.5f, 1.0f}; // Phân bố độ sáng (trong phạm vi 0-1)
        Color[] colors = {new Color(1f, 1f, 1f, 0f), new Color(0f, 0f, 0f, 0.6f), new Color(0f, 0f, 0f, 1f)};

        // Tạo một đối tượng RadialGradientPaint để làm hiệu ứng sáng mờ dần
        RadialGradientPaint radialGradient = new RadialGradientPaint(
                new Point2D.Float(playerX, playerY), // Tâm của ánh sáng
                radius, // Bán kính
                dist, // Vị trí của màu
                colors // Các màu sắc từ trong ra ngoài
        );

        // Vẽ lớp phủ tối
        g2.setPaint(radialGradient);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        g2.fillRect(0, 0, gp.getWidth(), gp.getHeight());

        // Khôi phục trạng thái ban đầu
        g2.setComposite(originalComposite);
    }
    int hasKey = 0;
    boolean close = false;
    void closeItem(Graphics2D g2){
        String itemName = "Press E to loot";
        g2.setFont(bigFont);
        g2.setColor(Color.WHITE);

        // Hiển thị tên item
        int textX = gp.screenWidth / 2 - g2.getFontMetrics().stringWidth(itemName) / 2;
        int textY = gp.screenHeight - 30;

        // Vẽ khung nền và tên item
        int textWidth = g2.getFontMetrics().stringWidth(itemName);
        int textHeight = g2.getFontMetrics().getHeight();
        int boxX = textX - 10;
        int boxY = textY - textHeight;
        int boxWidth = textWidth + 20;
        int boxHeight = textHeight + 10;

        g2.setColor(new Color(0, 0, 0, 180));
        g2.fillRoundRect(boxX, boxY, boxWidth, boxHeight, 15, 15);
        g2.setColor(Color.WHITE);
        g2.drawRoundRect(boxX, boxY, boxWidth, boxHeight, 15, 15);
        g2.drawString(itemName, textX, textY);
    }
    public void pickUpObj(int i) {
        if(i != 999) {
            String objName = gp.obj.get(i).objName;
            if(objName != null){
                switch (objName) {
                    case "Slime" -> {
                        hasKey++;
                        System.out.println(objName + " " + hasKey);
                    }
                    case "ThrowingBottle", "HPBottle", "Key","InvisiblePotion","Box","Artichoke","Feather","Bell","CrystalFragment1","CrystalFragment2","CrystalFragment3","CrystalFragment","AetherCrystal" -> {
                            boolean flag = false;
                            for (int j = 0; j < itemsCount.size(); j++) {
                                if (items.get(j).objName.equals(gp.obj.get(i).objName)) {
                                    flag = true;
                                    // Sửa từ add thành set để cập nhật đúng số lượng item
                                    itemsCount.set(j, itemsCount.get(j) + 1);
                                    pointer = j;
                                    break; // Thêm break để dừng vòng lặp sau khi tìm thấy item
                                }
                            }
                        if (!flag) {
                            if (items.size() < 8) {
                                // Nếu kho đồ chưa đầy
                                items.add(gp.obj.get(i));
                                itemsCount.add(1); // Thêm mới số lượng item là 1
                                pointer = items.size() - 1;
                            } else {
                                // Lấy vật phẩm hiện tại tại con trỏ
                                Entity replacedItem = items.get(pointer);

                                // Giảm số lượng của vật phẩm bị thay thế
                                for(int j = 0;j< itemsCount.get(pointer);j++){
                                    Entity droppedItem = createObject(replacedItem.objName);
                                    if (droppedItem != null) {
                                        droppedItem.worldX = gp.player.worldX; // Vị trí người chơi
                                        droppedItem.worldY = gp.player.worldY; // Vị trí người chơi
                                        gp.obj.add(droppedItem); // Thêm vật phẩm bị rơi vào danh sách đối tượng trong game
                                    }
                                }
                                itemsCount.set(pointer, 0);
                                // Thay thế vật phẩm bị thay thế bằng vật phẩm mới
                                items.add(pointer, gp.obj.get(i));
                                itemsCount.add(pointer, 1);

                                // Tạo lại vật phẩm bị thay thế và rơi ra tại vị trí người chơi

                            }
                        }
                        combineFragments(gp);
                        itemTimer = 50;
                        gp.obj.remove(gp.obj.get(i));
                        gp.soundManager.play("got_sth");
                    }
                    default -> {
                    }
                }
            }

        }
    }
    private void combineFragments(GamePanel gp) {
        // Kiểm tra các mảnh ngọc trong kho
        boolean hasFragment1 = false, hasFragment2 = false, hasFragment3 = false, hasCrystalFragment= false;

        for (int j = 0; j < items.size(); j++) {
            String objName = items.get(j).objName;
            if (objName.equals("CrystalFragment1")) hasFragment1 = true;
            if (objName.equals("CrystalFragment2")) hasFragment2 = true;
            if (objName.equals("CrystalFragment3")) hasFragment3 = true;
            if (objName.equals("CrystalFragment")) hasCrystalFragment= true;
        }

        // Kết hợp thành CrystalFragmentnếu đủ mảnh 1, 2, 3
        if (hasFragment1 && hasFragment2) {
            removeItems("CrystalFragment1", gp);
            removeItems("CrystalFragment2", gp);
            Entity f = createObject("CrystalFragment");
            f.worldX = this.worldX;
            f.worldY = this.worldY;
            gp.obj.add(f);
        }

        // Kết hợp thành AetherCrystal nếu có CrystalFragmentvà bất kỳ mảnh nào trong 1, 2, 3
        if (hasCrystalFragment&& (hasFragment3)) {
            removeItems("CrystalFragment", gp);
            removeItems("CrystalFragment3", gp);
            Entity f = createObject("AetherCrystal");
            f.worldX = this.worldX;
            f.worldY = this.worldY;
            gp.obj.add(f);
        }
    }
    private void removeItems(String objName, GamePanel gp) {
        for (int j = 0; j < items.size(); j++) {
            if (items.get(j).objName.equals(objName)) {
                // Nếu số lượng lớn hơn 1, giảm số lượng
                if (itemsCount.get(j) > 1) {
                    itemsCount.set(j, itemsCount.get(j) - 1);
                } else {
                    // Nếu số lượng bằng 1, xóa vật phẩm khỏi kho
                    items.remove(j);
                    itemsCount.remove(j);
                }
                return; // Thoát vòng lặp sau khi xử lý
            }
        }

        // Nếu không tìm thấy vật phẩm
        System.out.println("Item not found in inventory: " + objName);
    }

    public boolean isDarken = false;
    public void draw(Graphics2D g2) {
        if(true) {

            detectMoveAndDraw(g2);  // Nhận diện chuyển động và vẽ nhân vật
            rectDraw(g2);  // Vẽ ô collision
        }
    }
    public ArrayList<Quest> quests = new ArrayList<>();
    @Override
    public void drawUI(Graphics2D g2, GamePanel gp){
        if(isDarken) drawTorchEffect(g2, gp.screenWidth/2, gp.screenHeight/2);
        if(combat && !isDead){
            // Kích thước và vị trí của khung
            if(!quests.isEmpty()) drawQuests(g2);
            drawItems(g2);  // Vẽ danh sách các item

            // Hiển thị tên của item đang được chọn
            if (!items.isEmpty() && pointer < items.size() && itemTimer >= 0) {
                String itemName = items.get(pointer).objName;
                g2.setFont(bigFont);
                g2.setColor(Color.WHITE);

                // Hiển thị tên item
                int textX = gp.screenWidth / 2 - g2.getFontMetrics().stringWidth(itemName) / 2;
                int textY = gp.screenHeight - 30;

                // Vẽ khung nền và tên item
                int textWidth = g2.getFontMetrics().stringWidth(itemName);
                int textHeight = g2.getFontMetrics().getHeight();
                int boxX = textX - 10;
                int boxY = textY - textHeight;
                int boxWidth = textWidth + 20;
                int boxHeight = textHeight + 10;

                g2.setColor(new Color(0, 0, 0, 180));
                g2.fillRoundRect(boxX, boxY, boxWidth, boxHeight, 15, 15);
                g2.setColor(Color.WHITE);
                g2.drawRoundRect(boxX, boxY, boxWidth, boxHeight, 15, 15);
                g2.drawString(itemName, textX, textY);

                // Nếu người chơi đứng gần item, hiển thị thông báo "Press E to loot"

            }
            if (close) {
                closeItem(g2);
            }
            if (alpha <= 0.9) {
                int barX = this.screenX + 8;
                int barY = this.screenY - 10; // Thanh máu cách đầu Slime 10 pixel

                // Kích thước của thanh máu
                int barWidth = this.solidArea.width; // Chiều rộng của thanh máu bằng với kích thước của Slime
                int barHeight = 4; // Chiều cao của thanh máu

                // Tính toán phần trăm HP

                // Lưu trạng thái gốc của Graphics2D
                screenY = worldY - gp.player.worldY + gp.player.screenY;
                screenX = worldX - gp.player.worldX + gp.player.screenX;   // Chiều cao của thanh
                int maxTime = 150;   // Thời gian tàng hình tối đa

                // Tính độ rộng hiện tại của thanh dựa trên thời gian còn lại
                int currentWidth = (int) ((double) invisibleTimer / maxTime * barWidth);

                // Vẽ khung ngoài của thanh
                g2.setColor(Color.GRAY);
                g2.fillRect(barX,barY, barWidth, barHeight);

                // Vẽ phần thanh báo hiệu thời gian còn lại
                g2.setColor(new Color(0, 255, 0, 180));  // Màu xanh với độ trong suốt
                g2.fillRect(barX, barY, currentWidth, barHeight);
            }
        }

    }

    // Thêm phương thức để thêm nhiệm vụ
    public void addQuest(Quest quest) {
        quests.add(quest);
    }
    public void drawQuests(Graphics2D g2) {
        int questX = gp.screenWidth - 250; // Vị trí X bên phải màn hình
        int questY = 50; // Vị trí Y ban đầu
        int boxWidth = 220; // Độ rộng của khung
        int boxHeight = 30 * quests.size(); // Chiều cao của khung phụ thuộc vào số lượng nhiệm vụ

        // Set font và màu sắc
        g2.setFont(smallFont);

        // Vẽ khung nền cho danh sách Quest
        g2.setColor(new Color(0, 0, 0, 150)); // Nền đen với độ trong suốt
        g2.fillRoundRect(questX - 10, questY - 30, boxWidth, boxHeight + 10, 15, 15);

        // Vẽ viền cho khung
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(2)); // Độ dày của viền
        g2.drawRoundRect(questX - 10, questY - 30, boxWidth, boxHeight + 10, 15, 15);

        // Vẽ từng Quest
        for (Quest quest : quests) {
            String status = quest.isComplete ? "Completed" : quest.currentAmount + "/" + quest.targetAmount;

            // Hiệu ứng bóng đổ
            g2.setColor(Color.BLACK);  // Màu đen cho bóng
            g2.drawString(quest.name + ": " + status, questX + 2, questY + 2);  // Tạo bóng bằng cách dịch vị trí

            // Hiển thị văn bản nhiệm vụ
            if(quest.isComplete){
                g2.setColor(Color.GREEN);
            }
            else {
                g2.setColor(Color.WHITE);  // Màu trắng cho văn bản
            }
            g2.drawString(quest.name + ": " + status, questX, questY);

            questY += 20; // Di chuyển xuống cho nhiệm vụ tiếp theo
        }
    }

    public void updateQuests() {
        for (Quest quest : quests) {
            if (!quest.isComplete) {
                switch (quest.targetType) {
                    case "Items" -> {
                        for (Entity item : items) {
                            if (Objects.equals(item.objName, quest.targetObject)) {
                                quest.currentAmount = 1;
                                break;
                            }
                        }
                    }
                    case "kills" -> {
                        quest.currentAmount = kills;
                        if (kills >= quest.targetAmount) kills = 0;
                    }
                    case "interact" ->{
                        if(Objects.equals(interact, quest.targetObject)){
                            quest.currentAmount = 1;
                        }
                    }
                }
                // Kiểm tra trạng thái hoàn thành
                quest.checkCompletion();
            }
        }
    }
    public Entity createObject(String objectType) {
        switch (objectType) {
            case "Slime":
                return new Slime(gp);
            case "NPC":
                return new NPC(gp, "Te Quiero");
            case "Amireux":
                return new NPC(gp, "Amireux");
            case "Portal":
                return new Portal(gp);
            case "ThrowingBottle":
                return new ThrowingBottle(gp);
            case "HPBottle":
                return new HPBottle("HPBottle",gp);
            case "InvisiblePotion":
                return new HPBottle("InvisiblePotion",gp);
            case "Key":
                return new CommonItem("Key", gp);
            case "CrystalFragment1":
                return new CommonItem("CrystalFragment1", gp);
            case "CrystalFragment2":
                return new CommonItem("CrystalFragment2", gp);
            case "CrystalFragment3":
                return new CommonItem("CrystalFragment3", gp);
            case "CrystalFragment":
                return new CommonItem("CrystalFragment", gp);
            case "AetherCrystal":
                return new CommonItem("AetherCrystal", gp);
            case "ShopKeeper":
                return new ShopKeeper(gp);
            case "Soldier":
                return new Soldier(gp);
            case "Knight":
                return new Knight(gp);
            case "MageSeeker":
                return new Mage(gp);
            case "Golem":
                return new Golem(gp);
            case "Executioner":
                return new Executioner(gp);
            case "Tower":
                return new Tower(gp);
            case "Ghost":
                return new Ghost(gp);
            case "GuildMaster":
                return new GuildMaster(gp);
            case "Box":
                return new CommonItem("Box", gp);
            case "Feather":
                return new CommonItem("Feather", gp);
            case "Artichoke":
                return new CommonItem("Artichoke", gp);
            case "FinalBoss":
                return new FinalBoss(gp);
            default:
                System.out.println("Unknown object type: " + objectType);
                return null;
        }
    }
}

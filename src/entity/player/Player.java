package entity.player;

import main.KeyHandler;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;

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
    public boolean combat = true;
    // --------------------------Tham số cho nhiệm vụ--------------------------------------------
    public int kills = 0;
    public String interact = null;
    // --------------------------Hết tham số cho nhiệm vụ-----------------------------------------
    BufferedImage coin;
    public int locX = 0, locY = 0;
    public Player(GamePanel gp, KeyHandler keyH) {
        if(gp.map == 1){
            locX = 18;
            locY = 81;
        }
        else if(gp.map == 2){
            locX = 51;
            locY = 48;
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
        importAndSliceVertical("/player/move/B_witch_death.png",12, 0,0);

    }
    public void drawItems(Graphics2D g2) {

        int itemX = 10; // Xác định vị trí X (cạnh trái màn hình)
        int itemY = 50; // Xác định vị trí Y ban đầu
        int itemSize = 32; // Kích thước của mỗi item (hoặc bạn có thể lấy kích thước thực của hình ảnh)
        g2.drawImage(coin, itemX, itemY, itemSize- 10, itemSize- 5, null);

        // Vẽ số lượng item
        g2.setFont(new Font("Arial", Font.BOLD, 15));
        int textWidth1 = g2.getFontMetrics(new Font("Arial", Font.PLAIN, 15)).stringWidth("x" + money);
        g2.setColor(new Color(255,255,255,120));
        g2.fillRect(itemX + itemSize,itemY + itemSize/2 - 12,textWidth1,15);
        g2.setColor(Color.black);
        g2.drawString("x" + money, itemX + itemSize, itemY + itemSize/2);
        itemY += 10 + itemSize;
        // Lặp qua các item trong danh sách
        for (int i = 0; i < itemsCount.size(); i++) {
            Entity item = items.get(i); // Lấy item hiện tại

            // Nếu item này là item mà pointer đang chỉ đến, vẽ khung đỏ
            if(i == pointer) {
                g2.setColor(Color.RED);
                g2.drawRect(itemX - 2, itemY - 2, itemSize + 4, itemSize + 4); // Vẽ khung viền màu đỏ lớn hơn hình item một chút
            }

            // Vẽ hình ảnh của item (giả sử item có phương thức drawItem để vẽ hình ảnh của nó)
            g2.drawImage(item.animations.get(0).get(0), itemX, itemY, itemSize, itemSize, null);

            // Vẽ số lượng item
            g2.setFont(new Font("Arial", Font.BOLD, 20));
            g2.setColor(new Color(255,255,255,120));
            //g2.fillRect();
            g2.setColor(Color.black);
            g2.drawString("x" + itemsCount.get(i), itemX + itemSize + 5, itemY + itemSize / 2);

            // Di chuyển Y xuống cho item tiếp theo
            itemY += itemSize + 10; // Thêm khoảng cách giữa các item
        }
    }
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
            int objIndex = gp.cCheck.checkObject(this, true);
            // Điều kiện khi viên đạn hoặc slime chạm vào người chơi
            if (isTriggerOn && (gp.obj.get(objIndex).objName.equals("Slime_attack") || gp.obj.get(objIndex).objName.equals("enemyBullet")) && timer <= 0) {
                alpha = 1;
                invisibleTimer = 150;
                timer = 20;
                HP--;
                if(HP >= 0) isHurt = true;
                else{

                    isDead = true;
                }
            }

            // Giới hạn HP và Energy
            if (HP >= 10) HP = 10;
            if (Energy >= 200) Energy = 200;
            else if (Energy < 200) Energy = 200;

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
                if (keyH.EPressed && close) {
                    pickUpObj(objIndex);
                    close = false;
                }
            } else {
                close = false;
            }

            updateQuests();

            if (gp.keyH.i != -1 && !items.isEmpty() && (gp.keyH.i < items.size())) {
                if (pointer != gp.keyH.i) itemTimer = 50;
                pointer = gp.keyH.i;
            }

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
    }

    public void drawTorchEffect(Graphics2D g2, int playerX, int playerY) {
        // Lưu trạng thái ban đầu của Graphics2D
        Composite originalComposite = g2.getComposite();

        // Kích thước và bán kính của vòng sáng quanh người chơi
        int radius = 150;  // Bán kính của vùng sáng
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
        g2.setFont(new Font("Arial", Font.PLAIN, 20));
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
                    case "ThrowingBottle", "HPBottle", "Key","InvisiblePotion" -> {
                        if (items.size() < 8) {
                            boolean flag = false;
                            for (int j = 0; j < items.size(); j++) {
                                if (items.get(j).objName.equals(gp.obj.get(i).objName)) {
                                    flag = true;
                                    // Sửa từ add thành set để cập nhật đúng số lượng item
                                    itemsCount.set(j, itemsCount.get(j) + 1);
                                    pointer = j;
                                    break; // Thêm break để dừng vòng lặp sau khi tìm thấy item
                                }
                            }
                            if (!flag) {
                                items.add(gp.obj.get(i));
                                itemsCount.add(1); // Thêm mới số lượng item là 1
                                pointer = items.size() - 1;
                            }
                        }
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
    public boolean isDarken = false;
    public void draw(Graphics2D g2) {
        if(combat) {

            detectMoveAndDraw(g2);  // Nhận diện chuyển động và vẽ nhân vật
            rectDraw(g2);  // Vẽ ô collision
        }
    }
    public ArrayList<Quest> quests = new ArrayList<>();
    @Override
    public void drawUI(Graphics2D g2, GamePanel gp){
        if(combat && !isDead){

            if(isDarken) drawTorchEffect(g2, gp.screenWidth/2, gp.screenHeight/2);
            if(!quests.isEmpty()) drawQuests(g2);
            drawItems(g2);  // Vẽ danh sách các item

            // Hiển thị tên của item đang được chọn
            if (!items.isEmpty() && pointer < items.size() && itemTimer >= 0) {
                String itemName = items.get(pointer).objName;
                g2.setFont(new Font("Arial", Font.PLAIN, 20));
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
        g2.setFont(new Font("Arial", Font.PLAIN, 15));

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

}

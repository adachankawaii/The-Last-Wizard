package entity.enemy;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

import entity.Items.Coin;
import entity.Items.CommonItem;
import entity.bullet.Bullet;
import entity.Entity;
// import entity.Items.Coin;
// import entity.bullet.NormalBullet;
// import entity.effect.Effect;
// import entity.player.Quest;
import entity.bullet.ThrowingObj;
import entity.effect.Effect;
import entity.npc.CombatWall;
import main.FontLoader;
import main.GamePanel;


public class Executioner extends Entity {
    Vector<Vector<String>> words = new Vector<>();
    int index = 0;
    int dialogueIndex = 0;
    int timer = 0;
    int delayTime = 0;
    int HP;
    double angle = 0;
    int animationDelay = 3;
    public int rootY = -1;
    public int rootX = -1;   
    boolean back = false;
    private int targetX, targetY;
    int moveSet = 0;
    HashMap<String, Integer> map = new HashMap<String, Integer>();
    public Vector<CombatWall> c = new Vector<>(); // Width = 1, Height = 8
    Font font =  FontLoader.loadFont("/UI/SVN-Determination Sans.otf",20);

    public Executioner(GamePanel gp) {
        layer = 2;
        objName = "Dementor";
        collision = true;
        direction = "down";
        HP = 35;
        speed = 1;
        isTrigger = true;
        this.gp = gp;
        rectGet(70*2, 70*2, 48*2, 52*2);
        getImage();
        aniCount = 1;
        map.put("bullet", 1);
        map.put("Bigbullet", 3);
        isEnemy = true;
        addWords(new String[]{"Sao tự dưng tôi lại thấy ớn lạnh nhỉ!","Đ-đó là the Dementor!!!","end"});
        addWords(new String[]{"Đây là mảnh ngọc thứ 3! Chúng ta đã thu thập được cả 3 mảnh ngọc rồi.","end"});

        isBoss =true;
        c.add(new CombatWall(gp, 1, 10));
        c.get(0).worldX = 17 * gp.tileSize;
        c.get(0).worldY = 18 * gp.tileSize;
        c.add(new CombatWall(gp, 10, 1));
        c.get(1).worldX = 27 * gp.tileSize;
        c.get(1).worldY = 42 * gp.tileSize;
    }
    public void addWords(String[] inputWords) {
        words.add(new Vector<>());
        words.get(words.size() - 1).addAll(List.of(inputWords));
    }
    public void getImage() {
        importAndSlice("/boss/boss2/idle.png", 4, 0, 0);
        importAndSlice("/boss/boss2/death.png", 20, 0 ,0 );
        importAndSlice("/boss/boss2/attacking.png", 6, 0,0 );
        importAndSlice("/boss/boss2/attacking2.png", 6, 0, 0);
        importAndSlice("/boss/boss2/skill.png", 12, 0 ,0);
        importAndSlice("/boss/boss2/summon.png", 4, 0,0);
    }
    int shootCounter = 0;
    boolean isResting = false;
    int restCounter = 160;
    boolean startTalk = false;
    boolean done = false;
    boolean isReverse = false;

    public void update() {
        int npcCenterX = worldX + solidArea.x + solidArea.width / 2;
        int npcCenterY = worldY + solidArea.y + solidArea.height / 2;
        spriteCounter++;
        if (spriteCounter > animationDelay) {

            if(!isReverse) {
                spriteNum++;
                if (spriteNum >= animations.get(aniCount).size()) {
                    if (!dead && aniCount != 5) spriteNum = 0;
                    else spriteNum = animations.get(aniCount).size() - 1;
                }
            }
            else{
                spriteNum--;
            }
            spriteCounter = 0;
        }

        timer--;
        if(!dead){
            if (rootY == -1) {
                rootY = this.worldY;
                rootX = this.worldX;
            }
            int playerCenterX = targetX + gp.player.solidArea.x + gp.player.solidArea.width / 2;
            int playerCenterY = targetY + gp.player.solidArea.y + gp.player.solidArea.height / 2;
            angle = Math.atan2(
                    targetY + (new Random().nextInt(gp.tileSize) - gp.tileSize / 2) - (worldY + solidArea.y + solidArea.height / 2),
                    targetX + (new Random().nextInt(gp.tileSize) - gp.tileSize / 2) - (worldX + solidArea.x + solidArea.width / 2)
            );

            double angleDegrees = Math.toDegrees(angle);
            if (angleDegrees > -22.5 && angleDegrees <= 22.5) {
                direction = "right";
                flip = false;
            } else if (angleDegrees > 22.5 && angleDegrees <= 67.5) {
                direction = "down-right";
                flip = false;
            } else if (angleDegrees > 67.5 && angleDegrees <= 112.5) {
                direction = "down";
            } else if (angleDegrees > 112.5 && angleDegrees <= 157.5) {
                direction = "down-left";
                flip = true;
            } else if (angleDegrees > 157.5 || angleDegrees <= -157.5) {
                direction = "left";
                flip = true;
            } else if (angleDegrees > -157.5 && angleDegrees <= -112.5) {
                direction = "up-left";
                flip = true;
            } else if (angleDegrees > -112.5 && angleDegrees <= -67.5) {
                direction = "up";
            } else if (angleDegrees > -67.5 && angleDegrees <= -22.5) {
                direction = "up-right";
                flip = false;
            }
            double distanceToTarget = Math.sqrt(Math.pow(npcCenterX - playerCenterX, 2) + Math.pow(npcCenterY - playerCenterY, 2));
            targetX = gp.player.worldX;
            targetY = gp.player.worldY;
            if (distanceToTarget <= 10 * gp.tileSize && !done) {
                for(int i = 0;i<c.size();i++) {
                    c.get(i).on = true;
                    gp.obj.add(c.get(i)); // Thêm tường vào danh sách đối tượng
                }
                gp.soundManager.stop("map22");
                gp.soundManager.loop("combat3");
                startTalk = true;
                done = true;
            }
            if (awake) {
                if (isResting) {
                    // Giai đoạn nghỉ
                    restCounter--;
                    if (distanceToTarget >= 5 * gp.tileSize) move(direction); // Di chuyển trong thời gian nghỉ
                    if (restCounter <= 0) {
                        isResting = false;
                        selectNextMoveSet(5);
                    }
                    animationDelay = 5;
                } else {
                    animationDelay = 3;
                    if (distanceToTarget >= 5 * gp.tileSize) move(direction);// Giai đoạn thực hiện hành động
                    executeMoveSet(npcCenterX, npcCenterY, playerCenterX, playerCenterY);
                }
            } else {
                aniCount = 0;
                animationDelay = 5;
            }

            collisionOn = false;
            delayTime--;
            delayMove--;
        }
        else {
            aniCount = 1;
            for(int i = 0;i<c.size();i++) {
                c.get(i).on = false;
            }
            collision = false;
            if(done) {
                startTalk = true;
                index = 1;
                dialogueIndex = 0;
                done = false;
            }
            gp.player.lineOn = true;
        }
    }
    private void selectNextMoveSet(int bound) {
        moveSet = bound == 4 ? new Random().nextInt(4) : 4; // Random từ 0 đến 2 (3 moveSet)
        shootCounter = 0; // Reset shootCounter cho moveSet mới
        aniCount = 0; // Reset animation state nếu cần
    }

    private void executeMoveSet(int npcCenterX, int npcCenterY, int playerCenterX, int playerCenterY) {
        switch (moveSet) {
            case 0:
                moveSetZero(npcCenterX, npcCenterY, playerCenterX, playerCenterY);
                break;
            case 1:
                moveSetOne(npcCenterX, npcCenterY, playerCenterX, playerCenterY);
                break;
            case 2:
                moveSetTwo(npcCenterX, npcCenterY, playerCenterX, playerCenterY);
                break;
            case 3:
                moveSetThree(npcCenterX, npcCenterY, playerCenterX, playerCenterY);
                break;
            case 4:
                moveSetFour(npcCenterX, npcCenterY, playerCenterX, playerCenterY);
                break;
        }
    }

    int delayMove = 0;
    int pauseCounter = 0; // Biến kiểm soát thời gian tạm dừng
    int count = 0;
    private void moveSetThree(int npcCenterX, int npcCenterY, int playerCenterX, int playerCenterY) {
        if (aniCount != 5) spriteNum = 0; // Đặt animation về trạng thái bắt đầu nếu cần
        aniCount = 5;

        if (pauseCounter > 0) {
            pauseCounter--; // Tạm dừng tại sprite đầu tiên
            return;
        }

        if (spriteNum >= animations.get(aniCount).size() - 1) {
            shootCounter++;
            spriteNum = animations.get(aniCount).size() - 1;
            if(shootCounter % 2 == 0) {
                ThrowingObj b = new ThrowingObj(null, "enemyBullet", -100 * gp.tileSize, -100 * gp.tileSize, 1, 1, npcCenterX, npcCenterY, 50, gp, 0, 7, 2, 2, targetX + (new Random().nextInt(16 * gp.tileSize) - 8 * gp.tileSize), targetY + (new Random().nextInt(16 * gp.tileSize) - 8 * gp.tileSize));
                gp.obj.add(b);
            }
            if (shootCounter >= 15) {
                aniCount = 0; // Chuyển về trạng thái mặc định
                spriteNum = 0;
                pauseCounter = 20; // Tạm dừng 10 frame
                startResting();
            }
        }
    }
    boolean red = true;
    private void moveSetZero(int npcCenterX, int npcCenterY, int playerCenterX, int playerCenterY) {
        if (aniCount != 2) spriteNum = 0;
        aniCount = 2;

        if (pauseCounter > 0) {
            pauseCounter--; // Tạm dừng tại sprite đầu tiên
            return;
        }
        if (spriteNum == 2) {
            double baseAngle = Math.atan2(playerCenterY - npcCenterY, playerCenterX - npcCenterX);

            // Số lượng đạn và khoảng cách góc giữa các đạn
            int bulletCount = 10;
            double angleStep = Math.toRadians(120.0 / (bulletCount - 1)); // Bước góc (-60 đến 60 độ)

            // Bắn từng luồng đạn
            for (int i = 0; i < bulletCount; i++) {
                // Góc hiện tại của từng viên đạn
                double angle = baseAngle - Math.toRadians(60) + i * angleStep;

                // Tính toán tọa độ mục tiêu cho từng viên đạn
                int targetBulletX = (int) (npcCenterX + Math.cos(angle) * 12*gp.tileSize);
                int targetBulletY = (int) (npcCenterY + Math.sin(angle) * 12*gp.tileSize);

                // Tạo đối tượng đạn
                Bullet b = new Bullet("/effect/effect1.png", "enemyBullet",
                        0, 0, 8 * 6, 8 * 6,
                        (int)(npcCenterX + Math.cos(angle) * 5*gp.tileSize), (int)(npcCenterY + Math.sin(angle) * 5*gp.tileSize), 8, gp,
                        0, 3, 1, 1,
                        targetBulletX, targetBulletY);
                b.root = this.objName;
                b.death = false;

                // Thêm đạn vào danh sách đối tượng
                gp.obj.add(b);
            }
        }
        if (spriteNum >= animations.get(aniCount).size() - 1) {
            count++;

            if(count >= 2) {
                count = 0;
                aniCount = 0;
                spriteNum = 0;
                pauseCounter = 20; // Tạm dừng 10 frame
                startResting();
            }
        }
    }

    private void moveSetOne(int npcCenterX, int npcCenterY, int playerCenterX, int playerCenterY) {
        if (aniCount != 3) spriteNum = 0;
        aniCount = 3;

        if (pauseCounter > 0) {
            pauseCounter--; // Tạm dừng tại sprite đầu tiên
            return;
        }
        if (spriteNum >= animations.get(aniCount).size() - 1) {
            // Số lượng đạn và khoảng cách góc giữa các đạn

        }
        if (spriteNum >= animations.get(aniCount).size() - 1) {
            count++;
            if(count >= 2) {
                int bulletCount = 10; // Ví dụ: 5 viên đạn
                double angleStep = Math.toRadians(360.0 / (bulletCount - 1)); // Bước góc (-180 đến 0 độ)

                // Bắn từng luồng đạn
                for (int i = 0; i < bulletCount; i++) {
                    // Góc hiện tại của từng viên đạn
                    double angle = Math.toRadians(180) + i * angleStep;

                    // Tính toán tọa độ mục tiêu cho từng viên đạn
                    int targetBulletX = (int) (npcCenterX + Math.cos(angle) * 12*gp.tileSize); // Khoảng cách từ tâm là 25
                    int targetBulletY = (int) (npcCenterY + Math.sin(angle) * 12*gp.tileSize);

                    // Tạo đối tượng đạn
                    Bullet b = new Bullet("/bullet/Slash.png", "enemyBullet",
                            0, 0, 8 * 6, 8 * 6,
                            (int)(npcCenterX + Math.cos(angle) * 3*gp.tileSize), (int)(npcCenterY + Math.sin(angle) * 3*gp.tileSize), 18, gp,
                            0, 8, 1, 1,
                            targetBulletX, targetBulletY);
                    b.isSlash = true;
                    b.root = this.objName;

                    // Thêm đạn vào danh sách đối tượng
                    gp.obj.add(b);
                }
                count = 0;
                aniCount = 0;
                spriteNum = 0;
                pauseCounter = 20; // Tạm dừng 10 frame
                startResting();
            }
        }
    }

    private void moveSetTwo(int npcCenterX, int npcCenterY, int playerCenterX, int playerCenterY) {
        if (aniCount != 4) spriteNum = 0;
        aniCount = 4;

        if (pauseCounter > 0) {
            pauseCounter--; // Tạm dừng tại sprite đầu tiên
            return;
        }

        if (spriteNum >= 6 && spriteNum <= 8) {
            // Sinh số đạn ngẫu nhiên mỗi frame (ít nhất 1 đạn)
            int bulletCount = 1 + (int) (Math.random() * 5); // Số đạn từ 1 đến 5

            for (int i = 0; i < bulletCount; i++) {
                // Góc ngẫu nhiên từ 0 đến 360 độ
                double angle = Math.toRadians(Math.random() * 360);

                // Tính toán tọa độ mục tiêu cho từng viên đạn
                int targetBulletX = (int) (npcCenterX + Math.cos(angle) * gp.tileSize);
                int targetBulletY = (int) (npcCenterY + Math.sin(angle) * gp.tileSize);

                // Tạo đối tượng đạn
                Bullet b = new Bullet("/bullet/enemyBullet.png", "enemyBullet",
                        0, 0, 8 * 6, 8 * 6,
                        npcCenterX, npcCenterY, 15, gp,
                        0, 13, 1, 1,
                        targetBulletX, targetBulletY);
                b.root = this.objName;
                b.death = false;

                // Thêm đạn vào danh sách đối tượng
                gp.obj.add(b);
            }
        }
        if (spriteNum >= animations.get(aniCount).size() - 1) {
            count++;
            if(count >= 3) {
                count = 0;
                aniCount = 0;
                spriteNum = 0;
                pauseCounter = 20; // Tạm dừng 10 frame
                startResting();
            }
        }
    }
    private void moveSetFour(int npcCenterX, int npcCenterY, int playerCenterX, int playerCenterY) {
        if (aniCount != 1) spriteNum = 0;
        aniCount = 1;

        if (pauseCounter > 0) {
            pauseCounter--; // Tạm dừng tại sprite đầu tiên
            return;
        }
        red = false;
        collision = false;
        if (spriteNum >= animations.get(aniCount).size() - 1) {
            isReverse = true;
            int angle = new Random().nextInt(2);
            if (angle == 1) {
                flip = true;
            } else {
                flip = false;
            }
            worldX = gp.player.worldX + (angle == 0 ? -8 * gp.tileSize : -2 * gp.tileSize);
            worldY = gp.player.worldY - 5 * gp.tileSize;
        }
        if(isReverse && spriteNum <= 0){
            isReverse = false;
            selectNextMoveSet(4);
            executeMoveSet(npcCenterX, npcCenterY, playerCenterX, playerCenterY);
            red = true;
            collision = true;
        }
        move(direction);
    }


    private void startResting() {
        aniCount = 0;
        isResting = true;
        restCounter = 30; // Thời gian nghỉ, ví dụ 60 frame
    }
    private void move(String direction) {
        int npcCenterX = worldX + solidArea.x + solidArea.width / 2;
        int npcCenterY = worldY + solidArea.y + solidArea.height / 2;
        int playerCenterX = targetX + gp.player.solidArea.x + gp.player.solidArea.width / 2;
        int playerCenterY = targetY + gp.player.solidArea.y + gp.player.solidArea.height / 2;
        double distanceToTarget = Math.sqrt(Math.pow(npcCenterX - playerCenterX, 2) + Math.pow(npcCenterY - playerCenterY, 2));
        if(distanceToTarget >= 3*gp.tileSize) {
            switch (direction) {
                case "up":
                    worldY -= speed;
                    break;
                case "down":
                    worldY += speed;
                    break;
                case "left":
                    worldX -= speed;
                    break;
                case "right":
                    worldX += speed;
                    break;
                case "up-right":
                    worldX += (int) (speed / Math.sqrt(2));
                    worldY -= (int) (speed / Math.sqrt(2));
                    break;
                case "up-left":
                    worldX -= (int) (speed / Math.sqrt(2));
                    worldY -= (int) (speed / Math.sqrt(2));
                    break;
                case "down-right":
                    worldX += (int) (speed / Math.sqrt(2));
                    worldY += (int) (speed / Math.sqrt(2));
                    break;
                case "down-left":
                    worldX -= (int) (speed / Math.sqrt(2));
                    worldY += (int) (speed / Math.sqrt(2));
                    break;
            }
        }
    }




    boolean flip = false;

    @Override
    public void draw(Graphics2D g2, GamePanel gp) {
        // Lưu trạng thái gốc của Graphics2D
        screenY = worldY - gp.player.worldY + gp.player.screenY;
        screenX = worldX - gp.player.worldX + gp.player.screenX;
        BufferedImage image = animations.get(aniCount).get(spriteNum < animations.get(aniCount).size() ? spriteNum : animations.get(aniCount).size() - 1);
        // Tạo hình ảnh
        if(red) {
            image = makeSpriteRed(animations.get(aniCount).get(spriteNum < animations.get(aniCount).size() ? spriteNum : animations.get(aniCount).size() - 1));
        }
        // Lưu lại trạng thái ban đầu của Graphics2D
        AffineTransform old = g2.getTransform();

        // Kích thước của hình ảnh
        int imageWidth = (int)(image.getWidth()*2)*2;
        int imageHeight = (int)(image.getHeight()*2)*2;

        // Tính toán chính xác tâm của hình ảnh
        int centerX = screenX + imageWidth / 2 ;
        int centerY = screenY + imageHeight / 2;

        // Dịch hệ tọa độ đến tâm của vật thể (tâm của hình ảnh)
        g2.translate(centerX, centerY);

        // Xoay hệ tọa độ quanh tâm của hình ảnh

        // Lật hình ảnh theo trục Ox (nếu cần)
        if(flip) g2.scale(-1, 1);

        // Vẽ hình ảnh đã xoay và lật, với tọa độ được điều chỉnh để đúng vị trí
        g2.drawImage(image, -imageWidth / 2, -imageHeight / 2, (int)(imageWidth) , (int)(imageHeight), null);

        // Khôi phục lại trạng thái ban đầu của Graphics2D
        g2.setTransform(old);

        // Vẽ khung hình chữ nhật (nếu cần)
        rectDraw(g2);

    }
    @Override
    public void drawUI(Graphics2D g2, GamePanel gp){
        int dialogueBoxHeight = gp.tileSize * 2;
        int dialogueBoxY = gp.screenHeight - dialogueBoxHeight - 10;
        int dialogueBoxX = 20;
        int dialogueBoxWidth = gp.screenWidth - 40;

        int textX = dialogueBoxX + 20;
        int textY = dialogueBoxY + 40;
        if (!gp.player.combat && !words.isEmpty() && startTalk) {
            // Vẽ khung hội thoại
            g2.setColor(new Color(0, 0, 0, 180));
            g2.fillRoundRect(dialogueBoxX, dialogueBoxY, dialogueBoxWidth, dialogueBoxHeight, 25, 25);
            g2.setColor(Color.WHITE);
            g2.drawRoundRect(dialogueBoxX, dialogueBoxY, dialogueBoxWidth, dialogueBoxHeight, 25, 25);
            String currentDialogue = words.get(index).get(dialogueIndex);

            // Khi đến đoạn hội thoại yêu cầu lựa chọn

            if ((gp.keyH.SpacePressed || gp.mouseH.isClicked) && dialogueIndex < words.get(index).size() - 1 && timer <= 0) {
                dialogueIndex++; // Chuyển sang đoạn tiếp theo
                gp.keyH.SpacePressed = false;
                timer = 20;
            }
            if (dialogueIndex >= words.get(index).size() - 1) {
                startTalk = false;
                gp.player.combat = true;
                gp.keyH.SpacePressed = false;
                awake = true;
            }
            g2.setFont(font);
            g2.setColor(Color.WHITE);
            g2.drawString(objName + ": " + currentDialogue, textX, textY);
        } else {
            if (startTalk) {
                gp.player.combat = false;
                gp.keyH.SpacePressed = false;
                timer = 20;
            }
        }
        if (awake && !dead) {
            // Vẽ thanh máu của boss ngay dưới khung hội thoại
            int healthBarWidth = dialogueBoxWidth - 40; // Chiều dài thanh máu
            int healthBarHeight = 20; // Chiều cao thanh máu
            int healthBarX = dialogueBoxX;
            int healthBarY = dialogueBoxY + dialogueBoxHeight - 40; // Vị trí ngay dưới khung chat

            float healthPercentage = (float) HP / 35; // Tính phần trăm máu
            int filledWidth = (int) (healthBarWidth * healthPercentage);

            // Vẽ khung thanh máu
            g2.setColor(Color.BLACK);
            g2.fillRect(healthBarX, healthBarY, healthBarWidth, healthBarHeight);

            // Vẽ thanh máu dựa trên phần trăm máu còn lại
            g2.setColor(Color.RED);
            g2.fillRect(healthBarX, healthBarY, filledWidth, healthBarHeight);

            // Vẽ viền cho thanh máu
            g2.setColor(Color.WHITE);
            g2.drawRect(healthBarX, healthBarY, healthBarWidth, healthBarHeight);
            g2.setFont(font);
            g2.drawString("Giám ngục Dreadveil", healthBarX, healthBarY - 20);
        }
    }
    boolean dead = false;
    @Override
    public void onTriggerEnter(Entity entity){
        if(map.containsKey(entity.objName) && delayTime <= 0){
            awake = true;
            delayTime = 10;
            HP-= map.get(entity.objName);
            isHurt = true;
            if(HP <= 0){
                dead = true;
                CommonItem s = new CommonItem("CrystalFragment3", gp);
                s.worldX = 28*gp.tileSize;
                s.worldY = 28*gp.tileSize;
                gp.obj.add(s);
                Effect b = new Effect("/effect/effect1.png", 0, 0, s.worldX, s.worldY, 10, gp, 0, 2, 2, 0, 0);
                gp.obj.add(b);
                gp.soundManager.stop("combat3");
                gp.soundManager.loop("map22");
                int tmp = 25;
                for(int i = 0;i< tmp;i++){
                    Coin coin = new Coin(this.worldX + new Random().nextInt(4*gp.tileSize), this.worldY + new Random().nextInt(4*gp.tileSize), gp);
                    gp.obj.add(coin);
                }
            }
        }
    }
}
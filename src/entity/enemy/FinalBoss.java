package entity.enemy;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

import entity.Entity;
// import entity.Items.Coin;
// import entity.npc.CombatWall;
// import entity.player.Quest;
import entity.bullet.*;
import entity.effect.Effect;
import main.FontLoader;
import main.GamePanel;


public class FinalBoss extends Entity {
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
    boolean phase2 = false;
    HashMap<String, Integer> map = new HashMap<String, Integer>();
    Font font =  FontLoader.loadFont("/UI/SVN-Determination Sans.otf",20);

    public FinalBoss(GamePanel gp) {
        layer = 2;
        objName = "Fami";
        collision = true;
        direction = "down";
        HP = 75;
        speed = 4;
        isTrigger = true;
        this.gp = gp;
        rectGet(250, 250, 48*2, 52*2);
        getImage();
        aniCount = 1;
        map.put("bullet", 1);
        map.put("Bigbullet", 3);
        isEnemy = true;
        addWords(new String[]{"Must","Protect","Her","end"});
        isBoss = true;
    }
    public void addWords(String[] inputWords) {
        words.add(new Vector<>());
        words.get(words.size() - 1).addAll(List.of(inputWords));
    }
    public void getImage() {
        importAndSlice("/boss/FinalBoss/Idle.png", 8, 0,0);
        importAndSlice("/boss/FinalBoss/Attack.png", 8, 0,0);
        importAndSlice("/boss/FinalBoss/Death.png", 5, 0, 0);
    }
    int shootCounter = 0;
    boolean isResting = false;
    int restCounter = 160;
    boolean startTalk = false;
    boolean done = false;
    @Override
    public void update() {
        int npcCenterX = worldX + solidArea.x + solidArea.width / 2;
        int npcCenterY = worldY + solidArea.y + solidArea.height / 2;
        spriteCounter++;
        if (spriteCounter > animationDelay) {
            spriteNum++;
            if (spriteNum >= animations.get(aniCount).size()) {
                if(!dead && !phase2) spriteNum = 0;
                else spriteNum = animations.get(aniCount).size() - 1;
            }
            spriteCounter = 0;
        }

        timer--;
        if(!dead && !phase2){
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
                gp.soundManager.stop("map4");
                gp.soundManager.loop("combat4");
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
                        selectNextMoveSet();
                    }
                    animationDelay = 7;
                } else {
                    animationDelay = 3;
                    // Giai đoạn thực hiện hành động
                    executeMoveSet(npcCenterX, npcCenterY, playerCenterX, playerCenterY);
                }
            } else {
                aniCount = 0;
                animationDelay = 7;
            }

            collisionOn = false;
            delayTime--;
            delayMove--;
        }
        else{
            if(aniCount != 2) spriteNum = 0;
            aniCount = 2;
            if (phase2){
                phaseCount++;
                if(phaseCount%10 == 0){
                    int bulletCount = 1 + (int) (Math.random() * 5); // Số đạn từ 1 đến 5

                    for (int i = 0; i < bulletCount; i++) {
                        // Góc ngẫu nhiên từ 0 đến 360 độ
                        double angle = Math.toRadians(Math.random() * 360);

                        // Tính toán tọa độ mục tiêu cho từng viên đạn
                        int targetBulletX = (int) (npcCenterX + 8*Math.cos(angle) * gp.tileSize);
                        int targetBulletY = (int) (npcCenterY + 8*Math.sin(angle) * gp.tileSize);

                        Effect a = new Effect(null, 16,16,targetBulletX ,targetBulletY,40, gp, 8, 2,2, npcCenterX, npcCenterY);
                        gp.obj.add(a);
                    }
                }
                if(phaseCount >= 400){
                    HP++;
                    if(HP >= 75){
                        HP = 75;
                        int bulletCount = 18; // Ví dụ: 5 viên đạn
                        double angleStep = Math.toRadians(360.0 / (bulletCount - 1)); // Bước góc (-180 đến 0 độ)

                        // Bắn từng luồng đạn
                        for (int i = 0; i < bulletCount; i++) {
                            // Góc hiện tại của từng viên đạn
                            double angle = Math.toRadians(180) + i * angleStep;

                            // Tính toán tọa độ mục tiêu cho từng viên đạn
                            int targetBulletX = (int) (npcCenterX + Math.cos(angle) * 12 * gp.tileSize); // Khoảng cách từ tâm là 25
                            int targetBulletY = (int) (npcCenterY + Math.sin(angle) * 12 * gp.tileSize);

                            // Tạo đối tượng đạn
                            FireBullet b = new FireBullet("/bullet/Red Effect Bullet Impact Explosion 32x32.png", "enemyBullet",
                                    0, 0, 8 * 6, 8 * 6,
                                    (int) (npcCenterX), (int) (npcCenterY), 10, gp,
                                    0, 8, 1, 1,
                                    targetBulletX, targetBulletY, null);
                            b.root = this.objName;

                            // Thêm đạn vào danh sách đối tượng
                            gp.obj.add(b);
                        }
                        FireBullet b = new FireBullet("/bullet/Red Effect Bullet Impact Explosion 32x32.png", "enemyBullet", 12, 12, 8, 8, 14*gp.tileSize, 14*gp.tileSize, 165, gp, 0, 10, 1, 1, 14*gp.tileSize, 15*gp.tileSize, null);
                        b.die = false;
                        b.isSlash = true;
                        gp.obj.add(b);
                        FireBullet c = new FireBullet("/bullet/Red Effect Bullet Impact Explosion 32x32.png", "enemyBullet", 12, 12, 8, 8, 47*gp.tileSize, 14*gp.tileSize, 165, gp, 0, 10, 1, 1, 47* gp.tileSize,15*gp.tileSize, null);
                        c.die = false;
                        c.isSlash = true;
                        gp.obj.add(c);
                        double angle = random.nextDouble(Math.toRadians(360));
                        Effect a = new Effect("/effect/enemyEffect .png", 0, 0, npcCenterX + (int)(4*gp.tileSize* Math.cos(angle)), npcCenterY + (int)(4*gp.tileSize* Math.cos(angle)), 15, gp, 0, 2, 2, 0, 0);
                        gp.obj.add(a);
                        Ghost g = new Ghost(gp);
                        g.worldX = npcCenterX + (int)(4*gp.tileSize* Math.cos(angle));
                        g.worldY = npcCenterY + (int)(4*gp.tileSize* Math.cos(angle));
                        gp.obj.add(g);
                        phase2 = false;
                        isResting = false;

                    }
                }
            }
        }
    }
    int phaseCount = 0;
    private void selectNextMoveSet() {
        moveSet = new Random().nextInt(4); // Random từ 0 đến 2 (3 moveSet)
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
                moveSetFour(npcCenterX, npcCenterY, playerCenterX, playerCenterY);
                break;
        }
    }
    int delayMove = 0;
    private void moveSetZero(int npcCenterX, int npcCenterY, int playerCenterX, int playerCenterY) {
        if(delayMove <= 0){
            ThrowingObj b = new ThrowingObj(null,"enemyBullet", -100*gp.tileSize, -100*gp.tileSize,1,1, npcCenterX, npcCenterY,50,gp ,0, 7, 2, 2, targetX + (new Random().nextInt(20* gp.tileSize) - 10*gp.tileSize), targetY + (new Random().nextInt(20 * gp.tileSize) - 10*gp.tileSize));
            gp.obj.add(b);
            shootCounter++;
        }

        if (shootCounter >= 8) {
            delayMove = 0;
            startResting();
        }
    }
    Random random = new Random();
    private void moveSetOne(int npcCenterX, int npcCenterY, int playerCenterX, int playerCenterY) {
        if(delayMove <= 0){
            double angle = random.nextDouble(Math.toRadians(360));
            Effect a = new Effect("/effect/enemyEffect .png", 0, 0, npcCenterX + (int)(4*gp.tileSize* Math.cos(angle)), npcCenterY + (int)(4*gp.tileSize* Math.cos(angle)), 15, gp, 0, 2, 2, 0, 0);
            gp.obj.add(a);
            Ghost g = new Ghost(gp);
            g.worldX = npcCenterX + (int)(4*gp.tileSize* Math.cos(angle));
            g.worldY = npcCenterY + (int)(4*gp.tileSize* Math.cos(angle));
            gp.obj.add(g);
            shootCounter++;
            // Đặt lại thời gian chờ
            delayMove = 10;
        }

        if (shootCounter >= 1) {
            if(phase == 2) {
                int bulletCount = 12; // Ví dụ: 5 viên đạn
                double angleStep = Math.toRadians(360.0 / (bulletCount - 1)); // Bước góc (-180 đến 0 độ)

                // Bắn từng luồng đạn
                for (int i = 0; i < bulletCount; i++) {
                    // Góc hiện tại của từng viên đạn
                    double angle = Math.toRadians(180) + i * angleStep;

                    // Tính toán tọa độ mục tiêu cho từng viên đạn
                    int targetBulletX = (int) (npcCenterX + Math.cos(angle) * 12 * gp.tileSize); // Khoảng cách từ tâm là 25
                    int targetBulletY = (int) (npcCenterY + Math.sin(angle) * 12 * gp.tileSize);

                    // Tạo đối tượng đạn
                    FireBullet b = new FireBullet("/bullet/Red Effect Bullet Impact Explosion 32x32.png", "enemyBullet",
                            0, 0, 8 * 6, 8 * 6,
                            (int) (targetBulletX - 10*gp.tileSize), (int) (targetBulletY - 10*gp.tileSize), 10, gp,
                            0, 8, 1, 1,
                            targetBulletX, targetBulletY, null);
                    b.root = this.objName;

                    // Thêm đạn vào danh sách đối tượng
                    gp.obj.add(b);
                }

            }
            delayMove = 0;
            startResting();
        }
    }

    private void moveSetTwo(int npcCenterX, int npcCenterY, int playerCenterX, int playerCenterY) {
        if(aniCount != 1 && phase == 2){
            Effect a = new Effect("/effect/enemyEffect .png", 0, 0, npcCenterX, npcCenterY, 50, gp, 0, 2, 2, 0, 0);
            gp.obj.add(a);
            int angle = new Random().nextInt(2);
            if (angle == 1) {
                flip = true;
            } else {
                flip = false;
            }
            worldX = gp.player.worldX + (angle == 0 ? 10 * gp.tileSize : -10* gp.tileSize) - (solidArea.x + solidArea.width / 2);
            worldY = gp.player.worldY - 10 * gp.tileSize;
            Effect b = new Effect("/effect/enemyEffect .png", 0, 0, npcCenterX, npcCenterY, 50, gp, 0, 2, 2, 0, 0);
            gp.obj.add(b);
        }
        aniCount = 1;
        if(delayMove <= 0){
            Effect a = new Effect("/effect/enemyEffect .png", 0, 0, npcCenterX +  (flip ? -12-32 : 12+41), npcCenterY - 45, 20, gp, 0, 2, 2, 0, 0);
            gp.obj.add(a);
            Bullet b = new Bullet("/bullet/Red Effect Bullet Impact Explosion 32x32.png", "enemyBullet", 12,12, 8, 8, npcCenterX +  (flip ? -12-32 : 12+41), npcCenterY - 45, 65, gp, 0, 7, 1, 1, targetX + (new Random().nextInt(4 * gp.tileSize) - 2*gp.tileSize), targetY + (new Random().nextInt(4 * gp.tileSize) - 2*gp.tileSize));
            b.off = false;
            gp.obj.add(b);
            shootCounter++;
            // Đặt lại thời gian chờ
            delayMove = 5;
        }

        if (shootCounter >= 20) {
            delayMove = 0;
            startResting();
        }
    }
    private void moveSetFour(int npcCenterX, int npcCenterY, int playerCenterX, int playerCenterY){
        if(delayMove <= 0){
            Effect a = new Effect("/effect/enemyEffect .png", 0, 0, npcCenterX + (flip ? -12 - 32 : 12 + 41), npcCenterY - 45, 20, gp, 0, 2, 2, 0, 0);
            gp.obj.add(a);
            if(shootCounter == 0){
                ClusterBomb b = new ClusterBomb("/effect/Fire.png", "enemyBullet", -100 * gp.tileSize, -100 * gp.tileSize, 1, 1, npcCenterX, npcCenterY, 50, gp, 0, 7, 2, 2, targetX, targetY);
                gp.obj.add(b);
                if(phase == 2){
                    ClusterBomb clusterBomb = new ClusterBomb("/effect/Fire.png", "enemyBullet", -100 * gp.tileSize, -100 * gp.tileSize, 1, 1, npcCenterX, npcCenterY, 50, gp, 0, 7, 2, 2, targetX + gp.tileSize, targetY);
                    gp.obj.add(clusterBomb);
                }
                delayMove = 20;
                shootCounter++;
            }
            else if(shootCounter == 1){
                FireBullet b = new FireBullet("/bullet/Red Effect Bullet Impact Explosion 32x32.png", "enemyBullet", 12,12, 8, 8, npcCenterX +  (flip ? -12-32 : 12+41), npcCenterY - 45, 65, gp, 0, 10, 1, 1, targetX, targetY, null);
                gp.obj.add(b);
                if(phase == 2){
                    FireBullet c = new FireBullet("/bullet/Red Effect Bullet Impact Explosion 32x32.png", "enemyBullet", 12,12, 8, 8, npcCenterX +  (flip ? -12-32 : 12+41), npcCenterY - 45, 65, gp, 0, -10, 1, 1, targetX, targetY, null);
                    gp.obj.add(c);
                }
                delayMove = 20;
                shootCounter++;
            }
        }
        if(shootCounter >= 2){
            delayMove = 0;
            startResting();
        }
    }

    private void startResting() {
        shootCounter = 0;
        aniCount = 0;
        isResting = true;
        restCounter = 60; // Thời gian nghỉ, ví dụ 60 frame
    }


    private void move(String direction) {
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


    boolean flip = false;

    @Override
    public void draw(Graphics2D g2, GamePanel gp) {
        // Lưu trạng thái gốc của Graphics2D
        screenY = worldY - gp.player.worldY + gp.player.screenY;
        screenX = worldX - gp.player.worldX + gp.player.screenX;

        // Tạo hình ảnh
        BufferedImage image = animations.get(aniCount).get(spriteNum < animations.get(aniCount).size() ? spriteNum : animations.get(aniCount).size()-1);

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
        if(!dead){
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
            if (awake) {
                // Vẽ thanh máu của boss ngay dưới khung hội thoại
                int healthBarWidth = dialogueBoxWidth - 40; // Chiều dài thanh máu
                int healthBarHeight = 20; // Chiều cao thanh máu
                int healthBarX = dialogueBoxX;
                int healthBarY = dialogueBoxY + dialogueBoxHeight - 40; // Vị trí ngay dưới khung chat

                float healthPercentage = (float) HP / 75; // Tính phần trăm máu
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
                g2.drawString(objName, healthBarX, healthBarY - 20);
            }
        }
    }
    boolean dead = false;
    int phase = 1;
    @Override
    public void onTriggerEnter(Entity entity){
        if(map.containsKey(entity.objName) && delayTime <= 0){
            awake = true;
            delayTime = 10;
            HP-= map.get(entity.objName);
            isHurt = true;
            if(HP <= 0){
                if(phase == 2 && !phase2) {
                dead = true;
                gp.soundManager.stop("combat4");
                gp.soundManager.loop("map4");
                }
                else {
                    phase = 2;
                    phase2 = true;
                }
            }
        }
    }
}
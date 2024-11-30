package entity.enemy;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Random;
import entity.Entity;
import entity.Items.Coin;
import entity.bullet.Bullet;
// import entity.bullet.NormalBullet;
import entity.effect.Effect;
import main.GamePanel;


public class Soldier extends Entity {
    int timer = 0;
    int delayTime = 0;
    int HP;
    double angle = 0;
    int animationDelay = 3;
    private int rootX = -1, rootY = -1;
    boolean back = false;
    private int targetX, targetY;
    HashMap<String, Integer> map = new HashMap<String, Integer>();
    public Soldier(GamePanel gp) {
        layer = 2;
        objName = "Soldier";
        collision = true;
        direction = "down";
        HP = 8;
        speed = 5;
        isTrigger = false;
        this.gp = gp;
        rectGet(0, 0, 32, 48);
        getImage();
        aniCount = 1;
        map.put("bullet", 1);
        map.put("Bigbullet", 3);
        isEnemy = true;
    }
    public void getImage() {
        importAndSlice("/enemy/Soldier/Archer-idle-spritesheet.png", 3, 0,0);
        importAndSlice("/enemy/Soldier/run1.png", 3, 0,0);
        importAnImage("/enemy/Soldier/run2.png",false);
        importAndSlice("/enemy/Soldier/Archer-atk-spritesheet.png", 3, 0,0);
    }
    @Override
    public void update() {
        spriteCounter++;
        if (spriteCounter > animationDelay) {
            spriteNum++;
            if (spriteNum >= animations.get(aniCount).size()) spriteNum = 0;
            spriteCounter = 0;
        }
        isTriggerOn = false;
        timer++;

        if (rootY == -1) {
            rootY = this.worldY;
            rootX = this.worldX;
        }
        // Kiểm tra khoảng cách giữa root và enemy
        double distanceToRoot = Math.sqrt(Math.pow(rootX - worldX, 2) + Math.pow(rootY - worldY, 2));
        if(back && distanceToRoot <= 0.5*gp.tileSize){
            back  =false;
        }
        if (!back) {
            targetX = gp.player.worldX;
            targetY = gp.player.worldY;
        } else {
            targetX = rootX;
            targetY = rootY;
        }

        angle = Math.atan2(targetY - worldY, targetX - worldX);
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

        //gp.cCheck.checkObjectForObj(this);

        int npcCenterX = worldX + gp.tileSize / 2;
        int npcCenterY = worldY + gp.tileSize / 2;

        int playerCenterX = targetX + gp.tileSize / 2;
        int playerCenterY = targetY + gp.tileSize / 2;

        double distanceToTarget = Math.sqrt(Math.pow(npcCenterX - playerCenterX, 2) + Math.pow(npcCenterY - playerCenterY, 2));
        gp.cCheck.checkMapObject(this);

        // Xử lý va chạm với tường và di chuyển sang hai bên nếu có va chạm
        /*if (collisionOn) {
            Random rand = new Random();
            if (rand.nextBoolean()) {
                // Thử di chuyển lên hoặc xuống khi gặp va chạm
                if (direction.contains("up")) worldY += speed;
                else if (direction.contains("down")) worldY -= speed;
            } else {
                // Thử di chuyển trái hoặc phải khi gặp va chạm
                if (direction.contains("left")) worldX += speed;
                else if (direction.contains("right")) worldX -= speed;
            }
            collisionOn = false; // Đặt lại biến để tránh va chạm lặp lại
        }*/
        if(!back) {
            if (!collisionOn && delayTime <= 0 && distanceToTarget >= 9 * gp.tileSize && awake && gp.player.alpha >= 1f) {
                if (aniCount != 1) {
                    spriteNum = 0;
                    spriteCounter = 0;
                }
                aniCount = 1;
                animationDelay = 3;
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

                timer = 50;
            } else if (distanceToTarget <= 9 * gp.tileSize && gp.player.alpha >= 1) {
                if (timer >= 50) {
                    aniCount = 2;
                    animationDelay = 10;
                    if (spriteNum == animations.get(aniCount).size() - 1) {
                        Bullet b = new Bullet("/bullet/Bow and Arrows.png", "enemyBullet", 12,12, 8, 8, this.worldX, this.worldY, 50, gp, 0, 7, 1, 1, targetX, targetY);
                        b.root = this.objName;
                        gp.obj.add(b);
                        timer = 0;
                        spriteNum = 0;
                    }
                } else if (timer <= 30) {
                    aniCount = 0;
                    animationDelay = 7;
                } else {
                    aniCount = 2;
                    animationDelay = 13;
                    spriteNum = 0;

                }
            } else {
                if (aniCount != 0) {
                    spriteNum = 0;
                    spriteCounter = 0;
                }
                aniCount = 0;
                animationDelay = 7;
                timer = 0;
            }
        }
        else{
            if (aniCount != 1) {
                spriteNum = 0;
                spriteCounter = 0;
            }
            aniCount = 1;
            animationDelay = 3;
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
        collisionOn = false;
        delayTime--;
        delayHP--;
    }
    int delayHP = 0;
    boolean flip = false;

    @Override
    public void draw(Graphics2D g2, GamePanel gp) {
        // Tọa độ để vẽ thanh máu trên đầu của Slime
        int barX = this.screenX;
        int barY = this.screenY - 10; // Thanh máu cách đầu Slime 10 pixel

        // Kích thước của thanh máu
        int barWidth = this.solidArea.width; // Chiều rộng của thanh máu bằng với kích thước của Slime
        int barHeight = 4; // Chiều cao của thanh máu

        // Tính toán phần trăm HP
        double healthPercent = (double) HP / 8.0; // HP hiện tại chia cho HP tối đa

        // Lưu trạng thái gốc của Graphics2D
        screenY = worldY - gp.player.worldY + gp.player.screenY;
        screenX = worldX - gp.player.worldX + gp.player.screenX;

        // Tạo hình ảnh
        BufferedImage image = animations.get(aniCount).get(spriteNum < animations.get(aniCount).size() ? spriteNum : animations.get(aniCount).size()-1);

        // Lưu lại trạng thái ban đầu của Graphics2D
        AffineTransform old = g2.getTransform();

        // Kích thước của hình ảnh
        int imageWidth = (int)(gp.tileSize*2);
        int imageHeight = (int)(gp.tileSize*2);

        // Tính toán chính xác tâm của hình ảnh
        int centerX = screenX + gp.tileSize / 2 ;
        int centerY = screenY + gp.tileSize / 2;

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

        // Vẽ thanh máu
        if (awake) {
            // Vẽ nền thanh máu (màu xám)
            g2.setColor(new Color(100, 100, 100));
            g2.fillRect(barX, barY, barWidth, barHeight);

            // Vẽ thanh máu (màu đỏ) với độ dài tùy thuộc vào lượng HP
            g2.setColor(Color.RED);
            g2.fillRect(barX, barY, (int)(barWidth * healthPercent), barHeight);
        }
    }

    @Override
    public void onTriggerEnter(Entity entity){

        if(map.containsKey(entity.objName) && delayHP <= 0){
            awake = true;
            delayHP = 10;
            HP-= map.get(entity.objName);
            isHurt = true;
            if(HP <= 0){
                int tmp = new Random().nextInt(3);
                for(int i = 0;i< tmp;i++){
                    Coin coin = new Coin(this.worldX + new Random().nextInt(gp.tileSize), this.worldY + new Random().nextInt(gp.tileSize), gp);
                    gp.obj.add(coin);
                }

                Effect a = new Effect("/effect/effect1.png", 0, 0, this.worldX, this.worldY, 10, gp, 0, 2, 2, 0, 0);
                gp.obj.add(a);
                gp.soundManager.play("slime_die");
                gp.player.kills++;
                gp.obj.remove(this);
            }
        }
    }
}
package entity.enemy;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Random;

import entity.Entity;
import entity.Items.Coin;
import entity.bullet.Bullet;
import entity.bullet.NormalBullet;
import entity.bullet.ThrowingObj;
import entity.effect.Effect;
import main.GamePanel;


public class Soldier extends Entity {
    int timer = 0;
    int delayTime = 0;
    int HP;
    double angle = 0;
    int animationDelay = 3;
    private int rootX, rootY;
    boolean back = false;
    HashMap<String, Integer> map = new HashMap<String, Integer>();
    public Soldier(GamePanel gp) {
        this.isTrigger = true;
        objName = "Slime";
        collision = true;
        direction = "down";
        HP = 8;
        speed = 3;
        isTrigger = false;
        this.gp = gp;
        rectGet(0, 0, 48, 48);
        getImage();
        aniCount = 1;
        map.put("bullet", 1);
        map.put("Bigbullet", 3);
        this.rootX = 23*gp.tileSize;
        this.rootY = 21*gp.tileSize;
    }
    boolean awake = false;
    public void getImage() {
        importAndSlice("/enemy/Soldier/Archer-idle-spritesheet.png", 3, 0,0);
        importAndSlice("/enemy/Soldier/run1.png", 3, 0,0);
        importAnImage("/enemy/Soldier/run2.png",false);
        importAndSlice("/enemy/Soldier/Archer-atk-spritesheet.png", 3, 0,0);
    }
    @Override
    public void update() {
        System.out.println(rootX + " " + rootY);
        spriteCounter++; // Đếm số lần cập nhật
        if(spriteCounter > animationDelay) { //
            spriteNum++; // Tăng lên để ch
            if(spriteNum >= animations.get(aniCount).size()) spriteNum = 0;
            spriteCounter = 0;
        }
        isTriggerOn = false;
        timer++;
        angle = Math.atan2(gp.player.worldY - worldY, gp.player.worldX - worldX);
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

// Kiểm tra va chạm và di chuyển
        gp.cCheck.checkTileForObj(this);
        gp.cCheck.checkObjectForObj(this);
        int npcCenterX = worldX + gp.tileSize / 2;
        int npcCenterY = worldY + gp.tileSize / 2;

        int playerCenterX = gp.player.worldX + gp.tileSize / 2;
        int playerCenterY = gp.player.worldY + gp.tileSize / 2;
        timer++;
        double distance = Math.sqrt(Math.pow(npcCenterX - playerCenterX, 2) + Math.pow(npcCenterY - playerCenterY, 2));
        if (!collisionOn && delayTime <= 0 && distance <= 15*gp.tileSize && distance >= 5*gp.tileSize) {
            if(aniCount != 1) {
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
                    worldX += (int) (speed / Math.sqrt(2)); // Di chuyển theo cả 2 trục với tỷ lệ cân đối
                    worldY -= (int) (speed / Math.sqrt(2));
                    break;
                case "up-left":
                    worldX -= (int) (speed / Math.sqrt(2));
                    worldY -= (int) (speed / Math.sqrt(2));
                    break;
                case "down-right":
                    worldX += (int) (speed / Math.sqrt(2));
                    worldY += (int) (speed / Math.sqrt(2));
                    flip = false;
                    break;
                case "down-left":
                    worldX -= (int) (speed / Math.sqrt(2));
                    worldY += (int) (speed / Math.sqrt(2));
                    break;
            }
            timer = 0;
        }

        else if(distance <= 5*gp.tileSize && gp.player.alpha >= 1){
            if(timer >= 50) {
                aniCount = 2;
                animationDelay = 10;
                if(spriteNum == animations.get(aniCount).size()-1) {
                    NormalBullet b = new NormalBullet(null,"enemyBullet", 20, 20,4,4, this.worldX, this.worldY,50,gp ,0, 7, 2, 2, gp.player.worldX, gp.player.worldY);                    b.root = this.objName;
                    gp.obj.add(b);
                    timer = 0;
                    spriteNum = 0;
                }
            }
            else if(timer <= 30){
                aniCount = 0;
                animationDelay = 7;
            }
            else {
                aniCount = 2;
                animationDelay = 13;
                spriteNum = 0;

            }
        }
        else{
            if(aniCount != 0) {
                spriteNum = 0;
                spriteCounter = 0;
            }
            aniCount = 0;
            animationDelay = 7;
            timer = 0;
        }
        collisionOn = false;
        delayTime--;
    }
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
        int imageWidth = (int)(gp.tileSize*1.5);
        int imageHeight = (int)(gp.tileSize*1.5);

        // Tính toán chính xác tâm của hình ảnh
        int centerX = screenX + gp.tileSize / 2 ;
        int centerY = screenY + gp.tileSize / 2;

        // Dịch hệ tọa độ đến tâm của vật thể (tâm của hình ảnh)
        g2.translate(centerX, centerY);

        // Xoay hệ tọa độ quanh tâm của hình ảnh

        // Lật hình ảnh theo trục Ox (nếu cần)
        if(flip) g2.scale(-1, 1);

        // Vẽ hình ảnh đã xoay và lật, với tọa độ được điều chỉnh để đúng vị trí
        g2.drawImage(image, -imageWidth / 2, -imageHeight / 2, (int)(imageWidth), (int)(imageHeight), null);

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

        if(map.containsKey(entity.objName) && delayTime <= 0){
            awake = true;
            System.out.println(entity.objName);
            delayTime = 10;
            HP-= map.get(entity.objName);
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
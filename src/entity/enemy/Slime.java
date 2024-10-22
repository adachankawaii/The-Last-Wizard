package entity.enemy;

import java.awt.*;
import java.util.HashMap;
import java.util.Random;

import entity.Entity;
import entity.Items.Coin;
import entity.bullet.Bullet;
import entity.effect.Effect;
import main.GamePanel;

import javax.crypto.EncryptedPrivateKeyInfo;

public class Slime extends Entity {
    int timer = 0;
    int delayTime = 0;
    int HP;
    HashMap<String, Integer> map = new HashMap<String, Integer>();
    public Slime(GamePanel gp) {
        this.isTrigger = true;
        objName = "Slime";
        collision = true;
        direction = "down";
        HP = 8;
        this.gp = gp;
        rectGet(0, 0, 48, 48);
        getSlimeImage();
        map.put("bullet", 1);
        map.put("Bigbullet", 3);
    }
    boolean awake = false;
    public void getSlimeImage() {

        // CHUYỂN ĐỘNG IDLE CỦA SLIME.
        // [1] - IDLE
        importEachImage(new String[]{"/enemy/slime_0.png",
                "/enemy/slime_1.png","/enemy/slime_2.png",
                "/enemy/slime_3.png", "/enemy/slime_4.png",
                "/enemy/slime_5.png", "/enemy/slime_6.png"}, true);

        // [2] - BỊ TẤN CÔNG

        // [3] - TẤN CÔNG

        // [4] - DIE
    }

    @Override
    public void update() {
        spriteCounter++; // Đếm số lần cập nhật
        if(spriteCounter > 7) { //
            spriteNum++; // Tăng lên để ch
            if(spriteNum >= animations.get(aniCount).size()) spriteNum = 0;
            spriteCounter = 0;
        }
        isTriggerOn = false;
        timer++;
        if(timer >= 60 && Math.abs((gp.player.worldX-this.worldX)/gp.tileSize) <= 7 && Math.abs((gp.player.worldY-this.worldY)/gp.tileSize) <= 7 && gp.player.alpha >= 1){
            Bullet b = new Bullet("/bullet/bullet.png","enemyBullet", 20, 20,4,4, this.worldX, this.worldY,20,gp ,0, 5, 2, 2, gp.player.worldX, gp.player.worldY);
            b.death = false;
            b.root = this.objName;
            gp.obj.add(b);
            timer = 0;
        }
        delayTime--;
    }

    @Override
    public void draw(Graphics2D g2, GamePanel gp) {
        drawObjImage(g2, gp);
        rectDraw(g2);

        // Tọa độ để vẽ thanh máu trên đầu của Slime
        int barX = this.screenX;
        int barY = this.screenY - 10; // Thanh máu cách đầu Slime 10 pixel

        // Kích thước của thanh máu
        int barWidth = this.solidArea.width; // Chiều rộng của thanh máu bằng với kích thước của Slime
        int barHeight = 4; // Chiều cao của thanh máu

        // Tính toán phần trăm HP
        double healthPercent = (double) HP / 8.0; // HP hiện tại chia cho HP tối đa
        if(awake){
            g2.setColor(new Color(100,100,100));
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
                gp.obj.remove(this);
            }
        }
    }
}
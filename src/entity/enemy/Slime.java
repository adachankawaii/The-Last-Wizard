package entity.enemy;

import java.awt.Graphics2D;
import entity.Entity;
import entity.bullet.Bullet;
import entity.bullet.NormalBullet;
import entity.bullet.ThrowingObj;
import main.GamePanel;

public class Slime extends Entity {
    int timer = 0;
    public Slime(GamePanel gp) {
        this.isTrigger = true;
        objName = "Slime";
        collision = true;
        this.gp = gp;
        rectGet(0, 0, 48, 48);
        getSlimeImage();
    }

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
    }

    @Override
    public void draw(Graphics2D g2, GamePanel gp) {
        drawObjImage(g2, gp);
        rectDraw(g2);
    }
}

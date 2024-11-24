package entity.enemy;

import entity.Entity;
import entity.bullet.Bullet;
import entity.effect.Effect;
import main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.Vector;

public class Tower extends Entity {
    public Tower(GamePanel gp) {
        layer = 2;
        objName = "Tower";
        collision = true;
        this.isTrigger = false;
        this.gp = gp;
        getNPCImage();
        rectGet(8,8,gp.tileSize,gp.tileSize);
    }
    public void getNPCImage() {
        importAndSlice("/enemy/Tower 05 - Level 03 - Weapon.png",8,0,0);
    }
    boolean shoot = false;
    @Override
    public void update(){
        spriteCounter++; // Đếm số lần cập nhật
        int npcCenterX = worldX + gp.tileSize / 2;
        int npcCenterY = worldY + gp.tileSize / 2;

        int playerCenterX = gp.player.worldX + gp.tileSize / 2;
        int playerCenterY = gp.player.worldY + gp.tileSize / 2;

        double distanceToTarget = Math.sqrt(Math.pow(npcCenterX - playerCenterX, 2) + Math.pow(npcCenterY - playerCenterY, 2));
        if (spriteCounter > 3) {
            spriteNum++; // Tăng lên để chuyển đổi giữa các khung hình
            if (spriteNum >= animations.get(aniCount).size()-1) {
                if(shoot && distanceToTarget <= 8*gp.tileSize) {
                    Effect a = new Effect("/effect/Red Effect Bullet Impact Explosion 32x32.png", 0, 0, worldX, worldY - gp.tileSize, 8, gp, 0, 2, 2, gp.player.worldX, gp.player.worldY);
                    gp.obj.add(a);
                    Bullet b = new Bullet("/bullet/Red Effect Bullet Impact Explosion 32x32.png", "enemyBullet", 12, 12, 8, 8, worldX, worldY - gp.tileSize, 50, gp, 0, 7, 1, 1, gp.player.worldX, gp.player.worldY);
                    b.death = false;
                    gp.obj.add(b);
                }
                else {
                    shoot = true;
                }
                spriteNum = 0;
            }
            spriteCounter = 0;
        }
    }
    @Override
    public void draw(Graphics2D g2, GamePanel gp) {
        // Tính tọa độ gốc để vẽ sprite dựa trên vị trí trong thế giới
        screenX = worldX - gp.player.worldX + gp.player.screenX;
        screenY = worldY - gp.player.worldY + gp.player.screenY;

        if(worldX + gp.tileSize > gp.player.worldX - gp.player.screenX
                && worldX - gp.tileSize < gp.player.worldX + gp.player.screenX
                && worldY + gp.tileSize > gp.player.worldY - gp.player.screenY
                && worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
            BufferedImage image = animations.get(aniCount).get(spriteNum);
            g2.drawImage(image, screenX - gp.tileSize* 2 - 8, screenY - gp.tileSize* 3, gp.tileSize* 6, gp.tileSize* 6, null);
        }
        rectDraw(g2);
    }

}

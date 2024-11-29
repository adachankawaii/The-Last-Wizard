package entity.Items;

import entity.Entity;
import entity.bullet.Bullet;
import entity.effect.Effect;
import entity.npc.Portal;
import main.FontLoader;
import main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

public class Pole extends Entity {
    int count = 1;
    int delayTime = 0;
    HashMap<String ,Integer> map = new HashMap<>();
    int target = 1;
    Font bigFont = FontLoader.loadFont("/UI/SVN-Determination Sans.otf", 20);
    public Pole(GamePanel gp, String objname, int target, int worldX, int worldY) {
        layer = 2;
        objName = objname;
        collision = true;
        this.isTrigger = false;
        this.gp = gp;
        getNPCImage();
        rectGet(8,0,gp.tileSize,gp.tileSize);
        map.put("bullet", 1);
        map.put("Bigbullet", 1);
        this.target = target;
        this.worldX = worldX*gp.tileSize;
        this.worldY = worldY*gp.tileSize;
    }
    public void getNPCImage() {
        importAndSlice("/enemy/Tower 05 - Level 03 - Weapon.png",8,0,0);
    }
    @Override
    public void update(){
        spriteCounter++; // Đếm số lần cập nhật
        if (spriteCounter > 3) {
            spriteNum++; // Tăng lên để chuyển đổi giữa các khung hình
            if (spriteNum >= animations.get(aniCount).size()-1) {
                spriteNum = 0;
            }
            spriteCounter = 0;
        }
        delayTime--;
        done = count == target;
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
    @Override
    public void drawUI(Graphics2D g2, GamePanel gp){
        g2.setFont(bigFont);
        int textWidth = g2.getFontMetrics(bigFont).stringWidth(objName);
        screenX = worldX - gp.player.worldX + gp.player.screenX + this.solidArea.width / 2 - textWidth / 2;
        screenY = worldY - gp.player.worldY + gp.player.screenY - 5;
        g2.setColor(Color.WHITE);
        g2.drawString("" + count, screenX, screenY);
    }
    @Override
    public void onTriggerEnter(Entity entity){
        if(map.containsKey(entity.objName) && delayTime <= 0){
            delayTime = 10;
            if(Objects.equals(objName, "Pole")) {
                count += map.get(entity.objName);
                if (count > 9) {
                    count = 1;
                }
            }
        }
    }
}

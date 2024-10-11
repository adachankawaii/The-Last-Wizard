package entity.npc;

import java.awt.Graphics2D;

import entity.Entity;
import main.GamePanel;

public class NPC extends Entity {

    GamePanel gp;
    public NPC() {
        objName = "HP potion";
        collision = true;
        this.isTrigger = true;
        rectGet(0, 0, 48, 48);
        getNPCImage();
    }

    public void getNPCImage() {

        // IMPORT NPC
        importAnImage("/bullet/HP potion.png", true);
    }

    @Override
    public void update() {
        spriteCounter++; // Đếm số lần cập nhật
        if(spriteCounter > 5) { // 
            spriteNum++; // Tăng lên để ch
            if(spriteNum >= animations.get(aniCount).size()) spriteNum = 0;
            spriteCounter = 0;
        }       
    }

    @Override
    public void draw(Graphics2D g2, GamePanel gp) {
        drawObjImage(g2, gp);
        rectDraw(g2);
    }

}

package entity.Items;

import entity.Entity;
import entity.bullet.ThrowingObj;
import main.GamePanel;

import java.awt.*;

public class ThrowingBottle extends Entity {
    public ThrowingBottle(GamePanel gp) {
        objName = "ThrowingBottle";
        collision = true;
        this.isTrigger = true;
        rectGet(0, 0, 48, 48);
        getNPCImage();
        isItem = true;
        this.gp = gp;
    }

    public void getNPCImage() {

        // IMPORT NPC
        importAnImage("/bullet/Throwing potion.png", true);
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
    @Override
    public void effect(){
        ThrowingObj b = new ThrowingObj(null,"Bigbullet", 20, 20,1,1, gp.player.worldX, gp.player.worldY,30,gp ,0, 7, 4, 4, gp.mouseX, gp.mouseY);
        gp.obj.add(b);
    }
}

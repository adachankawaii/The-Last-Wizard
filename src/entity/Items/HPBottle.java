package entity.Items;

import entity.Entity;
import entity.bullet.ThrowingObj;
import main.GamePanel;

import java.awt.*;
import java.util.Objects;

public class HPBottle extends Entity {
    public HPBottle(String name) {
        objName = name;
        collision = true;
        this.isTrigger = true;
        rectGet(0, 0, 48, 48);
        getNPCImage();
        isItem = true;
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
    @Override
    public void effect(){
        if(Objects.equals(objName, "HPBottle")) gp.player.HP += 4;
        else if (Objects.equals(objName, "InvisiblePotion")) {
            gp.player.alpha = 0.5f;
        }
    }
}

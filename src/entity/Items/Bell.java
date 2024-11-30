package entity.Items;

import entity.Entity;
import entity.effect.Effect;
import main.GamePanel;

import java.awt.*;

public class Bell extends Entity {
    public Bell(GamePanel gp) {
        objName = "Bell";
        collision = true;
        this.isTrigger = true;
        rectGet(0, 0, 48, 48);
        getNPCImage();
        isItem = true;
        this.gp = gp;
    }

    public void getNPCImage() {
        importAnImage("/item/Bell.png", true);
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
        if(!gp.player.call){
            Crow crow = new Crow(gp);
            crow.worldX = gp.player.worldX;
            crow.worldY = gp.player.worldY;
            Effect a = new Effect("/effect/effect1.png", 0, 0, crow.worldX, crow.worldY, 10, gp, 0, 2, 2, 0, 0);
            gp.obj.add(a);
            gp.obj.add(crow);
            gp.player.call = true;
            gp.player.Energy -= 50;
        }
    }

}

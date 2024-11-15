package entity.npc;

import entity.Entity;
import main.GamePanel;

import java.awt.*;
import java.util.Objects;

public class CombatWall extends Entity {
    public CombatWall(GamePanel gp, int width, int height) {
        layer = 0;
        objName = "CombatWall";
        collision = true;
        this.isTrigger = true;
        this.gp = gp;
        rectGet(0, 0, gp.tileSize * width, gp.tileSize * height);
        on = false;
        getNPCImage();
    }
    public void getNPCImage() {
        importAnImage("/npc/merchant.png", true);
    }
    @Override
    public void update(){
        if(on){
            isTrigger = false;
        }
        else {
            isTrigger = true;
        }
    }
    @Override
    public void draw(Graphics2D g2, GamePanel gp) {
        if(on) {
            g2.setColor(new Color(0, 150, 255, 100));
            g2.fillRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);
            drawObjImage(g2, gp);
            rectDraw(g2);
        }
    }
}

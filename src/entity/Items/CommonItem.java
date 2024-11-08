package entity.Items;

import entity.Entity;
import main.GamePanel;

import java.awt.*;

public class CommonItem extends Entity {
    GamePanel gp;
    String path;
    public CommonItem(String name, GamePanel gp) {
        objName = name;
        collision = true;
        this.isTrigger = true;
        rectGet(0, 0, 48, 48);
        getNPCImage(path);
        this.gp = gp;
        isItem = true;
    }

    public void getNPCImage(String path) {
        switch (objName) {
            case "Key":
                path = "/item/key.png";
                break;
            default:
                break;
        }
        // IMPORT NPC
        importAnImage(path, true);
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
        CommonItem commonItem = new CommonItem(this.objName, gp);
        commonItem.worldX = gp.player.worldX;
        commonItem.worldY = gp.player.worldY;
        gp.obj.add(commonItem);
    }
}


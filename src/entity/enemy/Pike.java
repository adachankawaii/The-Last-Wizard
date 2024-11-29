package entity.enemy;

import entity.Entity;
import main.GamePanel;

import java.awt.*;
// import java.awt.image.BufferedImage;
// import java.util.Vector;

public class Pike extends Entity{
    int timer = 0;
    public boolean canDeath = false;
    public Pike(GamePanel gp){
        layer = 0;
        objName = "enemyBullet";
        collision = true;
        this.isTrigger = true;
        this.gp = gp;
        rectGet(8, 8, gp.tileSize, gp.tileSize);
        getNPCImage();
        direction = "down";
    }
    public void getNPCImage() {
        importAndSlice("/effect/Fire.png", 4, 0,0);
    }
    @Override
    public void update(){
        if (spriteCounter > 4) {
            spriteNum++;
            if (spriteNum >= animations.get(aniCount).size()) spriteNum = 0;
            spriteCounter = 0;
        }
        spriteCounter++;
        timer++;
        if(timer >= 300 && canDeath){
            gp.obj.remove(this);
        }
    }
    @Override
    public void draw(Graphics2D g2, GamePanel gp){
        drawObjImage(g2, gp);
        rectDraw(g2);
    }

}

package entity.Items;

import entity.Entity;
import entity.effect.Effect;
import main.FontLoader;
import main.GamePanel;

import java.awt.*;
import java.util.Objects;

public class Chest extends Entity {
    public Chest(GamePanel gp) {
        objName = "Chest";
        collision = true;
        this.isTrigger = true;
        rectGet(0, 0, 48, 48);
        getNPCImage();
        isItem = true;
        this.gp = gp;
        layer = 0;
    }

    public void getNPCImage() {
        importAnImage("/mapobj/map3/Treasure.png", true);
    }
    int timer = 0;
    @Override
    public void draw(Graphics2D g2, GamePanel gp) {
        drawObjImage(g2, gp);
        rectDraw(g2);
    }
    @Override
    public void update() {
        spriteCounter++; // Đếm số lần cập nhật
        if(spriteCounter > 5) { //
            spriteNum++; // Tăng lên để ch
            if(spriteNum >= animations.get(aniCount).size()) spriteNum = 0;
            spriteCounter = 0;
        }
        timer--;
    }
    Font bigFont = FontLoader.loadFont("/UI/SVN-Determination Sans.otf", 20);
    @Override
    public void drawUI(Graphics2D g2, GamePanel gp){
        int npcCenterX = worldX + gp.tileSize / 2;
        int npcCenterY = worldY + gp.tileSize / 2;

        int playerCenterX = gp.player.worldX + gp.tileSize / 2;
        int playerCenterY = gp.player.worldY + gp.tileSize / 2;

        double distance = Math.sqrt(Math.pow(npcCenterX - playerCenterX, 2) + Math.pow(npcCenterY - playerCenterY, 2));
        if(distance <= 1.5*gp.tileSize) {
            String prompt = "Press Space to interact";
            int promptWidth = g2.getFontMetrics(bigFont).stringWidth(prompt);
            int promptX = (gp.screenWidth - promptWidth) / 2;
            int promptY = gp.screenHeight - gp.tileSize - 40;

            int boxWidth = promptWidth + 20;
            int boxHeight = 40;
            int boxX = promptX - 10;
            int boxY = promptY - 30;

            g2.setColor(new Color(0, 0, 0, 180));
            g2.fillRoundRect(boxX, boxY, boxWidth, boxHeight, 15, 15);
            g2.setColor(Color.WHITE);
            g2.drawRoundRect(boxX, boxY, boxWidth, boxHeight, 15, 15);
            g2.setFont(bigFont);
            g2.setColor(Color.WHITE);
            g2.drawString(prompt, promptX, promptY);

            if ((gp.keyH.SpacePressed) && timer <= 0) {
                Skill skill = new Skill(gp, "MagicBook");
                skill.worldX = this.worldX + gp.tileSize;
                skill.worldY = this.worldY + gp.tileSize;
                Effect a = new Effect("/effect/effect1.png", 0, 0, this.worldX, this.worldY, 10, gp, 0, 2, 2, 0, 0);
                gp.obj.add(a);
                gp.obj.add(skill);
                gp.obj.remove(this);
            }
        }
    }


}

package object;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import main.GamePanel;

public class OBJ_Slime extends SuperObject{
    public OBJ_Slime() {
        name = "Slime";
        getPlayerImage();
    }
    public void getPlayerImage() {
        importEachImage(new String[]{"/enemy/frame_0.png",
        "/enemy/frame_1.png","/enemy/frame_2.png",
        "/enemy/frame_3.png", "/enemy/frame_4.png",
        "/enemy/frame_5.png", "/enemy/frame_6.png"}, true);
    }

    public void update() {
        spriteCounter++;
        if(spriteCounter > 5) {
            spriteNum++;
            if(spriteNum >= animations.get(aniCount).size()) spriteNum = 0;
            spriteCounter = 0;
        }
    }

    public void draw(Graphics2D g2d, GamePanel gp) {
            int screenX = worldX - gp.player.worldX + gp.player.screenX;
            int screenY = worldY - gp.player.worldY + gp.player.screenY;
        
            if(worldX + gp.titleSize > gp.player.worldX - gp.player.screenX
            && worldX - gp.titleSize < gp.player.worldX + gp.player.screenX
            && worldY + gp.titleSize > gp.player.worldY - gp.player.screenY
            && worldY - gp.titleSize < gp.player.worldY + gp.player.screenY) {
                BufferedImage image = animations.get(aniCount).get(spriteNum);
                g2d.drawImage(image, screenX, screenY, gp.titleSize, gp.titleSize, null);
            }
    }
}

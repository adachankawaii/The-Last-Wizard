package entity.enemy;

import java.awt.Graphics2D;
import entity.Entity;
import main.GamePanel;

public class Slime extends Entity {
    public Slime() {

        objName = "Slime";
        collision = true;

        rectGet(0, 0, 48, 48);
        getSlimeImage();
    }

    public void getSlimeImage() {

        // CHUYỂN ĐỘNG IDLE CỦA SLIME.
        // [1] - IDLE
        importEachImage(new String[]{"/enemy/slime_0.png",
        "/enemy/slime_1.png","/enemy/slime_2.png",
        "/enemy/slime_3.png", "/enemy/slime_4.png",
        "/enemy/slime_5.png", "/enemy/slime_6.png"}, true);

        // [2] - BỊ TẤN CÔNG

        // [3] - TẤN CÔNG

        // [4] - DIE
    }

    @Override
    public void update() {
        spriteCounter++; // Đếm số lần cập nhật
        if(spriteCounter > 7) { // 
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

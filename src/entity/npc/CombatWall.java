package entity.npc;

import entity.Entity;
import main.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;
import java.util.Vector;

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
        BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();


        int b = 255; // Giá trị ngẫu nhiên cho Blue

        Color randomColor = new Color(0, 250, b, 1); // Tạo màu ngẫu nhiên từ RGB
        g2d.setColor(randomColor);
        g2d.fillRect(0, 0, 4, 4); // Vẽ hình chữ nhật đỏ kích thước 100x100

        // Giải phóng đối tượng Graphics2D sau khi vẽ
        g2d.dispose();
        Vector<BufferedImage> tmp = new Vector<>();
        tmp.add(image);
        animations.add(tmp);
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

package entity.npc;

import entity.Entity;
// import entity.effect.Effect;
import main.GamePanel;

// import java.awt.*;
// import java.awt.image.BufferedImage;
// import java.util.Objects;
// import java.util.Random;
// import java.util.Vector;

public class CombatWall extends Entity {
    public CombatWall(GamePanel gp, int width, int height) {
        layer = 0;
        objName = "CombatWall";
        collision = true;
        this.isTrigger = true;
        this.gp = gp;
        rectGet(0, 0, gp.tileSize * width, gp.tileSize * height);
        on = false;
        // getNPCImage();
    }
    // public void getNPCImage() {
    //     BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
    //     Graphics2D g2d = image.createGraphics();


    //     int b = 255; // Giá trị ngẫu nhiên cho Blue

    //     Color randomColor = new Color(0, 250, b, 0); // Tạo màu ngẫu nhiên từ RGB
    //     g2d.setColor(randomColor);
    //     g2d.fillRect(0, 0, 4, 4); // Vẽ hình chữ nhật đỏ kích thước 100x100

    //     // Giải phóng đối tượng Graphics2D sau khi vẽ
    //     g2d.dispose();
    //     Vector<BufferedImage> tmp = new Vector<>();
    //     tmp.add(image);
    //     animations.add(tmp);
    // }
    @Override
    public void update(){
        if(on){
            isTrigger = false;
        }
        else {
            isTrigger = true;
        }
    }
    // Random random = new Random();
    // @Override
    // public void draw(Graphics2D g2, GamePanel gp) {
    //     if(on) {
    //         Effect glowEffect = new Effect(null, 16, 16,
    //                  worldX + random.nextInt(gp.tileSize*(solidArea.width/gp.tileSize)) - gp.tileSize*(solidArea.width/gp.tileSize)/2,
    //                 this.worldY + random.nextInt(gp.tileSize*(solidArea.height/gp.tileSize)) - gp.tileSize*(solidArea.height/gp.tileSize)/2,
    //                 50, gp, 7, 1.5, 1.5, this.worldX + gp.tileSize*(solidArea.width/gp.tileSize - 1), worldY+ gp.tileSize*(solidArea.height/gp.tileSize - 1));
    //         gp.obj.add(glowEffect);
    //         Effect glowEffect2 = new Effect(null, 16, 16,
    //                  worldX + gp.tileSize*(solidArea.width/gp.tileSize-1) + random.nextInt(gp.tileSize*(solidArea.width/gp.tileSize)) - gp.tileSize*(solidArea.width/gp.tileSize)/2,
    //                 this.worldY + gp.tileSize*(solidArea.height/gp.tileSize - 1)+ random.nextInt(gp.tileSize*(solidArea.height/gp.tileSize)) - gp.tileSize*(solidArea.height/gp.tileSize)/2,
    //                 50, gp, 7, 1.5, 1.5, this.worldX, worldY);
    //         gp.obj.add(glowEffect2);
    //         drawObjImage(g2, gp);
    //         rectDraw(g2);
    //     }
    // }
}

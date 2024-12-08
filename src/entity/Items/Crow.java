package entity.Items;

import entity.Entity;
import entity.bullet.Bullet;
// import entity.bullet.NormalBullet;
import entity.bullet.NormalBullet;
import entity.effect.Effect;
import main.GamePanel;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
// import java.util.Objects;

public class Crow extends Entity {
    public int count = 0;
    int delay = 0;
    public Crow(GamePanel gp){
        layer = 2;
        objName = "Crow";
        collision = false;
        direction = "down";
        isTrigger = false;
        this.gp = gp;
        rectGet(-8, -8, 1, 1);
        getImage();

        // Bắn từng luồng đạn

        }
    void getImage(){
        importAndSlice("/item/crow.png", 3,0,0);
    }
    @Override
    public void update(){
        spriteCounter++;
        if (spriteCounter > 5) {
            spriteNum++;
            if (spriteNum >= animations.get(aniCount).size()) spriteNum = 0;
            spriteCounter = 0;
        }
        this.worldX = gp.player.worldX - 30;
        this.worldY = gp.player.worldY - 30;
        this.flip = gp.player.flip;
        if(count <= 20 && delay <= 0){
            if(count % 10 == 0 ){
                int bulletCount = 3; // Ví dụ: 5 viên đạn
                double angleStep = Math.toRadians(360.0 / (bulletCount - 1)); // Bước góc (-180 đến 0 độ)

                for (int i = 0; i < bulletCount; i++) {
                    // Góc hiện tại của từng viên đạn
                    double angle = Math.toRadians(180) + i * angleStep;

                    // Tính toán tọa độ mục tiêu cho từng viên đạn
                    int targetBulletX = (int) (worldX + Math.cos(angle) * 12 * gp.tileSize); // Khoảng cách từ tâm là 25
                    int targetBulletY = (int) (worldY + Math.sin(angle) * 12 * gp.tileSize);

                    // Tạo đối tượng đạn
                    NormalBullet b = new NormalBullet("/bullet/bullet.png", "bullet",
                            0, 0, 8 * 6, 8 * 6,
                            (int) (worldX), (int) (worldY), 18, gp,
                            0, 8, 1, 1,
                            targetBulletX, targetBulletY);
                    b.death = false;
                    gp.obj.add(b);
                }
            }
            Bullet b = new Bullet("/bullet/bullet.png","bullet",12,12, 8, 8, worldX, worldY,20,gp ,0, 12, 1, 1, gp.mouseX, gp.mouseY);
            gp.obj.add(b);
            b.off = false;
            delay = 20;
            count++;
        }
        delay--;
        if(count > 20){
            gp.player.call = false;
            Effect a = new Effect("/effect/effect1.png", 0, 0, this.worldX, this.worldY, 10, gp, 0, 2, 2, 0, 0);
            gp.obj.add(a);
            gp.obj.remove(this);
        }
    }
    @Override
    public void draw(Graphics2D g2, GamePanel gp){
        screenY = worldY - gp.player.worldY + gp.player.screenY;
        screenX = worldX - gp.player.worldX + gp.player.screenX;

        // Tạo hình ảnh
        BufferedImage image = animations.get(aniCount).get(spriteNum < animations.get(aniCount).size() ? spriteNum : animations.get(aniCount).size()-1);

        // Lưu lại trạng thái ban đầu của Graphics2D
        AffineTransform old = g2.getTransform();

        // Kích thước của hình ảnh
        int imageWidth = (int)(gp.tileSize);
        int imageHeight = (int)(gp.tileSize);

        // Tính toán chính xác tâm của hình ảnh
        int centerX = screenX + gp.tileSize / 2 ;
        int centerY = screenY + gp.tileSize / 2;

        // Dịch hệ tọa độ đến tâm của vật thể (tâm của hình ảnh)
        g2.translate(centerX, centerY);

        // Xoay hệ tọa độ quanh tâm của hình ảnh

        // Lật hình ảnh theo trục Ox (nếu cần)
        if(flip) g2.scale(-1, 1);

        // Vẽ hình ảnh đã xoay và lật, với tọa độ được điều chỉnh để đúng vị trí
        g2.drawImage(image, -imageWidth / 2, -imageHeight / 2, (int)(imageWidth) , (int)(imageHeight), null);

        // Khôi phục lại trạng thái ban đầu của Graphics2D
        g2.setTransform(old);

        // Vẽ khung hình chữ nhật (nếu cần)
        rectDraw(g2);
    }
}

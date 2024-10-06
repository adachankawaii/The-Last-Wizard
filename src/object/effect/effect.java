package object.effect;

import main.GamePanel;
import object.SuperObject;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class effect extends SuperObject {
    GamePanel gp;
    int lifeTime = 0;

    public effect(String path,int rectX, int rectY, int worldX, int worldY,int lifeTime, GamePanel gp, int speed, int scaleX, int scaleY){
        this.rect = new Rectangle();
        importAndSlice(path, 4, 0,0);
        this.rect.width = rectX;
        this.rect.height = rectY;
        this.gp = gp;
        this.worldX = worldX;
        this.worldY = worldY;
        aniCount = 0;
        this.lifeTime = lifeTime;
        collision = false;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.speed = speed;
        angle = Math.atan2(gp.mouseY - worldY, gp.mouseX - worldX);
    }
    public int timer = 0;
    double angle = 0;
    double scaleX, scaleY;
    int speed =0;

    @Override
    public void draw(Graphics2D g, GamePanel gp) {
        // Tính toán vị trí trên màn hình
        screenY = worldY - gp.player.worldY + gp.player.screenY;
        screenX = worldX - gp.player.worldX + gp.player.screenX;

        // Tạo hình ảnh
        BufferedImage image = animations.get(aniCount).get(spriteNum);

        // Lưu lại trạng thái ban đầu của Graphics2D
        AffineTransform old = g.getTransform();

        // Kích thước của hình ảnh
        int imageWidth = (int) (image.getWidth() * scaleX);
        int imageHeight = (int) (image.getHeight() * scaleY);

        // Tính toán chính xác tâm của hình ảnh
        int centerX = screenX + gp.titleSize / 2;
        int centerY = screenY + gp.titleSize / 2;

        // Dịch hệ tọa độ đến tâm của vật thể (tâm của hình ảnh)
        g.translate(centerX, centerY);

        // Xoay hệ tọa độ quanh tâm của hình ảnh
        g.rotate(angle);

        // Lật hình ảnh theo trục Ox (nếu cần)
        //g.scale(-1, 1);

        // Vẽ hình ảnh đã xoay và lật, với tọa độ được điều chỉnh để đúng vị trí
        g.drawImage(image, -imageWidth / 2, -imageHeight / 2, (int)(imageWidth), (int)(imageHeight), null);

        // Khôi phục lại trạng thái ban đầu của Graphics2D
        g.setTransform(old);
    }



    @Override
    public void update() {
        spriteCounter++;
        if(spriteCounter > 2) {
            spriteNum++;
            if(spriteNum >= animations.get(aniCount).size()) spriteNum = 0;
            spriteCounter = 0;
        }
        collisionOn = false;
        if (timer <= 60) {
            this.worldX += (int)(speed*Math.cos(angle));
            this.worldY += (int)(speed*Math.sin(angle));
        }
        if(timer >= lifeTime) {
            gp.obj.remove(this);
        }

        timer++;

    }
}

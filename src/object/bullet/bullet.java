package object.bullet;

import main.GamePanel;
import object.SuperObject;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class bullet extends SuperObject {
    GamePanel gp;
    public int timer = 0;
    public int lifeTime = 0;
    public double angle = 0;
    public bullet(String path,int rectX, int rectY, int worldX, int worldY,int lifeTime, GamePanel gp){
        this.rect = new Rectangle();
        importAndSlice(path, 4, 0,0);
        this.rect.x = rectX;
        this.rect.y = rectY;
        this.gp = gp;
        this.worldX = gp.player.worldX;
        this.worldY = gp.player.worldY;
        aniCount = 0;
        this.lifeTime = lifeTime;
        collision = true;
        speed = 8;
    }
    @Override
    public void draw(Graphics2D g, GamePanel gp) {
        // Tính toán vị trí trên màn hình
        screenY = worldY - gp.player.worldY + gp.player.screenY;
        screenX = worldX - gp.player.worldX + gp.player.screenX;

        // Tạo hình ảnh
        BufferedImage image = animations.get(aniCount).get(spriteNum);

        // Lưu lại trạng thái ban đầu của Graphics2D
        AffineTransform old = g.getTransform();

        // Dịch chuyển hệ tọa độ đến tâm của vật thể (tâm của hình ảnh)
        int centerX = screenX + gp.titleSize / 4; // Tâm theo trục X
        int centerY = screenY + gp.titleSize / 2; // Tâm theo trục Y
        g.translate(centerX, centerY);

        // Xoay hệ tọa độ quanh tâm vật thể (nếu cần)
        g.rotate(angle);

        // Lật hình ảnh theo trục Ox
        g.scale(-1, 1); // Lật theo trục Ox (ngang)

        // Vẽ hình ảnh, điều chỉnh lại để tâm vật thể đúng vị trí
        g.drawImage(image, -gp.titleSize / 4, -gp.titleSize / 2, gp.titleSize / 2, gp.titleSize, null);

        // Đặt lại trạng thái ban đầu của Graphics2D
        g.setTransform(old);
    }


    @Override
    public void update() {
        spriteCounter++;
        if(spriteCounter > 5) {
            spriteNum++;
            if(spriteNum >= animations.get(aniCount).size()) spriteNum = 0;
            spriteCounter = 0;
        }
        collisionOn = false;

        if (timer <= 40) {
            this.worldX += (int)(speed*Math.cos(angle));
            this.worldY += (int)(speed*Math.sin(angle));
        }

        if(timer >= 60 || collisionOn) {
            for(int i = 0;i<gp.obj.size();i++){
                if(gp.obj.get(i) ==  this){
                    gp.obj.remove(this);
                }
            }
        }


        timer++;

    }
}
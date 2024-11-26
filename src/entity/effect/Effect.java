package entity.effect;

import main.GamePanel;
import entity.Entity;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Objects;
import java.util.Vector;

public class Effect extends Entity {
    GamePanel gp;
    int lifeTime = 0;
    int targetX, targetY;
    public Effect(String path,int rectX, int rectY, int worldX, int worldY,
    int lifeTime, GamePanel gp, int speed, double scaleX, double scaleY,int targetX, int targetY){
        isBullet = true;
        this.solidArea = new Rectangle();
        layer = 2;
        if(path != null) importAndSlice(path, 4, 0,0);
        else{
            BufferedImage image = new BufferedImage(rectX/8, rectY/8, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = image.createGraphics();


            int b = 255; // Giá trị ngẫu nhiên cho Blue
            Color randomColor = new Color(0, 250, b); // Tạo màu ngẫu nhiên từ RGB
            if (Objects.equals(this.objName, "enemyBullet")) {
                randomColor = new Color(255, 0, 0); // Tạo màu ngẫu nhiên từ RGB
            }

            g2d.setColor(randomColor);
            g2d.fillRect(0, 0, rectX/8, rectY/8); // Vẽ hình chữ nhật đỏ kích thước 100x100

            // Giải phóng đối tượng Graphics2D sau khi vẽ
            g2d.dispose();
            Vector<BufferedImage> tmp = new Vector<>();
            tmp.add(image);
            animations.add(tmp);
        }
        this.solidArea.width = 0;
        this.solidArea.height = 0;
        this.gp = gp;
        this.worldX = worldX;
        this.worldY = worldY;
        aniCount = 0;
        this.targetX = targetX;
        this.targetY = targetY;
        this.lifeTime = lifeTime;
        collision = false;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.speed = speed;
        angle = Math.atan2(targetY - worldY, targetX - worldX);
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
        int centerX = screenX + gp.tileSize / 2;
        int centerY = screenY + gp.tileSize / 2;

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
        if(animations.get(aniCount).size() == 1){
            spriteNum  = 0;
        }else {
            spriteCounter++;
            if (spriteCounter > 2) {
                spriteNum++;
                if (spriteNum >= animations.get(aniCount).size() - 1) spriteNum = 0;
                spriteCounter = 0;
            }
        }
        isTriggerOn = false;
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

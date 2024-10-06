package object.bullet;

import main.GamePanel;
import object.SuperObject;
import object.effect.effect;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.Vector;

public class bullet extends SuperObject {
    GamePanel gp;
    public int timer = 0;
    public int lifeTime = 0;
    double angle = 0;
    int animationDelay = 2;
    double scaleX, scaleY;
    public bullet(String path,String name, int rectX, int rectY, int worldX, int worldY,int lifeTime, GamePanel gp, int w, int speed, double scaleX, double scaleY){
        this.rect = new Rectangle();
        animationDelay = 2;
        if(path != null) importAndSlice(path, 4, 0,w);
        else{
            BufferedImage image = new BufferedImage(rectX, rectY, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = image.createGraphics();


            int b = 255; // Giá trị ngẫu nhiên cho Blue

            Color randomColor = new Color(0, 160, b); // Tạo màu ngẫu nhiên từ RGB
            g2d.setColor(randomColor);
            g2d.fillRect(0, 0, rectX, rectY); // Vẽ hình chữ nhật đỏ kích thước 100x100

            // Giải phóng đối tượng Graphics2D sau khi vẽ
            g2d.dispose();
            Vector<BufferedImage> tmp = new Vector<>();
            tmp.add(image);
            animations.add(tmp);
        }
        this.rect.x = 0;
        this.rect.y = 0;
        this.rect.width = rectX;
        this.rect.height = rectY;
        this.gp = gp;
        this.worldX = worldX;
        this.worldY = worldY;
        angle = Math.atan2(gp.mouseY - worldY, gp.mouseX - worldX);
        aniCount = 0;
        solidAreaDefaultX = rect.x;
        solidAreaDefaultY = rect.y;
        this.lifeTime = lifeTime;
        collision = true;
        this.speed = speed;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.name = name;
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

        // Kích thước của hình ảnh
        int imageWidth = (int) (image.getWidth() * scaleX);
        int imageHeight = (int) (image.getHeight() * scaleY);

        // Tính toán chính xác tâm của hình ảnh
        int centerX = screenX + gp.titleSize / 2 ;
        int centerY = screenY + gp.titleSize / 2;

        // Dịch hệ tọa độ đến tâm của vật thể (tâm của hình ảnh)
        g.translate(centerX, centerY);

        // Xoay hệ tọa độ quanh tâm của hình ảnh
        g.rotate(angle);

        // Lật hình ảnh theo trục Ox (nếu cần)
        g.scale(-1, 1);

        // Vẽ hình ảnh đã xoay và lật, với tọa độ được điều chỉnh để đúng vị trí
        g.drawImage(image, -imageWidth / 2, -imageHeight / 2, (int)(imageWidth), (int)(imageHeight), null);
        // Khôi phục lại trạng thái ban đầu của Graphics2D


        g.setTransform(old);

    }



    @Override
    public void update() {
        spriteCounter++;
        if(spriteCounter > animationDelay) {
            spriteNum++;
            if(spriteNum >= animations.get(aniCount).size()) spriteNum = 0;
            spriteCounter = 0;
            specialMethod();
        }

        collisionOn = false;

        if(Math.toDegrees(angle) > -45 && Math.toDegrees(angle) <= 45) direction = "right";
        if(Math.toDegrees(angle) > 45 && Math.toDegrees(angle) <= 135) direction = "down";
        if(Math.toDegrees(angle) > 135 || Math.toDegrees(angle) <= -135) direction = "left";
        if(Math.toDegrees(angle) > -135 && Math.toDegrees(angle) <= -45) direction = "up";
        int i = gp.collision.checkObjectForObj(this);
        if(i != 999){
            gp.obj.remove(i);
        }
        gp.collision.checkTileForObj(this);
        if (timer <= 60) {
            this.worldX += (int)(speed*Math.cos(angle));
            this.worldY += (int)(speed*Math.sin(angle));
        }

        if(timer > lifeTime || collisionOn) {
            if(collisionOn){
                effect a = new effect("/effect/effect2.png", 0,0,this.worldX,this.worldY,10, gp, 0, 1,1);
                gp.obj.add(a);
            }

            gp.obj.remove(this);
        }


        timer++;

    }
    public void specialMethod(){

    }
}
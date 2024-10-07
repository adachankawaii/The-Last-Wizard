package entity.bullet;

import entity.effect.Effect;
import main.GamePanel;

import java.util.Random;

public class ThrowingObj extends Bullet {
        int x0, y0;
        double vx, vy, gravity = 0.98; // Giá trị trọng lực

        public ThrowingObj(String path, String name, int rectX, int rectY, int worldX, int worldY, int lifeTime, GamePanel gp, int w, int speed, double scaleX, double scaleY) {
            super(path, name, rectX, rectY, worldX, worldY, lifeTime, gp, w, speed, scaleX, scaleY);
            x0 = this.worldX;
            y0 = this.worldY;

            // Tính toán vận tốc theo trục x và y
            vx = (double) (gp.mouseX - x0) / lifeTime;

            // Tính toán vận tốc theo trục y với sự điều chỉnh trọng lực (ngược với chiều y trong Swing)
            vy = (double) (gp.mouseY - y0 - 0.5 * gravity * lifeTime * lifeTime) / lifeTime;
        }

        @Override
        public void update() {
            spriteCounter++;
            if (spriteCounter > animationDelay) {
                spriteNum++;
                if (spriteNum >= animations.get(aniCount).size()) spriteNum = 0;
                spriteCounter = 0;
                specialMethod();
            }

            collisionOn = false;
            timer++;

            // Cập nhật vị trí theo công thức parabol
            this.worldX = (int) (x0 + vx * timer);
            this.worldY = (int) (y0 + vy * timer + 0.5 * gravity * timer * timer); // trọng lực tăng y theo thời gian

            // Kiểm tra nếu vượt quá thời gian sống hoặc va chạm
            if (timer >= lifeTime) {
                Bullet a = new Bullet("/effect/effect1.png","Boom",32,32,this.worldX,this.worldY,10, gp, 0, 0,2, 2);
                a.death = false;
                gp.obj.add(a);
                gp.obj.remove(this); // Xóa vật thể nếu đã hết thời gian hoặc va chạm
            }
        }
        public void specialMethod(){
            Effect a = new Effect(null, 16,16,this.worldX ,this.worldY,10, gp, 2, 2,2);
            gp.obj.add(a);
        }
    }



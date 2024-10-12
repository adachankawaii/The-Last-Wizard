package entity.bullet;

import entity.effect.Effect;
import main.GamePanel;

public class ThrowingObj extends Bullet {
        int x0, y0;
        double vx, vy, gravity = 0.98; // Giá trị trọng lực

        public ThrowingObj(String path, String name,int solidAreaX, int solidAreaY, int rectX, int rectY, int worldX, int worldY, int lifeTime, GamePanel gp, int w, int speed, double scaleX, double scaleY, int TtargetX, int TtargetY) {
            super(path, name,solidAreaX, solidAreaY, rectX, rectY, worldX, worldY, lifeTime, gp, w, speed, scaleX, scaleY, TtargetX, TtargetY);
            x0 = this.worldX;
            y0 = this.worldY;
            int stargetX = (targetX-x0)/gp.tileSize;
            int stargetY = (targetY - y0)/gp.tileSize;
            if(Math.abs(stargetX) >= 7) stargetX = (stargetX/Math.abs(stargetX))*7;
            if(Math.abs(stargetY) >= 7) stargetY = (stargetY/Math.abs(stargetY))*7;
            // Tính toán vận tốc theo trục x và y
            vx = (double) (stargetX*gp.tileSize) / lifeTime;

            // Tính toán vận tốc theo trục y với sự điều chỉnh trọng lực (ngược với chiều y trong Swing)
            vy = (stargetY*gp.tileSize - 0.5 * gravity * lifeTime * lifeTime) / lifeTime;
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
                Bullet a = new Bullet("/effect/effect1.png",this.objName,-10,-10,(int)(16*scaleX),(int)(16*scaleY),this.worldX,this.worldY,10, gp, 0, 0,scaleX, scaleY, this.targetX, this.targetY);
                a.death = false;
                gp.obj.add(a);
                gp.obj.remove(this); // Xóa vật thể nếu đã hết thời gian hoặc va chạm
            }
        }
        public void specialMethod(){
            Effect a = new Effect(null, 16,16,this.worldX ,this.worldY,10, gp, 2, 2,2, targetX, targetY);
            gp.obj.add(a);
        }
    }


